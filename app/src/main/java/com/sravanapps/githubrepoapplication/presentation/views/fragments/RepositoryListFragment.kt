package com.sravanapps.githubrepoapplication.presentation.views.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sravanapps.githubrepoapplication.R
import com.sravanapps.githubrepoapplication.data.remoteservices.RetrofitInstance
import com.sravanapps.githubrepoapplication.data.repositories.GitHubRepositoryImpl
import com.sravanapps.githubrepoapplication.domain.usecases.GetRepositoriesUseCase
import com.sravanapps.githubrepoapplication.presentation.adapters.RepositoryAdapter
import com.sravanapps.githubrepoapplication.presentation.viewmodels.GitHubViewModel
import com.sravanapps.githubrepoapplication.presentation.viewmodels.GitHubViewModelFactory
import com.sravanapps.githubrepoapplication.presentation.views.UiState

class RepositoryListFragment : Fragment() {

    private lateinit var viewModel: GitHubViewModel
    private lateinit var adapter: RepositoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var languageSpinner: Spinner
    private lateinit var txtUserName: TextView
    private var debounceHandler = Handler(Looper.getMainLooper())
    private var isLoadingMore = false
    private val debounceDelay: Long = 600
    private var username: String? = null
    private var pageCount: Int = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_repository_list, container, false)
        arguments?.let {
            username = it.getString("username")
            pageCount = it.getInt("pageCount")
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = GitHubRepositoryImpl(RetrofitInstance.apiService)
        val getRepositoriesUseCase = GetRepositoriesUseCase(repository = repository)
        val factory = GitHubViewModelFactory(getRepositoriesUseCase)

        viewModel = ViewModelProvider(this, factory)[GitHubViewModel::class.java]

        languageSpinner = view.findViewById(R.id.languageSpinner)
        txtUserName = view.findViewById(R.id.txtUserName)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)

        setupRecyclerView()
        observeData()
        setupErrorHandling()
        username?.let {
            txtUserName.text = it
            viewModel.loadRepositories(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        debounceHandler.removeCallbacksAndMessages(null)
    }

    private fun setupRecyclerView() {
        adapter = RepositoryAdapter(
            onClick = {

            },
            onLoadMoreCallback = { username?.let { viewModel.loadMore(it) } }
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    isLoadingMore = true
                    adapter.setLoadingIndicator(true)
                    debounceHandler.postDelayed({
                        username?.let { viewModel.loadMore(it) }
                        isLoadingMore = false
                    }, debounceDelay)
                }
            }
        })
    }

    private fun observeData() {
        viewModel.repositories.observe(viewLifecycleOwner) { repositoryList ->
            adapter.setLoadingIndicator(false)
            adapter.submitList(repositoryList)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            adapter.setLoadingIndicator(isLoading)
        }

        viewModel.languages.observe(viewLifecycleOwner) { languages ->
            val languageList = languages.toList()
            context?.let {
                val spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, languageList)
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                languageSpinner.adapter = spinnerAdapter
            }
        }

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = languageSpinner.selectedItem as String
                viewModel.filterRepositoriesByLanguage(selectedLanguage)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupErrorHandling() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> progressBar.isVisible = false // making this false because we have another progressbar in the recyclerview for pagination
                is UiState.Success -> progressBar.isVisible = false
                is UiState.Error -> {
                    progressBar.isVisible = false
                    showError(state.message)
                }
                is UiState.NoMoreResults -> {
                    Toast.makeText(context, "No more results available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }
}
