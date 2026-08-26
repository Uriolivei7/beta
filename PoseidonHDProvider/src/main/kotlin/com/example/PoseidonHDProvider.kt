package com.example

import android.util.Log
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
        } catch (e: Exception) { Log.w("PoseidonHD", "parseNextData: ${e.message}"); null }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        if (page > 1) return newHomePageResponse(emptyList(), false)
        val url = if (request.data == "/") mainUrl else mainUrl + request.data
        Log.d("PoseidonHD", "getMainPage: url=$url page=$page")
        val html = try { app.get(url).text } catch (e: Exception) { Log.e("PoseidonHD", "getMainPage: error $url", e); return newHomePageResponse(emptyList(), false) }
        Log.d("PoseidonHD", "getMainPage: html=${html.length}")
        val json = parseNextData(html)
        if (json == null) Log.w("PoseidonHD", "getMainPage: __NEXT_DATA__ null url=$url")
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
            Log.w("PoseidonHD", "getMainPage: 0 items url=$url keys=${pageProps?.keys()?.let { it.asSequence().joinToString() } ?: "null"}")
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
        Log.d("PoseidonHD", "getMainPage: ${request.data} -> ${items.size} items")
        return newHomePageResponse(HomePageList(request.name, items.distinctBy { it.url }.take(30)), false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("PoseidonHD", "search: query=$query")
        val url = "$mainUrl/search?q=${java.net.URLEncoder.encode(query, "UTF-8")}"
        val html = try { app.get(url).text } catch (e: Exception) { Log.e("PoseidonHD", "search: error $url", e); return emptyList() }
        val json = parseNextData(html)
        if (json == null) Log.w("PoseidonHD", "search: parseNextData null query=$query")
        val props = json?.optJSONObject("props")?.optJSONObject("pageProps")
        val arr = props?.optJSONArray("movies")
        if (arr == null) {
            Log.w("PoseidonHD", "search: movies null query=$query")
            return emptyList()
        }
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
        Log.d("PoseidonHD", "search: $query -> ${res.size} resultados")
        return res
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("PoseidonHD", "load: url=$url")
        val html = try { app.get(url).text } catch (e: Exception) { Log.e("PoseidonHD", "load: error $url", e); return null }
        Log.d("PoseidonHD", "load: html=${html.length} hasNextData=${html.contains("__NEXT_DATA__")}")
        val json = parseNextData(html) ?: run { Log.w("PoseidonHD", "load: parseNextData null url=$url"); return null }
        val props = json.optJSONObject("props")?.optJSONObject("pageProps") ?: run { Log.w("PoseidonHD", "load: pageProps null url=$url"); return null }

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
            Log.d("PoseidonHD", "load: serie=$title seasons=${serieObj.optJSONArray("seasons")?.length() ?: 0}")
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
                        val epUrl = if (!epSlug.isNullOrBlank()) {
                            val parts = epSlug.split("/")
                            if (parts.size >= 7) "$mainUrl/serie/${parts[1]}/${parts[2]}/temporada/${parts[4]}/episodio/${parts[6]}"
                            else "$mainUrl/${epSlug.replaceFirst("series/","serie/")}"
                        } else "$url/temporada/$seasonNum/episodio/$epNum"
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
                Log.w("PoseidonHD", "load: 0 episodios url=$url")
                episodes.add(newEpisode(url) { this.name = title })
            }
            Log.d("PoseidonHD", "load: serie $title -> ${episodes.size} episodios")
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
        Log.d("PoseidonHD", "loadLinks: data=$data")
        val html = try { app.get(data).text } catch (e: Exception) { Log.e("PoseidonHD", "loadLinks: error $data", e); return false }
        Log.d("PoseidonHD", "loadLinks: html=${html.length} hasNextData=${html.contains("__NEXT_DATA__")}")
        val json = parseNextData(html)
        if (json == null) Log.w("PoseidonHD", "loadLinks: parseNextData null")
        val props = json?.optJSONObject("props")?.optJSONObject("pageProps")

        val videoObj = props?.optJSONObject("thisMovie") ?: props?.optJSONObject("episode") ?: props?.optJSONObject("serie")
        val videos = videoObj?.optJSONObject("videos") ?: props?.optJSONObject("videos")
        Log.d("PoseidonHD", "loadLinks: videoObj=${videoObj != null} videos=${videos?.length() ?: "null"} keys=${videos?.keys()?.asSequence()?.joinToString() ?: "null"}")
        if (videos != null) {
            for (key in listOf("latino","spanish","english")) {
                val arr = videos.optJSONArray(key) ?: continue
                Log.d("PoseidonHD", "loadLinks: $key -> ${arr.length()} links")
                for (i in 0 until arr.length()) {
                    val o = arr.optJSONObject(i) ?: continue
                    val link = o.optString("result")
                    if (link.contains("player.poseidonhd2.co")) {

                        try {
                            val pDoc = app.get(link).document
                            val iframe = pDoc.selectFirst("iframe[src]")?.attr("src")
                            val finalUrl = fixUrl(iframe ?: link) ?: link
                            loadExtractor(finalUrl, data, subtitleCallback, callback)
                        } catch (e: Exception) {
                            Log.w("PoseidonHD", "loadLinks: iframe error $link: ${e.message}")
                            loadExtractor(link, data, subtitleCallback, callback)
                        }
                    } else if (link.isNotBlank()) {
                        loadExtractor(link, data, subtitleCallback, callback)
                    }
                }
            }
            return true
        }
        Log.w("PoseidonHD", "loadLinks: videos null, buscando iframes data=$data")
        val doc = Jsoup.parse(html, mainUrl)
        var found = false
        doc.select("iframe[src]").forEach {
            val src = fixUrl(it.attr("src")) ?: return@forEach
            Log.d("PoseidonHD", "loadLinks: iframe $src")
            loadExtractor(src, data, subtitleCallback, callback)
            found = true
        }
        if (!found) Log.w("PoseidonHD", "loadLinks: 0 iframes html=${html.length}")
        Log.d("PoseidonHD", "loadLinks: found=$found")
        return found
    }
}
