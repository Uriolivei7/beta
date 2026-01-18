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
    override var mainUrl = "https://ww3.tioplus.net"
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
        return url
            .replace("vidhideplus.com", "vidhide.com")
            .replace("vidhidepro.com", "vidhide.com")
            .replace("vudeo.co", "vudeo.net")
            .replace("luluvideo.com", "luluvdo.com")
            .replace("filemoon.to", "filemoon.sx")
            .replace("filemoon.link", "filemoon.sx")
            .replace("waaw.to", "netu.to") // WAAW es Netu
            .replace("emturbovid.com/t/", "emturbovid.com/v/")
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
                        seasonsMap.values.forEach { list ->
                            list.forEach { info ->
                                val epTitle = info.title ?: "Episodio"
                                val seasonNum = info.season ?: 1
                                val epNum = info.episode ?: 1
                                val img = info.image
                                val realimg = if (img.isNullOrEmpty()) null else "https://image.tmdb.org/t/p/w342${img.replace("\\/", "/")}"

                                epi.add(newEpisode("$url/season/$seasonNum/episode/$epNum") {
                                    this.name = epTitle
                                    this.season = seasonNum
                                    this.episode = epNum
                                    this.posterUrl = realimg
                                })
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
                newTvSeriesLoadResponse(title, url, tvType, epi) {
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
        val tag = "PlushdProvider"
        val linkRegex = Regex("window\\.location\\.href\\s*=\\s*'([^']*)'")

        val customExtractors = listOf(
            PelisplusUpnsPro(),
            PelisplusUpnsPro2(),
            PelisplusUpnsPro3(),
            PelisplusRpmStream(),
            EmturbovidCom(),
            VidhideCustom(),
            LuluvdoCustom(),
            VudeoCustom()
        )

        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Referer" to data
        )

        val doc = app.get(data, headers = headers).document
        val servers = doc.select("div ul.subselect li").toList()

        servers.forEach { serverLi ->
            try {
                val serverData = serverLi.attr("data-server") ?: return@forEach
                val encodedTwo = base64Encode(serverData.toByteArray())
                val playerUrl = "$mainUrl/player/$encodedTwo"

                val text = app.get(playerUrl, headers = headers).text
                val link = linkRegex.find(text)?.destructured?.component1()

                if (!link.isNullOrBlank()) {
                    val fixedLink = fixPelisplusHostsLinks(link)
                    Log.i(tag, "Enlace extraído: $fixedLink")

                    var extractorFound = false

                    for (extractor in customExtractors) {
                        val domainMatch = extractor.mainUrl.replace("https://", "").replace("http://", "")
                        if (fixedLink.contains(domainMatch)) {
                            extractor.getUrl(fixedLink, playerUrl, subtitleCallback, callback)
                            Log.d(tag, "Extractor manual detectó: ${extractor.name}")
                            extractorFound = true
                            break
                        }
                    }

                    if (!extractorFound) {
                        loadExtractor(fixedLink, playerUrl, subtitleCallback, callback)
                    }

                    linksFound = true
                }
            } catch (e: Exception) {
                Log.e(tag, "Error en servidor: ${e.message}")
            }
            delay(1000L)
        }
        return linksFound
    }
    
}