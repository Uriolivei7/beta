package com.example

import android.util.Base64
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.network.CloudflareKiller
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.nicehttp.NiceResponse
import kotlinx.coroutines.delay
import java.util.*

class LatanimeProvider : MainAPI() {
    companion object {
        fun getType(t: String): TvType {
            return if (t.contains("OVA") || t.contains("Especial")) TvType.OVA
            else if (t.contains("Pelicula")) TvType.AnimeMovie
            else TvType.Anime
        }

        fun getDubStatus(title: String): DubStatus {
            return if (title.contains("Latino") || title.contains("Castellano"))
                DubStatus.Dubbed
            else DubStatus.Subbed
        }

        private fun base64Decode(encoded: String): String {
            return try {
                String(Base64.decode(encoded, Base64.DEFAULT), Charsets.UTF_8)
            } catch (e: IllegalArgumentException) {
                Log.e("LatanimePlugin", "Error decoding Base64: ${e.message}")
                ""
            }
        }
    }

    override var mainUrl = "https://latanime.org"
    override var name = "LatAnime"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.AnimeMovie,
        TvType.OVA,
        TvType.Anime,
    )

    private val cloudflareKiller = CloudflareKiller()
    suspend fun appGetChildMainUrl(url: String): NiceResponse {
        return app.get(url, interceptor = cloudflareKiller, headers = cloudflareKiller.getCookieHeaders(mainUrl).toMap())
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val urls = listOf(
            Pair("$mainUrl/emision", "En emisión"),
            Pair("$mainUrl/animes?fecha=false&genero=false&letra=false&categoria=Película", "Películas"),
            Pair("$mainUrl/animes", "Animes"),
        )

        val items = ArrayList<HomePageList>()

        try {
            val mainDoc = app.get(mainUrl).document
            val carouselItems = mainDoc.select("div.carousel-item a[href*=/anime/], div.carousel-item a[href*=/pelicula/]").mapNotNull { element ->
                val itemUrl = element.attr("href")
                val title = element.selectFirst("span.span-slider")?.text()?.trim() ?: ""
                val description = element.selectFirst("p.p-slider")?.text()?.trim()

                val posterElement = element.selectFirst("img.preview-image, img.d-block")
                val poster = posterElement?.attr("data-src")?.ifBlank {
                    posterElement.attr("src")
                } ?: ""

                if (itemUrl.isNotBlank() && title.isNotBlank()) {
                    newAnimeSearchResponse(title, fixUrl(itemUrl)) {
                        this.posterUrl = fixUrl(poster)
                        addDubStatus(getDubStatus(title))
                    }
                } else null
            }

            if (carouselItems.isNotEmpty()) {
                items.add(HomePageList("Destacados", carouselItems))
            }

            urls.forEach { (url, name) ->
                val doc = app.get(url).document

                val home = doc.select("div.col-md-4, div.col-lg-3, div.col-xl-2, div.col-6").mapNotNull { article ->
                    val linkElement = article.selectFirst("a[href*=/anime/], a[href*=/pelicula/]")
                    if (linkElement == null) return@mapNotNull null

                    val itemUrl = linkElement.attr("href")
                    val title = linkElement.selectFirst("h3.my-1, h3")?.text()?.trim()
                        ?: linkElement.selectFirst("div.seriedetails h3")?.text()?.trim()
                        ?: linkElement.attr("title").trim()

                    if (itemUrl.isBlank() || title.isBlank()) {
                        Log.w("LatanimeProvider", "Elemento sin URL o título")
                        return@mapNotNull null
                    }

                    val imgElement = linkElement.selectFirst("img.img-fluid2, img.img-fluid, img")
                    val poster = imgElement?.let {
                        it.attr("data-src").ifBlank { it.attr("src") }
                    }?.let { imgUrl ->
                        when {
                            imgUrl.startsWith("http") -> imgUrl
                            imgUrl.startsWith("//") -> "https:$imgUrl"
                            imgUrl.startsWith("/") -> "$mainUrl$imgUrl"
                            else -> "$mainUrl/$imgUrl"
                        }
                    } ?: ""

                    newAnimeSearchResponse(title, fixUrl(itemUrl)) {
                        this.posterUrl = poster
                        addDubStatus(getDubStatus(title))
                    }
                }.filter { it.name.isNotBlank() }

                if (home.isNotEmpty()) {
                    items.add(HomePageList(name, home))
                }
            }

        } catch (e: Exception) {
            Log.e("LatanimeProvider", "ERROR en getMainPage: ${e.message}", e)
            throw ErrorLoadingException("Error al cargar la página principal: ${e.message}")
        }

        if (items.isEmpty()) {
            throw ErrorLoadingException("No se pudieron cargar elementos de la página principal.")
        }

        return newHomePageResponse(items)
    }

    private fun fixUrl(url: String): String {
        return when {
            url.startsWith("http") -> url
            url.startsWith("//") -> "https:$url"
            url.startsWith("/") -> "$mainUrl$url"
            else -> "$mainUrl/$url"
        }
    }

    private fun getDubStatus(title: String): DubStatus {
        return when {
            title.contains("Latino", ignoreCase = true) -> DubStatus.Dubbed
            title.contains("Castellano", ignoreCase = true) -> DubStatus.Dubbed
            title.contains("Subtitulado", ignoreCase = true) -> DubStatus.Subbed
            else -> DubStatus.Subbed
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = appGetChildMainUrl("$mainUrl/buscar?q=$query").document
        delay(2000)
        return doc.select("div.col-md-4.col-lg-3.col-xl-2.col-6.my-3").mapNotNull { article ->
            val itemLink = article.selectFirst("a")
            val title = itemLink?.selectFirst("div.seriedetails h3.my-1")?.text() ?: ""
            val href = itemLink?.attr("href")

            if (href == null) {
                Log.w("LatanimePlugin", "WARN: href es nulo para un elemento en search.")
                return@mapNotNull null
            }

            val imageElement = article.selectFirst("div.col-md-4.col-lg-3.col-xl-2.col-6.my-3 a div.series img.img-fluid2.shadow-sm")
            val src = imageElement?.attr("src") ?: ""
            val dataSrc = imageElement?.attr("data-src") ?: ""
            val image = if (dataSrc.isNotEmpty()) fixUrl(dataSrc) else if (src.isNotEmpty()) fixUrl(src) else ""

            newAnimeSearchResponse(title, fixUrl(href)) {
                this.type = TvType.Anime
                this.posterUrl = image
                addDubStatus(getDubStatus(title))
                this.posterHeaders = cloudflareKiller.getCookieHeaders(mainUrl).toMap()
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("LatanimeProvider", "==== INICIANDO CARGA: $url ====")

        val document = app.get(url).document

        // 1. Obtener Título Original y Limpiar
        val rawTitle = document.selectFirst("div.col-lg-9 h2")?.text()?.trim()
            ?: document.selectFirst("h2")?.text()?.trim()
            ?: ""

        // Regex para detectar y extraer información
        val dubRegex = Regex("""(?i)\b(Latino|Castellano|Subtitulado|Español)\b""")
        val yearRegex = Regex("""\s*\((\d{4})\)""")

        // Detectamos el idioma antes de borrarlo del título
        val audioFound = dubRegex.find(rawTitle)?.value?.trim() ?: "Subtitulado"
        val isDubbed = rawTitle.contains("Latino", ignoreCase = true) || rawTitle.contains("Castellano", ignoreCase = true)

        // Limpiamos el título: Quitamos el idioma y el año entre paréntesis, pero dejamos la temporada (S1, S2, etc.)
        val cleanTitle = rawTitle
            .replace(dubRegex, "")
            .replace(yearRegex, "")
            .replace(Regex("""\s+"""), " ") // Eliminar espacios dobles
            .trim()

        // 2. Metadatos (Año, Poster, Descripción)
        val poster = document.selectFirst("meta[property=og:image]")?.attr("content")
            ?: document.selectFirst("div.col-lg-3 img")?.attr("src") ?: ""

        val description = document.selectFirst("p.my-2.opacity-75")?.text()?.trim()
            ?: document.selectFirst("div.col-lg-9 p:contains(Los cazadores)")?.text()?.trim() ?: ""

        // Intentar sacar el año del título primero, si no, del texto de la web
        val yearFromTitle = yearRegex.find(rawTitle)?.groupValues?.get(1)?.toIntOrNull()
        val dateText = document.selectFirst("span.span-tiempo")?.text() ?: ""
        val year = yearFromTitle ?: Regex("\\b(\\d{4})\\b").find(dateText)?.value?.toIntOrNull()

        // 3. Tags (Géneros + Idioma como etiqueta)
        val tags = document.select("a[href*=/genero/] div.btn").map { it.text().trim() }.toMutableList()
        if (!tags.contains(audioFound)) tags.add(0, audioFound) // Añade "Latino" o "Sub" al inicio de las etiquetas

        // 4. Logs de depuración
        Log.d("LatanimeProvider", "[DATOS] Original: $rawTitle")
        Log.d("LatanimeProvider", "[DATOS] Limpio: $cleanTitle")
        Log.d("LatanimeProvider", "[DATOS] Año: $year | Audio: $audioFound")
        Log.d("LatanimeProvider", "[DATOS] Tags: $tags")

        // 5. Extracción de Episodios
        val episodeElements = document.select("div[style*='max-height: 400px'] a[href*=episodio]")
        Log.d("LatanimeProvider", "[INFO] Episodios encontrados en HTML: ${episodeElements.size}")

        val episodes = episodeElements.mapIndexedNotNull { index, element ->
            try {
                val epUrl = element.attr("href")
                val epTitleText = element.selectFirst("div.cap-layout")?.text()?.trim() ?: ""

                val epNum = Regex("(?:episodio|capitulo)[\\s-]*(\\d+)", RegexOption.IGNORE_CASE)
                    .find(epUrl + epTitleText)?.groupValues?.getOrNull(1)?.toIntOrNull()

                if (epUrl.isNotBlank() && epNum != null) {
                    newEpisode(fixUrl(epUrl)) {
                        this.name = epTitleText.ifBlank { "Episodio $epNum" }
                        this.episode = epNum
                        // Extraer poster del episodio si existe
                        val img = element.selectFirst("img")
                        this.posterUrl = fixUrl(img?.attr("data-src")?.ifBlank { img.attr("src") } ?: "")
                    }
                } else {
                    Log.w("LatanimeProvider", "[WARN] No se pudo parsear episodio en índice $index (URL: $epUrl)")
                    null
                }
            } catch (e: Exception) {
                Log.e("LatanimeProvider", "[ERROR] Error en episodio $index: ${e.message}")
                null
            }
        }

        // 6. Determinar Tipo y Respuesta
        val tvType = when {
            url.contains("/pelicula/") -> TvType.Movie
            tags.contains("OVA") || tags.contains("Especial") -> TvType.OVA
            else -> TvType.Anime
        }

        // 1. Decidimos qué etiqueta de audio poner
        val audioLabel = if (isDubbed) "Latino" else "Subtitulado"

        // 2. Nos aseguramos de que el audio esté en la lista de etiquetas
        if (!tags.contains(audioLabel)) {
            tags.add(0, audioLabel)
        }

        return if (tvType == TvType.Movie) {
            newMovieLoadResponse(cleanTitle, url, tvType, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags // El audio aparecerá aquí como etiqueta
                this.year = year
            }
        } else {
            newTvSeriesLoadResponse(cleanTitle, url, tvType, episodes) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
                this.year = year
                this.showStatus = if (document.html().contains("En emisión", true)) ShowStatus.Ongoing else ShowStatus.Completed
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("LatanimePlugin", "loadLinks called with data: $data")

        var foundLinks = false
        val doc = appGetChildMainUrl(data).document
        try {
            doc.select("ul.cap_repro li#play-video").forEach { playerElement ->
                Log.d("LatanimePlugin", "Found player element: ${playerElement.outerHtml()}")

                val encodedUrl = playerElement.selectFirst("a.play-video")?.attr("data-player")
                Log.d("LatanimePlugin", "Encoded URL found: $encodedUrl")

                if (encodedUrl.isNullOrEmpty()) {
                    Log.w("LatanimePlugin", "Encoded URL is null or empty for $data. Could not find player data-player attribute.")
                    return@forEach
                }

                val urlDecoded = base64Decode(encodedUrl)
                Log.d("LatanimePlugin", "Decoded URL (Base64): $urlDecoded")

                val url = urlDecoded.replace("https://monoschinos2.com/reproductor?url=", "")
                    .replace("https://sblona.com", "https://watchsb.com")
                Log.d("LatanimePlugin", "Final URL for Extractor: $url")

                if (url.isNotEmpty()) {
                    loadExtractor(url, mainUrl, subtitleCallback, callback)
                    foundLinks = true
                } else {
                    Log.w("LatanimePlugin", "WARN: URL final para el extractor está vacía después de decodificar y reemplazar.")
                }
            }
        } catch (e: Exception) {
            Log.e("LatanimePlugin", "Error in loadLinks for data '$data': ${e.message}", e)
        }

        Log.d("LatanimePlugin", "loadLinks finished for data: $data with foundLinks: $foundLinks")
        return foundLinks
    }
}