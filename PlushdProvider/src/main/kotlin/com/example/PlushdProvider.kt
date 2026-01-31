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

        // 2. Poster Vertical (Simplificado para evitar el aviso del IDE)
        var verticalPoster = doc.select(".data img, .poster img, picture img").firstNotNullOfOrNull {
            val src = it.attr("data-src").ifBlank { it.attr("src") }
            // Filtramos: que no sea el fondo, que sea de tmdb y que no sea un logo
            if (src.isNotBlank() && src != backimage && src.contains("tmdb.org") && !src.contains("logo")) {
                src
            } else null
        }?.replace("original", "w342")

        // 3. Fallback: Si no encontró nada, intenta buscar por el atributo alt o usa el fondo
        if (verticalPoster.isNullOrBlank()) {
            verticalPoster = doc.selectFirst("img[alt*='$title'], .itemA img")?.let {
                it.attr("data-src").ifBlank { it.attr("src") }
            }?.replace("original", "w342") ?: backimage
        }

        Log.d("PlushdProvider", "Poster FINAL para Favoritos: $verticalPoster")
        Log.d("PlushdProvider", "Background para Ficha: $backimage")

        val description = doc.selectFirst("div.description")?.text() ?: ""
        val tags = doc.select("div.home__slider .genres:contains(Generos) a").map { it.text() }
        val epi = ArrayList<Episode>()

        // ... (tu lógica de episodios se queda igual)
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

        servers.forEachIndexed { index, serverLi ->
            val serverName = serverLi.text().trim()
            try {
                val serverData = serverLi.attr("data-server").takeIf { it.isNotEmpty() } ?: return@forEachIndexed

                Log.d("PlushdProvider", "[$index] $serverName")
                Log.d("PlushdProvider", "  📋 data-server (base64): $serverData")

                // ⭐ DECODIFICAR BASE64 DOS VECES
                try {
                    // Primera decodificación
                    val decoded1 = Base64.decode(serverData, Base64.DEFAULT)
                    val decoded1Str = String(decoded1, Charsets.UTF_8)
                    Log.d("PlushdProvider", "  🔓 Primera decodificación: $decoded1Str")

                    // Segunda decodificación
                    val decoded2 = Base64.decode(decoded1Str, Base64.DEFAULT)
                    val finalUrl = String(decoded2, Charsets.UTF_8)
                    Log.d("PlushdProvider", "  🔓 Segunda decodificación (URL FINAL): $finalUrl")

                    if (finalUrl.startsWith("http")) {
                        val fixedLink = fixPelisplusHostsLinks(finalUrl)
                        Log.d("PlushdProvider", "  ✅ Link procesado: $fixedLink")

                        val extractorReferer = when {
                            fixedLink.contains("vidhide") -> mainUrl
                            fixedLink.contains("upns.pro") -> "https://pelisplus.upns.pro/"
                            fixedLink.contains("strp2p.com") -> "https://pelisplus.strp2p.com/"
                            fixedLink.contains("emturbovid.com") -> "https://emturbovid.com/"
                            fixedLink.contains("listeamed.net") -> "https://listeamed.net/"
                            else -> fixedLink
                        }

                        try {
                            val tempLinks = mutableListOf<ExtractorLink>()

                            loadExtractor(
                                url = fixedLink,
                                referer = extractorReferer,
                                subtitleCallback = subtitleCallback,
                                callback = { link ->
                                    tempLinks.add(link)
                                    Log.d("PlushdProvider", "    ➜ Link: ${link.name} | ${link.quality}p")
                                }
                            )

                            if (tempLinks.isNotEmpty()) {
                                allLinks.addAll(tempLinks)
                                Log.d("PlushdProvider", "  ✅ $serverName: ${tempLinks.size} enlaces")
                            } else {
                                Log.w("PlushdProvider", "  ⚠️ $serverName: 0 enlaces")
                            }
                        } catch (e: Exception) {
                            Log.e("PlushdProvider", "  ❌ Error extractor: ${e.message}")
                        }
                    } else {
                        Log.w("PlushdProvider", "  ⚠️ URL decodificada inválida: $finalUrl")
                    }

                } catch (e: Exception) {
                    Log.e("PlushdProvider", "  ❌ Error decodificando base64: ${e.message}")
                }

            } catch (e: Exception) {
                Log.e("PlushdProvider", "  ❌ Error general: ${e.message}")
                e.printStackTrace()
            }

            if (index < servers.size - 1) delay(300L)
        }

        Log.d("PlushdProvider", "═══════════════════════════════════════")
        Log.d("PlushdProvider", "TOTAL ENLACES CAPTURADOS: ${allLinks.size}")

        allLinks.forEach { link ->
            Log.d("PlushdProvider", "📤 Enviando: ${link.name} | ${link.url}")
            callback(link)
        }

        Log.d("PlushdProvider", "═══════════════════════════════════════")

        return allLinks.isNotEmpty()
    }

}