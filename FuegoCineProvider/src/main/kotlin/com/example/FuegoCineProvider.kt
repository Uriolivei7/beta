package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

class FuegoCineProvider : MainAPI() {
    override var mainUrl = "https://www.fuegocine.com"
    override var name = "FuegoCine"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override val mainPage = mainPageOf(
        "/" to "Últimos",
        "/search/label/Serie" to "Series",
        "/search/label/Anime" to "Animes",
        "/search/label/Movie" to "Películas",
    )

    private fun fixUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        return when {
            url.startsWith("//") -> "https:$url"
            url.startsWith("/") -> mainUrl + url
            else -> url
        }
    }

    private fun Element.toSearchResult(): SearchResponse? {
        // Blogger feed o HTML: buscar link del post
        val a = selectFirst("a[href*='.html']") ?: selectFirst("a") ?: return null
        val href = fixUrl(a.attr("href")) ?: return null
        if (!href.contains("fuegocine.com") && !href.startsWith("http")) return null
        var title = a.attr("title").ifBlank { a.text() }.trim()
        if (title.isBlank()) {
            title = selectFirst("h2,h3")?.text()?.trim() ?: return null
        }
        // evitar entradas duplicadas de paginación
        if (href.contains("/search/")) return null
        val img = selectFirst("img")?.attr("src")?.let { fixUrl(it) } ?: selectFirst("img")?.attr("data-src")?.let { fixUrl(it) }
        // tipo por label o url
        val isSerie = href.contains("/todas-las-temporadas") || text().contains("Temporada", true)
        val type = if (isSerie) TvType.TvSeries else TvType.Movie
        return newMovieSearchResponse(title, href, type) {
            this.posterUrl = img
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = when {
            request.data == "/" -> "$mainUrl/feeds/posts/default?alt=json&max-results=20&start-index=${(page-1)*20+1}"
            request.data.startsWith("/search/label/") -> {
                val label = request.data.substringAfterLast("/")
                "$mainUrl/feeds/posts/default/-/$label?alt=json&max-results=20&start-index=${(page-1)*20+1}"
            }
            else -> "$mainUrl${request.data}"
        }

        // Si es feed json, parsearlo
        if (url.contains("alt=json")) {
            val json = app.get(url).text
            // Blogger feed: feed.entry[]
            val entryRegex = Regex("\"title\"\\s*:\\s*\\{\"\\\$t\"\\s*:\\s*\"(.*?)\"")
            // Usar Jsoup sobre content.$t es más fiable: parsear con regex de links
            val linkRegex = Regex("\"href\"\\s*:\\s*\"(https://www\\.fuegocine\\.com/[^\"]+\\.html)\"")
            val links = linkRegex.findAll(json).map { it.groupValues[1] }.distinct().toList()
            val items = links.mapNotNull { link ->
                val title = link.substringAfterLast("/").replace(".html","").replace("-"," ").replaceFirstChar { it.uppercase() }
                newMovieSearchResponse(title, link, TvType.Movie) { this.posterUrl = null }
            }
            // Intentar obtener poster/title real desde content si hay
            // Fallback: si no hay links, usar HTML
            if (items.isNotEmpty()) {
                return newHomePageResponse(HomePageList(request.name, items), hasNext = items.size >= 20)
            }
        }

        val doc = app.get(if (url.contains("alt=json")) mainUrl else url).document
        val items = doc.select("article, div.post").mapNotNull { it.toSearchResult() }
            .distinctBy { it.url }
        return newHomePageResponse(HomePageList(request.name, items), hasNext = items.size >= 18)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/feeds/posts/default?alt=json&q=${java.net.URLEncoder.encode(query, "UTF-8")}&max-results=20"
        val json = app.get(url).text
        val linkRegex = Regex("\"href\"\\s*:\\s*\"(https://www\\.fuegocine\\.com/[^\"]+\\.html)\"")
        val links = linkRegex.findAll(json).map { it.groupValues[1] }.distinct().toList()
        return links.mapNotNull { link ->
            val title = link.substringAfterLast("/").substringBefore(".html").replace("-"," ").replaceFirstChar { it.uppercase() }
            newMovieSearchResponse(title, link, TvType.Movie) { }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document
        val title = doc.selectFirst("h1")?.text()?.trim()
            ?: doc.selectFirst("meta[property='og:title']")?.attr("content")?.trim()
            ?: return null
        val poster = doc.selectFirst("div[data-post-type] img")?.attr("src")?.let { fixUrl(it) }
            ?: doc.selectFirst("meta[property='og:image']")?.attr("content")
        val plot = doc.selectFirst("p#tmdb-synopsis")?.text()
            ?: doc.selectFirst("meta[name='description']")?.attr("content")
        val typeAttr = doc.selectFirst("div[data-post-type]")?.attr("data-post-type")?.lowercase() ?: ""
        val isSerie = typeAttr.contains("serie") || url.contains("temporada") || doc.selectFirst("#ocultar")?.html()?.contains("temporada", true) == true
        val year = doc.selectFirst("li[data-year]")?.attr("data-year")?.toIntOrNull()
            ?: Regex("""\b(19\d{2}|20\d{2})\b""").find(doc.text())?.groupValues?.get(1)?.toIntOrNull()
        val tags = doc.selectFirst("ul.post-details")?.attr("data-genres")?.split(",")?.map { it.trim() }

        // Episodios: buscar dentro de #ocultar
        val episodes = mutableListOf<Episode>()
        val ocultar = doc.selectFirst("div#ocultar")
        if (ocultar != null) {
            // buscar enlaces a episodios
            val epLinks = ocultar.select("a[href*='.html']").map { it.attr("href") }.filter { it.contains(".html") }
            // Si hay lista de temporadas, mapear
            var idx = 1
            for (link in epLinks.distinct()) {
                val epUrl = fixUrl(link) ?: continue
                if (epUrl == url) continue
                if (!epUrl.contains("fuegocine.com")) continue
                episodes.add(newEpisode(epUrl) {
                    this.name = "Episodio $idx"
                    this.episode = idx
                    this.season = 1
                })
                idx++
                if (idx > 100) break
            }
        }

        return if (isSerie && episodes.isNotEmpty()) {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.plot = plot
                this.tags = tags
                this.year = year
            }
        } else if (isSerie) {
            // Serie sin lista detectada: tratar como serie con episodio único (el mismo url)
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, listOf(newEpisode(url){ this.name = title })) {
                this.posterUrl = poster
                this.plot = plot
                this.tags = tags
                this.year = year
            }
        } else {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = plot
                this.tags = tags
                this.year = year
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
        val ocultar = doc.selectFirst("div#ocultar")
        val candidates = mutableListOf<String>()
        ocultar?.select("iframe[src]")?.forEach { candidates.add(it.attr("src")) }
        ocultar?.select("a[href]")?.forEach { href ->
            val u = href.attr("href")
            if (u.startsWith("http") && !u.contains("fuegocine.com")) candidates.add(u)
            val dataSrc = href.attr("data-src")
            if (dataSrc.startsWith("http")) candidates.add(dataSrc)
        }
        // Fallback: buscar cualquier url http en el html de ocultar
        if (candidates.isEmpty() && ocultar != null) {
            Regex("""https?://[^\s"'<>]+""").findAll(ocultar.html()).forEach {
                val u = it.value
                if (u.contains("fuegocine.com")) return@forEach
                if (u.endsWith(".jpg") || u.endsWith(".png")) return@forEach
                candidates.add(u)
            }
        }
        // También buscar en todo el doc si ocultar vacío
        if (candidates.isEmpty()) {
            doc.select("iframe[src]").forEach { candidates.add(it.attr("src")) }
        }
        candidates.distinct().forEach { link ->
            val fixed = fixUrl(link) ?: return@forEach
            loadExtractor(fixed, data, subtitleCallback, callback)
        }
        return candidates.isNotEmpty()
    }
}
