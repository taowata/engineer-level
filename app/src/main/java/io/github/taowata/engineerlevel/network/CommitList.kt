package io.github.taowata.engineerlevel.network

import com.squareup.moshi.Json

data class CommitList(
    @Json(name = "owner") val commitList: List<Int>,
)