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
            title.contains("Latino", ignoreCase = true) || title.contains("Castellano", ignoreCase = true) -> DubStatus.Dubbed
            else -> DubStatus.Subbed
        }
    }

    private fun cleanTitle(rawTitle: String): String {
        val dubRegex = Regex("""(?i)\s*\b(Latino|Castellano|Subtitulado|Español)\b""")
        val yearRegex = Regex("""\s*\((\d{4})\)""")
        return rawTitle
            .replace(dubRegex, "")
            .replace(yearRegex, "")
            .replace(Regex("""\s+"""), " ")
            .trim()
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

            // Carousel
            val carouselItems = mainDoc.select("div.carousel-item a[href*=/anime/], div.carousel-item a[href*=/pelicula/]").mapNotNull { element ->
                val itemUrl = element.attr("href")
                val title = element.selectFirst("span.span-slider")?.text()?.trim() ?: ""
                val posterElement = element.selectFirst("img.preview-image, img.d-block")
                val poster = posterElement?.attr("data-src")?.ifBlank { posterElement.attr("src") } ?: ""

                if (itemUrl.isNotBlank() && title.isNotBlank()) {
                    newAnimeSearchResponse(cleanTitle(title), fixUrl(itemUrl)) {
                        this.posterUrl = fixUrl(poster)
                        addDubStatus(getDubStatus(title))
                    }
                } else null
            }
            if (carouselItems.isNotEmpty()) items.add(HomePageList("Destacados", carouselItems))

            // Secciones
            urls.forEach { (url, name) ->
                val doc = app.get(url).document
                val home = doc.select("div.col-md-4, div.col-lg-3, div.col-xl-2, div.col-6").mapNotNull { article ->
                    val linkElement = article.selectFirst("a[href*=/anime/], a[href*=/pelicula/]") ?: return@mapNotNull null
                    val itemUrl = linkElement.attr("href")
                    val title = linkElement.selectFirst("h3.my-1, h3")?.text()?.trim() ?: linkElement.attr("title").trim()
                    val imgElement = linkElement.selectFirst("img.img-fluid2, img.img-fluid, img")
                    val poster = fixUrl(imgElement?.attr("data-src")?.ifBlank { imgElement.attr("src") } ?: "")

                    newAnimeSearchResponse(cleanTitle(title), fixUrl(itemUrl)) {
                        this.posterUrl = poster
                        addDubStatus(getDubStatus(title))
                    }
                }
                if (home.isNotEmpty()) items.add(HomePageList(name, home))
            }
        } catch (e: Exception) {
            Log.e("LatanimeProvider", "Error en getMainPage: ${e.message}")
        }
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val response = app.get("$mainUrl/buscar?q=$query", interceptor = cloudflareKiller)
        val doc = response.document
        return doc.select("div.col-md-4.col-lg-3.col-xl-2.col-6.my-3").mapNotNull { article ->
            val itemLink = article.selectFirst("a")
            val title = itemLink?.selectFirst("div.seriedetails h3.my-1")?.text() ?: ""
            val href = itemLink?.attr("href") ?: return@mapNotNull null
            val imageElement = article.selectFirst("img.img-fluid2")
            val image = fixUrl(imageElement?.attr("data-src")?.ifBlank { imageElement.attr("src") } ?: "")

            newAnimeSearchResponse(cleanTitle(title), fixUrl(href)) {
                this.type = TvType.Anime
                this.posterUrl = image
                addDubStatus(getDubStatus(title))
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("LatanimeProvider", "Cargando contenido de: $url")
        val document = app.get(url).document
        val rawTitle = document.selectFirst("div.col-lg-9 h2")?.text()?.trim() ?: ""

        // --- LÓGICA DE IDIOMAS ---
        val tags = document.select("a[href*=/genero/] div.btn").map { it.text().trim() }
            .filter { !it.contains("Latino", true) && !it.contains("Castellano", true) && !it.contains("Subtitulado", true) }
            .toMutableList()

        if (rawTitle.contains("Latino", true)) tags.add(0, "Latino")
        if (rawTitle.contains("Castellano", true)) tags.add(0, "Castellano")
        if (rawTitle.contains("Subtitulado", true) || (!rawTitle.contains("Latino", true) && !rawTitle.contains("Castellano", true))) {
            tags.add(0, "Subtitulado")
            tags.add(1, "Japonés") // Añadimos Japonés si es subtitulado
        }

        val poster = document.selectFirst("meta[property=og:image]")?.attr("content") ?: ""
        val description = document.selectFirst("p.my-2.opacity-75")?.text()?.trim() ?: ""

        // --- LÓGICA DE EPISODIOS LIMPIOS ---
        val episodes = document.select("div[style*='max-height: 400px'] a[href*=episodio]").mapIndexedNotNull { _, element ->
            val epUrl = element.attr("href")
            val epTitleText = element.selectFirst("div.cap-layout")?.text()?.trim() ?: ""
            // Extraer solo el número
            val epNum = Regex("""(\d+)""").find(epUrl + epTitleText)?.value?.toIntOrNull()

            if (epUrl.isNotBlank() && epNum != null) {
                newEpisode(fixUrl(epUrl)) {
                    this.name = "Episodio $epNum" // Forzamos que solo diga Episodio X
                    this.episode = epNum
                }
            } else {
                Log.w("LatanimeProvider", "No se pudo procesar episodio en URL: $epUrl")
                null
            }
        }.reversed() // Invertir para que el 1 salga primero si es necesario

        val tvType = if (url.contains("/pelicula/")) TvType.Movie else TvType.Anime

        return if (tvType == TvType.Movie) {
            newMovieLoadResponse(cleanTitle(rawTitle), url, tvType, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
            }
        } else {
            newTvSeriesLoadResponse(cleanTitle(rawTitle), url, tvType, episodes) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
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
        Log.d("LatanimeProvider", "Buscando links en: $data")
        val doc = app.get(data, interceptor = cloudflareKiller).document
        var found = false

        doc.select("ul.cap_repro li#play-video").forEach { playerElement ->
            val encodedUrl = playerElement.selectFirst("a.play-video")?.attr("data-player")
            if (!encodedUrl.isNullOrEmpty()) {
                val urlDecoded = base64Decode(encodedUrl)
                val finalUrl = urlDecoded.replace("https://monoschinos2.com/reproductor?url=", "")
                    .replace("https://sblona.com", "https://watchsb.com")

                if (finalUrl.isNotEmpty()) {
                    Log.d("LatanimeProvider", "Extrayendo de: $finalUrl")
                    loadExtractor(finalUrl, mainUrl, subtitleCallback, callback)
                    found = true
                }
            }
        }
        if (!found) Log.e("LatanimeProvider", "No se encontraron reproductores válidos en $data")
        return found
    }
}