package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.loadExtractor
import android.util.Log
import kotlinx.coroutines.*

data class SeriesDef(
    val name: String,
    val slug: String,
    val type: TvType,
    var poster: String = "",
    var posterLoaded: Boolean = false
)

class LegadoskywalkerProvider : MainAPI() {
    override var mainUrl = "https://serieslegadoskywalker.blogspot.com"
    override var name = "Legado Skywalker"
    override var lang = "es"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = false
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Movie, TvType.Cartoon)

    override val mainPage = mainPageOf(
        "CAT:animadas" to "Series Animadas",
        "CAT:liveaction" to "Series Live Action",
        "CAT:peliculas" to "Películas",
        "CAT:otros" to "Otros",
    )

    private val animadas = mutableListOf(
        SeriesDef("The Clone Wars", "the-clone-wars", TvType.Cartoon),
        SeriesDef("Rebels", "rebels", TvType.Cartoon),
        SeriesDef("The Bad Batch", "the-bad-batch", TvType.Cartoon),
        SeriesDef("Star Wars Visions", "star-wars-visions", TvType.Cartoon),
        SeriesDef("Tales of the Jedi", "tales-of-the-jedi", TvType.Cartoon),
        SeriesDef("Tales of the Empire", "tales-of-the-empire", TvType.Cartoon),
        SeriesDef("Clone Wars (2003)", "clone-wars", TvType.Cartoon),
        SeriesDef("Resistance", "resistance", TvType.Cartoon),
        SeriesDef("Star Wars Droids", "star-wars-droids", TvType.Cartoon),
        SeriesDef("Star Wars Ewoks", "star-wars-ewoks", TvType.Cartoon),
        SeriesDef("Lego Star Wars", "lego-star-wars", TvType.Cartoon),
        SeriesDef("Maul: Shadow Lord", "maul-shadow-lord", TvType.Cartoon),
    )

    private val liveaction = mutableListOf(
        SeriesDef("The Mandalorian", "the-mandalorian", TvType.TvSeries),
        SeriesDef("Ahsoka", "ahsoka", TvType.TvSeries),
        SeriesDef("Andor", "andor", TvType.TvSeries),
        SeriesDef("Obi-Wan Kenobi", "obi-wan-kenobi", TvType.TvSeries),
        SeriesDef("The Book of Boba Fett", "the-book-of-boba-fett", TvType.TvSeries),
        SeriesDef("The Acolyte", "the-acolyte", TvType.TvSeries),
        SeriesDef("Skeleton Crew", "skeleton-crew", TvType.TvSeries),
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
        val url = if (isSeriesPage) "$mainUrl/p/$slug.html" else "$mainUrl/search/label/${java.net.URLEncoder.encode(slug, "UTF-8")}"
        return try {
            val doc = app.get(url, timeout = 30L).document
            doc.select("div.post img, .post-body img").firstOrNull()?.attr("abs:src")
                ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
        } catch (e: Exception) { null }
    }

    private suspend fun ensureSeriesPoster(s: SeriesDef) {
        if (s.posterLoaded) return
        s.poster = loadPoster(s.slug, isSeriesPage = true) ?: loadPoster(s.slug, isSeriesPage = false) ?: ""
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
            newMovieSearchResponse(s.name, "SERIES:${s.slug}", s.type) {
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
        val seriesUrl = "$mainUrl/p/$slug.html"
        val doc = try {
            app.get(seriesUrl, referer = mainUrl, timeout = 30L).document
        } catch (e: Exception) {
            Log.e("LegadoSkywalker", "loadSeries: cannot fetch $seriesUrl: ${e.message}")
            return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, emptyList()) {
                this.plot = "Error al cargar la serie"
            }
        }

        val body = doc.select(".post-body.entry-content, .post-body").firstOrNull()
        if (body == null) {
            Log.e("LegadoSkywalker", "loadSeries: no post-body found")
            return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, emptyList())
        }

        val seasonAnchors = body.select("a[href*='temporada']")
        Log.d("LegadoSkywalker", "loadSeries: ${seasonAnchors.size} season links found")

        if (seasonAnchors.isEmpty()) {
            Log.w("LegadoSkywalker", "loadSeries: no season links, trying all <a> in body")
            body.select("a[href]").forEach { a ->
                val h = a.attr("abs:href")
                if (h.contains("/p/") && h.contains(mainUrl)) {
                    Log.d("LegadoSkywalker", "  link: $h")
                }
            }
        }

        val seasonUrls = seasonAnchors.mapNotNull { a ->
            val h = a.attr("abs:href")
            if (h.isBlank()) a.attr("href").let { if (it.startsWith("/")) "$mainUrl$it" else if (it.startsWith("http")) it else null }
            else h
        }.distinct()

        Log.d("LegadoSkywalker", "loadSeries: ${seasonUrls.size} unique season URLs: $seasonUrls")
        if (seasonUrls.isEmpty()) {
            return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, emptyList()) {
                this.plot = "No se encontraron temporadas"
            }
        }

        val allEpisodes = mutableListOf<Episode>()
        seasonUrls.forEach { seasonUrl ->
            try {
                val seasonNum = Regex("""temporada[-\s]*(\d+)""").find(seasonUrl)?.groupValues?.get(1)?.toIntOrNull()
                val seasonDoc = app.get(seasonUrl, referer = mainUrl, timeout = 30L).document
                val seasonBody = seasonDoc.select(".post-body.entry-content, .post-body").firstOrNull()
                if (seasonBody == null) {
                    Log.w("LegadoSkywalker", "  season $seasonNum: no post-body")
                    return@forEach
                }

                // Find episode links within post-body
                val epLinks = seasonBody.select("a[href*='blogspot.com/20'], a[href*='blogspot.com/20'], a[href*='/20']")
                Log.d("LegadoSkywalker", "  season $seasonNum: ${epLinks.size} episode links")

                if (epLinks.isEmpty()) {
                    // Try any <a> inside numbered divs
                    seasonBody.select("div a[href]").forEach { a ->
                        val h = a.attr("abs:href")
                        if (h.contains("/20") && !h.contains("temporada") && !h.contains("label")) {
                            val epNum = a.parents().select("div, span").text().trim()
                            val title = a.text().trim()
                            Log.d("LegadoSkywalker", "    ep: $title -> $h")
                            allEpisodes.add(newEpisode(h) {
                                this.name = title.ifBlank { "Episodio ${allEpisodes.size + 1}" }
                                this.season = seasonNum ?: 1
                                this.episode = allEpisodes.size + 1
                            })
                        }
                    }
                    return@forEach
                }

                epLinks.forEachIndexed { idx, a ->
                    val href = a.attr("abs:href")
                    val title = a.text().trim()
                    if (href.contains("temporada") || href.contains("label") || title.isBlank()) return@forEachIndexed
                    // Try to get ep number from URL
                    val epFromUrl = Regex("""[cC](\d+)""").find(href)?.groupValues?.get(1)?.toIntOrNull()
                    allEpisodes.add(newEpisode(href) {
                        this.name = title
                        this.season = seasonNum ?: 1
                        this.episode = epFromUrl ?: (idx + 1)
                    })
                }
            } catch (e: Exception) {
                Log.e("LegadoSkywalker", "  season error: ${e.message}")
            }
        }

        if (allEpisodes.isEmpty()) {
            Log.e("LegadoSkywalker", "loadSeries: NO episodes found for $slug")
            return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, emptyList())
        }

        allEpisodes.sortBy { (it.season ?: 1) * 1000 + (it.episode ?: 1) }
        Log.d("LegadoSkywalker", "loadSeries: ${allEpisodes.size} total episodes")
        return newTvSeriesLoadResponse(seriesName, "SERIES:$slug", TvType.TvSeries, allEpisodes) {
            this.plot = "Todos los capítulos de $seriesName"
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

    private suspend fun loadMovieByLabel(label: String): LoadResponse {
        Log.d("LegadoSkywalker", "loadMovieByLabel: $label")
        val encoded = java.net.URLEncoder.encode(label, "UTF-8")
        val doc = app.get("$mainUrl/search/label/$encoded", timeout = 30L).document
        val posts = doc.select("div.post")
        val episodes = mutableListOf<Episode>()
        posts.forEachIndexed { idx, post ->
            try {
                val link = post.select("h1 a, h2 a").firstOrNull()?.attr("abs:href")
                    ?: post.select("a[href*='/20']").firstOrNull()?.attr("abs:href") ?: return@forEachIndexed
                val title = post.select("h1, h2").text().trim()
                val poster = post.select("img").firstOrNull()?.attr("abs:src")
                    ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                episodes.add(newEpisode(link) {
                    this.name = title; this.season = 1; this.episode = idx + 1; this.posterUrl = poster
                })
            } catch (e: Exception) {}
        }
        val name = allMovies().firstOrNull { it.slug == label }?.name ?: label
        val poster = episodes.firstOrNull()?.posterUrl
        if (episodes.size == 1) {
            return newMovieLoadResponse(name, episodes[0].data, TvType.Movie, episodes[0].data) {
                this.posterUrl = poster
            }
        }
        return newTvSeriesLoadResponse(name, "MOVIE:$label", TvType.Movie, episodes) {
            this.posterUrl = poster; this.plot = name
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
                Log.d("LegadoSkywalker", "loadExtractor: $url")
                val success = loadExtractor(url, data, subtitleCallback) { link ->
                    Log.d("LegadoSkywalker", "Extracted: ${link.url.take(80)} quality=${link.quality}")
                    callback(link)
                    anySuccess = true
                }
                if (!success) Log.w("LegadoSkywalker", "loadExtractor false for $url")
            } catch (e: Exception) {
                Log.e("LegadoSkywalker", "loadExtractor error: ${e.message}")
            }
        }
        return anySuccess
    }
}
