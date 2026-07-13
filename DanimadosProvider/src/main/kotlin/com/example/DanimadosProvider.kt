package com.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element

class DanimadosProvider : MainAPI() {
    companion object {
        private const val BASE_URL = "https://danimados.cc"
    }

    override var mainUrl = BASE_URL
    override var name = "Danimados"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.Cartoon, TvType.TvSeries)

    override val mainPage = mainPageOf("/" to "Series")

    private val genreSelectors = listOf(
        "#genre_60s" to "Años 60",
        "#genre_70s" to "Años 70",
        "#genre_80s" to "Años 80",
        "#genre_90s" to "Años 90",
        "#genre_00s" to "Años 2000",
        "#genre_10s" to "Años 2010",
        "#genre_20s" to "Años 2020",
        "#genre_sitcom" to "Sitcoms",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val doc = app.get(BASE_URL).document
        val homeLists = mutableListOf<HomePageList>()

        for ((selector, title) in genreSelectors) {
            val items = doc.select("$selector > article.item.tvshows").mapNotNull { it.toSearchResponse() }
            if (items.isNotEmpty()) {
                homeLists.add(HomePageList(title, items))
            }
        }

        return newHomePageResponse(homeLists)
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        val doc = app.get("$BASE_URL/?s=$query").document
        val items = doc.select("article.item.tvshows").mapNotNull { it.toSearchResponse() }
        if (items.isNotEmpty()) return items
        return null
    }

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document

        val title = doc.selectFirst(".sheader .data h1")?.text()
            ?: doc.selectFirst("title")?.text()?.substringBefore(" –")?.substringBefore(" -")?.trim()
            ?: return null

        val poster = doc.selectFirst(".sheader .poster img")?.attr("src")
            ?: doc.selectFirst("meta[property='og:image']")?.attr("content")
            ?: ""

        val description = doc.selectFirst(".hero-overview, .wp-content p")?.text()
            ?: doc.selectFirst("meta[name='description']")?.attr("content")

        val year = doc.selectFirst(".sheader .data .meta-top span")?.text()?.let { extractYear(it) }

        val rating = doc.selectFirst("#repimdb strong")?.text()?.toDoubleOrNull()

        val episodes = extractEpisodes(doc)

        if (episodes.isNotEmpty()) {
            return newTvSeriesLoadResponse(title, url, TvType.Cartoon, episodes) {
                this.posterUrl = fixUrl(poster)
                this.plot = description
                this.year = year
                this.score = rating?.let { Score.from10(it) }
            }
        }

        return newMovieLoadResponse(title, url, TvType.Cartoon, url) {
            this.posterUrl = fixUrl(poster)
            this.plot = description
            this.year = year
            this.score = rating?.let { Score.from10(it) }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document

        // Get admin-ajax.php URL
        val ajaxScript = doc.selectFirst("#dt_main_ajax-js-extra")
        val ajaxUrl = if (ajaxScript != null) {
            val match = Regex("""url["']?\s*:\s*["']([^"']+)""").find(ajaxScript.html())
            match?.groupValues?.get(1)?.let { fixRelativeUrl(it) } ?: "$BASE_URL/wp-admin/admin-ajax.php"
        } else {
            "$BASE_URL/wp-admin/admin-ajax.php"
        }

        // Get post ID from player option
        val postId = doc.selectFirst("#player-option-1")?.attr("data-post")
            ?: doc.selectFirst(".dooplay_player_option")?.attr("data-post")
        if (postId == null) return false

        // POST to admin-ajax.php
        val playerResp = app.post(
            ajaxUrl,
            headers = mapOf(
                "X-Requested-With" to "XMLHttpRequest",
                "Accept" to "*/*",
                "Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8",
                "Referer" to data,
            ),
            data = mapOf(
                "action" to "doo_player_ajax",
                "post" to postId,
                "nume" to "1",
                "type" to "tv",
            )
        ).parsedSafe<PlayerResponse>() ?: return false

        val embedHtml = playerResp.embedUrl ?: return false
        val iframeSrc = Regex("""src=["']([^"']+)["']""").find(embedHtml)?.groupValues?.get(1)
        if (iframeSrc != null && iframeSrc.isNotBlank()) {
            return loadExtractor(iframeSrc, data, subtitleCallback, callback)
        }

        return false
    }

    private fun extractEpisodes(doc: org.jsoup.nodes.Document): List<Episode> {
        val episodes = mutableListOf<Episode>()

        // Dooplay #seasons .se-c structure
        val seasonContainers = doc.select("#seasons > .se-c")
        if (seasonContainers.isNotEmpty()) {
            for (seasonContainer in seasonContainers) {
                val seasonNum = seasonContainer.selectFirst(".se-q .se-t")?.text()?.toIntOrNull() ?: continue
                val episodeItems = seasonContainer.select(".se-a ul.episodios > li")
                for ((epIdx, li) in episodeItems.withIndex()) {
                    val link = li.selectFirst(".episodiotitle a") ?: continue
                    val epHref = link.attr("abs:href")
                    val epTitle = link.text().trim()
                    val numText = li.selectFirst(".numerando")?.text()
                    val epNum = Regex("""\d+\s*-\s*(\d+)""").find(numText ?: "")?.groupValues?.get(1)?.toIntOrNull()
                        ?: (epIdx + 1)

                    if (epHref.isNotBlank()) {
                        episodes.add(newEpisode(epHref) {
                            this.name = epTitle.ifEmpty { "Episodio $epNum" }
                            this.season = seasonNum
                            this.episode = epNum
                        })
                    }
                }
            }
            return episodes
        }

        // Fallback: flat episodios list
        val flatEps = doc.select("#seasons .se-a ul.episodios > li")
        for ((ei, li) in flatEps.withIndex()) {
            val link = li.selectFirst(".episodiotitle a") ?: continue
            val epHref = link.attr("abs:href")
            val epTitle = link.text().trim()
            val numText = li.selectFirst(".numerando")?.text()
            val sMatch = Regex("""(\d+)\s*-\s*(\d+)""").find(numText ?: "")
            val seasonNum = sMatch?.groupValues?.get(1)?.toIntOrNull() ?: 1
            val epNum = sMatch?.groupValues?.get(2)?.toIntOrNull() ?: (ei + 1)
            if (epHref.isNotBlank()) {
                episodes.add(newEpisode(epHref) {
                    this.name = epTitle.ifEmpty { "Episodio $epNum" }
                    this.season = seasonNum
                    this.episode = epNum
                })
            }
        }

        return episodes
    }

    private fun Element.toSearchResponse(): SearchResponse? {
        val link = select("a[href*='/series/']").first() ?: return null
        val href = link.attr("abs:href")
        val title = link.text().trim()
        if (title.isBlank()) return null
        val poster = select("img").first()?.attr("src")?.let { fixUrl(it) }
        val year = select(".data span").first()?.text()?.let { extractYear(it) }
        val rating = select(".rating").first()?.text()?.toDoubleOrNull()

        return newMovieSearchResponse(title, href, TvType.Cartoon) {
            this.posterUrl = poster
            this.year = year
            this.score = rating?.let { Score.from10(it) }
        }
    }

    private fun extractYear(text: String): Int? {
        return Regex("""(19\d{2}|20\d{2})""").find(text)?.groupValues?.get(1)?.toIntOrNull()
    }

    private fun String?.fixUrl(): String? {
        if (this.isNullOrBlank()) return null
        return if (startsWith("//")) "https:$this" else this
    }

    private fun fixRelativeUrl(url: String): String {
        if (url.startsWith("http://") || url.startsWith("https://")) return url
        return "${BASE_URL}${if (url.startsWith("/")) "" else "/"}$url"
    }

    data class PlayerResponse(
        @JsonProperty("embed_url") val embedUrl: String? = null,
        @JsonProperty("type") val type: String? = null,
    )
}
