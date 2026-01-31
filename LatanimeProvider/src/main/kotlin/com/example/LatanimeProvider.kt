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

    private fun cleanTitle(rawTitle: String): String {
        val junkRegex = Regex("""(?i)\s*\b(Latino|Castellano|Subtitulado|Español|Japonés|Japones|Audio)\b""")
        val yearRegex = Regex("""\s*\((\d{4})\)""")

        var cleaned = rawTitle
            .replace(junkRegex, "")
            .replace(yearRegex, "")
            .replace("()", "")
            .replace(Regex("""\s+"""), " ")
            .trim()

        return cleaned
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()

        try {
            val mainDoc = app.get(mainUrl).document
            val carouselItems = mainDoc.select("div.carousel-item a[href*=/anime/], div.carousel-item a[href*=/pelicula/]").mapNotNull { element ->
                val itemUrl = element.attr("href")
                val title = element.selectFirst("span.span-slider")?.text()?.trim() ?: ""
                if (itemUrl.isNotBlank() && title.isNotBlank()) {
                    newAnimeSearchResponse(cleanTitle(title), fixUrl(itemUrl)) {
                        val posterElement = element.selectFirst("img.preview-image, img.d-block")
                        this.posterUrl = fixUrl(posterElement?.attr("data-src")?.ifBlank { posterElement.attr("src") } ?: "")
                    }
                } else null
            }
            if (carouselItems.isNotEmpty()) items.add(HomePageList("Destacados", carouselItems))
        } catch (e: Exception) {
            Log.e("LatanimeProvider", "Error en Carousel: ${e.message}")
        }

        val sections = listOf(
            Pair("$mainUrl/emision", "En emisión"),
            Pair("$mainUrl/animes?fecha=false&genero=false&letra=false&categoria=Película", "Películas"),
            Pair("$mainUrl/animes", "Animes Recientes"),
        )

        sections.forEach { (url, sectionName) ->
            try {
                val doc = app.get(url).document
                val home = doc.select("div.col-md-4, div.col-lg-3, div.col-xl-2, div.col-6").mapNotNull { article ->
                    val linkElement = article.selectFirst("a[href*=/anime/], a[href*=/pelicula/]") ?: return@mapNotNull null
                    val itemUrl = linkElement.attr("href")
                    val title = linkElement.selectFirst("h3.my-1, h3")?.text()?.trim() ?: linkElement.attr("title").trim()
                    val imgElement = linkElement.selectFirst("img.img-fluid2, img.img-fluid, img")

                    newAnimeSearchResponse(cleanTitle(title), fixUrl(itemUrl)) {
                        this.posterUrl = fixUrl(imgElement?.attr("data-src")?.ifBlank { imgElement.attr("src") } ?: "")
                    }
                }
                if (home.isNotEmpty()) items.add(HomePageList(sectionName, home))
            } catch (e: Exception) {
                Log.e("LatanimeProvider", "Error en sección $sectionName: ${e.message}")
            }
        }
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/buscar?q=$query").document
        return doc.select("div.col-md-4.col-lg-3.col-xl-2.col-6.my-3").mapNotNull { article ->
            val itemLink = article.selectFirst("a")
            val title = itemLink?.selectFirst("div.seriedetails h3.my-1")?.text() ?: ""
            val href = itemLink?.attr("href") ?: return@mapNotNull null
            newAnimeSearchResponse(cleanTitle(title), fixUrl(href)) {
                val img = article.selectFirst("img")
                this.posterUrl = fixUrl(img?.attr("data-src")?.ifBlank { img.attr("src") } ?: "")
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("LatanimeProvider", "Cargando: $url")
        val document = app.get(url).document
        val rawTitle = document.selectFirst("div.col-lg-9 h2")?.text()?.trim() ?: ""

        val foundYear = Regex("""\((\d{4})\)""").find(rawTitle)?.groupValues?.get(1)?.toIntOrNull()

        val tags = document.select("a[href*=/genero/] div.btn").map { it.text().trim() }
            .filter { !it.contains(Regex("(?i)Latino|Castellano|Subtitulado|Japonés|Japones|Audio")) }
            .toMutableList()

        if (rawTitle.contains("Latino", true)) tags.add(0, "Latino")
        else if (rawTitle.contains("Castellano", true)) tags.add(0, "Castellano")
        else if (rawTitle.contains("Japonés", true) || rawTitle.contains("Japones", true)) {
            tags.add(0, "Subtitulado")
            tags.add(1, "Japonés")
        } else {
            tags.add(0, "Subtitulado")
        }

        val poster = document.selectFirst("meta[property=og:image]")?.attr("content") ?: ""
        val description = document.selectFirst("p.my-2.opacity-75")?.text()?.trim() ?: ""

        val episodes = document.select("div[style*='max-height: 400px'] a[href*=episodio]").mapNotNull { element ->
            val epUrl = element.attr("href")
            val epText = element.selectFirst("div.cap-layout")?.text()?.trim() ?: ""
            val epNum = Regex("""episodio-(\d+)""").find(epUrl)?.groupValues?.get(1)?.toIntOrNull()
                ?: Regex("""(\d+)""").find(epText)?.value?.toIntOrNull()

            if (epUrl.isNotBlank() && epNum != null) {
                newEpisode(fixUrl(epUrl)) {
                    this.name = "Capítulo $epNum"
                    this.episode = epNum
                }
            } else null
        }.reversed()

        val tvType = if (url.contains("/pelicula/")) TvType.Movie else TvType.Anime

        return if (tvType == TvType.Movie) {
            newMovieLoadResponse(cleanTitle(rawTitle), url, tvType, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
                this.year = foundYear
            }
        } else {
            newTvSeriesLoadResponse(cleanTitle(rawTitle), url, tvType, episodes) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
                this.year = foundYear
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
        val doc = app.get(data).document
        var found = false
        doc.select("ul.cap_repro li#play-video").forEach { playerElement ->
            val encodedUrl = playerElement.selectFirst("a.play-video")?.attr("data-player")
            if (!encodedUrl.isNullOrEmpty()) {
                val finalUrl = base64Decode(encodedUrl).replace("https://monoschinos2.com/reproductor?url=", "")
                if (finalUrl.isNotEmpty()) {
                    loadExtractor(finalUrl, mainUrl, subtitleCallback, callback)
                    found = true
                }
            }
        }
        return found
    }
}