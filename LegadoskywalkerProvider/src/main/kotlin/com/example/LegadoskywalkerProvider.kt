package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.loadExtractor
import android.util.Log

data class SeriesDef(val name: String, val label: String, val type: TvType, var poster: String = "", var posterLoaded: Boolean = false)

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
        SeriesDef("The Clone Wars", "The Clone Wars", TvType.Cartoon),
        SeriesDef("Rebels", "Rebels", TvType.Cartoon),
        SeriesDef("The Bad Batch", "The Bad Batch", TvType.Cartoon),
        SeriesDef("Star Wars Visions", "Star Wars Visions", TvType.Cartoon),
        SeriesDef("Tales of the Jedi", "Tales of the Jedi", TvType.Cartoon),
        SeriesDef("Tales of the Empire", "Tales of the Empire", TvType.Cartoon),
        SeriesDef("Maul: Shadow Lord", "Maul : Shadow Lord", TvType.Cartoon),
        SeriesDef("Resistance", "Resistance", TvType.Cartoon),
        SeriesDef("Clone Wars (2003)", "Clone Wars (2003)", TvType.Cartoon),
        SeriesDef("Star Wars Droids", "Star Wars Droids", TvType.Cartoon),
        SeriesDef("Star Wars Ewoks", "Star Wars Ewoks", TvType.Cartoon),
        SeriesDef("Lego Star Wars", "Lego Star Wars", TvType.Cartoon),
    )

    private val liveaction = mutableListOf(
        SeriesDef("The Mandalorian", "The Mandalorian", TvType.TvSeries),
        SeriesDef("Ahsoka", "Ahsoka", TvType.TvSeries),
        SeriesDef("Andor", "Andor", TvType.TvSeries),
        SeriesDef("Obi-Wan Kenobi", "Obi-Wan Kenobi", TvType.TvSeries),
        SeriesDef("The Book of Boba Fett", "The Book of Boba Fett", TvType.TvSeries),
        SeriesDef("The Acolyte", "The Acolyte", TvType.TvSeries),
        SeriesDef("Skeleton Crew", "Skeleton Crew", TvType.TvSeries),
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

    private fun allSeries(): List<SeriesDef> = animadas + liveaction + peliculas + otros

    private suspend fun ensureSeriesPoster(s: SeriesDef) {
        if (s.posterLoaded) return
        try {
            val doc = app.get("$mainUrl/search/label/${java.net.URLEncoder.encode(s.label, "UTF-8")}", timeout = 30L).document
            val img = doc.select("div.post img").firstOrNull()?.attr("abs:src")
            if (img != null) {
                s.poster = img.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                Log.d("LegadoSkywalker", "Poster OK: ${s.name} -> ${s.poster}")
            } else {
                Log.w("LegadoSkywalker", "Poster empty for: ${s.name}")
            }
        } catch (e: Exception) {
            Log.e("LegadoSkywalker", "Poster error ${s.name}: ${e.message}")
        }
        s.posterLoaded = true
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d("LegadoSkywalker", "getMainPage: ${request.data} page=$page")
        val series = when (request.data) {
            "CAT:animadas" -> animadas
            "CAT:liveaction" -> liveaction
            "CAT:peliculas" -> peliculas
            "CAT:otros" -> otros
            else -> return newHomePageResponse(HomePageList(request.name, emptyList()), hasNext = false)
        }
        Log.d("LegadoSkywalker", "Category ${request.data}: ${series.size} series")
        series.forEach { ensureSeriesPoster(it) }
        val items = series.map { s ->
            newMovieSearchResponse(s.name, "SERIES:${s.label}", s.type) {
                this.posterUrl = s.poster.ifBlank { null }
                Log.d("LegadoSkywalker", "MainPage item: ${s.name} poster=${s.poster}")
            }
        }
        return newHomePageResponse(HomePageList(request.name, items, isHorizontalImages = true), hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("LegadoSkywalker", "search: $query")
        val ql = query.lowercase()
        val results = mutableListOf<SearchResponse>()

        allSeries().forEach { s ->
            if (s.name.lowercase().contains(ql) || s.label.lowercase().contains(ql)) {
                results.add(newMovieSearchResponse(s.name, "SERIES:${s.label}", s.type) {
                    this.posterUrl = s.poster.ifBlank { null }
                })
            }
        }

        if (results.isNotEmpty()) {
            Log.d("LegadoSkywalker", "search: found ${results.size} series by name match")
            return results
        }

        try {
            val encoded = java.net.URLEncoder.encode(query, "UTF-8")
            Log.d("LegadoSkywalker", "search: querying blog label=$encoded")
            val doc = app.get("$mainUrl/search/label/$encoded", timeout = 30L).document
            val posts = doc.select("div.post")
            Log.d("LegadoSkywalker", "search: blog returned ${posts.size} posts")
            posts.forEach { post ->
                try {
                    val title = post.select("h1, h2").text().trim()
                    val link = post.select("h1 a, h2 a").firstOrNull()?.attr("abs:href")
                        ?: post.select("a[href*='/20']").firstOrNull()?.attr("abs:href")
                    if (link == null) return@forEach
                    val poster = post.select("img").firstOrNull()?.attr("abs:src")
                        ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                    val type = if (title.contains("[T", ignoreCase = true)) TvType.TvSeries else TvType.Movie
                    results.add(newMovieSearchResponse(title, link, type) {
                        this.posterUrl = poster
                    })
                } catch (e: Exception) {
                    Log.e("LegadoSkywalker", "search post parse error: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("LegadoSkywalker", "search blog error: ${e.message}")
        }

        Log.d("LegadoSkywalker", "search total: ${results.size} results")
        return results
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("LegadoSkywalker", "load: $url")
        if (url.startsWith("SERIES:")) {
            val label = url.removePrefix("SERIES:")
            return loadSeries(label)
        }
        return loadEpisode(url)
    }

    private suspend fun loadSeries(label: String): LoadResponse {
        Log.d("LegadoSkywalker", "loadSeries: label=$label")
        val encoded = java.net.URLEncoder.encode(label, "UTF-8")
        val doc = app.get("$mainUrl/search/label/$encoded", timeout = 30L).document
        val posts = doc.select("div.post")
        Log.d("LegadoSkywalker", "loadSeries: ${posts.size} posts for label=$label")

        var seriesName = label
        allSeries().forEach { s ->
            if (s.label == label) seriesName = s.name
        }
        val episodes = mutableListOf<Episode>()
        posts.forEach { post ->
            try {
                val link = post.select("h1 a, h2 a").firstOrNull()?.attr("abs:href")
                    ?: post.select("a[href*='/20']").firstOrNull()?.attr("abs:href")
                    ?: return@forEach
                val rawTitle = post.select("h1, h2").text().trim()
                val poster = post.select("img").firstOrNull()?.attr("abs:src")
                    ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                val epMatch = Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(rawTitle)
                val epName = rawTitle.replace(Regex("""\s*\[T\d+[-\s]*C\d+\]\s*"""), "").trim()
                Log.d("LegadoSkywalker", "loadSeries ep: $rawTitle -> S${epMatch?.groupValues?.get(1)}E${epMatch?.groupValues?.get(2)} $link")
                episodes.add(newEpisode(link) {
                    this.name = epName.ifBlank { rawTitle }
                    this.season = epMatch?.groupValues?.get(1)?.toIntOrNull() ?: 1
                    this.episode = epMatch?.groupValues?.get(2)?.toIntOrNull() ?: (episodes.size + 1)
                    this.posterUrl = poster
                })
            } catch (e: Exception) {
                Log.e("LegadoSkywalker", "loadSeries post error: ${e.message}")
            }
        }
        if (episodes.isEmpty()) {
            Log.e("LegadoSkywalker", "loadSeries: NO episodes found for label=$label")
            return newTvSeriesLoadResponse(seriesName, "SERIES:$label", TvType.TvSeries, emptyList()) {
                this.plot = "No se encontraron episodios para $seriesName"
            }
        }
        episodes.sortBy { (it.season ?: 1) * 1000 + (it.episode ?: 1) }
        val poster = episodes.firstOrNull()?.posterUrl
        Log.d("LegadoSkywalker", "loadSeries: ${episodes.size} episodes sorted, first=${episodes.first().name}")
        return newTvSeriesLoadResponse(seriesName, "SERIES:$label", TvType.TvSeries, episodes) {
            this.posterUrl = poster
            this.plot = "Todos los capítulos de $seriesName"
        }
    }

    private suspend fun loadEpisode(url: String): LoadResponse {
        Log.d("LegadoSkywalker", "loadEpisode: $url")
        val doc = app.get(url, referer = mainUrl, timeout = 30L).document
        val title = doc.select("h1, h2").firstOrNull()?.text()?.trim()
            ?: doc.select("title").text().trim().substringBefore("|").trim()
        Log.d("LegadoSkywalker", "loadEpisode title: $title")
        val postBody = doc.select(".post-body.entry-content").firstOrNull()
            ?: doc.select(".post-body").firstOrNull()
        val poster = postBody?.select("img")?.firstOrNull()?.attr("abs:src")
            ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
        val plot = postBody?.text()?.trim()?.take(500)
        val tags = doc.select("a[rel=tag]").eachText()
        val year = Regex("""\b(19\d{2}|20\d{2})\b""").find(url)?.value?.toIntOrNull()
        val epMatch = Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(title)

        if (epMatch != null) {
            val season = epMatch.groupValues[1].toIntOrNull() ?: 1
            val episode = epMatch.groupValues[2].toIntOrNull() ?: 1
            val epName = title.replace(Regex("""\s*\[T\d+[-\s]*C\d+\]\s*"""), "").trim()
            Log.d("LegadoSkywalker", "loadEpisode: S${season}E${episode} name=$epName")
            val episodeData = newEpisode(url) {
                this.name = epName.ifBlank { title }; this.season = season; this.episode = episode; this.posterUrl = poster
            }
            val seriesName = tags.firstOrNull { it.contains("Temporada", ignoreCase = true) }
                ?.substringBefore("- Temporada")?.trim() ?: tags.firstOrNull { it != "post" } ?: "Star Wars"
            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, listOf(episodeData)) {
                this.posterUrl = poster; this.plot = plot; this.year = year; this.tags = tags.filter { it != "post" }
            }
        }
        Log.d("LegadoSkywalker", "loadEpisode: treating as Movie")
        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster; this.plot = plot; this.year = year; this.tags = tags.filter { it != "post" }
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
            val src = iframe.attr("data-src").ifBlank { iframe.attr("src") }
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

        Log.d("LegadoSkywalker", "loadLinks: found ${urls.size} iframe URLs: $urls")
        if (urls.isEmpty()) {
            Log.e("LegadoSkywalker", "loadLinks: NO iframes found in $data")
            return false
        }

        var anySuccess = false
        urls.distinct().forEach { url ->
            try {
                Log.d("LegadoSkywalker", "loadLinks: calling loadExtractor for $url")
                val success = loadExtractor(url, data, subtitleCallback) { link ->
                    Log.d("LegadoSkywalker", "loadExtractor OK: ${link.url} quality=${link.quality}")
                    callback(link)
                    anySuccess = true
                }
                if (!success) {
                    Log.w("LegadoSkywalker", "loadExtractor returned false for $url")
                }
            } catch (e: Exception) {
                Log.e("LegadoSkywalker", "loadExtractor error for $url: ${e.message}")
            }
        }
        return anySuccess
    }
}
