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
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Anime,
        TvType.Movie
    )

    private val TAG = "MetroSeries"
    
    val headers = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Referer" to "$mainUrl/",
        "Connection" to "keep-alive"
    )

    private fun Element.getMetroPoster(): String? {
        val img = this.selectFirst(".post-thumbnail figure img, .post-thumbnail img") ?: return null

        val lazyPoster = img.attr("data-lazy-src").takeIf { it.isNotBlank() }
        val normalPoster = img.attr("src").takeIf { it.isNotBlank() }

        val finalPoster = if (lazyPoster != null && !lazyPoster.contains("data:image")) {
            lazyPoster
        } else {
            normalPoster
        }

        return fixUrlNull(finalPoster)
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()
        val sections = listOf(
            Pair("Últimas series agregadas", ".series article"),
            Pair("Ultimas peliculas agregadas", ".movies article")
        )

        val soup = app.get(mainUrl, headers = headers).document

        sections.forEach { (name, selector) ->
            val home = soup.select(selector).mapNotNull { element ->
                val title = element.selectFirst(".entry-title")?.text() ?: return@mapNotNull null
                val href = element.selectFirst("a.lnk-blk")?.attr("abs:href")
                    ?: element.selectFirst("a")?.attr("abs:href") ?: return@mapNotNull null

                val poster = element.getMetroPoster()

                Log.d(TAG, "Logs: [DEBUG MAIN] $title -> $poster")

                newTvSeriesSearchResponse(title, href, if (href.contains("pelicula")) TvType.Movie else TvType.TvSeries) {
                    this.posterUrl = poster
                }
            }
            if (home.isNotEmpty()) items.add(HomePageList(name, home))
        }
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: Buscando: $query")
        val url = "$mainUrl/?s=$query"
        val document = app.get(url, headers = headers).document

        return document.select("div#aa-movies article.post, article.post").mapNotNull { element ->
            val title = element.selectFirst(".entry-title")?.text() ?: return@mapNotNull null
            val href = element.selectFirst("a.lnk-blk")?.attr("abs:href")
                ?: element.selectFirst("a")?.attr("abs:href") ?: return@mapNotNull null

            val poster = element.getMetroPoster()

            Log.d(TAG, "Logs: [DEBUG SEARCH] $title -> $poster")

            newTvSeriesSearchResponse(title, href, if (href.contains("pelicula")) TvType.Movie else TvType.TvSeries) {
                this.posterUrl = poster
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Logs: [2026-01-22] Cargando metadatos de: $url")
        val soup = app.get(url, headers = headers).document

        val title = soup.selectFirst("article.post header.entry-header h1")?.text() ?: ""
        val description = soup.select("article.post aside div.description p").text().trim()
        val year = soup.selectFirst("article.post header.entry-header span.year")?.text()?.toIntOrNull()

        val poster = soup.getMetroPoster()
        val backposter = soup.selectFirst("div.bghd img.TPostBg")?.attr("abs:src") ?: poster

        val tags = soup.select("span.genres a").map { it.text() }
        val tvType = if (url.contains("pelicula")) TvType.Movie else TvType.TvSeries

        val recommendations = soup.select(".serie.sm").mapNotNull { element ->
            val recTitle = element.selectFirst(".entry-title")?.text() ?: return@mapNotNull null
            val recUrl = element.selectFirst("a.lnk-blk")?.attr("abs:href") ?: return@mapNotNull null
            val recPoster = element.getMetroPoster()

            newTvSeriesSearchResponse(recTitle, recUrl, TvType.TvSeries) {
                this.posterUrl = recPoster
            }
        }

        val episodes = ArrayList<Episode>()
        if (tvType == TvType.TvSeries) {
            val datapost = soup.select("li.sel-temp a").attr("data-post")
            val seasons = soup.select("li.sel-temp a").map { it.attr("data-season") }

            coroutineScope {
                seasons.map { season ->
                    async(Dispatchers.IO) {
                        try {
                            val response = app.post("$mainUrl/wp-admin/admin-ajax.php", data = mapOf(
                                "action" to "action_select_season",
                                "season" to season,
                                "post" to datapost
                            ), referer = url).document

                            response.select("a").forEach { ep ->
                                val epHref = ep.attr("abs:href")
                                episodes.add(newEpisode(epHref) {
                                    this.season = season.toIntOrNull()
                                    this.name = ep.text()
                                })
                            }
                        } catch (e: Exception) { }
                    }
                }.awaitAll()
            }
        }

        return if (tvType == TvType.Movie) {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backposter
                this.plot = description
                this.tags = tags
            }
        } else {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backposter
                this.plot = description
                this.tags = tags
                this.year = year
                this.recommendations = recommendations
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