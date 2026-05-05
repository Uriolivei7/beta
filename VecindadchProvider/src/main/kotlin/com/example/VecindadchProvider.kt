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
        val teamImage = this.selectFirst("div.team-image") ?: this
        val link = teamImage.selectFirst("a")?.attr("href") ?: return null
        val title = this.selectFirst("div.team-desc h3")?.text()?.trim()
            ?: this.selectFirst("h3")?.text()?.trim()
            ?: this.selectFirst("h2")?.text()?.trim()
            ?: return null
        val img = teamImage.selectFirst("img")?.attr("src")
            ?: teamImage.selectFirst("img")?.attr("data-src")
            ?: ""

        return newTvSeriesSearchResponse(title, fixUrl(link)) {
            this.posterUrl = if (img.startsWith("http")) img else "$mainUrl$img"
        }
    }

    override val mainPage = mainPageOf(
        "$mainUrl/videos/_chapulin_colorado_/_ch_1973_/" to "Chapulín Colorado T1973",
        "$mainUrl/videos/_chapulin_colorado_/_ch_1974_/" to "Chapulín Colorado T1974",
        "$mainUrl/videos/_chapulin_colorado_/_ch_1975_/" to "Chapulín Colorado T1975",
        "$mainUrl/videos/_chapulin_colorado_/_ch_1976_/" to "Chapulín Colorado T1976",
        "$mainUrl/videos/_chapulin_colorado_/_ch_1977_/" to "Chapulín Colorado T1977",
        "$mainUrl/videos/_chapulin_colorado_/_ch_1978_/" to "Chapulín Colorado T1978",
        "$mainUrl/videos/_chavo_/_1973_/" to "Chavo T1973",
        "$mainUrl/videos/_chavo_/_1974_/" to "Chavo T1974",
        "$mainUrl/videos/_chavo_/_1975_/" to "Chavo T1975",
        "$mainUrl/videos/_chavo_/_1976_/" to "Chavo T1976",
        "$mainUrl/videos/_chavo_/_1977_/" to "Chavo T1977",
        "$mainUrl/videos/_chavo_/_1978_/" to "Chavo T1978",
        "$mainUrl/videos/_chavo_/_1979_/" to "Chavo T1979",
        "$mainUrl/videos/chapulin-animado/" to "Chapulín Animado",
        "$mainUrl/videos/_chavo_animado_/" to "Chavo Animado",
        "$mainUrl/videos/musical/" to "Canciones",
        "$mainUrl/videos/chompiras-peterete/" to "Chómpiras",
        "$mainUrl/videos/chapatin/" to "Dr. Chapatín",
        "$mainUrl/videos/entremes/" to "Entremés",
        "$mainUrl/videos/pelicula/" to "Películas",
        "$mainUrl/videos/intro/" to "Intros",
        "$mainUrl/videos/especiales/especial_roberto_gomez/" to "Especial Chespirito",
        "$mainUrl/videos/especiales/especial_ramon_valdes/" to "Especial Ramón",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()
        
        val html = safeAppGet(request.data) ?: return null
        val doc = Jsoup.parse(html)
        
        val results = doc.select("div.team-member").mapNotNull { card ->
            card.toSearchResponse()
        }.filter { it.url.contains("/videos/") }
            .distinctBy { it.url }

        if (results.isNotEmpty()) {
            items.add(HomePageList(request.name, results))
        }

        return if (items.isNotEmpty()) newHomePageResponse(items, false) else null
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=$query"
        val html = safeAppGet(url) ?: return emptyList()
        val doc = Jsoup.parse(html)
        
        return doc.select("div.team-member, div.post-item").mapNotNull { card ->
            card.toSearchResponse()
        }.filter { it.url.contains("/videos/") }
            .distinctBy { it.url }
    }

    private fun isCategoryUrl(url: String): Boolean {
        // Category/season URLs end with "/something/" without a deeper slug
        // e.g. /videos/_chavo_/_1973_/ or /videos/musical/
        // Video URLs have a specific slug: /videos/la-casa-de-los-fantasmas/
        val parts = url.replace(mainUrl, "").trim('/').split('/')
        return parts.size <= 3
    }

    private fun Element.toEpisode(urlBase: String, index: Int): Episode {
        val teamImage = this.selectFirst("div.team-image") ?: this
        val link = teamImage.selectFirst("a")?.attr("href") ?: ""
        val title = this.selectFirst("div.team-desc h3")?.text()?.trim()
            ?: this.selectFirst("h3")?.text()?.trim()
            ?: "Episodio ${index + 1}"
        val img = teamImage.selectFirst("img")?.attr("src")
            ?: teamImage.selectFirst("img")?.attr("data-src")
            ?: ""
        val desc = this.selectFirst("div.team-desc p")?.text()?.trim()

        return newEpisode(fixUrl(link)) {
            this.name = title
            this.season = 1
            this.episode = index + 1
            this.posterUrl = if (img.startsWith("http")) img else "$mainUrl$img".takeIf { it.isNotBlank() }
            this.description = desc
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = safeAppGet(url) ?: return null
        val doc = Jsoup.parse(html)

        val rawTitle = doc.selectFirst("h1")?.text()?.trim()
            ?: doc.selectFirst("title")?.text()?.substringBefore("-")?.trim()
            ?: doc.selectFirst("meta[property='og:title']")?.attr("content")
            ?: "Desconocido"

        val title = rawTitle.replace(Regex("(?i)\\s*\\(\\d{4}\\)\\s*"), "")
            .replace(Regex("(?i)\\s*-\\s*Videos.*"), "")
            .replace(Regex("(?i)\\s*\\|.*"), "")
            .trim()

        val description = doc.selectFirst("p")?.text()?.trim()
            ?: doc.selectFirst("meta[name=description]")?.attr("content")
            ?: ""

        val poster = doc.selectFirst("meta[property='og:image']")?.attr("content")
            ?: doc.selectFirst("div.team-image img")?.attr("src")
            ?: ""

        val episodes = if (isCategoryUrl(url)) {
            // Season/category page: extract all video cards as episodes
            doc.select("div.team-member").mapIndexed { index, card ->
                card.toEpisode(url, index)
            }.filter { it.data.isNotBlank() && it.data != url }
                .distinctBy { it.data }
        } else {
            // Individual video page: single episode
            listOf(newEpisode(url) {
                this.name = title
                this.posterUrl = poster.ifBlank { null }
                this.description = description.ifBlank { null }
            })
        }

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
        val html = safeAppGet(data) ?: return false
        val doc = Jsoup.parse(html)

        val iframes = doc.select("iframe")
        if (iframes.isEmpty()) return false

        var found = false
        iframes.forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotBlank()) {
                val fixedSrc = fixUrl(src)
                loadExtractor(fixedSrc, mainUrl, subtitleCallback, callback)
                found = true
            }
        }

        return found
    }
}
