package com.sravanapps.githubrepoapplication.presentation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sravanapps.githubrepoapplication.R


class UsernameFragment : Fragment() {
    private lateinit var usernameInput: EditText
    private lateinit var submitButton: Button
    private lateinit var spinnerPageCount: Spinner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_username, container, false)

        usernameInput = view.findViewById(R.id.usernameInput)
        submitButton = view.findViewById(R.id.submitButton)
        spinnerPageCount = view.findViewById(R.id.spinnerPageCount)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.page_counts,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPageCount.adapter = adapter
        }


        submitButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            if (username.isNotEmpty()) {
                navigateToNextFragment()
            } else {
                Toast.makeText(requireContext(), "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun navigateToNextFragment() {
        val username = usernameInput.text.toString()
        val pageCount = spinnerPageCount.selectedItem.toString().toInt()

        val bundle = Bundle().apply {
            putString("username", username)
            putInt("pageCount", pageCount)
        }

        findNavController().navigate(R.id.action_inputFragment_to_outputFragment, bundle)
    }
}