package com.example

import android.content.Context
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.CancellationException

class XuperVODProvider : MainAPI() {
    companion object {
        var pluginContext: Context? = null
        private const val TAG = "XuperVOD"
    }

    override var mainUrl = "https://modlyo.com/magis_web/"
    override var name = "XuperVOD"
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Movie)
    override var lang = "es"
    override val hasMainPage = true
    override val hasDownloadSupport = true

    private val headers = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Accept" to "application/json, text/plain, */*",
        "Accept-Language" to "es-ES,es;q=0.9",
        "Referer" to mainUrl,
    )

    private suspend fun apiCall(action: String, params: Map<String, Any>? = null): Any? {
        return try {
            val q = buildQueryString(action, params)
            val url = "$mainUrl/index.php?$q"
            Log.d(TAG, "API $action -> $url")
            val resp = app.get(url, headers = headers, timeout = 30L)
            if (!resp.isSuccessful) {
                Log.w(TAG, "API $action HTTP ${resp.code}")
                return null
            }
            val text = resp.text
            Log.d(TAG, "API $action response: ${text.take(500)}")
            val json = org.json.JSONObject(text)
            if (!json.optBoolean("ok", false)) {
                Log.w(TAG, "API $action error: ${json.optString("error", "sin error")}")
                return null
            }
            json.opt("data")
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "API $action falló: ${e.message}")
            null
        }
    }

    private fun buildQueryString(action: String, params: Map<String, Any>? = null): String {
        val sb = StringBuilder("action=$action")
        if (params != null) {
            for ((k, v) in params) {
                sb.append("&$k=")
                sb.append(java.net.URLEncoder.encode(v.toString(), "UTF-8"))
            }
        }
        return sb.toString()
    }

    override val mainPage = mainPageOf(
        "home" to "Descubrir",
        "biblioteca" to "Biblioteca",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "getMainPage llamado: page=$page data='${request.data}'")
        return try {
            val data = apiCall("home") ?: return null
            val items = parseHomeItems(data)
            Log.d(TAG, "getMainPage -> ${items.size} items")
            newHomePageResponse(HomePageList("Descubrir", items), false)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "getMainPage falló: ${e.message}")
            null
        }
    }

    private fun parseHomeItems(data: Any?): List<SearchResponse> {
        val items = mutableListOf<SearchResponse>()
        if (data == null) return items
        try {
            val rowsArray = when (data) {
                is org.json.JSONArray -> data
                is org.json.JSONObject -> data.optJSONArray("data") ?: org.json.JSONArray()
                else -> return items
            }

            for (i in 0 until rowsArray.length()) {
                val row = rowsArray.optJSONObject(i) ?: continue
                val rowItems = row.optJSONArray("items") ?: continue
                for (j in 0 until rowItems.length()) {
                    val item = rowItems.optJSONObject(j) ?: continue
                    val id = item.optString("id", item.optString("contentId", ""))
                    val title = item.optString("title", item.optString("name", ""))
                    val type = item.optString("type", item.optString("media_type", "movie"))
                    val poster = item.optString("poster", item.optString("image", ""))

                    if (id.isNotEmpty() && title.isNotEmpty()) {
                        items.add(newTvSeriesSearchResponse(title, "$mainUrl/detail/$id/$type", mapTvType(type)) {
                            this.posterUrl = if (poster.isNotEmpty()) poster else null
                        })
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "parseHomeItems falló: ${e.message}", e)
        }
        return items
    }

    private fun mapTvType(type: String): TvType {
        return when (type.lowercase()) {
            "series", "tv", "teleplay", "tvseries" -> TvType.TvSeries
            "anime", "animacion", "animación" -> TvType.Anime
            else -> TvType.Movie
        }
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        Log.d(TAG, "search() llamado: '$query'")
        return try {
            val data = apiCall("search", mapOf("q" to query)) ?: return null
            val results = parseSearchResults(data)
            Log.d(TAG, "search '$query' -> ${results.size} resultados")
            results
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "search falló: ${e.message}")
            null
        }
    }

    private fun parseSearchResults(data: Any?): List<SearchResponse> {
        val items = mutableListOf<SearchResponse>()
        if (data == null) return items
        try {
            val json = when (data) {
                is org.json.JSONArray -> data
                is org.json.JSONObject -> data.optJSONArray("results") ?: data.optJSONArray("data") ?: org.json.JSONArray()
                else -> return items
            }

            for (i in 0 until json.length()) {
                val item = json.optJSONObject(i) ?: continue
                val id = item.optString("id", item.optString("contentId", ""))
                val title = item.optString("title", item.optString("name", ""))
                val type = item.optString("type", item.optString("media_type", "movie"))
                val poster = item.optString("poster", item.optString("image", ""))

                if (id.isNotEmpty() && title.isNotEmpty()) {
                    items.add(newTvSeriesSearchResponse(title, "$mainUrl/detail/$id/$type", mapTvType(type)) {
                        this.posterUrl = if (poster.isNotEmpty()) poster else null
                    })
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "parseSearchResults falló: ${e.message}", e)
        }
        return items
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "load($url)")
        val parts = url.substringAfter("/detail/").split("/", limit = 2)
        if (parts.size < 2) return null
        val id = parts[0]
        val type = parts[1]
        return try {
            val metaObj = apiCall("meta", mapOf("id" to id))
            val meta = metaObj as? org.json.JSONObject ?: return null
            parseDetail(meta, id, type, url)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "load falló: ${e.message}", e)
            null
        }
    }

    private suspend fun parseDetail(json: org.json.JSONObject?, id: String, type: String, url: String): LoadResponse? {
        if (json == null) return null
        val title = json.optString("title", json.optString("name", ""))
        val description = json.optString("description", json.optString("plot", json.optString("overview", "")))
        val poster = json.optString("poster", json.optString("image", ""))
        val year = json.optString("year", "")

        val episodes = mutableListOf<Episode>()

        // Buscamos temporadas en diversas estructuras posibles del JSON
        val seasons = json.optJSONArray("seasons") ?: json.optJSONArray("season_list")

        Log.d(TAG, "parseDetail -> Título: $title, Temporadas encontradas: ${seasons?.length() ?: 0}")

        if (seasons != null) {
            for (s in 0 until seasons.length()) {
                val season = seasons.optJSONObject(s) ?: continue
                val seasonNum = season.optInt("season", season.optInt("season_number", season.optInt("number", s + 1)))
                val eps = season.optJSONArray("episodes") ?: season.optJSONArray("items") ?: continue

                Log.d(TAG, "Temporada $seasonNum tiene ${eps.length()} episodios")

                for (e in 0 until eps.length()) {
                    val ep = eps.optJSONObject(e) ?: continue
                    val epId = ep.optString("id", ep.optString("contentId", ""))
                    val epTitle = ep.optString("title", ep.optString("name", "Episodio ${e + 1}"))
                    val epNum = ep.optInt("number", ep.optInt("episode_number", e + 1))
                    val epPoster = ep.optString("poster", ep.optString("image", ""))
                    val epDesc = ep.optString("description", ep.optString("overview", ""))

                    episodes.add(newEpisode("$mainUrl/detail/$epId/episode") {
                        this.name = epTitle
                        this.season = seasonNum
                        this.episode = epNum
                        this.posterUrl = if (epPoster.isNotEmpty()) epPoster else null
                        this.description = if (epDesc.isNotEmpty()) epDesc else null
                    })
                }
            }
        }

        // Respaldo por si los episodios vienen planos sin array de temporadas
        val flatEpisodes = json.optJSONArray("episodes")
        if (episodes.isEmpty() && flatEpisodes != null) {
            for (e in 0 until flatEpisodes.length()) {
                val ep = flatEpisodes.optJSONObject(e) ?: continue
                val epId = ep.optString("id", "")
                val epTitle = ep.optString("title", "Episodio ${e + 1}")
                val epNum = ep.optInt("number", e + 1)
                val epPoster = ep.optString("poster", "")
                val epDesc = ep.optString("description", "")

                episodes.add(newEpisode("$mainUrl/detail/$epId/episode") {
                    this.name = epTitle
                    this.season = ep.optInt("season", 1)
                    this.episode = epNum
                    this.posterUrl = if (epPoster.isNotEmpty()) epPoster else null
                    this.description = if (epDesc.isNotEmpty()) epDesc else null
                })
            }
        }

        val finalType = if (type.lowercase() == "movie" && episodes.isEmpty()) TvType.Movie else mapTvType(type)

        if (finalType == TvType.Movie) {
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = if (poster.isNotEmpty()) poster else null
                this.plot = if (description.isNotEmpty()) description else null
                this.year = if (year.isNotEmpty()) year.toIntOrNull() else null
            }
        }

        return newTvSeriesLoadResponse(title, url, finalType, episodes) {
            this.posterUrl = if (poster.isNotEmpty()) poster else null
            this.plot = if (description.isNotEmpty()) description else null
            this.year = if (year.isNotEmpty()) year.toIntOrNull() else null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "loadLinks data='$data'")
        val parts = data.substringAfter("/detail/").split("/", limit = 2)
        if (parts.size < 2) return false
        val id = parts[0]

        return try {
            val metaObj = apiCall("meta", mapOf("id" to id))
            val meta = metaObj as? org.json.JSONObject ?: return false
            val streams = extractStreams(meta)
            if (streams.isEmpty()) return false
            for (stream in streams) {
                callback(newExtractorLink(name, stream.label, stream.url, stream.type) {
                    this.referer = mainUrl
                    this.headers = headers
                    this.quality = stream.quality
                })
            }
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "loadLinks falló: ${e.message}")
            false
        }
    }

    private fun extractStreams(data: org.json.JSONObject?): List<StreamInfo> {
        val streams = mutableListOf<StreamInfo>()
        if (data == null) return streams
        try {
            val streamsArray = data.optJSONArray("streams") ?: data.optJSONArray("sources") ?: return streams
            for (i in 0 until streamsArray.length()) {
                val s = streamsArray.optJSONObject(i) ?: continue
                val url = s.optString("url", "")
                val label = s.optString("label", s.optString("quality", "Desconocido"))
                val streamType = s.optString("type", "VIDEO")
                if (url.isNotEmpty()) {
                    streams.add(StreamInfo(label, url, if (streamType == "hls" || url.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO, mapQuality(label)))
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "extractStreams falló: ${e.message}")
        }
        return streams
    }

    private fun mapQuality(label: String): Int {
        val l = label.lowercase()
        return when {
            l.contains("1080") -> Qualities.P1080.value
            l.contains("720") -> Qualities.P720.value
            l.contains("480") -> Qualities.P480.value
            l.contains("360") -> Qualities.P360.value
            else -> Qualities.Unknown.value
        }
    }

    private data class StreamInfo(val label: String, val url: String, val type: ExtractorLinkType, val quality: Int)
}