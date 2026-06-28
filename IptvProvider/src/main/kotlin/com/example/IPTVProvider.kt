package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID
import android.util.Log

class IPTVProvider(mainUrl: String, name: String) : MainAPI() {
    override var mainUrl = mainUrl
    override var name = name
    override val hasMainPage = true
    override var lang = "mx"
    override val hasQuickSearch = true
    override val hasDownloadSupport = false
    override val supportedTypes = setOf(
        TvType.Live
    )

    val items = mutableMapOf<String, Playlist?>()
    val headers = mapOf("User-Agent" to "Player (Linux; Android 14)")

    companion object {
        private const val LOG_TAG = "IPTVProvider"
    }

    private fun getSafePosterUrl(url: String?): String {
        val isInvalid = url.isNullOrBlank() ||
                url == "null" ||
                !url.startsWith("http", ignoreCase = true)

        val finalUrl = if (isInvalid) {
            Log.d(LOG_TAG, "getSafePosterUrl: URL '$url' inválida, usando vacía")
            ""
        } else {
            url
        }

        return finalUrl
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(LOG_TAG, "=== getMainPage: name=$name, mainUrl=$mainUrl ===")
        items[name] = IptvPlaylistParser().parseM3U(app.get(mainUrl, headers = headers).text)
        val parsed = items[name]
        Log.d(LOG_TAG, "getMainPage: playlist cargada, items=${parsed?.items?.size ?: 0}")

        return newHomePageResponse(
            items[name]!!.items.groupBy { it.attributes["group-title"] }.map { group ->
                val title = group.key ?: ""
                val show = group.value.map { item ->
                    val streamurl = item.url.toString()
                    val channelname = item.title.toString()
                    val posterurl = getSafePosterUrl(item.attributes["tvg-logo"].toString())
                    val chGroup = item.attributes["group-title"].toString()
                    val key = item.attributes["key"].toString()
                    val keyid = item.attributes["keyid"].toString()

                    newLiveSearchResponse(
                        name = channelname,
                        url = LoadData(streamurl, channelname, posterurl, chGroup, key, keyid).toJson(),
                        type = TvType.Live
                    ) {
                        posterUrl = posterurl
                    }
                }

                HomePageList(title, show, isHorizontalImages = true)
            },
            hasNext = false
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(LOG_TAG, "=== search: query=$query, mainUrl=$mainUrl ===")
        if (items[name] == null) {
            Log.d(LOG_TAG, "search: playlist vacía, cargando...")
            items[name] = IptvPlaylistParser().parseM3U(app.get(mainUrl, headers = headers).text)
            Log.d(LOG_TAG, "search: playlist cargada, items=${items[name]?.items?.size ?: 0}")
        }

        return items[name]!!.items.filter { it.title.toString().lowercase().contains(query.lowercase()) }.map { item ->
            val streamurl = item.url.toString()
            val channelname = item.title.toString()
            val posterurl = getSafePosterUrl(item.attributes["tvg-logo"].toString())
            val chGroup = item.attributes["group-title"].toString()
            val key = item.attributes["key"].toString()
            val keyid = item.attributes["keyid"].toString()

            newLiveSearchResponse(
                name = channelname,
                url = LoadData(streamurl, channelname, posterurl, chGroup, key, keyid).toJson(),
                type = TvType.Live
            ) {
                posterUrl = posterurl
            }
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun load(url: String): LoadResponse {
        Log.d(LOG_TAG, "=== load: name=$name ===")
        val loadData = parseJson<LoadData>(url)
        Log.d(LOG_TAG, "load: title=${loadData.title}, url=${loadData.url.take(80)}, group=${loadData.group}")
        if (items[name] == null) {
            Log.d(LOG_TAG, "load: playlist vacía, cargando...")
            items[name] = IptvPlaylistParser().parseM3U(app.get(mainUrl, headers = headers).text)
            Log.d(LOG_TAG, "load: playlist cargada, items=${items[name]?.items?.size ?: 0}")
        }
        val recommendations = mutableListOf<LiveSearchResponse>()
        for (item in items[name]!!.items) {
            if (recommendations.size >= 24) break
            if (item.attributes["group-title"].toString() == loadData.group) {
                val rcStreamUrl = item.url.toString()
                val rcChannelName = item.title.toString()
                if (rcChannelName == loadData.title) continue
                val rcPosterUrl = getSafePosterUrl(item.attributes["tvg-logo"].toString())
                val rcChGroup = item.attributes["group-title"].toString()
                val key = item.attributes["key"].toString()
                val keyid = item.attributes["keyid"].toString()

                recommendations.add(newLiveSearchResponse(
                    name = rcChannelName,
                    url = LoadData(rcStreamUrl, rcChannelName, rcPosterUrl, rcChGroup, key, keyid).toJson(),
                    type = TvType.Live
                ) {
                    posterUrl = rcPosterUrl
                })
            }
        }
        Log.d(LOG_TAG, "load: recomendaciones=${recommendations.size}")

        return newLiveStreamLoadResponse(
            name = loadData.title,
            url = url,
            dataUrl = url,
        ) {
            posterUrl = loadData.poster
            recommendations.addAll(recommendations)
            this.recommendations = recommendations
            contentRating = null
            type = TvType.Live
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        Log.d(LOG_TAG, "=== loadLinks: name=$name ===")
        try {
            val loadData = parseJson<LoadData>(data)
            Log.d(LOG_TAG, "loadLinks: title=${loadData.title}, url=${loadData.url.take(80)}")
            if (items[name] == null) {
                Log.w(LOG_TAG, "loadLinks: playlist vacía, recargando...")
                items[name] = IptvPlaylistParser().parseM3U(app.get(mainUrl, headers = headers).text)
                Log.d(LOG_TAG, "loadLinks: playlist recargada, items=${items[name]?.items?.size ?: 0}")
            } else {
                Log.d(LOG_TAG, "loadLinks: playlist disponible, items=${items[name]?.items?.size ?: 0}")
            }
            val item = items[name]!!.items.firstOrNull { it.url == loadData.url }
            if (item == null) {
                Log.e(LOG_TAG, "loadLinks: URL no encontrada en playlist: ${loadData.url.take(80)}")
                return false
            }
            Log.d(LOG_TAG, "loadLinks: item encontrado, headers=${item.headers}")
            if (loadData.url.contains(".mpd")) {
                Log.d(LOG_TAG, "loadLinks: es MPD (DRM)")
                callback.invoke(
                    newDrmExtractorLink(
                        source = loadData.title,
                        name = this.name,
                        url = loadData.url,
                        uuid = UUID.randomUUID(),
                    ) {
                        kid = loadData.keyid.toString().trim()
                        key = loadData.key.toString().trim()
                    }
                )
            } else {
                Log.d(LOG_TAG, "loadLinks: es HLS/MPEGTS, creando ExtractorLink")
                callback.invoke(
                    newExtractorLink(
                        source = loadData.title,
                        name = this.name,
                        url = loadData.url,
                    ) {
                        headers = item.headers
                        referer = item.headers["referrer"] ?: ""
                    }
                )
            }
            Log.d(LOG_TAG, "loadLinks: OK, callback invocado")
            return true
        } catch (e: Exception) {
            Log.e(LOG_TAG, "loadLinks: EXCEPCIÓN: ${e::class.simpleName}: ${e.message}")
            e.printStackTrace()
            return false
        }
    }

    @Suppress("ObjectLiteralToLambda")
    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()

                return chain.proceed(request)
            }
        }
    }
}
