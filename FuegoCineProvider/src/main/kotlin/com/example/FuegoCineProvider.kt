package com.example

import android.util.Log
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

    /** Parse Blogger feed JSON, fixing escaped slashes */
    private fun parseBloggerFeed(json: String): List<String> {
        val unescaped = json.replace("\\/", "/")
        val linkRegex = Regex(""""href":"(https://www\.fuegocine\.com/[^"]+\.html)"""")
        return linkRegex.findAll(unescaped).map { it.groupValues[1] }.distinct().toList()
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val a = selectFirst("a[href*='.html']") ?: selectFirst("a") ?: return null
        val href = fixUrl(a.attr("href")) ?: return null
        if (!href.contains("fuegocine.com") && !href.startsWith("http")) return null
        var title = a.attr("title").ifBlank { a.text() }.trim()
        if (title.isBlank()) {
            title = selectFirst("h2,h3")?.text()?.trim() ?: return null
        }
        if (href.contains("/search/")) return null
        val img = selectFirst("img")?.attr("src")?.let { fixUrl(it) }
            ?: selectFirst("img")?.attr("data-src")?.let { fixUrl(it) }
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

        if (url.contains("alt=json")) {
            val rawJson = try { app.get(url).text } catch (e: Exception) {
                Log.e("FuegoCine", "getMainPage: error fetching feed", e)
                return newHomePageResponse(emptyList(), false)
            }
            val links = parseBloggerFeed(rawJson)
            Log.d("FuegoCine", "getMainPage: ${request.data} -> ${links.size} links")
            if (links.isNotEmpty()) Log.d("FuegoCine", "getMainPage: first=${links.first()}")
            val items = links.map { link ->
                val title = link.substringAfterLast("/").replace(".html", "").replace("-", " ")
                    .replaceFirstChar { it.uppercase() }
                newMovieSearchResponse(title, link, TvType.Movie) { this.posterUrl = null }
            }
            if (items.isNotEmpty()) {
                return newHomePageResponse(HomePageList(request.name, items), hasNext = items.size >= 20)
            }
            Log.w("FuegoCine", "getMainPage: 0 items from feed, falling back to HTML")
        }

        val doc = try {
            app.get(if (url.contains("alt=json")) mainUrl else url).document
        } catch (e: Exception) {
            Log.e("FuegoCine", "getMainPage: error fetching HTML", e)
            return newHomePageResponse(emptyList(), false)
        }
        val items = doc.select("article, div.post").mapNotNull { it.toSearchResult() }
            .distinctBy { it.url }
        Log.d("FuegoCine", "getMainPage: HTML fallback -> ${items.size} items")
        return newHomePageResponse(HomePageList(request.name, items), hasNext = items.size >= 18)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/feeds/posts/default?alt=json&q=${java.net.URLEncoder.encode(query, "UTF-8")}&max-results=20"
        Log.d("FuegoCine", "search: query=$query")
        val rawJson = try { app.get(url).text } catch (e: Exception) {
            Log.e("FuegoCine", "search: error", e)
            return emptyList()
        }
        val links = parseBloggerFeed(rawJson)
        Log.d("FuegoCine", "search: ${links.size} links found")
        return links.map { link ->
            val title = link.substringAfterLast("/").substringBefore(".html").replace("-", " ")
                .replaceFirstChar { it.uppercase() }
            newMovieSearchResponse(title, link, TvType.Movie) { }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("FuegoCine", "load: url=$url")
        val doc = try { app.get(url).document } catch (e: Exception) {
            Log.e("FuegoCine", "load: error", e)
            return null
        }
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

        Log.d("FuegoCine", "load: title=$title isSerie=$isSerie typeAttr=$typeAttr")

        val episodes = mutableListOf<Episode>()
        val ocultar = doc.selectFirst("div#ocultar")
        if (ocultar != null) {
            val epLinks = ocultar.select("a[href*='.html']").map { it.attr("href") }.filter { it.contains(".html") }
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
        Log.d("FuegoCine", "load: ${episodes.size} episodes found")

        return if (isSerie && episodes.isNotEmpty()) {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.plot = plot
                this.tags = tags
                this.year = year
            }
        } else if (isSerie) {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, listOf(newEpisode(url) { this.name = title })) {
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
        Log.d("FuegoCine", "loadLinks: data=$data")
        val doc = try { app.get(data).document } catch (e: Exception) {
            Log.e("FuegoCine", "loadLinks: error", e)
            return false
        }
        val ocultar = doc.selectFirst("div#ocultar")
        val candidates = mutableListOf<String>()

        // Buscar iframes dentro de #ocultar
        ocultar?.select("iframe[src]")?.forEach { candidates.add(it.attr("src")) }

        // Buscar iframes con data-src (lazy load)
        ocultar?.select("iframe[data-src]")?.forEach {
            val ds = it.attr("data-src")
            if (ds.startsWith("http")) candidates.add(ds)
        }

        // Buscar enlaces externos
        ocultar?.select("a[href]")?.forEach { href ->
            val u = href.attr("href")
            if (u.startsWith("http") && !u.contains("fuegocine.com")) candidates.add(u)
        }

        // Fallback: regex en el HTML de ocultar
        if (candidates.isEmpty() && ocultar != null) {
            Regex("""https?://[^\\s"'<>]+""").findAll(ocultar.html()).forEach {
                val u = it.value
                if (u.contains("fuegocine.com")) return@forEach
                if (u.endsWith(".jpg") || u.endsWith(".png") || u.endsWith(".css") || u.endsWith(".js")) return@forEach
                candidates.add(u)
            }
        }

        // Último fallback: iframes en todo el documento
        if (candidates.isEmpty()) {
            doc.select("iframe[src]").forEach { candidates.add(it.attr("src")) }
        }

        Log.d("FuegoCine", "loadLinks: ${candidates.size} candidates")
        candidates.distinct().forEach { link ->
            val fixed = fixUrl(link) ?: return@forEach
            Log.d("FuegoCine", "loadLinks: -> $fixed")
            loadExtractor(fixed, data, subtitleCallback, callback)
        }
        return candidates.isNotEmpty()
    }
}
