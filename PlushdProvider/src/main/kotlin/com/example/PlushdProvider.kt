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

    private fun base64Decode(string: String): String {
        return try {
            val cleanString = string.trim().replace("\n", "").replace("\r", "")
            Base64.decode(cleanString, Base64.DEFAULT).toString(Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e("PlusHD", "Log: Error decodificando Base64: ${e.message}")
            ""
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document
        var linksFound = false

        val servers = doc.select(".bg-tabs ul li")
        Log.d("PlusHD", "Log: Servidores encontrados: ${servers.size}")

        servers.forEach { it ->
            val dataServer = it.attr("data-server")
            if (dataServer.isBlank()) return@forEach

            try {
                val decode = base64Decode(dataServer)

                val url = if (!decode.startsWith("http")) {
                    "$mainUrl/player/$dataServer"
                } else {
                    decode
                }

                val videoUrl = if (url.contains("/player/")) {
                    Log.d("PlusHD", "Log: Intentando extraer de player -> $url")

                    val res = app.get(url, headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36",
                        "Referer" to data,
                        "Accept-Language" to "es-MX,es;q=0.9"
                    ))

                    val playerDoc = res.text

                    val foundLink = Regex("""https?://[^\s"'<>]+""").findAll(playerDoc)
                        .map { m -> m.value.replace("\\/", "/") }
                        .firstOrNull { link ->
                            val l = link.lowercase()
                            l.contains("wish") || l.contains("vidhide") || l.contains("filemoon") ||
                                    l.contains("stream") || l.contains("embed") || l.contains("player")
                        } ?: ""

                    if (foundLink.isBlank() && playerDoc.contains("eval(function")) {
                        Log.d("PlusHD", "Log: Detectado código ofuscado (Packer), buscando IDs...")
                        Regex("""['"]https?['"]\s*\+\s*['"]://[^'"]+""").find(playerDoc)?.value
                            ?.replace("\"", "")?.replace("'", "")?.replace(" ", "")?.replace("+", "") ?: ""
                    } else {
                        foundLink
                    }
                } else {
                    url
                }

                if (videoUrl.isNotBlank() && videoUrl.startsWith("http")) {
                    val fixedUrl = videoUrl.substringBefore("&url=").substringBefore("?url=")
                        .replace(Regex("""(\?|&)(id|m)=.*"""), "")

                    Log.i("PlusHD", "Log: ¡EXITO! Enviando a extractor: $fixedUrl")

                    loadExtractor(fixedUrl, "$mainUrl/", subtitleCallback, callback)
                    linksFound = true
                } else {
                    Log.w("PlusHD", "Log: No se encontró link válido en: $url")
                }
            } catch (e: Exception) {
                Log.e("PlusHD", "Log: Error procesando servidor: ${e.message}")
            }
        }
        return linksFound
    }

}