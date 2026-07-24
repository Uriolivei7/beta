package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.google.gson.JsonParser

class TMDBProvider : MainAPI() {
    override var mainUrl = "https://www.themoviedb.org"
    override var name = "TMDB"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override var lang = "mx"
    override val hasMainPage = true

    // Registrate en https://www.themoviedb.org/settings/api (gratis, 2 min)
    private val tmdbApiKey = "TU_API_KEY_DE_TMDB"

    // URL de tu TMDB Embed API autohosteada (Docker en tu VPS/PC)
    private val embedApiBase = "http://TU_IP:8787"

    private val apiBase = "https://api.themoviedb.org/3"
    private val imgBase = "https://image.tmdb.org/t/p/w500"
    private val headers = mapOf("Accept" to "application/json")

    override val mainPage = mainPageOf(
        "/trending/movie/week" to "Películas Tendencias",
        "/movie/now_playing" to "Películas Estrenos",
        "/movie/popular" to "Películas Populares",
        "/movie/top_rated" to "Películas Mejor Puntuadas",
        "/trending/tv/week" to "Series Tendencias",
        "/tv/popular" to "Series Populares",
        "/tv/top_rated" to "Series Mejor Puntuadas",
        "/tv/on_the_air" to "Series En Emisión",
    )

    private fun fixPoster(path: String?): String {
        if (path.isNullOrBlank()) return ""
        return if (path.startsWith("http")) path else "$imgBase$path"
    }

    private suspend fun tmdbRequest(endpoint: String): String {
        val sep = if (endpoint.contains("?")) "&" else "?"
        val url = "$apiBase$endpoint${sep}api_key=$tmdbApiKey&language=es-MX"
        Log.i("TMDB", "GET $url")
        return app.get(url, headers = headers).text
    }

    private fun parseSearchResults(json: String, type: TvType): List<SearchResponse> {
        val results = mutableListOf<SearchResponse>()
        try {
            val arr = JsonParser.parseString(json).asJsonObject.getAsJsonArray("results") ?: return results
            for (item in arr) {
                val obj = item.asJsonObject
                val id = obj.get("id").asInt
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
        } catch (e: Exception) {
            Log.e("TMDB", "parseSearchResults: ${e.message}")
        }
        return results
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        try {
            val endpoint = if (page > 1) "${request.data}?page=$page" else request.data
            val json = tmdbRequest(endpoint)
            val type = if (request.data.contains("movie", ignoreCase = true)) TvType.Movie else TvType.TvSeries
            val items = parseSearchResults(json, type)
            val totalPages = JsonParser.parseString(json).asJsonObject.get("total_pages")?.asInt ?: 1
            return newHomePageResponse(listOf(HomePageList(request.name, items.take(50))), page < totalPages)
        } catch (e: Exception) {
            Log.e("TMDB", "getMainPage error: ${e.message}")
            return null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        if (query.isBlank()) return emptyList()
        val results = mutableListOf<SearchResponse>()
        try {
            val q = query.replace(" ", "%20")
            results.addAll(parseSearchResults(tmdbRequest("/search/movie?query=$q"), TvType.Movie))
            results.addAll(parseSearchResults(tmdbRequest("/search/tv?query=$q"), TvType.TvSeries))
        } catch (e: Exception) {
            Log.e("TMDB", "search error: ${e.message}")
        }
        return results.distinctBy { it.url }.take(50)
    }

    override suspend fun load(url: String): LoadResponse? {
        try {
            val pageUrl = url
            val movieId = Regex("""/movie/(\d+)""").find(url)?.groupValues?.get(1)
            if (movieId != null) return loadMovie(movieId, pageUrl)
            val tvId = Regex("""/tv/(\d+)""").find(url)?.groupValues?.get(1)
            if (tvId != null) return loadTvSeries(tvId, pageUrl)
            Log.w("TMDB", "load: URL no reconocida: $url")
            return null
        } catch (e: Exception) {
            Log.e("TMDB", "load error: ${e.message}")
            return null
        }
    }

    private suspend fun loadMovie(id: String, pageUrl: String): LoadResponse? {
        val json = tmdbRequest("/movie/$id?append_to_response=credits,videos,recommendations")
        val root = JsonParser.parseString(json).asJsonObject
        val title = root.get("title").asString
        val poster = fixPoster(root.get("poster_path")?.asString)
        val bg = fixPoster(root.get("backdrop_path")?.asString)
        val year = root.get("release_date")?.asString?.take(4)?.toIntOrNull()
        val voteAvg = root.get("vote_average")?.asFloat
        val genres = root.getAsJsonArray("genres")?.mapNotNull { it.asJsonObject?.get("name")?.asString } ?: emptyList()

        return newMovieLoadResponse(title, pageUrl, TvType.Movie, "movie:$id") {
            this.posterUrl = poster
            this.backgroundPosterUrl = bg
            this.plot = root.get("overview")?.asString ?: ""
            if (year != null) this.year = year
            if (voteAvg != null) this.score = Score.from10((voteAvg * 10).toInt())
            this.tags = genres
            this.duration = root.get("runtime")?.asInt
            this.recommendations = parseRecommendations(root.getAsJsonArray("recommendations"))
        }
    }

    private suspend fun loadTvSeries(id: String, pageUrl: String): LoadResponse? {
        val json = tmdbRequest("/tv/$id?append_to_response=credits,videos,recommendations")
        val root = JsonParser.parseString(json).asJsonObject
        val title = root.get("name").asString
        val poster = fixPoster(root.get("poster_path")?.asString)
        val bg = fixPoster(root.get("backdrop_path")?.asString)
        val year = root.get("first_air_date")?.asString?.take(4)?.toIntOrNull()
        val voteAvg = root.get("vote_average")?.asFloat
        val genres = root.getAsJsonArray("genres")?.mapNotNull { it.asJsonObject?.get("name")?.asString } ?: emptyList()
        val seasons = root.getAsJsonArray("seasons")?.mapNotNull {
            it.asJsonObject?.get("season_number")?.asInt?.takeIf { s -> s > 0 }
        }?.sorted() ?: listOf(1)

        val episodes = mutableListOf<Episode>()
        for (seasonNum in seasons) {
            try {
                val seasonJson = tmdbRequest("/tv/$id/season/$seasonNum")
                val epList = JsonParser.parseString(seasonJson).asJsonObject.getAsJsonArray("episodes") ?: continue
                for (ep in epList) {
                    val epObj = ep.asJsonObject
                    val epNum = epObj.get("episode_number").asInt
                    episodes.add(newEpisode("tv:$id:$seasonNum:$epNum") {
                        this.name = epObj.get("name")?.asString ?: "Episodio $epNum"
                        this.episode = epNum
                        this.season = seasonNum
                        this.description = epObj.get("overview")?.asString ?: ""
                        this.posterUrl = fixPoster(epObj.get("still_path")?.asString)
                    })
                }
            } catch (e: Exception) {
                Log.w("TMDB", "Error temporada $seasonNum de $id: ${e.message}")
            }
        }
        episodes.sortWith(compareBy({ it.season ?: 1 }, { it.episode ?: 1 }))

        return newTvSeriesLoadResponse(title, pageUrl, TvType.TvSeries, episodes) {
            this.posterUrl = poster
            this.backgroundPosterUrl = bg
            this.plot = root.get("overview")?.asString ?: ""
            if (year != null) this.year = year
            if (voteAvg != null) this.score = Score.from10((voteAvg * 10).toInt())
            this.tags = genres
            this.recommendations = parseRecommendations(root.getAsJsonArray("recommendations"))
        }
    }

    private fun parseRecommendations(arr: com.google.gson.JsonArray?): List<SearchResponse>? {
        if (arr == null) return null
        val list = mutableListOf<SearchResponse>()
        for (item in arr) {
            try {
                val obj = item.asJsonObject
                val id = obj.get("id").asInt
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
            Log.i("TMDB", "loadLinks: data='$data'")
            val movieMatch = Regex("""movie:(\d+)""").find(data)
            val tvMatch = Regex("""tv:(\d+):(\d+):(\d+)""").find(data)

            val embedUrl = when {
                movieMatch != null -> "$embedApiBase/api/streams/movie/${movieMatch.groupValues[1]}"
                tvMatch != null -> "$embedApiBase/api/streams/series/${tvMatch.groupValues[1]}?season=${tvMatch.groupValues[2]}&episode=${tvMatch.groupValues[3]}"
                else -> return false
            }
            Log.i("TMDB", "loadLinks: GET $embedUrl")
            val resp = app.get(embedUrl).text
            return parseEmbedResponse(resp, callback)
        } catch (e: Exception) {
            Log.e("TMDB", "loadLinks error: ${e.message}")
            return false
        }
    }

    private suspend fun parseEmbedResponse(json: String, callback: (ExtractorLink) -> Unit): Boolean {
        try {
            val arr = JsonParser.parseString(json).asJsonArray
            if (arr.size() == 0) return false
            for (item in arr) {
                val obj = item.asJsonObject
                val streamUrl = obj.get("url")?.asString ?: continue
                val title = obj.get("title")?.asString ?: "Stream"
                val quality = (obj.get("quality")?.asString ?: "720p").lowercase()
                val qualityNum = when {
                    quality.contains("2160") || quality.contains("4k") -> 2160
                    quality.contains("1080") -> 1080
                    quality.contains("720") -> 720
                    quality.contains("480") -> 480
                    quality.contains("360") -> 360
                    else -> 720
                }
                val headersMap = obj.getAsJsonObject("headers")?.entrySet()?.associate {
                    it.key to (it.value?.asString ?: "")
                } ?: emptyMap()
                val refererVal = headersMap["Referer"] ?: headersMap["referer"] ?: ""
                callback.invoke(newExtractorLink("TMDB", title, streamUrl, ExtractorLinkType.M3U8) {
                    this.quality = qualityNum
                    this.referer = refererVal
                })
            }
            return true
        } catch (e: Exception) {
            Log.e("TMDB", "parseEmbedResponse: ${e.message}")
            return false
        }
    }
}
