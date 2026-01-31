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

    // --- FUNCIÓN DE LIMPIEZA PRIVADA PARA REUTILIZAR ---
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

            // 1. Carousel / Slider
            val carouselItems = mainDoc.select("div.carousel-item a[href*=/anime/], div.carousel-item a[href*=/pelicula/]").mapNotNull { element ->
                val itemUrl = element.attr("href")
                val title = element.selectFirst("span.span-slider")?.text()?.trim() ?: ""

                val posterElement = element.selectFirst("img.preview-image, img.d-block")
                val poster = posterElement?.attr("data-src")?.ifBlank { posterElement.attr("src") } ?: ""

                if (itemUrl.isNotBlank() && title.isNotBlank()) {
                    newAnimeSearchResponse(cleanTitle(title), fixUrl(itemUrl)) {
                        this.posterUrl = fixUrl(poster)
                        addDubStatus(getDubStatus(title)) // Usa el original para el icono
                    }
                } else null
            }

            if (carouselItems.isNotEmpty()) items.add(HomePageList("Destacados", carouselItems))

            // 2. Secciones Home
            urls.forEach { (url, name) ->
                val doc = app.get(url).document
                val home = doc.select("div.col-md-4, div.col-lg-3, div.col-xl-2, div.col-6").mapNotNull { article ->
                    val linkElement = article.selectFirst("a[href*=/anime/], a[href*=/pelicula/]") ?: return@mapNotNull null
                    val itemUrl = linkElement.attr("href")
                    val title = linkElement.selectFirst("h3.my-1, h3")?.text()?.trim()
                        ?: linkElement.selectFirst("div.seriedetails h3")?.text()?.trim()
                        ?: linkElement.attr("title").trim()

                    val imgElement = linkElement.selectFirst("img.img-fluid2, img.img-fluid, img")
                    val poster = fixUrl(imgElement?.attr("data-src")?.ifBlank { imgElement.attr("src") } ?: "")

                    newAnimeSearchResponse(cleanTitle(title), fixUrl(itemUrl)) {
                        this.posterUrl = poster
                        addDubStatus(getDubStatus(title))
                    }
                }.filter { it.name.isNotBlank() }

                if (home.isNotEmpty()) items.add(HomePageList(name, home))
            }
        } catch (e: Exception) {
            Log.e("LatanimeProvider", "ERROR en getMainPage: ${e.message}")
        }
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = appGetChildMainUrl("$mainUrl/buscar?q=$query").document
        delay(2000)
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
                this.posterHeaders = cloudflareKiller.getCookieHeaders(mainUrl).toMap()
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("LatanimeProvider", "==== INICIANDO CARGA: $url ====")
        val document = app.get(url).document

        val rawTitle = document.selectFirst("div.col-lg-9 h2")?.text()?.trim()
            ?: document.selectFirst("h2")?.text()?.trim() ?: ""

        val dubRegex = Regex("""(?i)\b(Latino|Castellano|Subtitulado|Español)\b""")
        val yearRegex = Regex("""\s*\((\d{4})\)""")

        val audioFound = dubRegex.find(rawTitle)?.value?.trim() ?: "Subtitulado"
        val isDubbed = rawTitle.contains("Latino", ignoreCase = true) || rawTitle.contains("Castellano", ignoreCase = true)

        // 1. Tags Limpios (Evitar que se mezclen audios de la web)
        val tags = document.select("a[href*=/genero/] div.btn").map { it.text().trim() }
            .filter { !it.contains("Latino", true) && !it.contains("Castellano", true) }
            .toMutableList()
        tags.add(0, audioFound)

        // 2. Metadatos
        val poster = document.selectFirst("meta[property=og:image]")?.attr("content") ?: ""
        val description = document.selectFirst("p.my-2.opacity-75")?.text()?.trim() ?: ""
        val year = yearRegex.find(rawTitle)?.groupValues?.get(1)?.toIntOrNull()
            ?: Regex("\\b(\\d{4})\\b").find(document.selectFirst("span.span-tiempo")?.text() ?: "")?.value?.toIntOrNull()

        // 3. Episodios
        val episodes = document.select("div[style*='max-height: 400px'] a[href*=episodio]").mapIndexedNotNull { index, element ->
            val epUrl = element.attr("href")
            val epTitleText = element.selectFirst("div.cap-layout")?.text()?.trim() ?: ""
            val epNum = Regex("""(\d+)""").find(epUrl + epTitleText)?.value?.toIntOrNull()

            if (epUrl.isNotBlank() && epNum != null) {
                newEpisode(fixUrl(epUrl)) {
                    this.name = epTitleText.ifBlank { "Episodio $epNum" }
                    this.episode = epNum
                    val img = element.selectFirst("img")
                    this.posterUrl = fixUrl(img?.attr("data-src")?.ifBlank { img.attr("src") } ?: "")
                }
            } else null
        }

        val tvType = if (url.contains("/pelicula/")) TvType.Movie else TvType.Anime

        return if (tvType == TvType.Movie) {
            newMovieLoadResponse(cleanTitle(rawTitle), url, tvType, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
                this.year = year
            }
        } else {
            newTvSeriesLoadResponse(cleanTitle(rawTitle), url, tvType, episodes) {
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