package com.sravanapps.githubrepoapplication.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sravanapps.githubrepoapplication.domain.models.Repository
import com.sravanapps.githubrepoapplication.domain.usecases.GetRepositoriesUseCase
import com.sravanapps.githubrepoapplication.presentation.views.UiState
import kotlinx.coroutines.launch

class GitHubViewModel(
    private val getRepositoriesUseCase: GetRepositoriesUseCase
) : ViewModel() {

    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>> = _repositories

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private var currentPage = 1
    private var isLastPage = false
    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading = _isLoading
    private var hasNoMoreResults = false

    private val _languages = MutableLiveData<Set<String>>()
    val languages: LiveData<Set<String>> get() = _languages

    private var allRepositories: MutableList<Repository> = mutableListOf()

    init {
        _languages.value = mutableSetOf("All")  // Include "All" by default
    }

    fun loadRepositories(username: String) {
        if (_isLoading.value == true || isLastPage) return

        _uiState.value = UiState.Loading
        _isLoading.postValue(true)

        viewModelScope.launch {
            val result = getRepositoriesUseCase(username, currentPage)
            result.onSuccess { repos ->
                if (repos.isEmpty()) {
                    isLastPage = true
                    _uiState.value = UiState.NoMoreResults
                    isLoading.value = false
                    hasNoMoreResults = true
                } else {
                    val updatedList = _repositories.value.orEmpty() + repos
                    _repositories.value = updatedList
                    allRepositories.addAll(updatedList)

                    val newLanguages = updatedList.mapNotNull { it.language }.toSet()
                    val updatedLanguages = _languages.value.orEmpty() + newLanguages
                    _languages.value = updatedLanguages

                    currentPage++
                    _uiState.value = UiState.Success
                }
            }.onFailure { error ->
                hasNoMoreResults = true
                _uiState.value = UiState.Error(error.message ?: "An error occurred")
            }
            isLoading.value = false
        }
    }

    fun filterRepositoriesByLanguage(language: String) {
        if (language == "All") {
            _repositories.value = allRepositories
        } else {
            _repositories.value = allRepositories.filter { it.language == language }
        }
    }

    fun loadMore(username: String) {
        if (!isLastPage) {
            loadRepositories(username)
        }
    }
    fun isLastPage(): Boolean = isLastPage

    fun getHasNoMoreResults() : Boolean= hasNoMoreResults
}


