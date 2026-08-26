package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import org.json.JSONObject

class PoseidonHDProvider : MainAPI() {
    override var mainUrl = "https://www.poseidonhd2.co"
    override var name = "PoseidonHD2"
    override var lang = "mx"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override val mainPage = mainPageOf(
        "/series" to "Series",
        "/peliculas/tendencias/semana" to "Tendencias",
        "/" to "Películas",
    )

    private fun fixUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        return when {
            url.startsWith("//") -> "https:$url"
            url.startsWith("/") -> mainUrl + url
            else -> url
        }
    }

    private fun parseNextData(html: String): JSONObject? {
        return try {
            val m = Regex("""<script id="__NEXT_DATA__"[^>]*>(.*?)</script>""", RegexOption.DOT_MATCHES_ALL).find(html)
                ?: return null
            JSONObject(m.groupValues[1])
        } catch (_: Exception) { null }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        if (page > 1) return newHomePageResponse(emptyList(), false)
        val url = if (request.data == "/") mainUrl else mainUrl + request.data
        val html = app.get(url).text
        val json = parseNextData(html)
        val pageProps = json?.optJSONObject("props")?.optJSONObject("pageProps")
        val items = mutableListOf<SearchResponse>()

        val keys = listOf("movies","otherMovies","relatedMovies","topMoviesDay","topMoviesWeek","series","topSeries")
        for (k in keys) {
            val arr = pageProps?.optJSONArray(k) ?: continue
            for (i in 0 until arr.length()) {
                val o = arr.optJSONObject(i) ?: continue
                val title = o.optJSONObject("titles")?.optString("name") ?: o.optString("name")
                val tid = o.optString("TMDbId").ifBlank { o.optString("id") }
                val poster = o.optJSONObject("images")?.optString("poster")
                val slugObj = o.optJSONObject("url")?.optString("slug") ?: continue
                // slug like movies/87513/motor-city -> /pelicula/87513/...
                val isMovie = slugObj.startsWith("movies/")
                val id = slugObj.substringAfter("/")
                val link = if (isMovie) "$mainUrl/pelicula/$id" else "$mainUrl/serie/$id"
                val type = if (isMovie) TvType.Movie else TvType.TvSeries
                if (title.isNullOrBlank()) continue
                items.add(newMovieSearchResponse(title, link, type) { this.posterUrl = poster })
                if (items.size > 40) break
            }
        }

        if (items.isEmpty()) {
            val doc = Jsoup.parse(html, mainUrl)
            doc.select("a[href*='/pelicula/'], a[href*='/serie/']").forEach { a ->
                val href = fixUrl(a.attr("href")) ?: return@forEach
                val title = a.attr("title").ifBlank { a.text() }.trim()
                if (title.length < 2) return@forEach
                val img = a.selectFirst("img")?.attr("src")?.let { fixUrl(it) }
                val type = if (href.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
                if (items.none { it.url == href }) {
                    items.add(newMovieSearchResponse(title, href, type) { this.posterUrl = img })
                }
            }
        }
        return newHomePageResponse(HomePageList(request.name, items.distinctBy { it.url }.take(30)), false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/search?q=${java.net.URLEncoder.encode(query, "UTF-8")}"
        val html = app.get(url).text
        val json = parseNextData(html)
        val props = json?.optJSONObject("props")?.optJSONObject("pageProps")
        val arr = props?.optJSONArray("movies") ?: return emptyList()
        val res = mutableListOf<SearchResponse>()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val title = o.optJSONObject("titles")?.optString("name") ?: continue
            val poster = o.optJSONObject("images")?.optString("poster")
            val slug = o.optJSONObject("url")?.optString("slug") ?: continue
            val isMovie = slug.startsWith("movies/")
            val id = slug.substringAfter("/")
            val link = if (isMovie) "$mainUrl/pelicula/$id" else "$mainUrl/serie/$id"
            val type = if (isMovie) TvType.Movie else TvType.TvSeries
            res.add(newMovieSearchResponse(title, link, type) { this.posterUrl = poster })
        }
        return res
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = app.get(url).text
        val json = parseNextData(html) ?: return null
        val props = json.optJSONObject("props")?.optJSONObject("pageProps") ?: return null

        val movieObj = props.optJSONObject("thisMovie")
        val serieObj = props.optJSONObject("serie") ?: props.optJSONObject("thisSerie")
        if (movieObj != null) {
            val title = movieObj.optJSONObject("titles")?.optString("name") ?: return null
            val poster = movieObj.optJSONObject("images")?.optString("poster")
            val back = movieObj.optJSONObject("images")?.optString("backdrop")
            val plot = movieObj.optString("overview")
            val year = movieObj.optString("releaseDate").substringBefore("-").toIntOrNull()
            val tags = movieObj.optJSONArray("genres")?.let { a -> (0 until a.length()).mapNotNull { a.optJSONObject(it)?.optString("name") } }
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.backgroundPosterUrl = back
                this.plot = plot
                this.year = year
                this.tags = tags
            }
        } else if (serieObj != null) {
            val title = serieObj.optJSONObject("titles")?.optString("name") ?: serieObj.optString("name") ?: return null
            val poster = serieObj.optJSONObject("images")?.optString("poster")
            val back = serieObj.optJSONObject("images")?.optString("backdrop")
            val plot = serieObj.optString("overview")
            val year = serieObj.optString("firstAirDate")?.substringBefore("-")?.toIntOrNull()
                ?: serieObj.optString("releaseDate")?.substringBefore("-")?.toIntOrNull()
            val seasonsArr = serieObj.optJSONArray("seasons") ?: props.optJSONArray("seasons") ?: props.optJSONArray("temporadas")
            val episodes = mutableListOf<Episode>()
            if (seasonsArr != null) {
                for (i in 0 until seasonsArr.length()) {
                    val s = seasonsArr.optJSONObject(i) ?: continue
                    val seasonNum = s.optInt("number", s.optInt("season_number", i))
                    if (seasonNum == 0) continue
                    val eps = s.optJSONArray("episodes") ?: s.optJSONArray("capitulos") ?: continue
                    for (j in 0 until eps.length()) {
                        val ep = eps.optJSONObject(j) ?: continue
                        val epNum = ep.optInt("number", ep.optInt("episode_number", j+1))
                        val epTitle = ep.optString("title").ifBlank { ep.optString("name").ifBlank { "Episodio $epNum" } }
                        val epThumb = ep.optString("image")

                        val epSlug = ep.optJSONObject("url")?.optString("slug")
                        val epUrl = if (!epSlug.isNullOrBlank()) "$mainUrl/${epSlug.replaceFirst("series/","serie/")}" else "$url/temporada/$seasonNum/episodio/$epNum"
                        episodes.add(newEpisode(epUrl) {
                            this.name = epTitle
                            this.season = seasonNum
                            this.episode = epNum
                            this.posterUrl = epThumb
                        })
                    }
                }
            }

            if (episodes.isEmpty()) {
                episodes.add(newEpisode(url) { this.name = title })
            }
            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.backgroundPosterUrl = back
                this.plot = plot
                this.year = year
            }
        }

        val doc = Jsoup.parse(html, mainUrl)
        val title = doc.selectFirst("h1")?.text() ?: return null
        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = doc.selectFirst("meta[property='og:image']")?.attr("content")
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val html = app.get(data).text
        val json = parseNextData(html)
        val props = json?.optJSONObject("props")?.optJSONObject("pageProps")
        
        val videoObj = props?.optJSONObject("thisMovie") ?: props?.optJSONObject("serie") ?: props?.optJSONObject("episode")
        val videos = videoObj?.optJSONObject("videos") ?: props?.optJSONObject("videos")
        if (videos != null) {
            for (key in listOf("latino","spanish","english")) {
                val arr = videos.optJSONArray(key) ?: continue
                for (i in 0 until arr.length()) {
                    val o = arr.optJSONObject(i) ?: continue
                    val link = o.optString("result")
                    if (link.contains("player.poseidonhd2.co")) {
                        // player.php?h=... -> seguir redirect o extraer iframe
                        try {
                            val pDoc = app.get(link).document
                            val iframe = pDoc.selectFirst("iframe[src]")?.attr("src")
                            val finalUrl = fixUrl(iframe ?: link) ?: link
                            loadExtractor(finalUrl, data, subtitleCallback, callback)
                        } catch (_: Exception) {
                            loadExtractor(link, data, subtitleCallback, callback)
                        }
                    } else if (link.isNotBlank()) {
                        loadExtractor(link, data, subtitleCallback, callback)
                    }
                }
            }
            return true
        }
        // fallback: buscar iframes directos
        val doc = Jsoup.parse(html, mainUrl)
        var found = false
        doc.select("iframe[src]").forEach {
            val src = fixUrl(it.attr("src")) ?: return@forEach
            loadExtractor(src, data, subtitleCallback, callback)
            found = true
        }
        return found
    }
}
