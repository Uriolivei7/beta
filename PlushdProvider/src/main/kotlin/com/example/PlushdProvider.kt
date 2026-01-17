package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element
import android.util.Base64
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
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

    private fun fixPelisplusHostsLinks(url: String): String {
        val trimmedUrl = url.trim().trimEnd('/')
        val id = trimmedUrl.substringAfterLast("/")

        val vidhideMirrors = Regex("vidhide|mivalyo|dinisglows|dhtpre|callistanise|pixibay|vidhideplus|allmistery|entervideo")

        return when {
            trimmedUrl.contains(vidhideMirrors) -> "https://vidhidepro.com/v/$id"
            trimmedUrl.contains("vudeo.co") -> "https://vudeo.co/$id"
            trimmedUrl.contains("waaw.to") || trimmedUrl.contains("hqq.to") -> "https://hqq.to/watch/$id"
            trimmedUrl.contains(Regex("lulu\\.st|lulustream\\.com|luluvdo\\.com")) ->
                "https://lulustream.com/e/$id".replace("/e/e/", "/e/")
            trimmedUrl.contains(Regex("hglink\\.to|swdyu\\.com|cybervynx\\.com|dumbalag\\.com|awish\\.pro|streamwish\\.to")) ->
                "https://streamwish.to/e/$id".replace("/e/e/", "/e/")
            trimmedUrl.contains("rpmstream") -> "https://pelisplus.upns.pro/$id"
            trimmedUrl.contains("emturbovid") || trimmedUrl.contains("turbovid") -> "https://turbovid.eu/$id"

            else -> trimmedUrl
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
        val stableUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

        val doc = app.get(data).document
        val servers = doc.select("div ul.subselect li")

        servers.forEach { serverLi ->
            val sData = serverLi.attr("data-server")
            val sName = serverLi.selectFirst("span")?.text() ?: "Server"

            try {
                if (sData.isNullOrEmpty()) return@forEach
                val playerUrl = "$mainUrl/player/${base64Encode(sData.toByteArray())}"

                // Timeout equilibrado para no congelar pero dar tiempo a Vidhide
                val text = app.get(playerUrl, referer = data, timeout = 30).text
                val link = linkRegex.find(text)?.destructured?.component1()

                if (!link.isNullOrBlank()) {
                    val fixedLink = fixPelisplusHostsLinks(link)
                    Log.d("PlushdProvider", "Link Detectado ($sName): $fixedLink")

                    // Intentamos extractor oficial
                    val found = loadExtractor(fixedLink, fixedLink, subtitleCallback, callback)

                    // FORZAMOS EL LINK SIEMPRE (Incluso si loadExtractor dice que no o falla)
                    // Esto garantiza que en el episodio 7 aparezca Earnvids sí o sí
                    if (fixedLink.contains("vidhide") || fixedLink.contains("lulustream") || fixedLink.contains("upns.pro")) {
                        callback.invoke(
                            ExtractorLink(
                                source = sName,
                                name = "$sName (Directo)",
                                url = fixedLink,
                                referer = fixedLink,
                                quality = Qualities.P1080.value,
                                type = ExtractorLinkType.VIDEO,
                                headers = mapOf(
                                    "User-Agent" to stableUserAgent,
                                    "Connection" to "keep-alive"
                                )
                            )
                        )
                        linksFound = true
                    } else if (found) {
                        linksFound = true
                    }
                }
            } catch (e: Exception) {
                Log.e("PlushdProvider", "Error en $sName: ${e.message}")
            }
        }
        return linksFound
    }
}