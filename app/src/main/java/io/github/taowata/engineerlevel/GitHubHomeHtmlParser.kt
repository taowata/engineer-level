package io.github.taowata.engineerlevel

import org.jsoup.Jsoup

object GitHubHomeHtmlParser {
    fun getContributionNumber(url: String): String {
        val document = Jsoup.connect(url).get()
        // Contribution数を取得
        return document.select("h2.f4.text-normal.mb-2").last().text().split(" ").first()
    }
}