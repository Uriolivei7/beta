package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.jsoup.nodes.Element

class CinehdplusProvider : MainAPI() {
    override var mainUrl = "https://cinehdplus.org"
    override var name = "CineHD+"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.Cartoon,
    )

    override val mainPage = mainPageOf(
        "series/" to "Series",
        "peliculas/" to "Peliculas",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data}/page/$page").document
        val home = document.select("div.container main div.card__cover:not(.placebo)")
            .mapNotNull { it.toSearchResult() }
        return newHomePageResponse(
            list = HomePageList(
                name = request.name,
                list = home,
                isHorizontalImages = false
            ),
            hasNext = true
        )
    }

    private fun Element.toSearchResult(): SearchResponse {
        val title = this.select("a img").attr("alt")
        val href = this.select("a").attr("href")
        val posterUrl = fixUrlNull(this.select("a img").attr("src"))
        return newMovieSearchResponse(title, href, TvType.Movie) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("${mainUrl}/?s=$query").document
        val results =
            document.select("div.container div.card__cover").mapNotNull { it.toSearchResult() }
        return results
    }

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document
        val tvType = if (url.contains("/peliculas")) TvType.Movie else TvType.TvSeries
        val title = doc.selectFirst(".details__title h1")?.text()
        val plot = doc.selectFirst("head meta[property=og:description]")?.attr("content")
        val year = doc.selectFirst(".sub-meta span[itemprop=dateCreated]")?.text()?.toIntOrNull()
        val poster = doc.selectFirst(".details__cover figure img")?.attr("data-src")
        val backimage = doc.selectFirst("section.section div.backdrop img")?.attr("src")
        val tags = doc.selectFirst(".details__list li")?.text()?.substringAfter(":")?.split(",")
        val trailer = doc.selectFirst("#OptYt iframe")?.attr("data-src")?.replaceFirst("https://www.youtube.com/embed/","https://www.youtube.com/watch?v=")
        val recommendations = doc.select("div.container div.card__cover").mapNotNull { it.toSearchResult() }

        val episodes = doc.select("div.tab-content div.episodios-todos").flatMap { seasonElement ->
            val seasonNum = seasonElement.attr("id").replaceFirst("season-", "").toIntOrNull()
            seasonElement.select(".episodios_list li").mapIndexed { idx, epi ->
                val epUrl = epi.selectFirst("a")?.attr("href") ?: ""
                val epImgElement = epi.selectFirst("figure img")
                val epTitle = epImgElement?.attr("alt")
                val epImg = epImgElement?.attr("src")

                newEpisode(epUrl) {
                    this.name = epTitle
                    this.season = seasonNum
                    this.episode = idx + 1
                    this.posterUrl = epImg
                }
            }.toList()
        }

        return when (tvType) {
            TvType.Movie -> newMovieLoadResponse(title ?: "", url, TvType.Movie, url) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backimage ?: poster
                this.plot = plot
                this.tags = tags
                this.year = year
                this.recommendations = recommendations
                addTrailer(trailer)
            }
            TvType.TvSeries -> newTvSeriesLoadResponse(
                title ?: "",
                url, tvType, episodes,
            ) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backimage ?: poster
                this.plot = plot
                this.tags = tags
                this.year = year
                this.recommendations = recommendations
                addTrailer(trailer)
            }
            else -> null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document
        val options = doc.select("li.clili")

        options.forEach { element ->
            val lang = element.attr("data-lang")
            val optId = element.attr("data-tplayernv")
            val frame = doc.selectFirst("div#$optId")?.selectFirst("iframe")?.attr("data-src")
                ?.substringAfter("player.php?h=")?.substringBefore("&")

            if (frame != null) {
                val apiBase = mainUrl.replaceFirst("https://", "https://api.")
                try {
                    val gotoDoc = app.get("$apiBase/ir/goto.php?h=$frame").document
                    val form1 = gotoDoc.selectFirst("form")
                    val url1 = form1?.selectFirst("input#url")?.attr("value")

                    if (url1 != null) {
                        val rdDoc = app.post("$apiBase/ir/rd.php", data = mapOf("url" to url1)).document
                        val form2 = rdDoc.selectFirst("form")
                        val url2 = form2?.selectFirst("input#url")?.attr("value")

                        if (url2 != null) {
                            val ddhDoc = app.post("$apiBase/ir/redir_ddh.php", data = mapOf("url" to url2, "dl" to "0")).document
                            val form3 = ddhDoc.selectFirst("form")
                            val actionUrl = form3?.attr("action")
                            val vid = form3?.selectFirst("input#vid")?.attr("value")
                            val hash = form3?.selectFirst("input#hash")?.attr("value")

                            if (actionUrl != null && vid != null && hash != null) {
                                val finalDoc = app.post(actionUrl, data = mapOf("vid" to vid, "hash" to hash)).document
                                val script = finalDoc.selectFirst("script:containsData(link =)")?.html()
                                val encoded = script?.substringAfter("link = '")?.substringBefore("';")

                                if (encoded != null) {
                                    val link = base64Decode(encoded)
                                    loadSourceNameExtractor(
                                        lang,
                                        fixHostsLinks(link),
                                        "$mainUrl/",
                                        subtitleCallback,
                                        callback
                                    )
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("CineHD", "Error extrayendo link de $lang: ${e.message}")
                }
            }
        }
        return true
    }
}

suspend fun loadSourceNameExtractor(
    source: String,
    url: String,
    referer: String? = null,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit,
) {
    loadExtractor(url, referer, subtitleCallback) { link ->
        val idiomaLabel = when {
            source.lowercase().contains("lat") -> "Latino"
            source.lowercase().contains("es") || source.lowercase().contains("cast") -> "Castellano"
            else -> source
        }

        val calidadLabel = if (link.quality > 0) "${link.quality}p" else ""
        val nombreFinal = "CineHD+ $idiomaLabel [${link.source}] $calidadLabel".trim()

        kotlinx.coroutines.runBlocking {
            val extractorLink = newExtractorLink(
                source = nombreFinal,
                name = nombreFinal,
                url = link.url,
                type = link.type
            ) {
                this.quality = link.quality
                this.referer = link.referer ?: referer ?: ""
                this.headers = link.headers
                this.extractorData = link.extractorData
            }
            callback.invoke(extractorLink)
        }
    }
}

fun fixHostsLinks(url: String): String {
    return url
        .replaceFirst("https://hglink.to", "https://streamwish.to")
        .replaceFirst("https://swdyu.com", "https://streamwish.to")
        .replaceFirst("https://cybervynx.com", "https://streamwish.to")
        .replaceFirst("https://dumbalag.com", "https://streamwish.to")
        .replaceFirst("https://mivalyo.com", "https://vidhidepro.com")
        .replaceFirst("https://dinisglows.com", "https://vidhidepro.com")
        .replaceFirst("https://dhtpre.com", "https://vidhidepro.com")
        .replaceFirst("https://filemoon.link", "https://filemoon.sx")
        .replaceFirst("https://sblona.com", "https://watchsb.com")
        .replaceFirst("https://lulu.st", "https://lulustream.com")
        .replaceFirst("https://uqload.io", "https://uqload.com")
        .replaceFirst("https://do7go.com", "https://dood.la")
}