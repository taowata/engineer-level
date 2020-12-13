package io.github.taowata.engineerlevel.network

import com.squareup.moshi.Json

data class GitHubUser(
    @Json(name = "login") val userName: String,
    val avatar_url: String,
    val followers: Int,
    val following: Int
)