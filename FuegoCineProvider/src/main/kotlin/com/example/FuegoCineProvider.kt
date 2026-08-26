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

    /**
     * Extract poster per-entry from Blogger feed JSON.
     * Each entry has content.$t with HTML containing <img src="TMDB_URL"/>
     * Returns map of URL slug -> poster URL.
     */
    private fun extractPostersFromFeed(json: String): Map<String, String> {
        // Raw JSON has literal \u003C, \u003E, \/, \" as escape sequences
        val unescaped = json
            .replace("\\/", "/")
            .replace("\\\"", "\"")
            .replace("\\u003C", "<")
            .replace("\\u003E", ">")
        val result = mutableMapOf<String, String>()
        val tmdbRegex = Regex("""<img src="(https?://media\.themoviedb\.org/[^"]+)"""")
        val linkRegex = Regex(""""href":"(https://www\.fuegocine\.com/[^"]+\.html)"""")

        // For each content block, find the nearest preceding link
        val allLinks = linkRegex.findAll(unescaped).toList()
        val allTmdbs = tmdbRegex.findAll(unescaped).toList()

        // Match each TMDB URL to its nearest preceding post URL
        for (tmdbMatch in allTmdbs) {
            val tmdbUrl = tmdbMatch.groupValues[1]
            // Find the last link before this TMDB URL
            val nearestLink = allLinks.lastOrNull { it.range.first < tmdbMatch.range.first }
            if (nearestLink != null) {
                val slug = nearestLink.groupValues[1].substringAfterLast("/").substringBefore(".html")
                result[slug] = tmdbUrl
            }
        }
        Log.d("FuegoCine", "extractPosters: ${result.size} posters from feed")
        return result
    }

    /** Find poster for a URL from the pre-parsed poster map */
    private fun findPoster(url: String, posterMap: Map<String, String>): String? {
        val slug = url.substringAfterLast("/").substringBefore(".html")
        return posterMap[slug]
    }

    /**
     * Detect if a title looks like a series episode: "Name NxM" or "Name NxM - Title"
     */
    private fun parseEpisodeTitle(title: String): Triple<String, Int, Int>? {
        val epRegex = Regex("""(.+?)\s+(\d+)x(\d+)(?:\s+.*)?""", RegexOption.IGNORE_CASE)
        val match = epRegex.find(title) ?: return null
        val seriesName = match.groupValues[1].trim()
        val season = match.groupValues[2].toIntOrNull() ?: 1
        val episode = match.groupValues[3].toIntOrNull() ?: 1
        return Triple(seriesName, season, episode)
    }

    /** Build a clean display title from a URL slug */
    private fun slugToTitle(url: String): String {
        return url.substringAfterLast("/").substringBefore(".html")
            .replace("-", " ").replaceFirstChar { it.uppercase() }
    }

    /**
     * Normalize a series name for comparison: lowercase, remove accents, special chars.
     */
    private fun normalizeSeriesName(name: String): String {
        return name.lowercase()
            .replace(Regex("[áà]"), "a").replace(Regex("[éè]"), "e")
            .replace(Regex("[íì]"), "i").replace(Regex("[óò]"), "o")
            .replace(Regex("[úù]"), "u").replace(Regex("[ñ]"), "n")
            .replace(Regex("[^a-z0-9 ]"), " ")
            .replace(Regex("\\s+"), " ").trim()
    }

    /** Check if two series names are similar enough to be the same series */
    private fun seriesNamesMatch(a: String, b: String): Boolean {
        val na = normalizeSeriesName(a)
        val nb = normalizeSeriesName(b)
        // Direct substring check
        if (na.contains(nb) || nb.contains(na)) return true
        // Word overlap: check if most words from one name appear in the other
        val wordsA = na.split(Regex(" ")).filter { it.length >= 2 }.toSet()
        val wordsB = nb.split(Regex(" ")).filter { it.length >= 2 }.toSet()
        if (wordsA.isEmpty() || wordsB.isEmpty()) return false
        val overlap = wordsA.intersect(wordsB).size
        val maxWords = maxOf(wordsA.size, wordsB.size)
        return overlap.toFloat() / maxWords >= 0.6f
    }

    private fun groupItems(links: List<String>, posterMap: Map<String, String> = emptyMap()): List<SearchResponse> {
        data class EpisodeEntry(val url: String, val seriesName: String, val season: Int, val episode: Int)

        val movies = mutableListOf<Pair<String, String>>() // (title, url)
        val episodesBySeries = mutableMapOf<String, MutableList<EpisodeEntry>>()

        for (link in links) {
            val rawTitle = slugToTitle(link)
            val epInfo = parseEpisodeTitle(rawTitle)
            if (epInfo != null) {
                val (seriesName, season, episode) = epInfo
                episodesBySeries.getOrPut(seriesName) { mutableListOf() }
                    .add(EpisodeEntry(link, seriesName, season, episode))
            } else {
                movies.add(rawTitle to link)
            }
        }

        // Build series items: prefer the landing page URL if available
        val seriesItems = mutableListOf<SearchResponse>()
        for ((seriesName, entries) in episodesBySeries) {
            entries.sortWith(compareBy({ it.season }, { it.episode }))

            val landingPage = movies.firstOrNull { (title, _) ->
                seriesNamesMatch(title, seriesName)
            }

            val seriesUrl = landingPage?.second ?: entries.first().url
            val poster = findPoster(seriesUrl, posterMap)

            seriesItems.add(
                newMovieSearchResponse(seriesName, seriesUrl, TvType.TvSeries) {
                    this.posterUrl = poster
                }
            )
        }

        // Remove movies that were absorbed into series
        val filteredMovies = movies.filter { (title, _) ->
            episodesBySeries.keys.none { seriesName -> seriesNamesMatch(title, seriesName) }
        }

        val movieItems = filteredMovies.map { (title, url) ->
            val poster = findPoster(url, posterMap)
            newMovieSearchResponse(title, url, TvType.Movie) {
                this.posterUrl = poster
            }
        }

        // Series first, then movies
        return seriesItems + movieItems
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

            val items = groupItems(links, posterMap)
            Log.d("FuegoCine", "getMainPage: ${items.size} items (after grouping)")
            if (items.isNotEmpty()) {
                return newHomePageResponse(HomePageList(request.name, items), hasNext = links.size >= 20)
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
        val url = "$mainUrl/feeds/posts/default?alt=json&q=${java.net.URLEncoder.encode(query, "UTF-8")}&max-results=40"
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

        val items = groupItems(links, posterMap)
        Log.d("FuegoCine", "search: ${items.size} items (after grouping from ${links.size} links)")
        return items
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

        val episodes = mutableListOf<Episode>()

        if (isSerie) {
            // Try to find episode links in the page (NxM pattern in href)
            val epLinks = doc.select("a[href*='.html']").map { it.attr("href") }
                .filter { it.contains("fuegocine.com") && it != url && it.contains(Regex("""\d+x\d+""")) }
                .distinct()

            if (epLinks.isNotEmpty()) {
                // Episodes found on the page directly
                var idx = 1
                for (epUrl in epLinks) {
                    val fixedUrl = fixUrl(epUrl) ?: continue
                    val epTitle = slugToTitle(epUrl)
                    val epInfo = parseEpisodeTitle(epTitle)
                    episodes.add(newEpisode(fixedUrl) {
                        this.name = if (epInfo != null) "T${epInfo.second}E${epInfo.third}" else "Episodio $idx"
                        this.season = epInfo?.second ?: 1
                        this.episode = epInfo?.third ?: idx
                    })
                    idx++
                }
            } else {
                // No episode links on page — query Blogger feed to find NxM posts for this series
                val seriesName = title
                    .replace(Regex("""\s*\(\d{4}.*\)\s*$"""), "")
                    .replace(Regex("""\s+\d+x\d+.*$"""), "")
                    .trim()
                val urlSlug = url.substringAfterLast("/").substringBefore(".html")
                Log.d("FuegoCine", "load: no episodes on page, searching feed for '$seriesName'")
                val feedEpisodes = findEpisodesFromFeed(seriesName, urlSlug)
                if (feedEpisodes.isNotEmpty()) {
                    Log.d("FuegoCine", "load: found ${feedEpisodes.size} episodes from feed")
                    for ((epUrl, season, episode) in feedEpisodes) {
                        episodes.add(newEpisode(epUrl) {
                            this.name = "T${season}E${episode}"
                            this.season = season
                            this.episode = episode
                        })
                    }
                }
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

    private suspend fun findEpisodesFromFeed(seriesName: String, urlSlug: String? = null): List<Triple<String, Int, Int>> {
        // Try search by title first, then by slug keywords as fallback
        val queries = mutableListOf(seriesName.trim())
        if (urlSlug != null) {
            // Extract keywords from slug: "tu-amigo-y-vecino-spider-man" -> "amigo vecino spider man"
            val slugWords = urlSlug.replace("-", " ").split(Regex("\\s+")).filter { it.length >= 3 && it != "las" && it != "todas" }
            val slugQuery = slugWords.joinToString(" ")
            if (slugQuery.isNotBlank() && slugQuery != seriesName.trim()) {
                queries.add(slugQuery)
            }
        }

        for (query in queries) {
            val encoded = java.net.URLEncoder.encode(query, "UTF-8")
            Log.d("FuegoCine", "findEpisodesFromFeed: searching '$query'")
            val feedUrl = "$mainUrl/feeds/posts/default?alt=json&q=$encoded&max-results=50"
            val rawJson = try {
                app.get(feedUrl).text
            } catch (e: Exception) {
                Log.e("FuegoCine", "findEpisodesFromFeed: error", e)
                continue
            }
            val links = parseBloggerFeed(rawJson)
            val results = mutableListOf<Triple<String, Int, Int>>()
            for (link in links) {
                val title = slugToTitle(link)
                val epInfo = parseEpisodeTitle(title)
                if (epInfo != null) {
                    if (seriesNamesMatch(epInfo.first, seriesName)) {
                        val fixedUrl = fixUrl(link) ?: continue
                        results.add(Triple(fixedUrl, epInfo.second, epInfo.third))
                    }
                }
            }
            if (results.isNotEmpty()) {
                results.sortWith(compareBy({ it.second }, { it.third }))
                Log.d("FuegoCine", "findEpisodesFromFeed: found ${results.size} episodes with query '$query'")
                return results
            }
        }
        Log.d("FuegoCine", "findEpisodesFromFeed: 0 episodes found")
        return emptyList()
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

    private fun parseSVLinks(doc: org.jsoup.nodes.Document): List<Map<String, String>> {
        val results = mutableListOf<Map<String, String>>()
        val scripts = doc.select("script")
        for (script in scripts) {
            val text = script.html()
            if (!text.contains("_SV_LINKS")) continue
            val arrayStart = text.indexOf("_SV_LINKS")
            if (arrayStart == -1) continue
            val bracketStart = text.indexOf("[", arrayStart)
            val bracketEnd = text.indexOf("]", bracketStart)
            if (bracketStart == -1 || bracketEnd == -1) continue
            val arrayContent = text.substring(bracketStart + 1, bracketEnd)
            val objectRegex = Regex("""\{[^{}]*\}""")
            for (match in objectRegex.findAll(arrayContent)) {
                val obj = match.value
                val entry = mutableMapOf<String, String>()
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
            break
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
        val epInfo = parseEpisodeTitle(rawTitle)
        val displayTitle = if (epInfo != null) "${epInfo.first} - T${epInfo.second}E${epInfo.third}" else rawTitle
        val type = if (epInfo != null) TvType.TvSeries else TvType.Movie
        return newMovieSearchResponse(displayTitle, href, type) {
            this.posterUrl = img
        }
    }
}
