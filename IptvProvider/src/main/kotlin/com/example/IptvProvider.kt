package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import org.json.JSONArray

class IptvProvider(override var mainUrl: String, override var name: String) : MainAPI() {
    override val hasMainPage = true
    override var lang = "un"
    override val hasQuickSearch = true
    override val hasDownloadSupport = false
    override val supportedTypes = setOf(TvType.Live)

    private val items = mutableMapOf<String, Playlist?>()
    private val headers = mapOf("User-Agent" to "Player (Linux; Android 14)")

    private suspend fun ensureLoaded() {
        if (items[name] != null) return

        val creds = parseXtreamCreds(mainUrl)
        Log.d(TAG, "ensureLoaded: fetch $mainUrl xtream=${creds != null}")

        if (creds != null) {
            val viaApi = loadXtreamApi(creds)
            if (!viaApi.isNullOrEmpty()) {
                items[name] = Playlist(viaApi)
                return
            }
            Log.d(TAG, "ensureLoaded: xtream API sin canales, probando M3U")
        }

        try {
            val resp = app.get(mainUrl, headers = headers, timeout = 40L)
            val text = resp.text
            val playlist = try {
                IptvPlaylistParser().parseM3U(text)
            } catch (e: Exception) {
                Log.e(TAG, "ensureLoaded: M3U invalido code=${resp.code} len=${text.length} body=${snippet(text)}", e)
                throw e
            }
            if (playlist.items.isEmpty()) {
                Log.d(TAG, "ensureLoaded: 0 canales code=${resp.code} len=${text.length} body=${snippet(text)}")
            } else {
                Log.d(TAG, "ensureLoaded: len=${text.length} canales=${playlist.items.size}")
            }
            items[name] = playlist
        } catch (e: Exception) {
            Log.e(TAG, "ensureLoaded fallo url=$mainUrl", e)
            throw e
        }
    }

    private suspend fun loadXtreamApi(c: XtreamCreds): List<PlaylistItem>? {
        return try {
            val apiBase = "${c.base}/player_api.php?username=${c.user}&password=${c.pass}"
            val catNames = mutableMapOf<String, String>()
            val catsText = app.get("$apiBase&action=get_live_categories", headers = headers, timeout = 40L).text
            if (catsText.trim().startsWith("[")) {
                val cats = JSONArray(catsText)
                for (i in 0 until cats.length()) {
                    val o = cats.optJSONObject(i) ?: continue
                    catNames[o.optString("category_id")] = o.optString("category_name")
                }
                Log.d(TAG, "xtream categorias=${cats.length()} nombres=${catNames.size}")
            } else {
                Log.d(TAG, "xtream categorias no JSON: ${snippet(catsText)}")
            }

            val streamsText = app.get("$apiBase&action=get_live_streams", headers = headers, timeout = 90L).text
            if (!streamsText.trim().startsWith("[")) {
                Log.d(TAG, "xtream streams no JSON: ${snippet(streamsText)}")
                return null
            }
            val arr = JSONArray(streamsText)
            val out = ArrayList<PlaylistItem>(arr.length())
            for (i in 0 until arr.length()) {
                val o = arr.optJSONObject(i) ?: continue
                val id = o.optString("stream_id")
                if (id.isBlank()) continue
                val catId = o.optString("category_id")
                val cat = catNames[catId] ?: catId
                val ext = o.optString("container_extension").ifBlank { "ts" }
                out.add(
                    PlaylistItem(
                        title = o.optString("name").ifBlank { id },
                        attributes = mapOf(
                            "group-title" to cat,
                            "tvg-logo" to o.optString("stream_icon")
                        ),
                        url = "${c.base}/live/${c.user}/${c.pass}/$id.$ext"
                    )
                )
            }
            Log.d(TAG, "xtream API: canales=${out.size} categorias=${catNames.size}")
            out
        } catch (e: Exception) {
            Log.e(TAG, "xtream API fallo", e)
            null
        }
    }

    private fun snippet(text: String): String =
        text.replace("\r", " ").replace("\n", " ").take(300)

    private fun makeResponse(item: PlaylistItem): LiveSearchResponse {
        val streamUrl = item.url.toString()
        val channelName = item.title.toString()
        val posterUrl = item.attributes["tvg-logo"].toString()
        val chGroup = item.attributes["group-title"].toString()
        val key = item.attributes["key"].toString()
        val keyid = item.attributes["keyid"].toString()

        return newLiveSearchResponse(
            name = channelName,
            url = LoadData(streamUrl, channelName, posterUrl, chGroup, key, keyid).toJson(),
            type = TvType.Live
        ) { this.posterUrl = posterUrl }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(TAG, "getMainPage page=$page name=$name")
        ensureLoaded()

        val groups = items[name]!!.items.groupBy { it.attributes["group-title"] }.map { group ->
            HomePageList(
                group.key ?: "",
                group.value.map { makeResponse(it) },
                isHorizontalImages = true
            )
        }
        Log.d(TAG, "getMainPage: canales=${items[name]?.items?.size} grupos=${groups.size}")

        return newHomePageResponse(
            groups,
            hasNext = false
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "search: '$query' name=$name")
        ensureLoaded()

        val results = items[name]!!.items.filter {
            it.title.toString().lowercase().contains(query.lowercase())
        }.map { makeResponse(it) }
        Log.d(TAG, "search: '$query' resultados=${results.size}")

        return results
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun load(url: String): LoadResponse {
        val loadData = parseJson<LoadData>(url)
        Log.d(TAG, "load: ${loadData.title} grupo=${loadData.group} name=$name")
        ensureLoaded()

        val recommendations = mutableListOf<LiveSearchResponse>()
        for (item in items[name]!!.items) {
            if (recommendations.size >= 24) break
            if (item.attributes["group-title"].toString() == loadData.group) {
                if (item.title.toString() == loadData.title) continue
                recommendations.add(makeResponse(item))
            }
        }
        Log.d(TAG, "load: ${loadData.title} recomendaciones=${recommendations.size}")

        return newLiveStreamLoadResponse(
            name = loadData.title,
            url = url,
            dataUrl = url,
        ) {
            posterUrl = loadData.poster
            this.recommendations = recommendations
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val loadData = parseJson<LoadData>(data)
        Log.d(TAG, "loadLinks: ${loadData.title} url=${loadData.url} name=$name")
        ensureLoaded()
        val item = items[name]?.items?.firstOrNull { it.url == loadData.url }
        Log.d(TAG, "loadLinks: item encontrado=${item != null} headers=${item?.headers?.keys}")

        if (loadData.url.contains(".mpd", ignoreCase = true)) {
            callback.invoke(
                newDrmExtractorLink(
                    this.name,
                    loadData.title,
                    loadData.url,
                    ExtractorLinkType.DASH,
                    CLEARKEY_UUID
                ) {
                    this.kid = loadData.keyid ?: ""
                    this.key = loadData.key ?: ""
                }
            )
            Log.d(TAG, "loadLinks: emitido DASH con DRM")
            return true
        }

        val linkType = when {
            loadData.url.contains(".m3u8", true) || loadData.url.contains(".m3u", true) ->
                ExtractorLinkType.M3U8
            loadData.url.contains(".ts", true) || loadData.url.contains(".mp4", true) ||
                loadData.url.contains(".mkv", true) || loadData.url.contains(".avi", true) ||
                loadData.url.contains(".flv", true) || loadData.url.contains(".webm", true) ->
                ExtractorLinkType.VIDEO
            else -> ExtractorLinkType.M3U8
        }

        callback.invoke(
            newExtractorLink(
                name = loadData.title,
                source = this.name,
                url = loadData.url,
                type = linkType
            ) {
                this.headers = item?.headers ?: emptyMap()
                this.referer = item?.headers?.get("referrer") ?: ""
                this.quality = Qualities.Unknown.value
            }
        )
        Log.d(TAG, "loadLinks: emitido $linkType ok")
        return true
    }

    companion object {
        const val TAG = "IPTV"
    }
}
