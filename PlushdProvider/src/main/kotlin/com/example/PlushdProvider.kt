package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element
import android.util.Base64
import kotlinx.coroutines.delay
import java.net.URL

class PlushdProvider : MainAPI() {
    override var mainUrl = "https://tioplus.app"
    override var name = "PlusHD"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Anime,
        TvType.Cartoon,
        TvType.Movie,
        TvType.AsianDrama,
    )

    override val mainPage = mainPageOf(
        "series" to "Series",
        "animes" to "Animes",
        "peliculas" to "Peliculas",
        "doramas" to "Doramas",
    )

    private fun base64Encode(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun fixPelisplusHostsLinks(url: String): String {
        var fixedUrl = url

        val hostReplacements = mapOf(
            "https://hglink.to" to "https://streamwish.to",
            "https://swdyu.com" to "https://streamwish.to",
            "https://cybervynx.com" to "https://streamwish.to",
            "https://dumbalag.com" to "https://streamwish.to",
            "https://earnvids.com" to "https://streamwish.to",
            "https://awish.pro" to "https://streamwish.to",

            "https://mivalyo.com" to "https://vidhidepro.com",
            "https://dinisglows.com" to "https://vidhidepro.com",
            "https://dhtpre.com" to "https://vidhidepro.com",
            "https://mobilefast.to" to "https://vidhidepro.com",
            "https://vidhide.com" to "https://vidhidepro.com",

            "https://filemoon.link" to "https://filemoon.sx",
            "https://filemoon.to" to "https://filemoon.sx",
            "https://moon.app" to "https://filemoon.sx",
            "https://plusto.app" to "https://filemoon.sx",
            "https://callistanise.com" to "https://filemoon.sx",

            "https://sblona.com" to "https://watchsb.com",
            "https://lulu.st" to "https://lulustream.com",
            "https://uqload.io" to "https://uqload.com",
            "https://do7go.com" to "https://dood.la",
            "https://doodstream.com" to "https://dood.la",
            "https://streamtape.com" to "https://streamtape.cc"
        )

        hostReplacements.forEach { (old, new) ->
            if (fixedUrl.startsWith(old)) {
                fixedUrl = fixedUrl.replaceFirst(old, new)
            }
        }

        return fixedUrl
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data}/$page").document
        val home = document.select(".articlesList article").filter { !it.selectFirst("a")?.attr("target").equals("_blank") }
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

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst("a h2")?.text() ?: return null
        val link = this.selectFirst("a.itemA")?.attr("href") ?: return null
        val img = this.selectFirst("picture img")?.attr("data-src")

        val searchType = when {
            link.contains("/serie") -> TvType.TvSeries
            link.contains("/anime") -> TvType.Anime
            link.contains("/pelicula") -> TvType.Movie
            link.contains("/dorama") -> TvType.AsianDrama
            else -> TvType.Movie
        }

        return when (searchType) {
            TvType.Movie -> newMovieSearchResponse(title, link, searchType) { this.posterUrl = img }
            else -> newTvSeriesSearchResponse(title, link, searchType) { this.posterUrl = img }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/api/search/$query").document
        val results = document.select("article.item").mapNotNull { it.toSearchResult() }
        return results
    }


    data class MainTemporada(val elements: Map<String, List<MainTemporadaElement>>)

    data class MainTemporadaElement(
        val title: String? = null,
        val image: String? = null,
        val season: Int? = null,
        val episode: Int? = null
    )

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document

        val tvType = when {
            url.contains("/serie") -> TvType.TvSeries
            url.contains("/anime") -> TvType.Anime
            url.contains("/pelicula") -> TvType.Movie
            url.contains("/dorama") -> TvType.AsianDrama
            else -> TvType.TvSeries
        }

        val title = doc.selectFirst(".slugh1")?.text() ?: ""
        val backimage = doc.selectFirst("head meta[property=og:image]")?.attr("content") ?: ""
        val poster = backimage.replace("original", "w500")
        val description = doc.selectFirst("div.description")?.text() ?: ""
        val tags = doc.select("div.home__slider .genres:contains(Generos) a").map { it.text() }
        val epi = ArrayList<Episode>()

        if (tvType != TvType.Movie) {
            val script = doc.select("script").firstOrNull { it.html().contains("seasonsJson = ") }?.html()
            if (!script.isNullOrEmpty()) {
                val jsonRegex = Regex("seasonsJson\\s*=\\s*(\\{[^;]*\\});")
                val match = jsonRegex.find(script)
                val jsonscript: String? = match?.groupValues?.get(1)

                if (!jsonscript.isNullOrEmpty()) {
                    try {
                        val seasonsMap = parseJson<Map<String, List<MainTemporadaElement>>>(jsonscript)
                        seasonsMap.values.map { list ->
                            list.map { info ->
                                val epTitle = info.title
                                val seasonNum = info.season
                                val epNum = info.episode
                                val img = info.image
                                val realimg =
                                    if (img.isNullOrEmpty()) null else "https://image.tmdb.org/t/p/w342${
                                        img.replace(
                                            "\\/",
                                            "/"
                                        )
                                    }"
                                val epurl = "$url/season/$seasonNum/episode/$epNum"
                                if (epTitle != null && seasonNum != null && epNum != null) {
                                    epi.add(
                                        newEpisode(epurl) {
                                            this.name = epTitle
                                            this.season = seasonNum
                                            this.episode = epNum
                                            this.posterUrl = realimg
                                        }
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("PlushdProvider", "Error al parsear seasonsJson: ${e.message}")
                    }
                }
            }
        }

        return when (tvType) {
            TvType.TvSeries, TvType.Anime, TvType.AsianDrama -> {
                newTvSeriesLoadResponse(
                    title,
                    url, tvType, epi,
                ) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backimage
                    this.plot = description
                    this.tags = tags
                }
            }
            TvType.Movie -> {
                newMovieLoadResponse(title, url, tvType, url) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backimage
                    this.plot = description
                    this.tags = tags
                }
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
        var linksFound = false
        val doc = app.get(data).document

        Log.d("PlushdProvider", "--- Iniciando carga de links ---")
        Log.d("PlushdProvider", "URL de la página: $data")

        val servers = doc.select("ul.subselect li")
        Log.d("PlushdProvider", "Servidores encontrados en HTML: ${servers.size}")

        servers.forEach { serverLi ->
            try {
                val serverName = serverLi.text()
                val serverData = serverLi.attr("data-server")

                if (serverData.isNullOrEmpty()) {
                    Log.w("PlushdProvider", "Servidor $serverName no tiene data-server")
                    return@forEach
                }

                val encodedTwo = base64Encode(serverData.toByteArray())
                val playerUrl = "$mainUrl/player/$encodedTwo"
                Log.d("PlushdProvider", "Procesando: $serverName -> Player URL: $playerUrl")

                val playerRes = app.get(playerUrl, headers = mapOf("Referer" to data))
                val playerHtml = playerRes.text
                val playerDoc = playerRes.document

                val linkRegex = Regex("window\\.location\\.href\\s*=\\s*'([^']*)'")
                var finalLink = linkRegex.find(playerHtml)?.groupValues?.get(1)

                if (finalLink.isNullOrBlank()) {
                    val iframe = playerDoc.selectFirst("iframe")
                    finalLink = iframe?.attr("src") ?: iframe?.attr("data-src")
                    Log.d("PlushdProvider", "Link extraído de iframe: $finalLink")
                } else {
                    Log.d("PlushdProvider", "Link extraído de Redirect JS: $finalLink")
                }

                if (!finalLink.isNullOrBlank()) {
                    val cleanLink = if (finalLink.startsWith("/")) "$mainUrl$finalLink" else finalLink
                    val fixedLink = fixPelisplusHostsLinks(cleanLink)

                    Log.d("PlushdProvider", "URL Final enviada al extractor: $fixedLink")

                    loadExtractor(
                        url = fixedLink,
                        referer = playerUrl,
                        subtitleCallback = subtitleCallback,
                        callback = callback
                    )
                    linksFound = true
                } else {
                    Log.e("PlushdProvider", "No se pudo encontrar ningún enlace de video en el player de $serverName")
                }
            } catch (e: Exception) {
                Log.e("PlushdProvider", "Error en servidor ${serverLi.text()}: ${e.message}")
            }
        }
        return linksFound
    }
}