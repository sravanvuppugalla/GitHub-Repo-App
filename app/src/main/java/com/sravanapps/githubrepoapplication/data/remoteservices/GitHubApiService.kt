package com.sravanapps.githubrepoapplication.data.remoteservices

import com.sravanapps.githubrepoapplication.data.models.RepositoryDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("users/{username}/repos")
    suspend fun getUserRepositories(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Response<List<RepositoryDto>>
}