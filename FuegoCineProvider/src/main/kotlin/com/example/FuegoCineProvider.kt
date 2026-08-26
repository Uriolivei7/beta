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

    /** Parse Blogger feed JSON, fixing escaped slashes. Returns list of post URLs. */
    private fun parseBloggerFeed(json: String): List<String> {
        val unescaped = json.replace("\\/", "/")
        val linkRegex = Regex(""""href":"(https://www\.fuegocine\.com/[^"]+\.html)"""")
        return linkRegex.findAll(unescaped).map { it.groupValues[1] }.distinct().toList()
    }

    /** Extract poster image URLs from Blogger feed entries. Returns map of post URL -> poster. */
    private fun extractPostersFromFeed(json: String): Map<String, String> {
        val unescaped = json.replace("\\/", "/")
        val posterMap = mutableMapOf<String, String>()
        // Split by entry boundaries using title as delimiter
        // Each entry has: "title":{"type":"text","$t":"POST_TITLE"}
        val entrySplits = unescaped.split("\"title\":{\"type\":\"text\",\"\$t\":\"")
        val tmdbRegex = Regex("""(https?://media\.themoviedb\.org/[^"\\]+)""")
        val htmlImgRegex = Regex("""<img src=\"(https?://[^\"]+)\"""")
        for (i in 1 until entrySplits.size) {
            val chunk = entrySplits[i]
            val titleEnd = chunk.indexOf('"')
            if (titleEnd == -1) continue
            val title = chunk.substring(0, titleEnd)
            val titleSlug = title.lowercase().replace(" ", "-")
            // Find TMDB poster in this entry's content
            val tmdbMatch = tmdbRegex.find(chunk)
            val htmlMatch = htmlImgRegex.find(chunk)
            val poster = tmdbMatch?.groupValues?.get(1) ?: htmlMatch?.groupValues?.get(1)
            if (poster != null) {
                // Store under multiple possible URL patterns for matching
                posterMap[titleSlug] = poster
            }
        }
        return posterMap
    }

    /** Find poster for a given post URL from the pre-parsed poster map. */
    private fun findPoster(url: String, posterMap: Map<String, String>): String? {
        val slug = url.substringAfterLast("/").substringBefore(".html")
        return posterMap[slug] ?: posterMap.values.firstOrNull()
    }

    /** Detect if a title looks like a series episode: "Name NxM" or "Name NxM - Title" */
    private fun detectEpisodeInfo(title: String): Pair<String, Pair<Int, Int>?> {
        val epRegex = Regex("""(.+?)\s+(\d+)x(\d+)(?:\s+.*)?""", RegexOption.IGNORE_CASE)
        val match = epRegex.find(title) ?: return title to null
        val seriesName = match.groupValues[1].trim()
        val season = match.groupValues[2].toIntOrNull() ?: 1
        val episode = match.groupValues[3].toIntOrNull() ?: 1
        return seriesName to (season to episode)
    }

    /** Build a clean display title from a URL slug */
    private fun slugToTitle(url: String): String {
        return url.substringAfterLast("/").substringBefore(".html")
            .replace("-", " ").replaceFirstChar { it.uppercase() }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = when {
            request.data == "/" -> "$mainUrl/feeds/posts/default?alt=json&max-results=20&start-index=${(page - 1) * 20 + 1}"
            request.data.startsWith("/search/label/") -> {
                val label = request.data.substringAfterLast("/")
                "$mainUrl/feeds/posts/default/-/$label?alt=json&max-results=20&start-index=${(page - 1) * 20 + 1}"
            }
            else -> "$mainUrl${request.data}"
        }

        if (url.contains("alt=json")) {
            val rawJson = try {
                app.get(url).text
            } catch (e: Exception) {
                Log.e("FuegoCine", "getMainPage: error fetching feed", e)
                return newHomePageResponse(emptyList(), false)
            }
            val links = parseBloggerFeed(rawJson)
            val posterMap = extractPostersFromFeed(rawJson)
            Log.d("FuegoCine", "getMainPage: ${request.data} -> ${links.size} links, ${posterMap.size} posters")
            if (links.isNotEmpty()) Log.d("FuegoCine", "getMainPage: first=${links.first()}")

            val items = links.map { link ->
                val rawTitle = slugToTitle(link)
                val (seriesName, epInfo) = detectEpisodeInfo(rawTitle)
                val displayTitle = if (epInfo != null) "$seriesName - T${epInfo.first}E${epInfo.second}" else rawTitle
                val type = if (epInfo != null) TvType.TvSeries else TvType.Movie
                val poster = findPoster(link, posterMap)

                newMovieSearchResponse(displayTitle, link, type) {
                    this.posterUrl = poster
                }
            }
            if (items.isNotEmpty()) {
                return newHomePageResponse(HomePageList(request.name, items), hasNext = items.size >= 20)
            }
            Log.w("FuegoCine", "getMainPage: 0 items from feed, falling back to HTML")
        }

        // HTML fallback
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
        val rawJson = try {
            app.get(url).text
        } catch (e: Exception) {
            Log.e("FuegoCine", "search: error", e)
            return emptyList()
        }
        val links = parseBloggerFeed(rawJson)
        val posterMap = extractPostersFromFeed(rawJson)
        Log.d("FuegoCine", "search: ${links.size} links found, ${posterMap.size} posters")
        return links.map { link ->
            val rawTitle = slugToTitle(link)
            val (seriesName, epInfo) = detectEpisodeInfo(rawTitle)
            val displayTitle = if (epInfo != null) "$seriesName - T${epInfo.first}E${epInfo.second}" else rawTitle
            val type = if (epInfo != null) TvType.TvSeries else TvType.Movie
            val poster = findPoster(link, posterMap)
            newMovieSearchResponse(displayTitle, link, type) {
                this.posterUrl = poster
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("FuegoCine", "load: url=$url")
        val doc = try {
            app.get(url).document
        } catch (e: Exception) {
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
        val isSerie = typeAttr.contains("serie") || typeAttr.contains("episode")
        val year = doc.selectFirst("li[data-year]")?.attr("data-year")?.toIntOrNull()
            ?: Regex("""\b(19\d{2}|20\d{2})\b""").find(doc.text())?.groupValues?.get(1)?.toIntOrNull()
        val tags = doc.selectFirst("ul.post-details")?.attr("data-genres")?.split(",")?.map { it.trim() }

        Log.d("FuegoCine", "load: title=$title isSerie=$isSerie typeAttr=$typeAttr")

        // For series: extract episode links from the page's eps-nav or post body links
        val episodes = mutableListOf<Episode>()
        if (isSerie) {
            // Look for episode links in the page
            val epLinks = doc.select("a[href*='.html']").map { it.attr("href") }
                .filter { it.contains("fuegocine.com") && it != url && it.contains(Regex("""\d+x\d+""")) }
                .distinct()
            var idx = 1
            for (epUrl in epLinks) {
                val fixedUrl = fixUrl(epUrl) ?: continue
                val epTitle = slugToTitle(epUrl)
                val (_, epInfo) = detectEpisodeInfo(epTitle)
                episodes.add(newEpisode(fixedUrl) {
                    this.name = if (epInfo != null) "T${epInfo.first}E${epInfo.second}" else "Episodio $idx"
                    this.season = epInfo?.first ?: 1
                    this.episode = epInfo?.second ?: idx
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
            // Series episode page loaded directly — treat as single episode
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
        val doc = try {
            app.get(data).document
        } catch (e: Exception) {
            Log.e("FuegoCine", "loadLinks: error fetching page", e)
            return false
        }

        // PRIMARY: Parse _SV_LINKS JavaScript variable from <script> tags
        val svLinks = parseSVLinks(doc)
        if (svLinks.isNotEmpty()) {
            Log.d("FuegoCine", "loadLinks: found ${svLinks.size} _SV_LINKS entries")
            for (entry in svLinks) {
                val url = entry["url"] ?: continue
                val lang = entry["lang"] ?: "es"
                val name = entry["name"] ?: "Server"
                Log.d("FuegoCine", "loadLinks: [$lang] $name -> $url")
                loadExtractor(url, data, subtitleCallback, callback)
            }
            return true
        }

        Log.w("FuegoCine", "loadLinks: no _SV_LINKS found")
        return false
    }

    /**
     * Parse the _SV_LINKS JavaScript array from the page.
     * Format: const _SV_LINKS = [{ lang: "lat", name: "FC✅", url: "...", tagVideo: false }, ...]
     */
    private fun parseSVLinks(doc: org.jsoup.nodes.Document): List<Map<String, String>> {
        val results = mutableListOf<Map<String, String>>()
        // Find all <script> tags
        val scripts = doc.select("script")
        for (script in scripts) {
            val text = script.html()
            if (!text.contains("_SV_LINKS")) continue
            // Extract the array content between [ and ]
            val arrayStart = text.indexOf("_SV_LINKS")
            if (arrayStart == -1) continue
            val bracketStart = text.indexOf("[", arrayStart)
            val bracketEnd = text.indexOf("]", bracketStart)
            if (bracketStart == -1 || bracketEnd == -1) continue
            val arrayContent = text.substring(bracketStart + 1, bracketEnd)
            // Parse each { ... } block
            val objectRegex = Regex("""\{[^{}]*\}""")
            for (match in objectRegex.findAll(arrayContent)) {
                val obj = match.value
                val entry = mutableMapOf<String, String>()
                // Extract key: value pairs (non-quoted and quoted)
                val kvRegex = Regex("""(\w+)\s*:\s*(?:"([^"]*)"|([^,}\s]+))""")
                for (kv in kvRegex.findAll(obj)) {
                    val value = (if (kv.groupValues[2].isNotEmpty()) kv.groupValues[2] else kv.groupValues[3])
                        .replace("&#9989;", "✅")
                        .replace("&#10004;", "✅")
                    entry[kv.groupValues[1]] = value
                }
                if (entry.containsKey("url")) {
                    results.add(entry)
                }
            }
            break // Only need first _SV_LINKS
        }
        return results
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
        val rawTitle = slugToTitle(href)
        val (seriesName, epInfo) = detectEpisodeInfo(rawTitle)
        val displayTitle = if (epInfo != null) "$seriesName - T${epInfo.first}E${epInfo.second}" else rawTitle
        val type = if (epInfo != null) TvType.TvSeries else TvType.Movie
        return newMovieSearchResponse(displayTitle, href, type) {
            this.posterUrl = img
        }
    }
}
