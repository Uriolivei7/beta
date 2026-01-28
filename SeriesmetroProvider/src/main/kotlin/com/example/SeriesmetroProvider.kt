package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jsoup.nodes.Element

class SeriesmetroProvider : MainAPI() {
    override var mainUrl = "https://www3.seriesmetro.net"
    override var name = "SeriesMetro"
    override var lang = "es" // Cambiado a es para mejor compatibilidad
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Movie
    )

    private val TAG = "MetroSeries"

    private fun fixImg(url: String?): String? {
        if (url.isNullOrBlank()) return null
        val cleanUrl = url.trim()
        return when {
            cleanUrl.startsWith("//") -> "https:$cleanUrl"
            cleanUrl.startsWith("/") -> "$mainUrl$cleanUrl"
            else -> cleanUrl
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "Logs: Cargando página principal: $page")
        return try {
            val items = ArrayList<HomePageList>()
            val sections = listOf(
                Pair("Series", "$mainUrl/cartelera-series/page/$page"),
                Pair("Películas", "$mainUrl/cartelera-peliculas/page/$page")
            )

            sections.forEach { (title, url) ->
                val doc = app.get(url, headers = mapOf("Referer" to "$mainUrl/")).document
                val res = doc.select("article.post, .post").mapNotNull {
                    it.toSearchResult()
                }
                if (res.isNotEmpty()) items.add(HomePageList(title, res))
            }
            newHomePageResponse(items)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en getMainPage: ${e.message}")
            null
        }
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst(".entry-title")?.text() ?: return null

        var href = this.selectFirst("a")?.attr("href") ?: return null
        if (href.startsWith("/")) href = "$mainUrl$href"

        val imgElement = this.selectFirst("img")
        val rawImg = imgElement?.attr("data-src").takeIf { !it.isNullOrBlank() }
            ?: imgElement?.attr("srcset")?.substringBefore(" ")
            ?: imgElement?.attr("src")

        val posterUrl = fixImg(rawImg)

        Log.d(TAG, "Logs: [DEBUG] Titulo: $title | Poster: $posterUrl")

        return if (href.contains("/pelicula/")) {
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = posterUrl
            }
        } else {
            newTvSeriesSearchResponse(title, href, TvType.TvSeries) {
                this.posterUrl = posterUrl
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: Buscando: $query")
        val document = app.get("$mainUrl/?s=$query").document
        return document.select(".post").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Logs: [2026-01-22] Cargando metadatos de: $url")
        val doc = app.get(url).document

        val title = doc.selectFirst(".entry-header .entry-title, h1.entry-title")?.text() ?: ""

        // Arreglo de poster en vista detalle
        val posterElement = doc.selectFirst(".post.single .post-thumbnail img, .post-thumbnail img")
        val poster = fixImg(posterElement?.attr("src"))?.replace("/w185/", "/w500/")

        val description = doc.select(".description p, .entry-content p").joinToString { it.text() }
        val genres = doc.select(".genres a").map { it.text() }

        return if (url.contains("/pelicula/")) {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = genres
            }
        } else {
            val episodes = ArrayList<Episode>()
            val seasonElements = doc.select(".sel-temp a")

            coroutineScope {
                seasonElements.map { season ->
                    async(Dispatchers.IO) {
                        try {
                            val seasonNum = season.attr("data-season")
                            val post = season.attr("data-post")

                            val response = app.post(
                                "$mainUrl/wp-admin/admin-ajax.php",
                                data = mapOf(
                                    "action" to "action_select_season",
                                    "season" to seasonNum,
                                    "post" to post
                                ),
                                referer = url
                            ).document

                            response.select(".post").reversed().forEach { ep ->
                                val epHref = ep.select("a").attr("abs:href")
                                val epText = ep.select(".num-epi").text()
                                val epNumber = epText.substringAfter("x").substringBefore("–").trim().toIntOrNull()

                                val epThumb = fixImg(ep.selectFirst("img")?.attr("src"))

                                synchronized(episodes) {
                                    episodes.add(newEpisode(epHref) {
                                        this.name = "T$seasonNum - E$epNumber"
                                        this.season = seasonNum.toIntOrNull()
                                        this.episode = epNumber
                                        this.posterUrl = epThumb
                                    })
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Logs: Error cargando temporada: ${e.message}")
                        }
                    }
                }.awaitAll()
            }

            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes.sortedWith(compareBy({ it.season }, { it.episode }))) {
                this.posterUrl = poster
                this.plot = description
                this.tags = genres
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document
        Log.d(TAG, "Logs: [2026-01-22] Analizando opciones en: $data")

        doc.select(".aa-tbs-video li a").forEach { option ->
            val langText = option.select(".server").text().lowercase()
            val qualityName = when {
                langText.contains("latino") -> "Latino"
                langText.contains("español") || langText.contains("castellano") -> "Castellano"
                langText.contains("vose") || langText.contains("sub") -> "Subtitulado"
                else -> "SD"
            }

            val targetId = option.attr("href").replace("#", "")
            val container = doc.selectFirst("div#$targetId")
            val iframeUrl = container?.selectFirst("iframe")?.attr("data-src")
                ?: container?.selectFirst("iframe")?.attr("src") ?: ""

            if (iframeUrl.isNotBlank()) {
                val fullIframeUrl = if (iframeUrl.startsWith("//")) "https:$iframeUrl" else iframeUrl

                coroutineScope {
                    if (fullIframeUrl.contains("seriesmetro.net") && (fullIframeUrl.contains("trembed") || fullIframeUrl.contains("/ir/"))) {
                        try {
                            val innerDoc = app.get(fullIframeUrl).document
                            val realPlayerUrl = innerDoc.selectFirst("iframe")?.attr("src")
                                ?: innerDoc.selectFirst("a")?.attr("href")

                            if (!realPlayerUrl.isNullOrBlank()) {
                                loadExtractor(realPlayerUrl, fullIframeUrl, subtitleCallback) { link ->
                                    launch {
                                        val renamedLink = newExtractorLink(
                                            source = link.source,
                                            name = "${link.name} [$qualityName]",
                                            url = link.url,
                                            type = link.type
                                        ) {
                                            this.quality = link.quality
                                            this.referer = link.referer
                                            this.headers = link.headers
                                            this.extractorData = link.extractorData
                                        }
                                        callback(renamedLink)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Logs: Error en carga: ${e.message}")
                        }
                    } else {
                        loadExtractor(fullIframeUrl, data, subtitleCallback) { link ->
                            launch {
                                val renamedLink = newExtractorLink(
                                    link.source,
                                    "${link.name} [$qualityName]",
                                    link.url,
                                    link.type
                                ) {
                                    this.quality = link.quality
                                    this.referer = link.referer
                                }
                                callback(renamedLink)
                            }
                        }
                    }
                }
            }
        }
        return true
    }
}