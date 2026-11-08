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
    override var lang = "mx"
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
        "tvlive" to "TV en Vivo",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "getMainPage llamado: page=$page data='${request.data}'")
        return try {
            val root = apiRoot("live") ?: return null
            val channels = parseLiveChannels(root.optJSONArray("data"))
            Log.d(TAG, "getMainPage -> ${channels.size} canales")
            val list = channels.toMutableList()
            newHomePageResponse(HomePageList("TV en Vivo", list), false)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "getMainPage falló: ${e.message}")
            null
        }
    }

    private fun parseLiveChannels(dataArray: org.json.JSONArray?): List<SearchResponse> {
        val items = mutableListOf<SearchResponse>()
        if (dataArray == null) return items
        try {
            for (i in 0 until dataArray.length()) {
                val el = dataArray.optJSONObject(i) ?: continue
                val rowItems = el.optJSONArray("items")
                if (rowItems != null) {
                    for (j in 0 until rowItems.length()) {
                        rowItems.optJSONObject(j)?.let { addChannel(items, it) }
                    }
                } else {
                    addChannel(items, el)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "parseLiveChannels falló: ${e.message}")
        }
        return items.distinctBy { it.url }
    }

    private fun addChannel(items: MutableList<SearchResponse>, ch: org.json.JSONObject) {
        val id = ch.optString("id", ch.optString("contentId", ""))
        if (id.isEmpty()) return
        val title = ch.optString("title", ch.optString("name", ""))
        if (title.isEmpty()) return
        val poster = ch.optString("poster", ch.optString("logo", ""))
        val category = ch.optString("category", ch.optString("group", ""))
        items.add(newTvSeriesSearchResponse(title, "$mainUrl/live/$id", TvType.Live) {
            this.posterUrl = if (poster.isNotEmpty()) poster else null
            //this.plot = if (category.isNotEmpty()) category else null
        })
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        Log.d(TAG, "search() llamado: '$query'")
        return try {
            val root = apiRoot("live") ?: return null
            val all = parseLiveChannels(root.optJSONArray("data"))
            val q = query.lowercase()
            val filtered = all.filter { it.name.lowercase().contains(q) }
            Log.d(TAG, "search '$query' -> ${filtered.size} canales")
            filtered
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "search falló: ${e.message}")
            null
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "load($url)")
        val id = url.substringAfter("/live/")
        return try {
            val root = apiRoot("live") ?: return null
            val all = parseLiveChannels(root.optJSONArray("data"))
            val found = all.firstOrNull { it.url.endsWith("/live/$id") } ?: return null
            newTvSeriesLoadResponse(found.name, url, TvType.Live, mutableListOf()) {
                this.posterUrl = found.posterUrl
                this.plot = found.name
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "load falló: ${e.message}")
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "loadLinks data='$data'")
        val id = data.substringAfter("/live/")

        return try {
            val root = apiRoot("live_stream", mapOf("id" to id)) ?: return false
            val stream = parseStream(root) ?: return false

            // La web reproduce vía proxy.php con headers; sin proxy el CDN rechaza.
            val playUrl = buildProxyUrl(stream)
            Log.d(TAG, "loadLinks proxy stream: ${playUrl.take(160)} (${stream.type})")

            callback(newExtractorLink(name, "TV en Vivo", playUrl, stream.type) {
                this.referer = "$mainUrl/"
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

    /** Construye la URL de proxy.php igual que la web (url + license + auth + ua). */
    private fun buildProxyUrl(stream: StreamData): String {
        val sb = StringBuilder("$mainUrl/proxy.php?url=")
        sb.append(java.net.URLEncoder.encode(stream.url, "UTF-8"))
        if (stream.license.isNotEmpty()) {
            sb.append("&license=").append(java.net.URLEncoder.encode(stream.license, "UTF-8"))
        }
        if (stream.auth.isNotEmpty()) {
            sb.append("&auth=").append(java.net.URLEncoder.encode(stream.auth, "UTF-8"))
        }
        if (stream.userAgent.isNotEmpty()) {
            sb.append("&ua=").append(java.net.URLEncoder.encode(stream.userAgent, "UTF-8"))
        }
        return sb.toString()
    }

    private fun parseStream(root: org.json.JSONObject): StreamData? {
        val dataArr = root.optJSONArray("data")
        val s = dataArr?.optJSONObject(0) ?: root.optJSONObject("data") ?: return null
        val url = s.optString("url", "")
        if (url.isEmpty()) return null
        val label = s.optString("label", "TV en Vivo")
        val streamType = s.optString("type", "hls")
        val hdrs = s.optJSONObject("headers")
        val license = hdrs?.optString("Content-License", "")?.takeIf { it.isNotEmpty() }
            ?: s.optString("license", "")
        val auth = hdrs?.optString("Content-Auth", "")?.takeIf { it.isNotEmpty() }
            ?: s.optString("auth", "")
        val ua = hdrs?.optString("User-Agent", "")?.takeIf { it.isNotEmpty() } ?: ""
        val isHls = streamType == "hls" || s.optBoolean("isHls", false) || url.contains(".m3u8")
        return StreamData(
            url = url,
            label = label,
            type = if (isHls) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO,
            quality = mapQuality(label),
            license = license,
            auth = auth,
            userAgent = ua,
        )
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
    )
}