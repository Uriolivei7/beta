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
        if (url.isBlank()) return url
        // Quitamos el # y cualquier espacio
        val cleanUrl = url.replace("/#", "/").replace("#", "").trim()

        return when {
            cleanUrl.contains("vidhide") || cleanUrl.contains("mivalyo") -> {
                val id = cleanUrl.split("/").last { it.isNotBlank() }
                "https://vidhidepro.com/e/$id" // Vidhide SÍ usa /e/
            }
            cleanUrl.contains("upns.pro") -> {
                val id = cleanUrl.split("/").last { it.isNotBlank() }
                "https://pelisplus.upns.pro/$id" // Upstream NO usa /e/ habitualmente aquí
            }
            cleanUrl.contains("strp2p.com") -> {
                val id = cleanUrl.split("/").last { it.isNotBlank() }
                "https://pelisplus.strp2p.com/$id"
            }
            cleanUrl.contains("emturbovid") -> {
                val id = cleanUrl.split("/").last { it.isNotBlank() }
                "https://emturbovid.com/e/$id"
            }
            // Para Netu/HQQ que vimos en tu log (waaw.to)
            cleanUrl.contains("waaw.to") || cleanUrl.contains("hqq.tv") -> {
                val id = cleanUrl.split("/").last { it.isNotBlank() }
                "https://hqq.tv/e/$id"
            }
            else -> cleanUrl
        }
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
            Log.d("PlushdProvider", "Título limpiado: $title | Año detectado: $year")
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
                this.year = year // Asignamos el año aquí
            }
            else -> newTvSeriesSearchResponse(title, link, searchType) {
                this.posterUrl = img
                this.year = year // Asignamos el año aquí
            }
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

        var title = doc.selectFirst(".slugh1")?.text() ?: ""
        val yearRegex = Regex("""\s*\((\d{4})\)$""")
        val match = yearRegex.find(title)
        val year = match?.groupValues?.get(1)?.toIntOrNull()

        if (match != null) {
            title = title.replace(yearRegex, "").trim()
        }

        // 1. Imagen de Fondo (Banner Horizontal)
        val backimage = doc.selectFirst("head meta[property=og:image]")?.attr("content") ?: ""

        // 2. Poster Vertical (El que quieres para Favoritos)
        // Buscamos específicamente la imagen que está en la columna de datos (izquierda)
        var verticalPoster = doc.selectFirst(".data .poster img, .data img, .itemA img")?.let {
            it.attr("data-src").ifBlank { it.attr("src") }
        }

        // Si el selector anterior falló o nos dio la misma imagen horizontal,
        // buscamos cualquier imagen que NO sea el backimage
        if (verticalPoster.isNullOrBlank() || verticalPoster == backimage) {
            verticalPoster = doc.select("img").firstOrNull {
                val src = it.attr("data-src").ifBlank { it.attr("src") }
                src.isNotBlank() && src != backimage && !src.contains("logo")
            }?.let { it.attr("data-src").ifBlank { it.attr("src") } } ?: backimage
        }

        Log.d("PlushdProvider", "Poster capturado para Favoritos: $verticalPoster")

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

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val allLinks = mutableListOf<ExtractorLink>()

        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
            "Referer" to "$mainUrl/"
        )

        val doc = app.get(data, headers = headers).document
        val servers = doc.select("div ul.subselect li")

        Log.d("PlushdProvider", "═══════════════════════════════════════")
        Log.d("PlushdProvider", "Servidores encontrados: ${servers.size}")

        // SOLO PROBAR EL PRIMER SERVIDOR PARA DEBUG
        val serverLi = servers.firstOrNull() ?: return false
        val serverName = serverLi.text().trim()

        try {
            val serverData = serverLi.attr("data-server")
            val playerUrl = "$mainUrl/player/$serverData"

            Log.d("PlushdProvider", "Testing: $serverName")
            Log.d("PlushdProvider", "data-server: $serverData")
            Log.d("PlushdProvider", "playerUrl: $playerUrl")

            val playerResponse = app.get(playerUrl, headers = mapOf("Referer" to data))
            val text = playerResponse.text

            Log.d("PlushdProvider", "═══════════════════════════════════════")
            Log.d("PlushdProvider", "RESPUESTA COMPLETA DEL PLAYER:")
            Log.d("PlushdProvider", text)
            Log.d("PlushdProvider", "═══════════════════════════════════════")

            // Intentar diferentes patrones
            val patterns = listOf(
                Regex("window\\.location\\.href\\s*=\\s*'([^']*)'"),
                Regex("window\\.location\\.href\\s*=\\s*\"([^\"]*)\""),
                Regex("window\\.location\\s*=\\s*'([^']*)'"),
                Regex("window\\.location\\s*=\\s*\"([^\"]*)\""),
                Regex("location\\.href\\s*=\\s*'([^']*)'"),
                Regex("location\\.href\\s*=\\s*\"([^\"]*)\""),
                Regex("https?://[^\\s'\"<>]+(?:vidhide|upns|emturbovid|listeamed|hqq)[^\\s'\"<>]+")
            )

            for ((index, pattern) in patterns.withIndex()) {
                val match = pattern.find(text)
                if (match != null) {
                    val link = match.groupValues.getOrNull(1) ?: match.value
                    Log.d("PlushdProvider", "Patrón $index encontró: $link")
                }
            }

        } catch (e: Exception) {
            Log.e("PlushdProvider", "Error: ${e.message}")
            e.printStackTrace()
        }

        return false
    }

}