package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import kotlin.collections.ArrayList

class VecindadchProvider : MainAPI() {
    override var mainUrl = "https://www.chavodel8.com"
    override var name = "VecindadCH"
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

    private fun getVideoCards(doc: org.jsoup.nodes.Document): Elements {
        val main = doc.selectFirst("section#page-content div.row.team-members.m-b-40")
        return main?.select(" > div[class^='col'] > div.team-member")
            ?: doc.select("div.row.team-members.m-b-40 > div[class^='col'] > div.team-member")
    }

    private fun Element.toSearchResponse(): SearchResponse? {
        val teamImage = this.selectFirst("div.team-image") ?: this
        val link = teamImage.selectFirst("a")?.attr("href") ?: return null
        val title = this.selectFirst("div.team-desc h3")?.text()?.trim()
            ?: this.selectFirst("h3")?.text()?.trim()
            ?: return null
        val img = teamImage.selectFirst("img")?.attr("src")
            ?: teamImage.selectFirst("img")?.attr("data-src")
            ?: ""

        return newTvSeriesSearchResponse(title, fixUrl(link)) {
            this.posterUrl = if (img.startsWith("http")) img else "$mainUrl$img"
        }
    }

    private fun Element.toEpisode(index: Int): Episode {
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

    private data class CatDef(
        val name: String,
        val url: String,
        val isSeason: Boolean = false,
        val posterUrl: String = ""
    )

    private val chapuBase = "$mainUrl/wp-content/uploads/2017/11"
    private val chavoBase = "$mainUrl/img/ep"
    private val animBase = "$mainUrl/wp-content/uploads/2017/11"

    private val homeRows = listOf(
        CatDef("Chapulín Colorado", "$mainUrl/videos/_chapulin_colorado_/", false),
        CatDef("Chavo del 8", "$mainUrl/videos/_chavo_/", false),
        //CatDef("Chapulín Animado", "$mainUrl/videos/chapulin-animado/", false),
        CatDef("Chavo Animado", "$mainUrl/videos/_chavo_animado_/", false),
        CatDef("Canciones", "$mainUrl/videos/musical/", false),
        CatDef("Chómpiras", "$mainUrl/videos/chompiras-peterete/", false),
        CatDef("Dr. Chapatín", "$mainUrl/videos/chapatin/", false),
        CatDef("Entremés", "$mainUrl/videos/entremes/", false),
        CatDef("Películas", "$mainUrl/videos/pelicula/", false),
        CatDef("Intros", "$mainUrl/videos/intro/", false),
    )

    private val subSeasons = mapOf(
        "Chapulín Colorado" to listOf(
            CatDef("T1973", "$mainUrl/videos/_chapulin_colorado_/_ch_1973_/", true, "$chapuBase/t73-1.jpg"),
            CatDef("T1974", "$mainUrl/videos/_chapulin_colorado_/_ch_1974_/", true, "$chapuBase/t74-1.jpg"),
            CatDef("T1975", "$mainUrl/videos/_chapulin_colorado_/_ch_1975_/", true, "$chapuBase/t75-1.jpg"),
            CatDef("T1976", "$mainUrl/videos/_chapulin_colorado_/_ch_1976_/", true, "$chapuBase/t76.jpg"),
            CatDef("T1977", "$mainUrl/videos/_chapulin_colorado_/_ch_1977_/", true, "$chapuBase/t77-1.jpg"),
            CatDef("T1978", "$mainUrl/videos/_chapulin_colorado_/_ch_1978_/", true, "$chapuBase/t78-1.jpg"),
        ),
        "Chavo del 8" to listOf(
            CatDef("T1973", "$mainUrl/videos/_chavo_/_1973_/", true, "$chavoBase/t73.jpg"),
            CatDef("T1974", "$mainUrl/videos/_chavo_/_1974_/", true, "$chavoBase/t74.jpg"),
            CatDef("T1975", "$mainUrl/videos/_chavo_/_1975_/", true, "$chavoBase/t75.jpg"),
            CatDef("T1976", "$mainUrl/videos/_chavo_/_1976_/", true, "$chavoBase/t76.jpg"),
            CatDef("T1977", "$mainUrl/videos/_chavo_/_1977_/", true, "$chavoBase/t77.jpg"),
            CatDef("T1978", "$mainUrl/videos/_chavo_/_1978_/", true, "$chavoBase/t78.jpg"),
            CatDef("T1979", "$mainUrl/videos/_chavo_/_1979_/", true, "$chavoBase/t79.jpg"),
        ),
        "Chavo Animado" to listOf(
            CatDef("Temporada 1", "$mainUrl/videos/_chavo_animado_/_temporada_1_/", true, "$animBase/t01.jpg"),
            CatDef("Temporada 2", "$mainUrl/videos/_chavo_animado_/_temporada_2_/", true, "$animBase/t02.jpg"),
            CatDef("Temporada 3", "$mainUrl/videos/_chavo_animado_/_temporada_3_/", true, "$animBase/t03.jpg"),
        ),
        /*
        "Chapulín Animado" to listOf(
            CatDef("Temporada 1", "$mainUrl/videos/chapulin-animado/_temporada_1_/", true, "$animBase/ca01.jpg"),
        ),*/
    )

    private val allUrls = (homeRows + subSeasons.values.flatten()).associateBy { it.url }

    override val mainPage = mainPageOf(
        *homeRows.map { it.url to it.name }.toTypedArray()
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()
        val cat = allUrls[request.data]

        when {
            cat == null -> return null
            cat.isSeason -> {
                val html = safeAppGet(cat.url) ?: return null
                val doc = Jsoup.parse(html)
                val cards = getVideoCards(doc)
                val results = cards.mapNotNull { it.toSearchResponse() }
                    .filter { it.url.contains("/videos/") }
                    .distinctBy { it.url }
                if (results.isNotEmpty()) items.add(HomePageList(cat.name, results))
            }
            cat.name in subSeasons -> {
                val seasons = subSeasons[cat.name]!!
                val results = seasons.map { s ->
                    newTvSeriesSearchResponse(s.name, s.url) {
                        this.posterUrl = s.posterUrl
                    }
                }
                if (results.isNotEmpty()) items.add(HomePageList(cat.name, results))
            }
            else -> {
                val html = safeAppGet(cat.url) ?: return null
                val doc = Jsoup.parse(html)
                val cards = getVideoCards(doc)
                val results = cards.mapNotNull { it.toSearchResponse() }
                    .filter { it.url.contains("/videos/") }
                    .distinctBy { it.url }
                if (results.isNotEmpty()) items.add(HomePageList(cat.name, results))
            }
        }

        return if (items.isNotEmpty()) newHomePageResponse(items, false) else null
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=$query"
        val html = safeAppGet(url) ?: return emptyList()
        val doc = Jsoup.parse(html)

        return doc.select("div.team-member, div.post-item").mapNotNull { it.toSearchResponse() }
            .filter { it.url.contains("/videos/") }
            .distinctBy { it.url }
    }

    private fun findSeasonName(url: String): String? {
        for ((parentName, seasons) in subSeasons) {
            if (seasons.any { it.url == url }) return parentName
        }
        return null
    }

    private fun findSeasonPoster(url: String): String {
        for ((_, seasons) in subSeasons) {
            seasons.find { it.url == url }?.takeIf { it.posterUrl.isNotBlank() }
                ?.let { return it.posterUrl }
        }
        return ""
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

        val seasonPoster = findSeasonPoster(url)
        val poster = if (seasonPoster.isNotBlank()) seasonPoster
        else {
            doc.selectFirst("meta[property='og:image']")?.attr("content")
                ?: doc.selectFirst("div.team-image img")?.attr("src")
                ?: ""
        }

        val isSeasonPage = findSeasonName(url) != null

        val episodes = if (isSeasonPage) {
            getVideoCards(doc).mapIndexed { index, card ->
                card.toEpisode(index)
            }.filter { it.data.isNotBlank() && it.data != url }
                .distinctBy { it.data }
        } else {
            listOf(newEpisode(url) {
                this.name = title
                this.posterUrl = poster.ifBlank { null }
                this.description = description.ifBlank { null }
            })
        }

        val recommendations = if (isSeasonPage) {
            null
        } else {
            doc.select("div.team-member").mapNotNull { it.toSearchResponse() }
                .filter { it.url != url && it.url.contains("/videos/") }
                .distinctBy { it.url }
                .take(12)
                .takeIf { it.isNotEmpty() }
        }

        return newTvSeriesLoadResponse(
            name = title,
            url = url,
            type = TvType.TvSeries,
            episodes = episodes
        ) {
            this.posterUrl = poster
            this.plot = description
            if (recommendations != null) this.recommendations = recommendations
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
