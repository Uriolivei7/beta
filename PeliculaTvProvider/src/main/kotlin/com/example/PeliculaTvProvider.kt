package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class PeliculaTvProvider : MainAPI() {

    override var mainUrl = "https://www.themoviedb.org"
    override var name = "PeliCulónTV"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override var lang = "mx"
    override val hasMainPage = true

    private val tmdbApiKey = "8267afabedf52d963f4d29a95254e784"
    private val apiBase = "https://api.themoviedb.org/3"
    private val imgBase = "https://image.tmdb.org/t/p/w500"
    private val headers = mapOf("Accept" to "application/json")

    override val mainPage = mainPageOf(
        "/trending/tv/week" to "Series Tendencias",
        "/tv/popular" to "Series Populares",
        "/tv/top_rated" to "Series Mejor Puntuadas",
        "/tv/on_the_air" to "Series En Emisión",
        "/trending/movie/week" to "Películas Tendencias",
        "/movie/now_playing" to "Películas Estrenos",
        "/movie/popular" to "Películas Populares",
        "/movie/top_rated" to "Películas Mejor Puntuadas",
    )

    private fun fixPoster(path: String?): String {
        if (path.isNullOrBlank()) return ""
        return if (path.startsWith("http")) path else "$imgBase$path"
    }

    private suspend fun tmdbRequest(endpoint: String): String? {
        return try {
            val sep = if (endpoint.contains("?")) "&" else "?"
            val url = "$apiBase$endpoint${sep}api_key=$tmdbApiKey&language=es-MX"
            val resp = app.get(url, headers = headers)
            if (resp.text.isNullOrBlank() || resp.text.startsWith("<!DOCTYPE")) {
                Log.w("PeliCulonTV", "tmdbRequest: response no JSON (${resp.text.take(80)}) para $endpoint")
                return null
            }
            resp.text
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "tmdbRequest error ${e.message} para $endpoint")
            null
        }
    }

    private fun parseObj(e: JsonElement?): JsonObject? {
        if (e != null && e !is JsonObject) Log.w("PeliCulonTV", "parseObj esperaba Object, era ${e::class.simpleName}: ${e.toString().take(100)}")
        return e as? JsonObject
    }

    private fun parseArr(e: JsonElement?): JsonArray? {
        if (e != null && e !is JsonArray) Log.w("PeliCulonTV", "parseArr esperaba Array, era ${e::class.simpleName}: ${e.toString().take(100)}")
        return e as? JsonArray
    }

    private fun parseSearchResults(json: String?, type: TvType): List<SearchResponse> {
        val results = mutableListOf<SearchResponse>()
        if (json.isNullOrBlank()) return results
        val root = parseObj(JsonParser.parseString(json)) ?: return results
        val arr = parseArr(root.get("results")) ?: return results
        for (item in arr) {
            val obj = parseObj(item) ?: continue
            val id = obj.get("id")?.asInt ?: continue
            val title = obj.get("title")?.asString ?: obj.get("name")?.asString ?: continue
            val poster = fixPoster(obj.get("poster_path")?.asString)
            val year = obj.get("release_date")?.asString?.take(4)?.toIntOrNull()
                ?: obj.get("first_air_date")?.asString?.take(4)?.toIntOrNull()
            val url = "$mainUrl/${if (type == TvType.Movie) "movie" else "tv"}/$id"
            results.add(newMovieSearchResponse(title, url, type) {
                this.posterUrl = poster
                if (year != null) this.year = year
            })
        }
        return results
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        try {
            val json = tmdbRequest(if (page > 1) "${request.data}?page=$page" else request.data) ?: return null
            val type = if (request.data.contains("movie", ignoreCase = true)) TvType.Movie else TvType.TvSeries
            val items = parseSearchResults(json, type)
            val root = parseObj(JsonParser.parseString(json)) ?: return null
            val totalPages = root.get("total_pages")?.asInt ?: 1
            return newHomePageResponse(listOf(HomePageList(request.name, items.take(50))), page < totalPages)
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "getMainPage error", e)
            return null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        if (query.isBlank()) return emptyList()
        val results = mutableListOf<SearchResponse>()
        try {
            val q = query.replace(" ", "%20")
            parseSearchResults(tmdbRequest("/search/movie?query=$q"), TvType.Movie).let { results.addAll(it) }
            parseSearchResults(tmdbRequest("/search/tv?query=$q"), TvType.TvSeries).let { results.addAll(it) }
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "search error query='$query'", e)
        }
        return results.distinctBy { it.url }.take(50)
    }

    override suspend fun load(url: String): LoadResponse? {
        try {
            val movieId = Regex("""/movie/(\d+)""").find(url)?.groupValues?.get(1)
            if (movieId != null) return loadMovie(movieId, url)
            val tvId = Regex("""/tv/(\d+)""").find(url)?.groupValues?.get(1)
            if (tvId != null) return loadTvSeries(tvId, url)
            Log.w("PeliCulonTV", "load: URL no reconocida: $url")
            return null
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "load error (${url.take(60)})", e)
            return null
        }
    }

    private suspend fun loadMovie(id: String, pageUrl: String): LoadResponse? {
        val json = tmdbRequest("/movie/$id?append_to_response=credits,videos,recommendations") ?: return null
        val root = parseObj(JsonParser.parseString(json)) ?: return null
        val title = root.get("title")?.asString ?: run {
            Log.w("PeliCulonTV", "loadMovie $id: sin title, keys=${root.keySet()}")
            return null
        }
        val poster = fixPoster(root.get("poster_path")?.asString)
        val bg = fixPoster(root.get("backdrop_path")?.asString)
        val year = root.get("release_date")?.asString?.take(4)?.toIntOrNull()
        val voteAvg = root.get("vote_average")?.asFloat
        val genres = parseArr(root.get("genres"))?.mapNotNull { parseObj(it)?.get("name")?.asString } ?: emptyList()

        return newMovieLoadResponse(title, pageUrl, TvType.Movie, "movie:$id") {
            this.posterUrl = poster
            this.backgroundPosterUrl = bg
            this.plot = root.get("overview")?.asString ?: ""
            if (year != null) this.year = year
            if (voteAvg != null) this.score = Score.from10((voteAvg * 10).toInt())
            this.tags = genres
            this.duration = root.get("runtime")?.asInt
            this.recommendations = parseRecommendations(parseArr(root.get("recommendations")))
        }
    }

    private suspend fun loadTvSeries(id: String, pageUrl: String): LoadResponse? {
        val json = tmdbRequest("/tv/$id?append_to_response=credits,videos,recommendations") ?: return null
        val root = parseObj(JsonParser.parseString(json)) ?: return null
        val title = root.get("name")?.asString ?: run {
            Log.w("PeliCulonTV", "loadTvSeries $id: sin name, keys=${root.keySet()}")
            return null
        }
        val poster = fixPoster(root.get("poster_path")?.asString)
        val bg = fixPoster(root.get("backdrop_path")?.asString)
        val year = root.get("first_air_date")?.asString?.take(4)?.toIntOrNull()
        val voteAvg = root.get("vote_average")?.asFloat
        val genres = parseArr(root.get("genres"))?.mapNotNull { parseObj(it)?.get("name")?.asString } ?: emptyList()
        val seasons = parseArr(root.get("seasons"))?.mapNotNull {
            parseObj(it)?.get("season_number")?.asInt?.takeIf { s -> s > 0 }
        }?.sorted() ?: listOf(1)
        if (seasons.isEmpty()) return null
        Log.i("PeliCulonTV", "loadTvSeries $id: $title, ${seasons.size} temporadas: $seasons")

        val episodes = mutableListOf<Episode>()
        for (seasonNum in seasons) {
            val seasonJson = tmdbRequest("/tv/$id/season/$seasonNum") ?: continue
            val seasonRoot = parseObj(JsonParser.parseString(seasonJson))
            if (seasonRoot == null) {
                Log.w("PeliCulonTV", "S${seasonNum} de $id: root no es Object, json=${seasonJson.take(100)}")
                continue
            }
            val epList = parseArr(seasonRoot.get("episodes"))
            if (epList == null) {
                Log.w("PeliCulonTV", "S${seasonNum} de $id: 'episodes' no es Array, keys=${seasonRoot.keySet()}")
                continue
            }
            for (ep in epList) {
                val epObj = parseObj(ep) ?: continue
                val epNum = epObj.get("episode_number")?.asInt ?: continue
                episodes.add(newEpisode("tv:$id:$seasonNum:$epNum") {
                    this.name = epObj.get("name")?.asString ?: "Episodio $epNum"
                    this.episode = epNum
                    this.season = seasonNum
                    this.description = epObj.get("overview")?.asString ?: ""
                    this.posterUrl = fixPoster(epObj.get("still_path")?.asString)
                })
            }
        }
        episodes.sortWith(compareBy({ it.season ?: 1 }, { it.episode ?: 1 }))
        Log.i("PeliCulonTV", "loadTvSeries $id: ${episodes.size} episodios totales (pre-return)")
        if (episodes.isEmpty()) {
            Log.w("PeliCulonTV", "loadTvSeries $id: 0 episodios, no se crea LoadResponse")
            return null
        }

        return newTvSeriesLoadResponse(title, pageUrl, TvType.TvSeries, episodes) {
            this.posterUrl = poster
            this.backgroundPosterUrl = bg
            this.plot = root.get("overview")?.asString ?: ""
            if (year != null) this.year = year
            if (voteAvg != null) this.score = Score.from10((voteAvg * 10).toInt())
            this.tags = genres
            this.recommendations = parseRecommendations(parseArr(root.get("recommendations")))
        }
    }

    private fun parseRecommendations(arr: JsonArray?): List<SearchResponse>? {
        if (arr == null) return null
        val list = mutableListOf<SearchResponse>()
        for (item in arr) {
            try {
                val obj = parseObj(item) ?: continue
                val id = obj.get("id")?.asInt ?: continue
                val title = obj.get("title")?.asString ?: obj.get("name")?.asString ?: continue
                val poster = fixPoster(obj.get("poster_path")?.asString)
                val isTv = obj.get("media_type")?.asString == "tv"
                val type = if (isTv) TvType.TvSeries else TvType.Movie
                list.add(newMovieSearchResponse(title, "$mainUrl/${if (isTv) "tv" else "movie"}/$id", type) {
                    this.posterUrl = poster
                })
            } catch (_: Exception) {}
        }
        return list.ifEmpty { null }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        try {
            val movieMatch = Regex("""movie:(\d+)""").find(data)
            val tvMatch = Regex("""tv:(\d+):(\d+):(\d+)""").find(data)

            if (movieMatch != null) {
                val tmdbId = movieMatch.groupValues[1]
                val embeds = listOf(
                    "https://embed.su/embed/movie/$tmdbId",
                    "https://vidsrc.pro/embed/movie/$tmdbId",
                    "https://vidlink.pro/embed/movie/$tmdbId",
                    "https://moviesapi.club/movie/$tmdbId",
                    "https://player.autoembed.cc/embed/movie/$tmdbId",
                    "https://vidsrc.me/embed/movie/$tmdbId",
                )
                for (url in embeds) {
                    try {
                        loadExtractor(url, mainUrl, subtitleCallback, callback)
                    } catch (_: Exception) {}
                }
                return true
            }

            if (tvMatch != null) {
                val tmdbId = tvMatch.groupValues[1]
                val season = tvMatch.groupValues[2]
                val episode = tvMatch.groupValues[3]
                val embeds = listOf(
                    "https://embed.su/embed/tv/$tmdbId/$season/$episode",
                    "https://vidsrc.pro/embed/tv/$tmdbId/$season/$episode",
                    "https://multiembed.mov/?video_id=$tmdbId&tmdb=1&s=$season&e=$episode",
                    "https://unlimplay.com/embed/tv/$tmdbId/$season/$episode",
                    "https://efilm.online/embed/tv/$tmdbId/$season/$episode",
                    "https://2embed.cc/embedtv2/$tmdbId&s=$season&e=$episode",
                )
                for (url in embeds) {
                    try {
                        loadExtractor(url, mainUrl, subtitleCallback, callback)
                    } catch (_: Exception) {}
                }
                return true
            }

            return false
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "loadLinks error: ${e.message}")
            return false
        }
    }
}
