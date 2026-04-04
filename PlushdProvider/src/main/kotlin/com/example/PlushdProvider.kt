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
        var title = this.selectFirst("a h2")?.text() ?: return null
        val link = this.selectFirst("a.itemA")?.attr("href") ?: return null
        val img = this.selectFirst("picture img")?.attr("data-src")

        val yearRegex = Regex("""\s*\((\d{4})\)$""")
        val match = yearRegex.find(title)
        val year = match?.groupValues?.get(1)?.toIntOrNull()

        if (match != null) {
            title = title.replace(yearRegex, "").trim()
        }

        val searchType = when {
            link.contains("/serie") -> TvType.TvSeries
            link.contains("/anime") -> TvType.Anime
            link.contains("/pelicula") -> TvType.Movie
            link.contains("/dorama") -> TvType.AsianDrama
            else -> TvType.Movie
        }

        return when (searchType) {
            TvType.Movie -> newMovieSearchResponse(title, link, searchType) {
                this.posterUrl = img
                this.year = year
            }
            else -> newTvSeriesSearchResponse(title, link, searchType) {
                this.posterUrl = img
                this.year = year
            }
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

        var title = doc.selectFirst(".slugh1")?.text() ?: ""
        val yearRegex = Regex("""\s*\((\d{4})\)$""")
        val match = yearRegex.find(title)
        val year = match?.groupValues?.get(1)?.toIntOrNull()

        if (match != null) {
            title = title.replace(yearRegex, "").trim()
        }

        val backimage = doc.selectFirst(".bg")?.attr("style")?.let {
            Regex("url\\(\"?(.*?)\"?\\)").find(it)?.groupValues?.get(1)
        } ?: doc.selectFirst("head meta[property=og:image]")?.attr("content") ?: ""

        var verticalPoster = doc.select(".poster img, .data img").firstNotNullOfOrNull {
            val src = it.attr("data-src").ifBlank { it.attr("src") }

            if (src.isNotBlank() &&
                !src.contains("nGfjgUlES2WuYrHXNNF4fbGe2Eq") &&
                src.contains("tmdb.org") &&
                !src.contains("/episodes/") &&
                !src.contains("/seasons/")
            ) {
                src
            } else null
        }?.replace("original", "w342")

        if (verticalPoster.isNullOrBlank()) {
            verticalPoster = doc.selectFirst(".poster img")?.attr("src")?.replace("original", "w342") ?: backimage
        }

        val description = doc.selectFirst("div.description")?.text() ?: ""
        val tags = doc.select("div.home__slider .genres:contains(Generos) a").map { it.text() }
        val epi = ArrayList<Episode>()

        if (tvType != TvType.Movie) {
            val script = doc.select("script").firstOrNull { it.html().contains("seasonsJson = ") }?.html()
            if (!script.isNullOrEmpty()) {
                val jsonRegex = Regex("seasonsJson\\s*=\\s*(\\{[^;]*\\});")
                val matchJson = jsonRegex.find(script)
                val jsonscript = matchJson?.groupValues?.get(1)

                if (!jsonscript.isNullOrEmpty()) {
                    try {
                        val seasonsMap = parseJson<Map<String, List<MainTemporadaElement>>>(jsonscript)
                        seasonsMap.values.forEach { list ->
                            list.forEach { info ->
                                val epTitle = info.title
                                val seasonNum = info.season
                                val epNum = info.episode
                                val img = info.image
                                val realimg = if (img.isNullOrEmpty()) null else "https://image.tmdb.org/t/p/w342${img.replace("\\/", "/")}"
                                val epurl = "$url/season/$seasonNum/episode/$epNum"
                                if (epTitle != null && seasonNum != null && epNum != null) {
                                    epi.add(newEpisode(epurl) {
                                        this.name = epTitle
                                        this.season = seasonNum
                                        this.episode = epNum
                                        this.posterUrl = realimg
                                    })
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
                newTvSeriesLoadResponse(title, url, tvType, epi) {
                    this.posterUrl = verticalPoster
                    this.backgroundPosterUrl = backimage
                    this.plot = description
                    this.tags = tags
                    this.year = year
                }
            }
            TvType.Movie -> {
                newMovieLoadResponse(title, url, tvType, url) {
                    this.posterUrl = verticalPoster
                    this.backgroundPosterUrl = backimage
                    this.plot = description
                    this.tags = tags
                    this.year = year
                }
            }
            else -> null
        }
    }

    private fun fixPelisplusHostsLinks(url: String): String {
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
            .replaceFirst("https://doodstream.com", "https://dood.la")
            .replaceFirst("https://streamtape.com", "https://streamtape.cc")
    }

    private fun extractStreamUrl(text: String): String? {
        val patterns = listOf(
            Regex("""window\.location\.href\s*=\s*['"]([^'"]+)['"]"""),
            Regex("""source:\s*['"]([^'"]+)['"]"""),
            Regex("""file:\s*['"]([^'"]+\.(mp4|m3u8|webm))['"]""", RegexOption.IGNORE_CASE),
            Regex("""url:\s*['"]([^'"]+)['"]"""),
            Regex("""src:\s*['"]([^'"]+)['"]"""),
            Regex("""video.*?src\s*[=:]\s*['"]([^'"]+)['"]""", RegexOption.IGNORE_CASE),
            Regex("""https?://[^\s'"]+\.(mp4|m3u8|webm)[^\s'"]*""", RegexOption.IGNORE_CASE),
            Regex("""player\.src\s*\(\s*['"]([^'"]+)['"]"""),
            Regex("""['"](https?://[^\s'"]+)['"].*?type.*?['"]application/vnd\.apple\.mpegurl['"]"""),
        )

        for (pattern in patterns) {
            val match = pattern.find(text)
            if (match != null) {
                val url = match.groupValues.getOrNull(1)
                if (!url.isNullOrBlank() && (url.startsWith("http") || url.contains("/"))) {
                    return url
                }
            }
        }
        return null
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

        val doc = app.get(data, headers = headers).document

        val loggingSubtitleCallback: (SubtitleFile) -> Unit = { file ->
            Log.d("PlushdProvider", "Subtítulo encontrado. URL: ${file.url}")
            subtitleCallback.invoke(file)
        }

        doc.select("div ul.subselect li").toList().forEach { serverLi ->
            try {
                val serverData = serverLi.attr("data-server")
                if (serverData.isNullOrEmpty()) return@forEach

                val playerUrl = "$mainUrl/player/$serverData"

                val text = app.get(playerUrl, headers = headers).text

                Log.d("PlushdProvider", "Player URL: $playerUrl")
                Log.d("PlushdProvider", "Player response (first 500 chars): ${text.take(500)}")

                if (text.contains("bloqueo temporal")) {
                    Log.w("PlushdProvider", "ADVERTENCIA: Bloqueo temporal detectado. Saltando servidor.")
                    return@forEach
                }

                if (text.contains("PLAYERHD")) {
                    Log.w("PlushdProvider", "Respuesta inválida: PLAYERHD. Intentando extraer URL del HTML original.")
                    
                    val playerDoc = app.get(playerUrl, headers = headers).document
                    Log.d("PlushdProvider", "Player HTML sample: ${playerDoc.html().take(2000)}")
                    
                    val dataTr = playerDoc.selectFirst("[data-tr]")?.attr("data-tr")
                        ?: playerDoc.selectFirst("#player-tr")?.attr("data-tr")
                        ?: playerDoc.selectFirst(".video-html")?.attr("data-tr")
                    
                    if (!dataTr.isNullOrBlank()) {
                        val trDecoded = String(Base64.decode(dataTr, Base64.NO_WRAP))
                        Log.d("PlushdProvider", "data-tr from player page: $dataTr -> decoded: $trDecoded")
                    } else {
                        Log.w("PlushdProvider", "No data-tr found in player page either")
                    }
                    
                    val episodeDoc = app.get(data, headers = headers).document
                    val dataTr2 = episodeDoc.selectFirst("#player-tr")?.attr("data-tr")
                    Log.d("PlushdProvider", "data-tr from episode page: $dataTr2")
                    
                    return@forEach
                }

                val link = linkRegex.find(text)?.destructured?.component1()

                val streamUrl = if (link.isNullOrBlank()) {
                    extractStreamUrl(text)
                } else {
                    link
                }

                if (!streamUrl.isNullOrBlank()) {
                    val fixedLink = fixPelisplusHostsLinks(streamUrl)

                    val extractorReferer = try {
                        val urlObject = URL(fixedLink)
                        urlObject.protocol + "://" + urlObject.host + "/"
                    } catch (e: Exception) {
                        Log.e("PlushdProvider", "Error al parsear URL para Referer: ${e.message}. Usando playerUrl como fallback.")
                        playerUrl
                    }

                    loadExtractor(
                        url = fixedLink,
                        referer = extractorReferer,
                        subtitleCallback = loggingSubtitleCallback,
                        callback = callback
                    )
                    linksFound = true
                }
            } catch (e: Exception) {
                Log.e("PlushdProvider", "Error al procesar el servidor: ${e.message}")
            }

            delay(1500L)
        }

        return linksFound
    }

}