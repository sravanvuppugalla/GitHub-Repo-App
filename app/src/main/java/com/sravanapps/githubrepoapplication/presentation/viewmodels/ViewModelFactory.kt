package com.sravanapps.githubrepoapplication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sravanapps.githubrepoapplication.domain.usecases.GetRepositoriesUseCase

class GitHubViewModelFactory(
    private val getRepositoriesUseCase: GetRepositoriesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GitHubViewModel::class.java)) {
            return GitHubViewModel(getRepositoriesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}