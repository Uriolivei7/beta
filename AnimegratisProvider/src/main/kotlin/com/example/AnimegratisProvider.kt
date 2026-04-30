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
        "$mainUrl/directorio" to "Anime",
        "$mainUrl/directorio?order=emision" to "En Emisión",
        "$mainUrl/directorio?order=recientes" to "Recientes",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (request.data.contains('?')) "${request.data}&page=$page" else "${request.data}?page=$page"
        Log.d("AnimeGratis", "getMainPage URL: $url")
        val document = app.get(url).document

        val ssrScript = document.selectFirst("script#ssr-init")
        Log.d("AnimeGratis", "ssr-script found: ${ssrScript != null}")

        if (ssrScript != null) {
            val json = ssrScript.data()
            Log.d("AnimeGratis", "SSR JSON (first 300): ${json.take(300)}")
            val items = parseSsrJson(json)
            Log.d("AnimeGratis", "SSR items parsed: ${items.size}")
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

        val fallback = document.select("a.block, a.inline-block").mapNotNull { el ->
            val img = el.selectFirst("img") ?: return@mapNotNull null
            val title = el.selectFirst("p, h3, span.font-semibold, span.text-sm")?.text()?.trim()
                ?: img.attr("alt")?.trim()
            val href = el.attr("href")
            val poster = img.attr("src").ifEmpty { img.attr("data-src") }
            if (title.isNullOrBlank() || href.isBlank()) null
            else newTvSeriesSearchResponse(title, fixUrl(href), TvType.Anime) {
                this.posterUrl = fixUrlNull(poster)
            }
        }
        Log.d("AnimeGratis", "Fallback HTML items: ${fallback.size}")
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
            Log.d("AnimeGratis", "Root keys: ${root.keys().asSequence().toList()}")

            val itemsArray = run {
                root.optJSONArray("items")
                    ?: root.optJSONObject("data")?.optJSONArray("items")
                    ?: root.optJSONObject("data")?.optJSONArray("results")
                    ?: root.optJSONArray("results")
            }

            if (itemsArray != null) {
                Log.d("AnimeGratis", "itemsArray length: ${itemsArray.length()}")
                for (i in 0 until itemsArray.length()) {
                    val item = itemsArray.getJSONObject(i)
                    Log.d("AnimeGratis", "Item[$i] keys: ${item.keys().asSequence().toList()}")

                    val title = item.optString("t", "").ifEmpty {
                        item.optString("title", "").ifEmpty {
                            item.optString("name", "")
                        }
                    }
                    val slug = item.optString("sl", "").ifEmpty {
                        item.optString("slug", "")
                    }
                    val image = item.optString("im", "").ifEmpty {
                        item.optString("image", "").ifEmpty {
                            item.optString("poster", "")
                        }
                    }

                    val href = if (slug.isNotBlank()) "$mainUrl/anime/$slug-anime" else ""
                    if (title.isNotBlank() && href.isNotBlank()) {
                        results.add(
                            newTvSeriesSearchResponse(title, href, TvType.Anime) {
                                this.posterUrl = fixUrlNull(image)
                            }
                        )
                    }
                }
            }
            results
        } catch (e: Exception) {
            Log.e("AnimeGratis", "Error parsing SSR JSON: ${e.message}")
            results
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/directorio?q=$query"
        Log.d("AnimeGratis", "search URL: $url")
        val document = app.get(url).document

        val ssrScript = document.selectFirst("script#ssr-init")
        if (ssrScript != null) {
            val items = parseSsrJson(ssrScript.data())
            if (items.isNotEmpty()) return items
        }

        return document.select("a.block, a.inline-block").mapNotNull { el ->
            val img = el.selectFirst("img") ?: return@mapNotNull null
            val title = el.selectFirst("p, h3, span.font-semibold, span.text-sm")?.text()?.trim()
                ?: img.attr("alt")?.trim()
            val href = el.attr("href")
            val poster = img.attr("src").ifEmpty { img.attr("data-src") }
            if (title.isNullOrBlank() || href.isBlank()) null
            else newTvSeriesSearchResponse(title, fixUrl(href), TvType.Anime) {
                this.posterUrl = fixUrlNull(poster)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("AnimeGratis", "load URL: $url")
        val document = app.get(url).document

        val title = document.selectFirst("h1")?.ownText()?.trim()
            ?: document.selectFirst("h1")?.text()?.trim()
            ?: "Unknown"
        Log.d("AnimeGratis", "title: $title")

        val poster = document.selectFirst("meta[property='og:image']")?.attr("content")
            ?: document.selectFirst("img[alt*='Poster']")?.attr("src")
            ?: document.selectFirst("img.rounded-lg")?.attr("src")
            ?: ""
        Log.d("AnimeGratis", "poster: $poster")

        val description = document.select("div.mx-3.mt-4 p, p.text-white\\/80").firstOrNull()?.text()
            ?: document.selectFirst("meta[name='description']")?.attr("content")
            ?: ""

        val tags = document.select("a[href*='/directorio/genero/']").map { it.text() }

        val statusText = document.select("header div.flex span").firstOrNull()?.text()
        val showStatus = when (statusText?.lowercase()) {
            "en emisión", "emitiendo", "ongoing" -> ShowStatus.Ongoing
            "finalizado", "completo", "completed" -> ShowStatus.Completed
            else -> null
        }

        val epInfo = document.selectFirst("header div.flex span.text-white\\/70")?.text()
        val year = Regex("(\\d{4})").find(epInfo ?: "")?.groupValues?.get(1)?.toIntOrNull()

        val episodes = mutableListOf<Episode>()

        document.select("#episode-select option").forEach { opt ->
            val epHref = fixUrl(opt.attr("value"))
            val epText = opt.text().trim()
            val epNum = Regex("(\\d+)").find(epText)?.groupValues?.get(1)?.toIntOrNull()
            if (epHref.isNotBlank() && !epHref.endsWith("-anime")) {
                episodes.add(
                    newEpisode(epHref) {
                        this.name = epText
                        this.episode = epNum
                    }
                )
            }
        }

        if (episodes.isEmpty()) {
            document.select("a.episode-chip").forEach { chip ->
                val epHref = fixUrl(chip.attr("href"))
                val epText = chip.attr("title").ifEmpty { "Episodio ${chip.text().trim()}" }
                val epNum = Regex("(\\d+)").find(chip.text())?.groupValues?.get(1)?.toIntOrNull()
                if (epHref.isNotBlank()) {
                    episodes.add(
                        newEpisode(epHref) {
                            this.name = epText
                            this.episode = epNum
                        }
                    )
                }
            }
        }

        if (episodes.isEmpty()) {
            document.select("a[href*='/episodio-']").forEach { a ->
                val epHref = fixUrl(a.attr("href"))
                val epText = a.attr("title").ifEmpty { a.text().trim() }
                val epNum = Regex("episodio-(\\d+)").find(epHref)?.groupValues?.get(1)?.toIntOrNull()
                    ?: Regex("(\\d+)").find(epText)?.groupValues?.get(1)?.toIntOrNull()
                if (epHref.isNotBlank() && !epHref.contains("-anime")) {
                    episodes.add(
                        newEpisode(epHref) {
                            this.name = epText.ifEmpty { "Episodio $epNum" }
                            this.episode = epNum
                        }
                    )
                }
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
