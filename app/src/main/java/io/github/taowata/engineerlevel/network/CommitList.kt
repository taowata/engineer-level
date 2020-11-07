package io.github.taowata.engineerlevel.network

import com.squareup.moshi.Json

data class Commits(
    @Json(name = "name") val name: String,
    @Json(name = "full_name") val fullName: String
)