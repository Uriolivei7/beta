package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.toJson

class IptvProvider(override var mainUrl: String, override var name: String) : MainAPI() {
    override val hasMainPage = true
    override var lang = "un"
    override val hasQuickSearch = true
    override val hasDownloadSupport = false
    override val supportedTypes = setOf(TvType.Live)

    private val items = mutableMapOf<String, Playlist?>()
    private val headers = mapOf("User-Agent" to "Player (Linux; Android 14)")

    private suspend fun ensureLoaded() {
        if (items[name] == null) {
            Log.d(TAG, "ensureLoaded: fetch $mainUrl")
            try {
                val text = app.get(mainUrl, headers = headers, timeout = 40L).text
                val playlist = IptvPlaylistParser().parseM3U(text)
                Log.d(TAG, "ensureLoaded: len=${text.length} canales=${playlist.items.size}")
                items[name] = playlist
            } catch (e: Exception) {
                Log.e(TAG, "ensureLoaded fallo url=$mainUrl", e)
                throw e
            }
        }
    }

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
