package com.sravanapps.githubrepoapplication.domain.models

data class Repository(val name: String,
                      val description: String?,
                      val language: String?,
                      val stars: Int,
                      val forks: Int,
                      val ownerName: String,
                      val ownerAvatar: String,
                      val updatedAt: String)
