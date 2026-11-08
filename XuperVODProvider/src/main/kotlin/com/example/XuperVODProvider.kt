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

    override var mainUrl = "https://modlyo.com/magis_web"
    override var name = "XuperVOD"
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Movie)
    override var lang = "es"
    override val hasMainPage = true
    override val hasDownloadSupport = true

    private val headers = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Accept" to "application/json, text/plain, */*",
        "Accept-Language" to "es-ES,es;q=0.9",
        "Referer" to "$mainUrl/",
    )

    private suspend fun apiRoot(action: String, params: Map<String, Any>? = null): org.json.JSONObject? {
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
            Log.d(TAG, "API $action response: ${text.take(400)}")
            val json = org.json.JSONObject(text)
            if (!json.optBoolean("ok", false)) {
                Log.w(TAG, "API $action error: ${json.optString("error", "sin error")}")
                return null
            }
            json
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
            val root = apiRoot("home") ?: return null
            val items = parseHomeItems(root.optJSONArray("data"))
            Log.d(TAG, "getMainPage -> ${items.size} items")
            newHomePageResponse(HomePageList("Descubrir", items.toMutableList()), false)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "getMainPage falló: ${e.message}")
            null
        }
    }

    private fun parseHomeItems(rowsArray: org.json.JSONArray?): List<SearchResponse> {
        val items = mutableListOf<SearchResponse>()
        if (rowsArray == null) return items
        try {
            for (i in 0 until rowsArray.length()) {
                val row = rowsArray.optJSONObject(i) ?: continue
                val rowItems = row.optJSONArray("items") ?: continue
                for (j in 0 until rowItems.length()) {
                    rowItems.optJSONObject(j)?.let { addItem(items, it) }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "parseHomeItems falló: ${e.message}")
        }
        return items.distinctBy { it.url }
    }

    private fun addItem(items: MutableList<SearchResponse>, item: org.json.JSONObject) {
        val id = item.optString("id", item.optString("contentId", ""))
        if (id.isEmpty()) return
        val title = item.optString("title", item.optString("name", ""))
        if (title.isEmpty()) return
        val type = item.optString("type", item.optString("media_type", "movie"))
        val poster = item.optString("poster", item.optString("image", ""))
        items.add(newTvSeriesSearchResponse(title, "$mainUrl/detail/$id/$type", mapTvType(type)) {
            this.posterUrl = if (poster.isNotEmpty()) poster else null
        })
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
            val root = apiRoot("search", mapOf("q" to query)) ?: return null
            val results = parseSearchResults(root.optJSONArray("data"))
            Log.d(TAG, "search '$query' -> ${results.size} resultados")
            results
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "search falló: ${e.message}")
            null
        }
    }

    private fun parseSearchResults(dataArray: org.json.JSONArray?): List<SearchResponse> {
        val items = mutableListOf<SearchResponse>()
        if (dataArray == null) return items
        try {
            for (i in 0 until dataArray.length()) {
                val el = dataArray.optJSONObject(i) ?: continue
                val rowItems = el.optJSONArray("items")
                if (rowItems != null) {
                    for (j in 0 until rowItems.length()) {
                        rowItems.optJSONObject(j)?.let { addItem(items, it) }
                    }
                } else {
                    addItem(items, el)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "parseSearchResults falló: ${e.message}")
        }
        return items.distinctBy { it.url }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "load($url)")
        val parts = url.substringAfter("/detail/").split("/", limit = 2)
        if (parts.size < 2) return null
        val id = parts[0]
        val type = parts[1].substringBefore("?")
        return try {
            val root = apiRoot("meta", mapOf("id" to id)) ?: return null
            val dataArr = root.optJSONArray("data")
            val meta = if (dataArr != null) dataArr.optJSONObject(0) else root.optJSONObject("data")
            parseDetail(meta, type, url)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "load falló: ${e.message}")
            null
        }
    }

    private suspend fun parseDetail(json: org.json.JSONObject?, type: String, url: String): LoadResponse? {
        if (json == null) return null
        val title = json.optString("title", json.optString("name", ""))
        val description = json.optString("description", json.optString("plot", ""))
        val poster = json.optString("poster", json.optString("image", ""))
        val year = json.optString("year", "")
        val metaType = json.optString("type", type)
        val seriesContentId = json.optString("contentId", json.optString("id", ""))

        val episodes = mutableListOf<Episode>()
        val seasons = json.optJSONArray("seasons")
        if (seasons != null) {
            for (s in 0 until seasons.length()) {
                val season = seasons.optJSONObject(s) ?: continue
                val seasonNum = season.optInt("number", s + 1)
                val seasonCid = season.optString("contentId", seriesContentId)
                val eps = season.optJSONArray("episodes") ?: continue
                for (e in 0 until eps.length()) {
                    val ep = eps.optJSONObject(e) ?: continue
                    val epId = ep.optString("id", "")
                    if (epId.isEmpty()) continue
                    val epTitle = ep.optString("title", "Episodio ${e + 1}")
                    val epNum = ep.optInt("number", e + 1)
                    val epPoster = ep.optString("poster", "")
                    val epDesc = ep.optString("description", "")
                    val epUrl = "$mainUrl/detail/$epId/episode?sc=$seasonCid"
                    episodes.add(newEpisode(epUrl) {
                        this.name = epTitle
                        this.season = seasonNum
                        this.episode = epNum
                        this.posterUrl = if (epPoster.isNotEmpty()) epPoster else null
                        this.description = if (epDesc.isNotEmpty()) epDesc else null
                    })
                }
            }
        }

        Log.d(TAG, "parseDetail '$title' type=$metaType temporadas=${seasons?.length() ?: 0} episodios=${episodes.size}")

        if (episodes.isEmpty()) {
            return newMovieLoadResponse(title, url, mapTvType(metaType), "$mainUrl/detail/$seriesContentId/movie") {
                this.posterUrl = if (poster.isNotEmpty()) poster else null
                this.plot = if (description.isNotEmpty()) description else null
                this.year = if (year.isNotEmpty()) year.toIntOrNull() else null
            }
        }

        return newTvSeriesLoadResponse(title, url, mapTvType(metaType), episodes) {
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
        val afterDetail = data.substringAfter("/detail/")
        val id = afterDetail.substringBefore("/")
        val rest = afterDetail.substringAfter("/", "")
        val type = rest.substringBefore("?").ifEmpty { "movie" }
        val seriesContentId = if (rest.contains("sc=")) rest.substringAfter("sc=").substringBefore("&") else ""

        if (id.isEmpty()) return false
        Log.d(TAG, "loadLinks id=$id type=$type seriesContentId=$seriesContentId")

        return try {
            val root = apiRoot(
                "streams",
                mapOf("id" to id, "type" to type, "seriesContentId" to seriesContentId),
            ) ?: return false

            val streams = parseStreams(root)
            if (streams.isEmpty()) {
                Log.w(TAG, "loadLinks sin streams para id=$id")
                return false
            }
            for (stream in streams) {
                for (sub in stream.subtitles) subtitleCallback(sub)
                val playUrl = buildProxyUrl(stream)
                Log.d(TAG, "loadLinks stream ${stream.label} -> ${playUrl.take(120)}")
                callback(newExtractorLink(name, stream.label, playUrl, stream.type) {
                    this.referer = "$mainUrl/"
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

    private fun buildProxyUrl(stream: StreamData): String {
        val sb = StringBuilder("$mainUrl/proxy.php?url=")
        sb.append(java.net.URLEncoder.encode(stream.url, "UTF-8"))
        if (stream.license.isNotEmpty()) sb.append("&license=").append(java.net.URLEncoder.encode(stream.license, "UTF-8"))
        if (stream.auth.isNotEmpty()) sb.append("&auth=").append(java.net.URLEncoder.encode(stream.auth, "UTF-8"))
        if (stream.userAgent.isNotEmpty()) sb.append("&ua=").append(java.net.URLEncoder.encode(stream.userAgent, "UTF-8"))
        return sb.toString()
    }

    private fun parseStreams(root: org.json.JSONObject): List<StreamData> {
        val out = mutableListOf<StreamData>()
        val dataArr = root.optJSONArray("data")
        val streamsArr = dataArr
            ?: root.optJSONObject("data")?.let { it.optJSONArray("streams") ?: it.optJSONArray("sources") }
            ?: return out
        for (i in 0 until streamsArr.length()) {
            val s = streamsArr.optJSONObject(i) ?: continue
            val url = s.optString("url", "")
            if (url.isEmpty()) continue
            val label = s.optString("label", s.optString("quality", "Desconocido"))
            val streamType = s.optString("type", "VIDEO")
            val hdrs = s.optJSONObject("headers")
            val license = hdrs?.optString("Content-License", "")?.takeIf { it.isNotEmpty() } ?: s.optString("license", "")
            val auth = hdrs?.optString("Content-Auth", "")?.takeIf { it.isNotEmpty() } ?: s.optString("auth", "")
            val ua = hdrs?.optString("User-Agent", "")?.takeIf { it.isNotEmpty() } ?: ""
            val isHls = streamType == "hls" || s.optBoolean("isHls", false) || url.contains(".m3u8")
            val subs = mutableListOf<SubtitleFile>()
            val subsArr = s.optJSONArray("subtitles")
            if (subsArr != null) {
                for (k in 0 until subsArr.length()) {
                    val sub = subsArr.optJSONObject(k) ?: continue
                    val subUrl = sub.optString("url", "")
                    if (subUrl.isEmpty()) continue
                    val subLang = sub.optString("language", sub.optString("lang", "es"))
                    val subLabel = sub.optString("label", subLang)
                    subs.add(SubtitleFile(subLang, subUrl))
                }
            }
            out.add(StreamData(
                url = url,
                label = label,
                type = if (isHls) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO,
                quality = mapQuality(label),
                license = license,
                auth = auth,
                userAgent = ua,
                subtitles = subs,
            ))
        }
        return out
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

    private data class StreamData(
        val url: String,
        val label: String,
        val type: ExtractorLinkType,
        val quality: Int,
        val license: String,
        val auth: String,
        val userAgent: String,
        val subtitles: List<SubtitleFile>,
    )
}