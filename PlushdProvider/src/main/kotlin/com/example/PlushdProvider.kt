package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element
import android.util.Base64
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
        return url
            .replace(Regex("https://.*(vidhide|mivalyo|dinisglows|dhtpre|callistanise|pixibay).*\\.(com|pro|cc|sx|plus|pre)"), "https://vidhidepro.com")
            .replace(Regex("https://(lulu\\.st|lulustream\\.com|luluvdo\\.com)"), "https://lulustream.com")
            .replace(Regex("https://(hglink\\.to|swdyu\\.com|cybervynx\\.com|dumbalag\\.com|awish\\.pro|streamwish\\.to)"), "https://streamwish.to")
            .replaceFirst("https://emturbovid.com", "https://turbovid.eu")
            .replaceFirst("https://filemoon.link", "https://filemoon.sx")
            .replaceFirst("https://sblona.com", "https://watchsb.com")
            .replaceFirst("https://uqload.io", "https://uqload.com")
            .replaceFirst("https://do7go.com", "https://dood.la")
            .replaceFirst("https://doodstream.com", "https://dood.la")
            .replaceFirst("https://streamtape.com", "https://streamtape.cc")
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
        val linkRegex = Regex("window\\.location\\.href\\s*=\\s*'([^']*)'")

        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Referer" to data
        )

        Log.d("PlushdProvider", "Cargando página de enlaces: $data")
        val doc = app.get(data, headers = headers).document

        doc.select("div ul.subselect li").toList().forEach { serverLi ->
            try {
                val serverData = serverLi.attr("data-server")
                if (serverData.isNullOrEmpty()) {
                    Log.w("PlushdProvider", "Servidor vacío o sin data-server")
                    return@forEach
                }

                val encodedOne = serverData.toByteArray()
                val encodedTwo = base64Encode(encodedOne)
                val playerUrl = "$mainUrl/player/$encodedTwo"

                Log.d("PlushdProvider", "Procesando servidor en: $playerUrl")
                val text = app.get(playerUrl, headers = headers).text

                if (text.contains("bloqueo temporal")) {
                    Log.w("PlushdProvider", "BLOQUEO TEMPORAL detectado en PlusHD")
                    return@forEach
                }

                val link = linkRegex.find(text)?.destructured?.component1()

                if (!link.isNullOrBlank()) {
                    val fixedLink = fixPelisplusHostsLinks(link)
                    Log.d("PlushdProvider", "Enlace extraído y corregido: $fixedLink")

                    val extractorReferer = try {
                        val urlObject = URL(fixedLink)
                        urlObject.protocol + "://" + urlObject.host + "/"
                    } catch (e: Exception) {
                        playerUrl
                    }

                    loadExtractor(
                        url = fixedLink,
                        referer = extractorReferer,
                        subtitleCallback = subtitleCallback,
                        callback = { link ->

                            val isVidhide = listOf("vidhide", "pixibay", "callistanise", "streamwish", "mivalyo", "awish")
                                .any { link.url.contains(it, ignoreCase = true) || link.source.contains(it, ignoreCase = true) }

                            Log.d("PlushdProvider", "Extractor encontró: ${link.name} (EsVidhide: $isVidhide)")

                            if (isVidhide) {
                                val finalLink = runBlocking {
                                    newExtractorLink(
                                        source = link.source,
                                        name = link.name,
                                        url = link.url,
                                        type = link.type,
                                    ) {
                                        this.quality = link.quality
                                        this.referer = fixedLink
                                        this.headers = mapOf(
                                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
                                            "Origin" to "https://tioplus.app",
                                            "Accept" to "*/*",
                                            "Connection" to "keep-alive"
                                        )
                                    }
                                }
                                callback.invoke(finalLink)
                            } else {
                                callback.invoke(link)
                            }
                            linksFound = true
                        }
                    )
                } else {
                    Log.w("PlushdProvider", "No se encontró URL de redirección en el player")
                }
            } catch (e: Exception) {
                Log.e("PlushdProvider", "Error procesando servidor: ${e.message}")
            }
            delay(500L)
        }
        return linksFound
    }
}