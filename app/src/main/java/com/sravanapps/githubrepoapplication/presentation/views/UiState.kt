package com.sravanapps.githubrepoapplication.presentation.views

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
    object NoMoreResults : UiState()
}