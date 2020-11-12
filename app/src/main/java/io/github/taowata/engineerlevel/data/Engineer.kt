package io.github.taowata.engineerlevel.data

class Engineer(
    var name: String,
    var contributions: Int,
    var followers: Int,
    var stars: Int,
    var languageAndBytes: MutableMap<String, Long>,
)