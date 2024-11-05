package com.sravanapps.githubrepoapplication.domain.usecases

import com.sravanapps.githubrepoapplication.domain.models.Repository
import com.sravanapps.githubrepoapplication.domain.repository.GitHubRepository

class GetRepositoriesUseCase(private val repository: GitHubRepository) {
    private var lastResponse: Result<List<Repository>>? = null

    suspend operator fun invoke(username: String, page: Int): Result<List<Repository>> {
        lastResponse = repository.fetchRepositories(username, page)
        return repository.fetchRepositories(username, page)
    }

    fun getLastResponse(): Result<List<Repository>>? {
        return lastResponse
    }
}