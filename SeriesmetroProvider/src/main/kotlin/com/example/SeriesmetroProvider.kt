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
        val title = this.selectFirst(".entry-header .entry-title")?.text() ?: return null
        val href = this.selectFirst(".lnk-blk")?.attr("abs:href") ?: return null
        val posterUrl = this.selectFirst(".post-thumbnail figure img")?.let {
            if (it.hasAttr("data-src")) it.attr("abs:data-src") else it.attr("abs:src")
        }

        return if (href.contains("pelicula")) {
            newMovieSearchResponse(title, href, TvType.Movie) { this.posterUrl = posterUrl }
        } else {
            newTvSeriesSearchResponse(title, href, TvType.TvSeries) { this.posterUrl = posterUrl }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: Buscando: $query")
        val document = app.get("$mainUrl/?s=$query").document
        return document.select(".post").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Logs: Cargando metadatos de: $url")
        val doc = app.get(url).document

        val title = doc.selectFirst("aside .entry-header .entry-title")?.text() ?: ""
        val poster = doc.selectFirst(".post-thumbnail img")?.attr("abs:src")?.replace("/w185/", "/w500/")
        val description = doc.select("aside .description p:not([class])").joinToString { it.text() }
        val genres = doc.select(".genres a").map { it.text() }

        return if (url.contains("pelicula")) {
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
                                val epNumber = ep.select(".entry-header .num-epi").text()
                                    .substringAfter("x").substringBefore("–").trim().toIntOrNull()

                                synchronized(episodes) {
                                    episodes.add(newEpisode(epHref) {
                                        this.name = "T$seasonNum - E$epNumber"
                                        this.season = seasonNum.toIntOrNull()
                                        this.episode = epNumber
                                    })
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Logs: Error cargando temporada: ${e.message}")
                        }
                    }
                }.awaitAll()
            }

            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes.sortedBy { it.episode }) {
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
        Log.d(TAG, "Logs: Iniciando carga de links")
        val doc = app.get(data).document
        var linksFound = false

        coroutineScope {
            val requests = doc.select(".aa-tbs-video a").map { option ->
                async(Dispatchers.IO) {
                    val serverName = option.select(".server").text()
                    val id = option.attr("href")
                    val iframe = doc.select("$id iframe").firstOrNull()
                    val iframeUrl = iframe?.attr("data-src")?.replace("#038;", "&") ?: ""

                    if (iframeUrl.isNotEmpty()) {
                        loadExtractor(iframeUrl, data, subtitleCallback) { link ->
                            val extractorLink = try {
                                runBlocking {
                                    newExtractorLink(
                                        source = link.source,
                                        name = "$serverName ${link.name}",
                                        url = link.url,
                                        type = link.type
                                    ) {
                                        this.referer = link.referer
                                        this.quality = link.quality
                                        this.headers = link.headers
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error creando link: ${e.message}")
                                null
                            }

                            extractorLink?.let {
                                callback.invoke(it)
                                linksFound = true
                            }
                        }
                    }
                }
            }
            requests.awaitAll()
        }
        return linksFound
    }
}