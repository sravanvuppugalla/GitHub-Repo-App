package com.sravanapps.githubrepoapplication.domain.repository

import com.sravanapps.githubrepoapplication.domain.models.Repository

interface GitHubRepository {
    suspend fun fetchRepositories(username: String, page: Int): Result<List<Repository>>
}
