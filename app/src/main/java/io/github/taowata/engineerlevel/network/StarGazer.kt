package io.github.taowata.engineerlevel.network

import com.squareup.moshi.Json

data class StarGazer (
    @Json(name = "login") val userName: String,
)