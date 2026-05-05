package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import kotlin.collections.ArrayList

class VecindadchProvider : MainAPI() {
    override var mainUrl = "https://www.chavodel8.com"
    override var name = "Vecindad CH"
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Cartoon,
        TvType.Anime,
    )

    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true

    private val baseHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.5",
        "Referer" to mainUrl
    )

    private suspend fun safeAppGet(url: String): String? {
        return try {
            val res = app.get(url, timeout = 30000L, headers = baseHeaders)
            if (res.isSuccessful) res.text else null
        } catch (e: Exception) {
            Log.e("VecindadCH", "Error fetching $url: ${e.message}")
            null
        }
    }

    private fun Element.toSearchResponse(): SearchResponse? {
        val link = this.selectFirst("a")?.attr("href") ?: return null
        val title = this.selectFirst("h3")?.text()?.trim()
            ?: this.selectFirst("h2")?.text()?.trim()
            ?: return null
        val img = this.selectFirst("img")?.attr("src")
            ?: this.selectFirst("img")?.attr("data-src")
            ?: ""
        
        return newTvSeriesSearchResponse(title, fixUrl(link)) {
            this.posterUrl = if (img.startsWith("http")) img else "$mainUrl$img"
        }
    }

    override val mainPage = mainPageOf(
        "$mainUrl/" to "Inicio",
        "$mainUrl/videos/_chavo_/" to "Chavo del 8",
        "$mainUrl/videos/_chapulin_colorado_/" to "Chapulín Colorado",
        "$mainUrl/videos/musical/" to "Canciones",
        "$mainUrl/videos/chompiras-peterete/" to "Chómpiras y Peterete",
        "$mainUrl/videos/chapatin/" to "Doctor Chapatín",
        "$mainUrl/videos/entremes/" to "Entremés",
        "$mainUrl/videos/pelicula/" to "Películas",
        "$mainUrl/videos/_chavo_animado_/" to "Chavo Animado",
        "$mainUrl/videos/chapulin-animado/" to "Chapulín Animado",
        "$mainUrl/videos/intro/" to "Introducciones",
        "$mainUrl/videos/publicidad/" to "Anuncios Comerciales",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()
        
        val html = safeAppGet(request.data) ?: return null
        val doc = Jsoup.parse(html)
        
        // Seleccionar tarjetas de videos (team-member y post-item)
        val results = doc.select("div.team-member, div.post-item").mapNotNull { card ->
            card.toSearchResponse()
        }.filter { it.url.contains("/videos/") || it.url.contains(mainUrl) }
            .distinctBy { it.url }

        if (results.isNotEmpty()) {
            val sectionName = request.name
            items.add(HomePageList(sectionName, results))
        }

        return if (items.isNotEmpty()) newHomePageResponse(items, false) else null
    }

    override suspend fun search(query: String): List<SearchResponse> {
        // WordPress usa ?s= para búsqueda
        val url = "$mainUrl/?s=$query"
        val html = safeAppGet(url) ?: return emptyList()
        val doc = Jsoup.parse(html)
        
        return doc.select("div.team-member, div.post-item, article").mapNotNull { card ->
            card.toSearchResponse()
        }.filter { it.url.contains("/videos/") }
            .distinctBy { it.url }
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = safeAppGet(url) ?: return null
        val doc = Jsoup.parse(html)

        // Título del h1
        val rawTitle = doc.selectFirst("h1")?.text()?.trim()
            ?: doc.selectFirst("title")?.text()?.substringBefore("-")?.trim()
            ?: doc.selectFirst("meta[property='og:title']")?.attr("content")
            ?: "Desconocido"
        
        // Limpiar título
        val title = rawTitle.replace(Regex("(?i)\\s*\\(\\d{4}\\)\\s*"), "")
            .replace(Regex("(?i)\\s*-\\s*Videos.*"), "")
            .replace(Regex("(?i)\\s*\\|.*"), "")
            .trim()

        // Descripción
        val description = doc.selectFirst("p")?.text()?.trim()
            ?: doc.selectFirst("meta[name=description]")?.attr("content")
            ?: ""

        // Poster desde og:image
        val poster = doc.selectFirst("meta[property='og:image']")?.attr("content")
            ?: doc.selectFirst("div.team-image img")?.attr("src")
            ?: doc.selectFirst("div.post-image img")?.attr("src")
            ?: ""

        // Crear un solo episodio con la URL de la página
        val episodes = listOf(newEpisode(url) {
            this.name = title
            this.posterUrl = poster.ifBlank { null }
            this.description = description.ifBlank { null }
        })

        // Elementos relacionados
        val recommendations = doc.select("div.team-member").mapNotNull { card ->
            val rec = card.toSearchResponse()
            if (rec != null && rec.url != url) rec else null
        }.distinctBy { it.url }.take(12)

        return newTvSeriesLoadResponse(
            name = title, 
            url = url, 
            type = TvType.TvSeries, 
            episodes = episodes
        ) {
            this.posterUrl = poster
            this.plot = description
            this.recommendations = recommendations
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("VecindadCH", "loadLinks - URL: $data")
        
        val html = safeAppGet(data) ?: return false
        val doc = Jsoup.parse(html)

        // Buscar todos los iframes en la página
        val iframes = doc.select("iframe")
        
        if (iframes.isEmpty()) {
            Log.e("VecindadCH", "No se encontraron iframes en la página")
            return false
        }

        var found = false
        iframes.forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotBlank()) {
                Log.d("VecindadCH", "Procesando iframe: $src")
                // Cargar extractores para mega.nz, youtube, etc.
                loadExtractor(src, mainUrl, subtitleCallback, callback)
                found = true
            }
        }

        return found
    }
}
