package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import android.util.Log
import kotlinx.coroutines.*

data class SeriesDef(
    val name: String,
    val slug: String,
    val type: TvType,
    var poster: String = "",
    var posterLoaded: Boolean = false,
    val labelName: String? = null,  // if set, use label search instead of /p/ page
    val postUrl: String? = null,    // if set, direct blog post URL (for movies)
)

class LegadoskywalkerProvider : MainAPI() {
    override var mainUrl = "https://serieslegadoskywalker.blogspot.com"
    override var name = "LegadoSkywalker"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = false
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Movie, TvType.Cartoon)

    private var mainPageCache: org.jsoup.nodes.Document? = null

    private fun encodeLabel(label: String) = java.net.URLEncoder.encode(label, "UTF-8").replace("+", "%20")

    private suspend fun getMainPageCached(): org.jsoup.nodes.Document {
        if (mainPageCache == null) {
            mainPageCache = app.get(mainUrl, timeout = 30L).document
        }
        return mainPageCache!!
    }

    private suspend fun fetchFeedEpisodes(labelName: String): MutableList<Episode> {
        val episodes = mutableListOf<Episode>()
        try {
            val feedUrl = "$mainUrl/feeds/posts/default/-/${encodeLabel(labelName)}?alt=json&max-results=50"
            Log.d("LegadoSkywalker", "feed API: $feedUrl")
            val raw = app.get(feedUrl, timeout = 30L).text
            val json = org.json.JSONObject(raw)
            val entries = json.getJSONObject("feed").optJSONArray("entry") ?: return episodes
            for (i in 0 until entries.length()) {
                try {
                    val entry = entries.getJSONObject(i)
                    val title = entry.getJSONObject("title").getString("\$t")
                    val links = entry.getJSONArray("link")
                    var url: String? = null
                    for (j in 0 until links.length()) {
                        val link = links.getJSONObject(j)
                        if (link.optString("rel") == "alternate") {
                            url = link.getString("href")
                            break
                        }
                    }
                    if (url == null) continue
                    val thumbnail = entry.optJSONObject("media\$thumbnail")?.optString("url")
                        ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                    val epMatch = Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(url)
                        ?: Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(title)
                    val seasonNum = epMatch?.groupValues?.get(1)?.toIntOrNull() ?: 1
                    val epNum = epMatch?.groupValues?.get(2)?.toIntOrNull() ?: (episodes.size + 1)
                    val epName = title.replace(Regex("""\s*\[T\d+[-\s]*C\d+\]\s*"""), "").trim()
                    episodes.add(newEpisode(url) {
                        this.name = epName.ifBlank { title }
                        this.season = seasonNum
                        this.episode = epNum
                        this.posterUrl = thumbnail
                    })
                } catch (e: Exception) {}
            }
        } catch (e: Exception) {
            Log.e("LegadoSkywalker", "feed API error: ${e.message}")
        }
        return episodes
    }

    override val mainPage = mainPageOf(
        "CAT:animadas" to "Series Animadas",
        "CAT:liveaction" to "Series Live Action",
        "CAT:peliculas" to "Películas",
        "CAT:otros" to "Otros",
    )

    private val animadas = mutableListOf(
        SeriesDef("The Clone Wars", "the-clone-wars", TvType.Cartoon),
        SeriesDef("Rebels", "rebels", TvType.Cartoon),
        SeriesDef("The Bad Batch", "the-bad-batch", TvType.Cartoon, labelName = "The Bad Batch"),
        SeriesDef("Star Wars Visions", "star-wars-visions", TvType.Cartoon),
        SeriesDef("Tales of the Jedi", "tales-of-the-jedi", TvType.Cartoon, labelName = "Tales of the Jedi"),
        SeriesDef("Tales of the Empire", "tales-of-the-empire", TvType.Cartoon, labelName = "Tales of the Empire"),
        SeriesDef("Clone Wars (2003)", "clone-wars", TvType.Cartoon),
        SeriesDef("Resistance", "resistance", TvType.Cartoon, labelName = "Resistance"),
        SeriesDef("Star Wars Droids", "star-wars-droids", TvType.Cartoon, labelName = "Star Wars Droids"),
        SeriesDef("Star Wars Ewoks", "star-wars-ewoks", TvType.Cartoon, labelName = "Star Wars Ewoks"),
        SeriesDef("Lego Star Wars", "lego-star-wars", TvType.Cartoon, labelName = "Lego Star Wars"),
        SeriesDef("Maul: Shadow Lord", "maul-shadow-lord", TvType.Cartoon, labelName = "Maul : Shadow Lord"),
    )

    private val liveaction = mutableListOf(
        SeriesDef("The Mandalorian", "the-mandalorian", TvType.TvSeries, labelName = "The Mandalorian"),
        SeriesDef("Ahsoka", "ahsoka", TvType.TvSeries, labelName = "Ahsoka"),
        SeriesDef("Andor", "andor", TvType.TvSeries, labelName = "Andor"),
        SeriesDef("Obi-Wan Kenobi", "obi-wan-kenobi", TvType.TvSeries, labelName = "Obi-Wan Kenobi"),
        SeriesDef("The Book of Boba Fett", "the-book-of-boba-fett", TvType.TvSeries, labelName = "The Book of Boba Fett"),
        SeriesDef("The Acolyte", "the-acolyte", TvType.TvSeries, labelName = "The Acolyte"),
        SeriesDef("Skeleton Crew", "skeleton-crew", TvType.TvSeries, labelName = "Skeleton Crew"),
    )

    private val peliculas = mutableListOf(
        SeriesDef("Episodio I: La Amenaza Fantasma", "Episodio I", TvType.Movie),
        SeriesDef("Episodio II: El Ataque de los Clones", "Episodio II", TvType.Movie),
        SeriesDef("Episodio III: La Venganza de los Sith", "Episodio III", TvType.Movie),
        SeriesDef("Episodio IV: Una Nueva Esperanza", "Episodio IV", TvType.Movie),
        SeriesDef("Episodio V: El Imperio Contraataca", "Episodio V", TvType.Movie),
        SeriesDef("Episodio VI: El Retorno del Jedi", "Episodio VI", TvType.Movie),
        SeriesDef("Episodio VII: El Despertar de la Fuerza", "Episodio VII", TvType.Movie),
        SeriesDef("Episodio VIII: Los Últimos Jedi", "Episodio VIII", TvType.Movie),
        SeriesDef("Episodio IX: El Ascenso de Skywalker", "Episodio IX", TvType.Movie),
        SeriesDef("Rogue One", "Rogue One", TvType.Movie),
        SeriesDef("Han Solo: Una Historia de Star Wars", "Han Solo", TvType.Movie),
    )

    private val otros = mutableListOf(
        SeriesDef("Fan Edits", "Fan-edits", TvType.Movie),
        SeriesDef("Star Wars Vintage", "Star Wars Vintage", TvType.Movie),
        SeriesDef("Documentales", "Documentales", TvType.Movie),
        SeriesDef("Cortos", "Cortos", TvType.Movie),
    )

    private fun allSeries(): List<SeriesDef> = animadas + liveaction
    private fun allMovies(): List<SeriesDef> = peliculas + otros

    private suspend fun loadPoster(slug: String, isSeriesPage: Boolean = true): String? {
        val url = if (isSeriesPage) "$mainUrl/p/${encodeLabel(slug)}.html" else "$mainUrl/search/label/${encodeLabel(slug)}"
        return try {
            val doc = app.get(url, timeout = 30L).document
            doc.select("div.post img, .post-body img, .separator img, .entry-content img").firstOrNull()?.attr("abs:src")
                ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
        } catch (e: Exception) { null }
    }

    private suspend fun loadPosterFromMainPage(seriesName: String): String? {
        return try {
            val doc = getMainPageCached()
            val sn = seriesName.lowercase()
            val items = doc.select(".movie-item")
            // Exact match by data-tooltip (full name), then by p text (short name)
            items.firstOrNull { item -> item.select("p").attr("data-tooltip").lowercase() == sn }
                ?: items.firstOrNull { item -> item.select("p").text().trim().lowercase() == sn }
                ?.select("img")?.firstOrNull()?.attr("abs:src")
                ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
        } catch (e: Exception) { null }
    }

    private suspend fun ensureSeriesPoster(s: SeriesDef) {
        if (s.posterLoaded) return
        s.poster = loadPoster(s.slug, isSeriesPage = true)
            ?: loadPoster(s.labelName ?: s.slug, isSeriesPage = false)
            ?: loadPosterFromMainPage(s.name)
            ?: ""
        Log.d("LegadoSkywalker", "Poster ${s.name}: ${s.poster}")
        s.posterLoaded = true
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d("LegadoSkywalker", "getMainPage: ${request.data}")
        val series = when (request.data) {
            "CAT:animadas" -> animadas
            "CAT:liveaction" -> liveaction
            "CAT:peliculas" -> peliculas
            "CAT:otros" -> otros
            else -> return newHomePageResponse(HomePageList(request.name, emptyList()), hasNext = false)
        }
        series.forEach { ensureSeriesPoster(it) }
        val items = series.map { s ->
            val link = if (s.type == TvType.Movie) "MOVIE:${s.slug}" else "SERIES:${s.slug}"
            newMovieSearchResponse(s.name, link, s.type) {
                this.posterUrl = s.poster.ifBlank { null }
            }
        }
        return newHomePageResponse(HomePageList(request.name, items, isHorizontalImages = true), hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("LegadoSkywalker", "search: $query")
        val ql = query.lowercase()
        val results = mutableListOf<SearchResponse>()

        allSeries().forEach { s ->
            if (s.name.lowercase().contains(ql) || s.slug.lowercase().contains(ql)) {
                results.add(newMovieSearchResponse(s.name, "SERIES:${s.slug}", s.type) {
                    this.posterUrl = s.poster.ifBlank { null }
                })
            }
        }
        allMovies().forEach { m ->
            if (m.name.lowercase().contains(ql) || m.slug.lowercase().contains(ql)) {
                results.add(newMovieSearchResponse(m.name, "MOVIE:${m.slug}", m.type) {
                    this.posterUrl = m.poster.ifBlank { null }
                })
            }
        }
        if (results.isNotEmpty()) {
            Log.d("LegadoSkywalker", "search: ${results.size} hardcoded matches")
            return results
        }

        try {
            val encoded = java.net.URLEncoder.encode(query, "UTF-8")
            val doc = app.get("$mainUrl/search?q=$encoded&max-results=20", timeout = 30L).document
            doc.select("div.post").forEach { post ->
                try {
                    val link = post.select("h1 a, h2 a").firstOrNull()?.attr("abs:href")
                        ?: post.select("a[href*='/20']").firstOrNull()?.attr("abs:href") ?: return@forEach
                    val title = post.select("h1, h2").text().trim()
                    val poster = post.select("img").firstOrNull()?.attr("abs:src")
                        ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                    val type = if (title.contains("[T", ignoreCase = true)) TvType.TvSeries else TvType.Movie
                    results.add(newMovieSearchResponse(title, link, type) { this.posterUrl = poster })
                } catch (e: Exception) {}
            }
        } catch (e: Exception) {
            Log.e("LegadoSkywalker", "search error: ${e.message}")
        }
        return results
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("LegadoSkywalker", "load: $url")
        val u = url.removePrefix(mainUrl).removePrefix("/")
        Log.d("LegadoSkywalker", "load clean: $u")
        when {
            u.startsWith("SERIES:") -> return loadSeries(u.removePrefix("SERIES:"))
            u.startsWith("MOVIE:") -> return loadMovieByLabel(u.removePrefix("MOVIE:"))
            u.contains("/p/") && u.contains("temporada") -> return loadSeasonPage(url)
            else -> return loadEpisodePage(url)
        }
    }

    private suspend fun loadSeries(slug: String): LoadResponse {
        Log.d("LegadoSkywalker", "loadSeries: slug=$slug")
        val seriesName = allSeries().firstOrNull { it.slug == slug }?.name ?: slug
        val def = allSeries().firstOrNull { it.slug == slug }

        // Label-based series fetch label search page instead of /p/ page
        if (def?.labelName != null) {
            val labelUrl = "$mainUrl/search/label/${encodeLabel(def.labelName)}?max-results=50"
            Log.d("LegadoSkywalker", "loadSeries: label series, fetching $labelUrl")
            return try {
                val doc = app.get(labelUrl, referer = mainUrl, timeout = 30L).document
                val episodes = mutableListOf<Episode>()
                val seen = mutableSetOf<String>()
                val postElements = doc.select("div.post")
                Log.d("LegadoSkywalker", "loadSeries: ${postElements.size} posts from label page")
                postElements.forEach { post ->
                    try {
                        val link = post.select("h1 a, h2 a").firstOrNull()?.attr("abs:href")
                            ?: post.select("a[href*='/20']").firstOrNull()?.attr("abs:href") ?: return@forEach
                        if (!seen.add(link)) return@forEach
                        val title = post.select("h1, h2").text().trim()
                        val poster = post.select("img").firstOrNull()?.attr("abs:src")
                            ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                        val epMatch = Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(link)
                            ?: Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(title)
                        val seasonNum = epMatch?.groupValues?.get(1)?.toIntOrNull() ?: 1
                        val epNum = epMatch?.groupValues?.get(2)?.toIntOrNull() ?: (episodes.size + 1)
                        val epName = title.replace(Regex("""\s*\[T\d+[-\s]*C\d+\]\s*"""), "").trim()
                        episodes.add(newEpisode(link) {
                            this.name = epName.ifBlank { title }
                            this.season = seasonNum
                            this.episode = epNum
                            this.posterUrl = poster
                        })
                    } catch (e: Exception) {}
                }
                if (episodes.isEmpty()) {
                    Log.e("LegadoSkywalker", "loadSeries: no episodes on label page for $slug, trying feed API")
                    val feedEpisodes = fetchFeedEpisodes(def.labelName)
                    if (feedEpisodes.isNotEmpty()) {
                        feedEpisodes.sortBy { (it.season ?: 1) * 1000 + (it.episode ?: 1) }
                        Log.d("LegadoSkywalker", "loadSeries: ${feedEpisodes.size} feed API episodes for $slug")
                        return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, feedEpisodes) {
                            this.plot = "Todos los capítulos de $seriesName"; this.posterUrl = def?.poster?.ifBlank { null }
                        }
                    }
                    return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, emptyList())
                }
                episodes.sortBy { (it.season ?: 1) * 1000 + (it.episode ?: 1) }
                Log.d("LegadoSkywalker", "loadSeries: ${episodes.size} label episodes for $slug")
                newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, episodes) {
                    this.plot = "Todos los capítulos de $seriesName"; this.posterUrl = def?.poster?.ifBlank { null }
                }
            } catch (e: Exception) {
                Log.e("LegadoSkywalker", "loadSeries: label fetch error: ${e.message}")
                return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, emptyList()) {
                    this.plot = "Error al cargar la serie"; this.posterUrl = def?.poster?.ifBlank { null }
                }
            }
        }

        // /p/ page series
        val seriesUrl = "$mainUrl/p/${encodeLabel(slug)}.html"
        val doc = try {
            app.get(seriesUrl, referer = mainUrl, timeout = 30L).document
        } catch (e: Exception) {
            Log.e("LegadoSkywalker", "loadSeries: cannot fetch $seriesUrl: ${e.message}")
            return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, emptyList()) {
                this.plot = "Error al cargar la serie"; this.posterUrl = def?.poster?.ifBlank { null }
            }
        }

        val body = doc.select(".post-body.entry-content").firstOrNull()
            ?: doc.select(".post-body").firstOrNull()
        Log.d("LegadoSkywalker", "loadSeries: body=${body != null}")

        val linkPool = (body ?: doc).select("a[href*='temporada']")
        Log.d("LegadoSkywalker", "loadSeries: ${linkPool.size} raw 'temporada' links")

        var seasonUrls = linkPool.mapNotNull { a ->
            a.attr("abs:href").ifBlank { null }
        }.filter { it.contains(slug, ignoreCase = true) && it.contains("temporada") }
            .map { it.replace("http://", "https://") }
            .distinctBy { 
                it.substringAfter(mainUrl).substringBefore("#").substringBefore("?")
                    .replace(Regex("""temporada[-\s]*(\d+)_\d+"""), "temporada-$1")
            }
            .sorted()
        Log.d("LegadoSkywalker", "loadSeries: ${seasonUrls.size} season URLs for slug '$slug': $seasonUrls")

        if (seasonUrls.isEmpty()) {
            Log.w("LegadoSkywalker", "loadSeries: no season links for slug='$slug', trying doc-wide")
            val allAnchors = doc.select("a[href*='temporada']")
            Log.d("LegadoSkywalker", "loadSeries: doc-wide a[href*='temporada']: ${allAnchors.size}")
            allAnchors.forEach { a ->
                val h = a.attr("abs:href")
                val r = a.attr("href")
                Log.d("LegadoSkywalker", "  href=$r abs:href=$h text=[${a.text()}]")
            }
            seasonUrls = allAnchors.mapNotNull { a ->
                a.attr("abs:href").ifBlank { null }
            }.filter { it.contains(slug, ignoreCase = true) && it.contains("temporada") }
                .map { it.replace("http://", "https://") }
                .distinctBy { 
                    it.substringAfter(mainUrl).substringBefore("#").substringBefore("?")
                        .replace(Regex("""temporada[-\s]*(\d+)_\d+"""), "temporada-$1")
                }
                .sorted()
        }

        if (seasonUrls.isEmpty()) {
            if (body != null) {
                Log.w("LegadoSkywalker", "loadSeries: no temporada links for $slug, trying inline episode links")
                val episodeLinks = body.select("a[href*='/20']").filter { a ->
                    val href = a.attr("abs:href").ifBlank { a.attr("href") }
                    val text = a.text().trim()
                    href.replace("http://", "https://").startsWith(mainUrl) && text.isNotBlank()
                        && !href.contains("temporada") && !href.contains("label")
                        && !href.contains("search") && !href.contains("comment")
                }
                if (episodeLinks.isNotEmpty()) {
                    val epList = mutableListOf<Episode>()
                    val seen = mutableSetOf<String>()
                    var epCounter = 1
                    episodeLinks.forEach { a ->
                        val href = a.attr("abs:href").ifBlank { a.attr("href") }.replace("http://", "https://")
                        val text = a.text().trim()
                        if (!seen.add(href)) return@forEach
                        val epFromUrl = Regex("""[cC](\d+)""").find(href)?.groupValues?.get(1)?.toIntOrNull()
                        val seasonFromUrl = Regex("""[tT](\d+)""").find(href)?.groupValues?.get(1)?.toIntOrNull()
                        epList.add(newEpisode(href) {
                            this.name = text
                            this.season = seasonFromUrl ?: 1
                            this.episode = epFromUrl ?: epCounter
                        })
                        epCounter++
                    }
                    if (epList.isNotEmpty()) {
                        Log.d("LegadoSkywalker", "loadSeries: ${epList.size} inline episodes for $slug")
                        return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, epList) {
                            this.plot = "Todos los capítulos de $seriesName"; this.posterUrl = def?.poster?.ifBlank { null }
                        }
                    }
                }
            } else {
                // body == null: /p/ page doesn't exist, try label fallback with series name
                Log.w("LegadoSkywalker", "loadSeries: body=null for $slug, trying label fallback")
                try {
                    val labelDoc = app.get("$mainUrl/search/label/${encodeLabel(seriesName)}?max-results=50", referer = mainUrl, timeout = 30L).document
                    val posts = labelDoc.select("div.post")
                    if (posts.isNotEmpty()) {
                        val epList = mutableListOf<Episode>()
                        val seen = mutableSetOf<String>()
                        posts.forEach { post ->
                            try {
                                val link = post.select("h1 a, h2 a").firstOrNull()?.attr("abs:href")
                                    ?: post.select("a[href*='/20']").firstOrNull()?.attr("abs:href") ?: return@forEach
                                if (!seen.add(link)) return@forEach
                                val title = post.select("h1, h2").text().trim()
                                val poster = post.select("img").firstOrNull()?.attr("abs:src")?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                                val epMatch = Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(link) ?: Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(title)
                                epList.add(newEpisode(link) {
                                    this.name = title.replace(Regex("""\s*\[T\d+[-\s]*C\d+\]\s*"""), "").trim().ifBlank { title }
                                    this.season = epMatch?.groupValues?.get(1)?.toIntOrNull() ?: 1
                                    this.episode = epMatch?.groupValues?.get(2)?.toIntOrNull() ?: (epList.size + 1)
                                    this.posterUrl = poster
                                })
                            } catch (e: Exception) {}
                        }
                        if (epList.isNotEmpty()) {
                            epList.sortBy { (it.season ?: 1) * 1000 + (it.episode ?: 1) }
                            Log.d("LegadoSkywalker", "loadSeries: ${epList.size} label fallback episodes for $slug")
                            return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, epList) {
                                this.plot = "Todos los capítulos de $seriesName"; this.posterUrl = def?.poster?.ifBlank { null }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LegadoSkywalker", "loadSeries: label fallback error: ${e.message}")
                }
                // Try feed API as last resort
                Log.w("LegadoSkywalker", "loadSeries: label fallback empty, trying feed API for $slug")
                val feedEpisodes = fetchFeedEpisodes(seriesName)
                if (feedEpisodes.isNotEmpty()) {
                    feedEpisodes.sortBy { (it.season ?: 1) * 1000 + (it.episode ?: 1) }
                    Log.d("LegadoSkywalker", "loadSeries: ${feedEpisodes.size} feed API episodes for $slug")
                    return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, feedEpisodes) {
                        this.plot = "Todos los capítulos de $seriesName"; this.posterUrl = def?.poster?.ifBlank { null }
                    }
                }
            }
            Log.e("LegadoSkywalker", "loadSeries: no temporada links for $slug")
            return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, emptyList()) {
                this.plot = "No se encontraron temporadas"; this.posterUrl = def?.poster?.ifBlank { null }
            }
        }

        val allEpisodes = mutableListOf<Episode>()
        seasonUrls.forEach { seasonUrl ->
            val seasonNum = Regex("""temporada[-\s]*(\d+)""").find(seasonUrl)?.groupValues?.get(1)?.toIntOrNull()
            val isPostUrl = seasonUrl.contains("/20") && !seasonUrl.contains("/p/")
            try {
                val seasonDoc = app.get(seasonUrl, referer = mainUrl, timeout = 30L).document
                val seasonBody = seasonDoc.select(".post-body.entry-content, .post-body, #post-body, .entry-content").firstOrNull()
                val foundEpisodes = mutableListOf<Episode>()

                var epCounter = 1
                if (seasonBody != null) {
                    seasonBody.select("a[href]").forEach { a ->
                        val href = a.attr("abs:href")
                        val text = a.text().trim()
                        if (href.isBlank() || !href.contains("/20")
                            || text.contains("Anterior", true) || text.contains("Siguiente", true)
                            || text.contains("Temporada", true) || text.contains("atrás", true)
                            || text.contains("regresar", true) || text.contains("volver", true)
                            || href.contains("temporada") || href.contains("label")) return@forEach
                        if (!href.replace("http://", "https://").startsWith(mainUrl)) return@forEach
                        val epFromUrl = Regex("""[cC](\d+)""").find(href)?.groupValues?.get(1)?.toIntOrNull()
                        foundEpisodes.add(newEpisode(href) {
                            this.name = text; this.season = seasonNum ?: 1; this.episode = epFromUrl ?: epCounter
                        })
                        epCounter++
                    }
                } else {
                    Log.w("LegadoSkywalker", "  season $seasonNum: no post-body, trying doc links")
                    seasonDoc.select("a[href]").forEach { a ->
                        val href = a.attr("abs:href")
                        val text = a.text().trim()
                        if (href.contains("/20") && text.isNotBlank()
                            && !text.contains("Anterior", true) && !text.contains("Siguiente", true)
                            && !text.contains("Temporada", true) && !href.contains("temporada")) {
                            val epFromUrl = Regex("""[cC](\d+)""").find(href)?.groupValues?.get(1)?.toIntOrNull()
                            foundEpisodes.add(newEpisode(href) {
                                this.name = text; this.season = seasonNum ?: 1; this.episode = epFromUrl ?: epCounter
                            })
                            epCounter++
                        }
                    }
                }

                if (foundEpisodes.isNotEmpty()) {
                    allEpisodes.addAll(foundEpisodes)
                } else if (isPostUrl) {
                    Log.d("LegadoSkywalker", "  season $seasonNum: direct episode post (fallback) $seasonUrl")
                    val epTitle = seasonDoc.select("h1, h2").firstOrNull()?.text()?.trim()
                        ?: seasonDoc.select("title").text().trim().substringBefore("|").trim()
                    val epName = epTitle.replace(Regex("""\s*\[T\d+[-\s]*C\d+\]\s*"""), "").trim()
                    val epFromUrl = Regex("""[cC](\d+)""").find(seasonUrl)?.groupValues?.get(1)?.toIntOrNull()
                    allEpisodes.add(newEpisode(seasonUrl) {
                        this.name = epName.ifBlank { "Episodio ${seasonNum ?: (allEpisodes.size + 1)}" }
                        this.season = seasonNum ?: 1
                        this.episode = epFromUrl ?: (seasonNum ?: (allEpisodes.size + 1))
                    })
                }
            } catch (e: Exception) {
                Log.e("LegadoSkywalker", "  season $seasonNum error: ${e.message}")
            }
        }

        if (allEpisodes.isEmpty()) {
            Log.e("LegadoSkywalker", "loadSeries: NO episodes found for $slug")
            return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, emptyList()) {
                this.posterUrl = def?.poster?.ifBlank { null }
            }
        }

        allEpisodes.sortBy { (it.season ?: 1) * 1000 + (it.episode ?: 1) }
        Log.d("LegadoSkywalker", "loadSeries: ${allEpisodes.size} total episodes")
        return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, allEpisodes) {
            this.plot = "Todos los capítulos de $seriesName"; this.posterUrl = def?.poster?.ifBlank { null }
        }
    }

    private suspend fun loadSeasonPage(url: String): LoadResponse {
        Log.d("LegadoSkywalker", "loadSeasonPage: $url")
        val seasonNum = Regex("""temporada[-\s]*(\d+)""").find(url)?.groupValues?.get(1)?.toIntOrNull() ?: 1
        val doc = app.get(url, referer = mainUrl, timeout = 30L).document
        val body = doc.select(".post-body.entry-content, .post-body").firstOrNull()
        val title = doc.select("title").text().trim().substringBefore("|").trim()
        val episodes = mutableListOf<Episode>()

        if (body != null) {
            body.select("a[href]").forEachIndexed { idx, a ->
                val href = a.attr("abs:href")
                val text = a.text().trim()
                if (href.contains(mainUrl) && href.contains("/20") && text.isNotBlank() && !href.contains("temporada")) {
                    val epFromUrl = Regex("""[cC](\d+)""").find(href)?.groupValues?.get(1)?.toIntOrNull()
                    episodes.add(newEpisode(href) {
                        this.name = text
                        this.season = seasonNum
                        this.episode = epFromUrl ?: (idx + 1)
                    })
                }
            }
        }

        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
            this.plot = "Temporada $seasonNum"
        }
    }

    private suspend fun loadEpisodePage(url: String): LoadResponse {
        Log.d("LegadoSkywalker", "loadEpisodePage: $url")
        val doc = app.get(url, referer = mainUrl, timeout = 30L).document
        val title = doc.select("h1, h2").firstOrNull()?.text()?.trim()
            ?: doc.select("title").text().trim().substringBefore("|").trim()
        val postBody = doc.select(".post-body.entry-content").firstOrNull() ?: doc.select(".post-body").firstOrNull()
        val poster = postBody?.select("img")?.firstOrNull()?.attr("abs:src")
            ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/") ?: ""
        val plot = postBody?.text()?.trim()?.take(500)
        val epMatch = Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(url)
        if (epMatch != null) {
            val season = epMatch.groupValues[1].toIntOrNull() ?: 1
            val episode = epMatch.groupValues[2].toIntOrNull() ?: 1
            val epName = title.replace(Regex("""\s*\[T\d+[-\s]*C\d+\]\s*"""), "").trim()
            val episodeData = newEpisode(url) {
                this.name = epName.ifBlank { title }; this.season = season; this.episode = episode; this.posterUrl = poster
            }
            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, listOf(episodeData)) {
                this.posterUrl = poster; this.plot = plot
            }
        }
        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster; this.plot = plot
        }
    }

    private suspend fun loadMovieByLabel(slug: String): LoadResponse {
        Log.d("LegadoSkywalker", "loadMovieByLabel: $slug")
        val movie = allMovies().firstOrNull { it.slug == slug }
        val name = movie?.name ?: slug
        val sn = name.lowercase()

        // Use feed API filtered by "Peliculas" label (more reliable than blog search)
        try {
            val feedUrl = "$mainUrl/feeds/posts/default/-/Peliculas?alt=json&max-results=50"
            val raw = app.get(feedUrl, timeout = 30L).text
            val json = org.json.JSONObject(raw)
            val entries = json.getJSONObject("feed").optJSONArray("entry") ?: throw Exception("no entries")
            for (i in 0 until entries.length()) {
                try {
                    val entry = entries.getJSONObject(i)
                    val title = entry.getJSONObject("title").getString("\$t")
                    val links = entry.getJSONArray("link")
                    var url: String? = null
                    var poster: String? = null
                    for (j in 0 until links.length()) {
                        val link = links.getJSONObject(j)
                        if (link.optString("rel") == "alternate") {
                            url = link.getString("href")
                            break
                        }
                    }
                    if (url == null) continue
                    val thumb = entry.optJSONObject("media\$thumbnail")?.optString("url")
                        ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                    // Exact title match first, then starts-with for compound titles
                    val tn = title.lowercase()
                    if (tn == sn || tn.startsWith(sn) || sn.startsWith(tn)) {
                        poster = thumb
                        return newMovieLoadResponse(name, url, TvType.Movie, url) {
                            this.posterUrl = poster
                        }
                    }
                } catch (e: Exception) {}
            }
        } catch (e: Exception) {
            Log.e("LegadoSkywalker", "loadMovieByLabel: feed API error: ${e.message}")
        }

        // Fallback: blog search
        Log.w("LegadoSkywalker", "loadMovieByLabel: feed API failed for $slug, trying blog search")
        try {
            val searchQ = encodeLabel(name)
            val doc = app.get("$mainUrl/search?q=$searchQ&max-results=5", timeout = 30L).document
            val posts = doc.select("div.post")
            for (post in posts) {
                try {
                    val link = post.select("h1 a, h2 a").firstOrNull()?.attr("abs:href")
                        ?: post.select("a[href*='/20']").firstOrNull()?.attr("abs:href") ?: continue
                    val title = post.select("h1, h2").text().trim()
                    val tn = title.lowercase()
                    // Avoid documentary/BTS posts
                    if (tn.contains("detrás") || tn.contains("detras") || tn.contains("escenas eliminadas") || tn.contains("documental")) continue
                    if (tn == sn || tn.startsWith(sn) || sn.startsWith(tn)) {
                        val poster = post.select("img").firstOrNull()?.attr("abs:src")
                            ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                        return newMovieLoadResponse(name, link, TvType.Movie, link) {
                            this.posterUrl = poster
                        }
                    }
                } catch (e: Exception) {}
            }
            val first = posts.firstOrNull()
            if (first != null) {
                val link = first.select("h1 a, h2 a").firstOrNull()?.attr("abs:href")
                    ?: first.select("a[href*='/20']").firstOrNull()?.attr("abs:href") ?: ""
                if (link.isNotBlank()) {
                    val poster = first.select("img").firstOrNull()?.attr("abs:src")
                        ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                    return newMovieLoadResponse(name, link, TvType.Movie, link) {
                        this.posterUrl = poster
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("LegadoSkywalker", "loadMovieByLabel: search error: ${e.message}")
        }

        Log.e("LegadoSkywalker", "loadMovieByLabel: no results for $slug")
        return newMovieLoadResponse(name, "$mainUrl/search?q=${encodeLabel(name)}", TvType.Movie, "$mainUrl/search?q=${encodeLabel(name)}") {
            this.plot = name
        }
    }

    override suspend fun loadLinks(
        data: String, isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("LegadoSkywalker", "loadLinks: $data")
        val doc = app.get(data, referer = mainUrl, timeout = 60L).document
        val urls = mutableListOf<String>()

        doc.select("iframe[data-src]").forEach { iframe ->
            val src = iframe.attr("data-src")
            val abs = when {
                src.startsWith("//") -> "https:$src"
                src.startsWith("http") -> src
                src.startsWith("/") -> "$mainUrl$src"
                else -> src
            }
            if (abs.isNotBlank() && !abs.startsWith("data:")) urls.add(abs)
        }
        if (urls.isEmpty()) {
            doc.select("iframe[src]").forEach { iframe ->
                val src = iframe.attr("abs:src").ifBlank { iframe.attr("src") }
                if (src.isNotBlank() && !src.startsWith("data:")) urls.add(src)
            }
        }

        Log.d("LegadoSkywalker", "loadLinks: ${urls.size} iframe URLs: $urls")
        if (urls.isEmpty()) {
            Log.e("LegadoSkywalker", "loadLinks: NO iframes in $data")
            return false
        }

        var anySuccess = false
        urls.distinct().forEach { url ->
            try {
                // First try loadExtractor (works for FileMoon, GDrive, etc)
                Log.d("LegadoSkywalker", "loadExtractor: $url")
                val success = loadExtractor(url, data, subtitleCallback) { link ->
                    Log.d("LegadoSkywalker", "Extracted: ${link.url.take(80)} quality=${link.quality}")
                    callback(link)
                    anySuccess = true
                }
                if (success) return@forEach
                Log.w("LegadoSkywalker", "loadExtractor false, trying app.get fallback")

                // Fallback: fetch via WebView and try to extract video URL
                val resp = app.get(url, referer = data, timeout = 60L)
                val html = resp.text
                Log.d("LegadoSkywalker", "fallback page ${html.length} chars")

                // Try sources: file, src, data-file, etc
                val patterns = listOf(
                    Regex("""(?:file|src|url)\s*:\s*["']([^"']+\.(?:m3u8|mp4)[^"']*)["']"""),
                    Regex("""<source[^>]+src=["']([^"']+\.(?:m3u8|mp4)[^"']*)["']"""),
                    Regex("""<video[^>]+src=["']([^"']+\.(?:m3u8|mp4)[^"']*)["']"""),
                    Regex("""https?://[^"'\s<>]+\.(?:m3u8|mp4)[^"'\s<>]*"""),
                    Regex("""data-file\s*=\s*["']([^"']+)["']"""),
                    Regex("""data-hash\s*=\s*["']([^"']+)["']"""),
                )
                var found = false
                for (p in patterns) {
                    val m = p.find(html)
                    if (m != null) {
                        val videoUrl = m.groupValues[1].ifBlank { m.value }.replace("\\/", "/")
                        Log.d("LegadoSkywalker", "  extracted: $videoUrl")
                        callback(newExtractorLink("Legado", "Video $videoUrl.takeLast(30)", videoUrl) {
                            this.referer = url
                        })
                        anySuccess = true; found = true; break
                    }
                }
                if (found) return@forEach

                // Look for nested iframe
                val iframeUrl = Regex("""<iframe[^>]+src=["']([^"']+)["']""").find(html)
                if (iframeUrl != null) {
                    val inner = iframeUrl.groupValues[1].let {
                        when {
                            it.startsWith("//") -> "https:$it"
                            it.startsWith("http") -> it
                            else -> "$mainUrl/$it"
                        }
                    }
                    Log.d("LegadoSkywalker", "  nested iframe: $inner")
                    val success2 = loadExtractor(inner, url, subtitleCallback, callback)
                    if (success2) { anySuccess = true }
                }
            } catch (e: Exception) {
                Log.e("LegadoSkywalker", "loadLinks error: ${e.message}")
            }
        }
        return anySuccess
    }
}
