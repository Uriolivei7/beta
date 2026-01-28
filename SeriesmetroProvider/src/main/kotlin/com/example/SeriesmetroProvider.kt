package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.jsoup.nodes.Element

class SeriesmetroProvider : MainAPI() {
    override var mainUrl = "https://www3.seriesmetro.net"
    override var name = "SeriesMetro"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Movie,
        TvType.Anime
    )

    private val TAG = "MetroSeries"

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "Logs: Cargando página principal: $page")
        return try {
            val items = ArrayList<HomePageList>()
            val sections = listOf(
                Pair("Series Recientes", "$mainUrl/cartelera-series/page/$page"),
                Pair("Películas", "$mainUrl/genero/pelicula/page/$page")
            )

            sections.forEach { (title, url) ->
                val doc = app.get(url).document
                val res = doc.select(".post").mapNotNull {
                    val sTitle = it.selectFirst(".entry-header .entry-title")?.text() ?: return@mapNotNull null
                    val sHref = it.selectFirst(".lnk-blk")?.attr("abs:href") ?: return@mapNotNull null
                    val sPoster = it.selectFirst(".post-thumbnail figure img")?.let { img ->
                        if (img.hasAttr("data-src")) img.attr("abs:data-src") else img.attr("abs:src")
                    }

                    if (sHref.contains("pelicula")) {
                        newMovieSearchResponse(sTitle, sHref, TvType.Movie) { this.posterUrl = sPoster }
                    } else {
                        newTvSeriesSearchResponse(sTitle, sHref, TvType.TvSeries) { this.posterUrl = sPoster }
                    }
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
        // Selector más flexible para el título
        val title = this.selectFirst(".entry-title")?.text() ?: return null
        val href = this.selectFirst("a.lnk-blk, a")?.attr("abs:href") ?: return null

        // CORRECCIÓN CRÍTICA DE POSTER:
        // 1. Buscamos el tag img
        val img = this.selectFirst(".post-thumbnail img")

        // 2. Intentamos sacar la URL de varios atributos posibles
        var posterUrl = img?.attr("abs:data-src").takeIf { !it.isNullOrBlank() }
            ?: img?.attr("abs:src")
            ?: img?.attr("abs:srcset")?.substringBefore(" ") // Para casos como American Carnage

        // 3. Si la URL empieza con //, le agregamos https:
        if (posterUrl?.startsWith("//") == true) {
            posterUrl = "https:$posterUrl"
        }

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

        fun fixUrl(url: String?): String? {
            if (url == null) return null
            return if (url.startsWith("//")) "https:$url" else url
        }

        val title = doc.selectFirst(".entry-header .entry-title, h1.entry-title")?.text() ?: ""

        val posterElement = doc.selectFirst(".post.single .post-thumbnail img, .post-thumbnail img")
        val poster = fixUrl(posterElement?.attr("src"))?.replace("/w185/", "/w500/")

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

                                val epThumb = fixUrl(ep.selectFirst("img")?.attr("src"))

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

        doc.select(".aa-tbs-video a").forEach { option ->
            val serverName = option.text()
            val targetId = option.attr("href").replace("#", "")

            val container = doc.selectFirst("div#$targetId")
            val iframe = container?.selectFirst("iframe")

            var iframeUrl = iframe?.attr("abs:data-src").takeIf { !it.isNullOrBlank() }
                ?: iframe?.attr("abs:src") ?: ""

            if (iframeUrl.isNotBlank()) {
                if (iframeUrl.contains("seriesmetro.net/ir/")) {
                    iframeUrl = app.get(iframeUrl).document.selectFirst("iframe")?.attr("abs:src") ?: ""
                }

                loadExtractor(iframeUrl, data, subtitleCallback, callback)
            }
        }
        return true
    }
}