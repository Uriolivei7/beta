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
        val cleanUrl = url.replace("/#", "/").replace("#", "").trim()

        return when {
            // Vidhide -> va a callistanise.com/v/
            cleanUrl.contains("vidhide") || cleanUrl.contains("vidhidepro") -> {
                val id = cleanUrl.split("/").last { it.isNotBlank() }
                "https://callistanise.com/v/$id"
            }
            // EmTurbovid -> va a turbovidhls.com/t/
            cleanUrl.contains("emturbovid") -> {
                val id = cleanUrl.split("/").last { it.isNotBlank() }
                "https://turbovidhls.com/t/$id"
            }
            // HQQ/Netu -> mantener waaw.to
            cleanUrl.contains("hqq.tv") || cleanUrl.contains("netu") -> {
                // Cambiar hqq.tv a waaw.to si es necesario
                cleanUrl.replace("hqq.tv", "waaw.to")
            }
            // waaw.to se queda igual
            cleanUrl.contains("waaw.to") -> cleanUrl
            // Listeamed se queda igual
            cleanUrl.contains("listeamed") -> cleanUrl
            // UPNS
            cleanUrl.contains("upns.pro") -> cleanUrl
            // strp2p
            cleanUrl.contains("strp2p.com") -> cleanUrl
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

        // 2. Poster Vertical (Buscando la imagen principal de la serie)
        var verticalPoster = doc.select(".data img, .poster img, picture img").firstNotNullOfOrNull {
            val src = it.attr("data-src").ifBlank { it.attr("src") }

            if (src.isNotBlank() &&
                src != backimage &&
                src.contains("tmdb.org") &&
                !src.contains("/episodes/") &&
                !src.contains("/seasons/") && // <--- ESTO descarta las fotos de temporadas
                !src.contains("logo")
            ) {
                src
            } else null
        }?.replace("original", "w342")

        // 3. Si lo anterior falló, intentamos sacar la imagen del Search (que suele ser la primera en el HTML)
        if (verticalPoster.isNullOrBlank()) {
            verticalPoster = doc.selectFirst(".itemA img, picture img")?.let {
                val src = it.attr("data-src").ifBlank { it.attr("src") }
                if (!src.contains("/seasons/") && !src.contains("/episodes/")) src else null
            }?.replace("original", "w342") ?: backimage.replace("original", "w342")
        }

        Log.d("PlushdProvider", "Poster FINAL (Debe ser el del Search): $verticalPoster")
        Log.d("PlushdProvider", "Background FINAL (Horizontal): $backimage")

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

        // Múltiples patrones para capturar URLs
        val urlPatterns = listOf(
            Regex("""window\.location\.href\s*=\s*['"]([^'"]+)['"]"""),
            Regex("""location\.href\s*=\s*['"]([^'"]+)['"]"""),
            Regex("""window\.open\s*\(\s*['"]([^'"]+)['"]"""),
            Regex("""src\s*=\s*['"]([^'"]*(?:embed|player|e/)[^'"]+)['"]"""),
            Regex("""iframe[^>]+src\s*=\s*['"]([^'"]+)['"]"""),
            Regex("""file\s*:\s*['"]([^'"]+\.m3u8[^'"]*)['"]"""),
            Regex("""source\s*:\s*['"]([^'"]+)['"]"""),
            Regex(""""url"\s*:\s*"([^"]+)""""),
            Regex("""https?://[^\s'"<>]+(?:\.m3u8|\.mp4|/embed/|/e/|/player/)[^\s'"<>]*""")
        )

        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Referer" to data,
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8"
        )

        val doc = app.get(data, headers = headers).document
        val servers = doc.select("div ul.subselect li")

        Log.d("PlushdProvider", "═══════════════════════════════════════")
        Log.d("PlushdProvider", "URL: $data")
        Log.d("PlushdProvider", "Servidores encontrados: ${servers.size}")

        val loggingSubtitleCallback: (SubtitleFile) -> Unit = { file ->
            Log.d("PlushdProvider", "📝 Subtítulo: ${file.url}")
            subtitleCallback.invoke(file)
        }

        servers.forEachIndexed { index, serverLi ->
            val serverName = serverLi.text().trim()

            try {
                val serverData = serverLi.attr("data-server")
                if (serverData.isNullOrEmpty()) {
                    Log.w("PlushdProvider", "[$index] $serverName - Sin data-server")
                    return@forEachIndexed
                }

                Log.d("PlushdProvider", "[$index] $serverName")
                Log.d("PlushdProvider", "  📋 data-server: $serverData")

                // Codificar a Base64 para la API del player
                val encodedData = Base64.encodeToString(
                    serverData.toByteArray(Charsets.UTF_8),
                    Base64.NO_WRAP
                )
                val playerUrl = "$mainUrl/player/$encodedData"

                Log.d("PlushdProvider", "  🔗 Player URL: $playerUrl")

                // Hacer petición al player
                val playerResponse = try {
                    app.get(playerUrl, headers = headers, timeout = 15)
                } catch (e: Exception) {
                    Log.e("PlushdProvider", "  ❌ Error en petición: ${e.message}")
                    return@forEachIndexed
                }

                val responseText = playerResponse.text

                // Verificar bloqueo temporal
                if (responseText.contains("bloqueo temporal", ignoreCase = true)) {
                    Log.w("PlushdProvider", "  ⚠️ Bloqueo temporal detectado")
                    delay(2000L) // Esperar más tiempo
                    return@forEachIndexed
                }

                // Buscar URLs con todos los patrones
                val foundUrls = mutableSetOf<String>()

                for (pattern in urlPatterns) {
                    pattern.findAll(responseText).forEach { match ->
                        val url = match.groupValues.getOrNull(1) ?: match.value
                        if (url.startsWith("http") && !url.contains("javascript")) {
                            foundUrls.add(url.trim())
                        }
                    }
                }

                Log.d("PlushdProvider", "  🔍 URLs encontradas: ${foundUrls.size}")

                if (foundUrls.isEmpty()) {
                    // Intentar parsear como HTML y buscar iframes
                    val playerDoc = playerResponse.document
                    playerDoc.select("iframe[src]").forEach { iframe ->
                        val iframeSrc = iframe.attr("src")
                        if (iframeSrc.startsWith("http")) {
                            foundUrls.add(iframeSrc)
                        }
                    }

                    // Buscar en scripts
                    playerDoc.select("script").forEach { script ->
                        val scriptText = script.html()
                        for (pattern in urlPatterns) {
                            pattern.findAll(scriptText).forEach { match ->
                                val url = match.groupValues.getOrNull(1) ?: match.value
                                if (url.startsWith("http")) {
                                    foundUrls.add(url.trim())
                                }
                            }
                        }
                    }
                }

                // Procesar cada URL encontrada
                foundUrls.forEach { rawUrl ->
                    try {
                        val fixedLink = fixPelisplusHostsLinks(rawUrl)
                        Log.d("PlushdProvider", "  ➜ Procesando: $fixedLink")

                        val extractorReferer = try {
                            val urlObject = java.net.URL(fixedLink)
                            "${urlObject.protocol}://${urlObject.host}/"
                        } catch (e: Exception) {
                            mainUrl
                        }

                        // Si es un enlace directo m3u8/mp4, agregarlo directamente
                        if (fixedLink.contains(".m3u8") || fixedLink.contains(".mp4")) {
                            val quality = when {
                                fixedLink.contains("1080") -> 1080
                                fixedLink.contains("720") -> 720
                                fixedLink.contains("480") -> 480
                                fixedLink.contains("360") -> 360
                                else -> Qualities.Unknown.value
                            }

                            callback(
                                newExtractorLink(
                                    source = this.name,
                                    name = "$serverName - Directo",
                                    url = fixedLink,
                                    type = if (fixedLink.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                                ) {
                                    this.referer = extractorReferer
                                    this.quality = quality
                                }
                            )
                            allLinks.add(
                                newExtractorLink(
                                    source = this.name,
                                    name = serverName,
                                    url = fixedLink,
                                    type = if (fixedLink.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                                ) {
                                    this.referer = extractorReferer
                                    this.quality = quality
                                }
                            )
                            Log.d("PlushdProvider", "    ✅ Enlace directo agregado")
                        } else {
                            // Usar extractor
                            val tempLinks = mutableListOf<ExtractorLink>()

                            loadExtractor(
                                url = fixedLink,
                                referer = extractorReferer,
                                subtitleCallback = loggingSubtitleCallback,
                                callback = { link ->
                                    tempLinks.add(link)
                                    callback(link)
                                    Log.d("PlushdProvider", "    ✅ ${link.name} | ${link.quality}p")
                                }
                            )

                            allLinks.addAll(tempLinks)

                            if (tempLinks.isEmpty()) {
                                Log.w("PlushdProvider", "    ⚠️ Extractor no devolvió enlaces")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("PlushdProvider", "    ❌ Error procesando URL: ${e.message}")
                    }
                }

                if (foundUrls.isEmpty()) {
                    Log.w("PlushdProvider", "  ⚠️ No se encontraron URLs en la respuesta")
                    // Debug: mostrar parte de la respuesta
                    Log.d("PlushdProvider", "  📄 Respuesta (primeros 500 chars): ${responseText.take(500)}")
                }

            } catch (e: Exception) {
                Log.e("PlushdProvider", "[$index] $serverName - Error general: ${e.message}")
                e.printStackTrace()
            }

            // Delay entre servidores para evitar rate limiting
            if (index < servers.size - 1) {
                delay(1000L)
            }
        }

        Log.d("PlushdProvider", "═══════════════════════════════════════")
        Log.d("PlushdProvider", "TOTAL ENLACES: ${allLinks.size}")
        Log.d("PlushdProvider", "═══════════════════════════════════════")

        return allLinks.isNotEmpty()
    }

}