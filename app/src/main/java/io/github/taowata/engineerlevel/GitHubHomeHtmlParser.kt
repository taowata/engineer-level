package io.github.taowata.engineerlevel

import org.jsoup.Jsoup

object GitHubHomeHtmlParser {
    fun getContributionNumber(url: String): Int {
        val document = Jsoup.connect(url).get()
        // Contribution数を取得
        val text = document.select("h2.f4.text-normal.mb-2").last().text().split(" ").first()
        var textToReturn = ""
        for (i in text.indices) {
            val char = text[i]
            if (char != ',') textToReturn += char
        }
        return textToReturn.toInt()
    }
}