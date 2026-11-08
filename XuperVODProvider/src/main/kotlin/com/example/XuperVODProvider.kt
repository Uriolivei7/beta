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

    private suspend fun apiCall(action: String, params: Map<String, Any>? = null): org.json.JSONObject? {
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
            val json = org.json.JSONObject(text)
            if (!json.optBoolean("ok", false)) {
                Log.w(TAG, "API $action error: ${json.optString("error", "sin error")}")
                return null
            }
            json.optJSONObject("data")
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "API $action falló: ${e.message}")
            null
        }
    }

    private fun buildQueryString(action: String, params: Map<String, Any>?): String {
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
            val home = apiCall("home") ?: return null
            val items = parseHomeItems(home)
            Log.d(TAG, "getMainPage -> ${items.size} items")
            val list = items.toMutableList()
            newHomePageResponse(HomePageList("Descubrir", list), false)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "getMainPage falló: ${e.message}")
            null
        }
    }

    private fun parseHomeItems(data: org.json.JSONObject?): List<SearchResponse> {
        val items = mutableListOf<SearchResponse>()
        if (data == null) return items
        try {
            val rows = data.optJSONArray("rows") ?: data.optJSONArray("items") ?: return items
            for (i in 0 until rows.length()) {
                val row = rows.optJSONObject(i) ?: continue
                val rowItems = row.optJSONArray("items") ?: continue
                for (j in 0 until rowItems.length()) {
                    val item = rowItems.optJSONObject(j) ?: continue
                    val id = item.optString("id", "")
                    val title = item.optString("title", "")
                    val type = item.optString("type", "movie")
                    val poster = item.optString("poster", "")
                    val year = item.optString("year", "")

                    val genresArray = item.optJSONArray("genres")
                    val genresList = mutableListOf<String>()
                    if (genresArray != null) {
                        for (g in 0 until genresArray.length()) {
                            genresList.add(genresArray.optString(g))
                        }
                    }
                    val genres = genresList.joinToString(",")

                    items.add(newTvSeriesSearchResponse(title, "$mainUrl/detail/$id/$type", mapTvType(type)) {
                        this.posterUrl = if (poster.isNotEmpty()) poster else null
                        this.year = if (year.isNotEmpty()) year.toIntOrNull() else null
                    })
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "parseHomeItems falló: ${e.message}", e)
        }
        return items
    }

    private fun mapTvType(type: String): TvType {
        return when (type.lowercase()) {
            "series", "tv", "teleplay" -> TvType.TvSeries
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

    private fun parseSearchResults(data: org.json.JSONObject?): List<SearchResponse> {
        val items = mutableListOf<SearchResponse>()
        if (data == null) return items
        try {
            val json = data.optJSONArray("results") ?: return items
            for (i in 0 until json.length()) {
                val item = json.optJSONObject(i) ?: continue
                val id = item.optString("id", "")
                val title = item.optString("title", "")
                val type = item.optString("type", "movie")
                val poster = item.optString("poster", "")
                items.add(newTvSeriesSearchResponse(title, "$mainUrl/detail/$id/$type", mapTvType(type)) {
                    this.posterUrl = if (poster.isNotEmpty()) poster else null
                })
            }
        } catch (e: Exception) {
            Log.w(TAG, "parseSearchResults falló: ${e.message}")
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
            val meta = apiCall("meta", mapOf("id" to id)) ?: return null
            parseDetail(meta, id, type)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "load falló: ${e.message}")
            null
        }
    }

    private suspend fun parseDetail(data: org.json.JSONObject?, url: String, type: String): LoadResponse? {
        val json = data ?: return null
        val title = json.optString("title", "")
        val description = json.optString("description", json.optString("plot", ""))
        val poster = json.optString("poster", "")
        val year = json.optString("year", "")
        val rating = json.optDouble("rating", 0.0)

        val genresArray = json.optJSONArray("genres")
        val genresList = mutableListOf<String>()
        if (genresArray != null) {
            for (g in 0 until genresArray.length()) {
                genresList.add(genresArray.optString(g))
            }
        }
        val genres = genresList.joinToString(",")

        val episodes = mutableListOf<Episode>()
        val seasons = json.optJSONArray("seasons")
        if (seasons != null) {
            for (s in 0 until seasons.length()) {
                val season = seasons.optJSONObject(s) ?: continue
                val seasonNum = season.optInt("season", s + 1)
                val eps = season.optJSONArray("episodes") ?: continue
                for (e in 0 until eps.length()) {
                    val ep = eps.optJSONObject(e) ?: continue
                    val epId = ep.optString("id", "")
                    val epTitle = ep.optString("title", "Episodio ${e + 1}")
                    val epNum = ep.optInt("number", e + 1)
                    val epPoster = ep.optString("poster", "")
                    val epDesc = ep.optString("description", "")

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

        return newTvSeriesLoadResponse(title, url, mapTvType(type), episodes) {
            this.posterUrl = if (poster.isNotEmpty()) poster else null
            this.plot = if (description.isNotEmpty()) description else null
            this.tags = if (genres.isNotEmpty()) genres.split(",") else null
            this.year = if (year.isNotEmpty()) year.toIntOrNull() else null
            //this.score = (rating * 10).toInt()
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
        val type = parts[1]

        return try {
            val meta = apiCall("meta", mapOf("id" to id)) ?: return false
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