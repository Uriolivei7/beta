package com.example

import android.util.Base64
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

class SeriesflixhdProvider : MainAPI() {
    override var mainUrl = "https://seriesflixhd.ink"
    override var name = "Seriesflixhd"
    override val supportedTypes = setOf(TvType.TvSeries)
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true

    private fun fixImgUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        return if (url.startsWith("//")) "https:$url" else url
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val link = selectFirst("a")?.attr("href") ?: return null
        val title = selectFirst("h2.Title")?.text()?.trim()
            ?: selectFirst("div.Title")?.text()?.trim() ?: return null
        val img = selectFirst("img.imglazy")?.attr("data-src")
            ?: selectFirst("img")?.attr("src") ?: ""

        return newTvSeriesSearchResponse(title, fixUrl(link)) {
            this.posterUrl = fixImgUrl(img)
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        if (page > 1) return null
        val doc = app.get(mainUrl).document
        val items = ArrayList<HomePageList>()

        val top10 = doc.select("div.top10 li.mvnew").mapNotNull { it.toSearchResult() }
        if (top10.isNotEmpty()) items.add(HomePageList("Top 10", top10))

        val series = doc.select("ul.MovieList.Rows li.TPostMv.series").mapNotNull { it.toSearchResult() }
        if (series.isNotEmpty()) items.add(HomePageList("Últimas Series", series))

        val episodes = doc.select("ul.MovieList.Rows.Episodes li.TPostMv").mapNotNull { it.toSearchResult() }
        if (episodes.isNotEmpty()) items.add(HomePageList("Últimos Episodios", episodes))

        return newHomePageResponse(items, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/?s=$query").document
        val results = doc.select("ul.MovieList.Rows li.TPostMv").mapNotNull { it.toSearchResult() }
        if (results.isNotEmpty()) return results
        return doc.select("div.TPostMv").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document

        val title = doc.selectFirst("h1.Title")?.text()?.trim() ?: return null
        val poster = doc.selectFirst("img.imglazy")?.attr("data-src") ?: ""
        val description = doc.selectFirst("div.Description p")?.text()?.trim()
        val yearStr = doc.selectFirst("span.Date")?.text()?.trim()
        val tags = doc.select("p.Genre a[href*='/genero/']").map { it.text().trim() }

        val seasonLinks = doc.select("section.SeasonBx div.Top div.Title a[href]").mapNotNull { element ->
            val seasonUrl = element.attr("href")
            val seasonText = element.text()
            val seasonNum = Regex("""\d+""").find(seasonText)?.value?.toIntOrNull()
                ?: return@mapNotNull null
            seasonNum to seasonUrl
        }

        val episodes = ArrayList<Episode>()

        for ((seasonNum, seasonUrl) in seasonLinks) {
            try {
                val seasonDoc = app.get(seasonUrl).document
                val rows = seasonDoc.select("table tbody tr")
                for (row in rows) {
                    val epLink = row.selectFirst("td.MvTbTtl a[href]") ?: continue
                    val epNum = row.selectFirst("td span.Num")?.text()?.toIntOrNull() ?: continue
                    val epName = epLink.text().trim()
                    val epUrl = epLink.attr("href")
                    val epImg = row.selectFirst("td.MvTbImg.B img.imglazy")?.attr("data-src")

                    episodes.add(newEpisode(epUrl) {
                        this.name = epName
                        this.season = seasonNum
                        this.episode = epNum
                        this.posterUrl = fixImgUrl(epImg)
                    })
                }
            } catch (_: Exception) {}
        }

        episodes.sortWith(compareBy({ it.season }, { it.episode }))

        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
            this.posterUrl = fixImgUrl(poster)
            this.backgroundPosterUrl = fixImgUrl(poster)
            this.plot = description
            this.tags = tags
            if (!yearStr.isNullOrBlank()) yearStr.toIntOrNull()?.let { this.year = it }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document
        val servers = doc.select("div.sgty[data-url]")
        var found = false

        for (server in servers) {
            val encoded = server.attr("data-url")
            if (encoded.isBlank()) continue

            try {
                val decoded = String(Base64.decode(encoded, Base64.DEFAULT), Charsets.UTF_8)
                if (loadExtractor(decoded, data, subtitleCallback, callback)) {
                    found = true
                }
            } catch (_: Exception) {}
        }

        return found
    }
}
