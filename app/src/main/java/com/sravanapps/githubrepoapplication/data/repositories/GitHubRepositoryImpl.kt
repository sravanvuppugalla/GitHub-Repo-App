package com.sravanapps.githubrepoapplication.data.repositories

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sravanapps.githubrepoapplication.data.models.RepositoryDto
import com.sravanapps.githubrepoapplication.data.remoteservices.GitHubApiService
import com.sravanapps.githubrepoapplication.domain.models.Repository
import com.sravanapps.githubrepoapplication.domain.repository.GitHubRepository
import retrofit2.Response

class GitHubRepositoryImpl(private val apiService: GitHubApiService): GitHubRepository {
    private val gson = Gson()

    override suspend fun fetchRepositories(username: String, page: Int): Result<List<Repository>> {
        return try {
            val response = apiService.getUserRepositories(username, page)
            handleApiResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun handleApiResponse(response: Response<List<RepositoryDto>>): Result<List<Repository>> {
        val errorMessage = extractErrorMessage(response)

        return when (response.code()) {
            in 200..299 -> {
                val repos = response.body()?.map { it.toDomainModel() } ?: emptyList()
                Result.success(repos)
            }
            400 -> { // Bad Request
                Result.failure(Exception("Bad Request: $errorMessage"))
            }
            401 -> { // Unauthorized
                Result.failure(Exception("Unauthorized: $errorMessage"))
            }
            403 -> { // Forbidden
                val accessForbidden = response.errorBody()?.string() ?: "Access forbidden"
                Result.failure(Exception("Forbidden: $accessForbidden"))
            }
            404 -> { // Not Found
                Result.failure(Exception("Not Found: $errorMessage"))
            }
            429 -> { // Too Many Requests
                val retryAfter = response.headers()["Retry-After"]?.toIntOrNull() ?: 60
                Result.failure(Exception("Rate limit exceeded. Try again in $retryAfter seconds."))
            }
            500 -> { // Internal Server Error
                Result.failure(Exception("Internal Server Error: $errorMessage"))
            }
            503 -> { // Service Unavailable
                Result.failure(Exception("Service Unavailable: $errorMessage"))
            }
            else -> { // Other errors
                Result.failure(Exception("Error ${response.code()}: $errorMessage"))
            }
        }
    }

    private fun extractErrorMessage(response: Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = gson.fromJson(errorBody, JsonObject::class.java)
                jsonObject.get("message")?.asString ?: "Unknown error"
            } else {
                "Unknown error"
            }
        } catch (e: Exception) {
            "Error parsing error message"
        }
    }
}

fun RepositoryDto.toDomainModel() = Repository(
    name = name,
    description = description,
    language = language,
    stars = stargazers_count,
    forks = forks_count,
    ownerName = owner.login,
    ownerAvatar = owner.avatar_url,
    updatedAt = updated_at
)