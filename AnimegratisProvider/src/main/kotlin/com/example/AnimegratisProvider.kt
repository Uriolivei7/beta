package com.example

import com.lagradost.cloudstream3.Episode
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.fixUrl
import com.lagradost.cloudstream3.fixUrlNull
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newEpisode
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newMovieLoadResponse
import com.lagradost.cloudstream3.newTvSeriesSearchResponse
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import android.util.Log
import com.lagradost.cloudstream3.DubStatus
import com.lagradost.cloudstream3.ShowStatus
import com.lagradost.cloudstream3.addEpisodes
import com.lagradost.cloudstream3.newAnimeLoadResponse
import org.json.JSONObject
import org.jsoup.nodes.Element

class AnimeGratisProvider : MainAPI() {
    override var mainUrl = "https://animegratis.net"
    override var name = "AnimeGratis"
    override val hasMainPage = true
    override var lang = "mx"
    override val hasDownloadSupport = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie
    )

    override val mainPage = mainPageOf(
        "$mainUrl/directorio" to "Animes",
        "$mainUrl/directorio/doblado" to "Doblados"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (request.data.contains('?')) "${request.data}&page=$page" else "${request.data}?page=$page"
        Log.d("AnimeGratis", "getMainPage URL: $url")
        val document = app.get(url).document

        val ssrScript = document.selectFirst("script#ssr-init")
        if (ssrScript != null) {
            val items = parseSsrJson(ssrScript.data())
            if (items.isNotEmpty()) {
                return newHomePageResponse(
                    list = HomePageList(
                        name = request.name,
                        list = items,
                        isHorizontalImages = false
                    ),
                    hasNext = true
                )
            }
        }

        val fallback = document.select("div.anime-card").mapNotNull { it.toAnimeCardResult() }
        return newHomePageResponse(
            list = HomePageList(
                name = request.name,
                list = fallback,
                isHorizontalImages = false
            ),
            hasNext = true
        )
    }

    private fun parseSsrJson(json: String): List<SearchResponse> {
        val results = mutableListOf<SearchResponse>()
        return try {
            val root = JSONObject(json)
            val animesArray = root.optJSONArray("animes") ?: return results
            Log.d("AnimeGratis", "SSR animes count: ${animesArray.length()}")

            for (i in 0 until animesArray.length()) {
                val item = animesArray.getJSONObject(i)
                val title = item.optString("t", "")
                val slug = item.optString("sl", "")
                val poster = item.optString("fb", "").ifEmpty {
                    item.optString("im", "")
                }

                val href = if (slug.isNotBlank()) "$mainUrl/anime/$slug-anime" else ""
                if (title.isNotBlank() && href.isNotBlank()) {
                    val ty = item.optString("ty", "")
                    val tvType = if (ty == "movie") TvType.AnimeMovie else TvType.Anime
                    results.add(
                        newTvSeriesSearchResponse(title, href, tvType) {
                            this.posterUrl = fixUrlNull(poster)
                        }
                    )
                }
            }
            results
        } catch (e: Exception) {
            Log.e("AnimeGratis", "Error parsing SSR JSON: ${e.message}")
            results
        }
    }

    private fun Element.toAnimeCardResult(): SearchResponse {
        val link = this.selectFirst("a") ?: this
        val href = fixUrl(link.attr("href"))
        val img = this.selectFirst("img")
        val poster = img?.run {
            attr("data-fallback").ifEmpty {
                attr("src").ifEmpty { attr("data-src") }
            }
        }
        val title = this.selectFirst("h3, h4, p.font-semibold")?.text()?.trim()
            ?: img?.attr("alt")?.replace("Portada de ", "")?.trim() ?: ""
        return newTvSeriesSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = fixUrlNull(poster)
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/directorio?q=$query"
        Log.d("AnimeGratis", "search URL: $url")
        val document = app.get(url).document

        val ssrScript = document.selectFirst("script#ssr-init")
        if (ssrScript != null) {
            val allItems = parseSsrJson(ssrScript.data())
            if (allItems.isNotEmpty()) {
                val q = query.lowercase()
                val filtered = allItems.filter {
                    it.name.lowercase().contains(q) ||
                    (it.posterUrl?.lowercase()?.contains(q) == true)
                }
                if (filtered.isNotEmpty()) return filtered
            }
        }

        return document.select("div.anime-card").mapNotNull { it.toAnimeCardResult() }
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("AnimeGratis", "load URL: $url")
        val document = app.get(url).document

        val title = document.selectFirst("h1")?.ownText()?.trim()
            ?: document.selectFirst("h1")?.text()?.trim()
            ?: "Unknown"

        val poster = document.selectFirst("meta[property='og:image']")?.attr("content")
            ?: document.selectFirst("div.relative.rounded-xl.overflow-hidden img[src*='cdn.animegratis.net']")?.attr("src")
            ?: document.selectFirst("div.rounded-xl.overflow-hidden > img[src*='cdn.animegratis.net']")?.attr("src")
            ?: run {
                val blurDiv = document.selectFirst("div.absolute.inset-0.bg-cover[style*='background-image']")
                blurDiv?.attr("style")?.let { style ->
                    Regex("url\\(([^)]+)\\)").find(style)?.groupValues?.get(1)
                }
            }
            ?: ""
        Log.d("AnimeGratis", "load poster: $poster")

        val description = document.select("div.space-y-3 > p.text-white\\/90").firstOrNull()?.text()
            ?: document.selectFirst("p.text-white\\/90.leading-relaxed")?.text()
            ?: ""

        val tags = document.select("div.space-y-3 a[href*='/directorio/genero/']").map { it.text() }

        val allSpans = document.select("div.flex.flex-wrap.gap-3 span.text-white\\/90")
        val statusText = allSpans.find { it.text().contains("Finalizado", ignoreCase = true) || it.text().contains("Emisión", ignoreCase = true) }?.text()
        val showStatus = when {
            statusText?.contains("Emisión", ignoreCase = true) == true -> ShowStatus.Ongoing
            statusText?.contains("Finalizado", ignoreCase = true) == true -> ShowStatus.Completed
            else -> null
        }

        val yearSpan = allSpans.find { it.text().contains("📅") }?.text()
        val year = Regex("(\\d{4})").find(yearSpan ?: "")?.groupValues?.get(1)?.toIntOrNull()

        val episodes = mutableListOf<Episode>()

        document.select("#episodes-grid a").forEach { card ->
            val epHref = fixUrl(card.attr("href"))
            val epNum = card.attr("data-episode").toIntOrNull()
            val epImg = card.selectFirst("img")
            val epPoster = epImg?.run {
                attr("src").ifEmpty { attr("data-src") }
            }
            if (epHref.isNotBlank()) {
                episodes.add(
                    newEpisode(epHref) {
                        this.name = "Episodio $epNum"
                        this.episode = epNum
                        this.posterUrl = fixUrlNull(epPoster)
                    }
                )
            }
        }

        Log.d("AnimeGratis", "Episodes found: ${episodes.size}")

        return if (episodes.isNotEmpty()) {
            newAnimeLoadResponse(title, url, TvType.Anime) {
                addEpisodes(DubStatus.Subbed, episodes)
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.showStatus = showStatus
                this.tags = tags
            }
        } else {
            newMovieLoadResponse(title, url, TvType.AnimeMovie, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
                this.year = year
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("AnimeGratis", "loadLinks URL: $data")
        val document = app.get(data).document

        val langMap = mapOf("ja" to "SUB", "cast" to "CAST", "lat" to "LAT")

        document.select("button.server-btn").forEach { btn ->
            val serverName = btn.attr("data-server")
            val serverUrl = btn.attr("data-url")
            val langGroup = btn.attr("data-lang-group")
            val langLabel = langMap[langGroup] ?: ""

            if (serverUrl.isNotBlank()) {
                val displayName = if (langLabel.isNotEmpty()) "[$langLabel] $serverName" else serverName
                Log.d("AnimeGratis", "Servidor: $displayName -> $serverUrl")
                try {
                    loadExtractor(serverUrl, mainUrl, subtitleCallback, callback)
                } catch (e: Exception) {
                    Log.e("AnimeGratis", "Error extractor $serverName: ${e.message}")
                }
            }
        }

        document.select("iframe#videoFrame").forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotBlank()) {
                Log.d("AnimeGratis", "iframe: $src")
                loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
            }
        }

        return true
    }
}
