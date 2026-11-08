package com.example

import android.content.Context
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.CancellationException

class XuperLiveProvider : MainAPI() {
    companion object {
        var pluginContext: Context? = null
        private const val TAG = "XuperLive"
    }

    override var mainUrl = "https://modlyo.com/magis_web"
    override var name = "XuperLive"
    override val supportedTypes = setOf(TvType.Live)
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
        "tvlive" to "TV en Vivo",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "getMainPage llamado: page=$page data='${request.data}'")
        return try {
            val live = apiCall("live") ?: return null
            val channels = parseLiveChannels(live)
            Log.d(TAG, "getMainPage -> ${channels.size} canales")
            val list = channels.toMutableList()
            newHomePageResponse(HomePageList("TV en Vivo", list), false)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "getMainPage falló: ${e.message}")
            null
        }
    }

    private fun parseLiveChannels(data: org.json.JSONObject?): List<SearchResponse> {
        val items = mutableListOf<SearchResponse>()
        if (data == null) return items
        try {
            val json = data.optJSONArray("data") ?: return items
            for (i in 0 until json.length()) {
                val ch = json.optJSONObject(i) ?: continue
                val id = ch.optString("id", "")
                val title = ch.optString("title", ch.optString("name", ""))
                val poster = ch.optString("poster", ch.optString("logo", ""))
                val category = ch.optString("category", ch.optString("group", ""))
                val country = ch.optString("country", "")
                items.add(newTvSeriesSearchResponse(title, "$mainUrl/channel/$id", TvType.Live) {
                    this.posterUrl = if (poster.isNotEmpty()) poster else null
                    //this.plot = if (category.isNotEmpty()) category else null
                    //this.tags = if (country.isNotEmpty()) listOf(country) else null
                })
            }
        } catch (e: Exception) {
            Log.w(TAG, "parseLiveChannels falló: ${e.message}")
        }
        return items
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        Log.d(TAG, "search() llamado: '$query'")
        Log.w(TAG, "search: TV en vivo no tiene búsqueda")
        return null
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "load($url)")
        val id = url.substringAfter("/channel/")
        return try {
            val live = apiCall("live") ?: return null
            val ch = findChannel(live, id) ?: return null
            newTvSeriesLoadResponse(ch.optString("title", ""), url, TvType.Live, mutableListOf()) {
                this.posterUrl = ch.optString("poster", ch.optString("logo", "")).takeIf { it.isNotEmpty() }
                this.plot = ch.optString("category", "")
                this.tags = ch.optString("country", "").takeIf { it.isNotEmpty() }?.let { listOf(it) }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "load falló: ${e.message}")
            null
        }
    }

    private fun findChannel(data: org.json.JSONObject?, id: String): org.json.JSONObject? {
        if (data == null) return null
        try {
            val json = data.optJSONArray("data") ?: return null
            for (i in 0 until json.length()) {
                val ch = json.optJSONObject(i) ?: continue
                if (ch.optString("id", "") == id) return ch
            }
        } catch (e: Exception) {
            Log.w(TAG, "findChannel falló: ${e.message}")
        }
        return null
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "loadLinks data='$data'")
        val id = data.substringAfter("/channel/")

        return try {
            val streamData = apiCall("live_stream", mapOf("id" to id)) ?: return false
            val stream = parseStream(streamData) ?: return false

            Log.d(TAG, "loadLinks stream: ${stream.url} (${stream.type})")

            callback(newExtractorLink(name, stream.label, stream.url, stream.type) {
                this.referer = mainUrl
                this.headers = headers
                this.quality = stream.quality
            })
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "loadLinks falló: ${e.message}")
            false
        }
    }

    private fun parseStream(data: org.json.JSONObject?): StreamInfo? {
        if (data == null) return null
        try {
            val streams = data.optJSONArray("streams") ?: data.optJSONArray("sources") ?: return null
            val s = streams.optJSONObject(0) ?: return null
            val url = s.optString("url", "")
            val label = s.optString("label", "TV en Vivo")
            val streamType = s.optString("type", "hls")
            if (url.isEmpty()) return null

            return StreamInfo(
                label = label,
                url = url,
                type = if (streamType == "hls" || url.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO,
                quality = mapQuality(label)
            )
        } catch (e: Exception) {
            Log.w(TAG, "parseStream falló: ${e.message}", e)
            return null
        }
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