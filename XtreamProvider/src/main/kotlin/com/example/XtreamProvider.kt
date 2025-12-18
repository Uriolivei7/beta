package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.toJson

class XtreamProvider(val host: String, override var name: String, val user: String, val pass: String) : MainAPI() {
    override var mainUrl = host
    override val hasMainPage = true
    override var lang = "mx"
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Movie, TvType.Live)

    private val apiURL = "${mainUrl.removeSuffix("/")}/player_api.php?username=$user&password=$pass"
    private val headers = mapOf("User-Agent" to "IPTVSmarters")
    private var searchCache = mutableListOf<Stream>()

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val home = mutableListOf<HomePageList>()

        val liveCats = safeParse<List<Category>>(app.get("$apiURL&action=get_live_categories", headers = headers).text)
        val liveStreams = safeParse<List<Stream>>(app.get("$apiURL&action=get_live_streams", headers = headers).text)
        val vodCats = safeParse<List<Category>>(app.get("$apiURL&action=get_vod_categories", headers = headers).text)
        val vodStreams = safeParse<List<Stream>>(app.get("$apiURL&action=get_vod_streams", headers = headers).text)

        searchCache.clear()
        searchCache.addAll(liveStreams)
        searchCache.addAll(vodStreams)

        val liveGrouped = liveStreams.groupBy { it.category_id }
        liveCats.take(15).forEach { cat ->
            val list = liveGrouped[cat.category_id]?.map { it.toSearchResponse(TvType.Live) }
            if (!list.isNullOrEmpty()) home.add(HomePageList(cat.category_name, list))
        }

        val vodGrouped = vodStreams.groupBy { it.category_id }
        vodCats.take(15).forEach { cat ->
            val list = vodGrouped[cat.category_id]?.map { it.toSearchResponse(TvType.Movie) }
            if (!list.isNullOrEmpty()) home.add(HomePageList("Cine: ${cat.category_name}", list))
        }

        return newHomePageResponse(home, hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        if (searchCache.isEmpty()) {
            searchCache.addAll(safeParse<List<Stream>>(app.get("$apiURL&action=get_live_streams", headers = headers).text))
            searchCache.addAll(safeParse<List<Stream>>(app.get("$apiURL&action=get_vod_streams", headers = headers).text))
        }
        return searchCache.filter { it.name.contains(query, ignoreCase = true) }.map {
            it.toSearchResponse(if (it.stream_type == "live") TvType.Live else TvType.Movie)
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val data = parseJson<Data>(url)
        val isLive = data.stream_type == "live"

        return if (isLive) {
            // CORRECCIÓN: name, url, dataUrl (como String)
            newLiveStreamLoadResponse(data.name, url, url) {
                this.posterUrl = data.stream_icon
            }
        } else {
            // CORRECCIÓN: name, url, type, dataUrl
            newMovieLoadResponse(data.name, url, TvType.Movie, url) {
                this.posterUrl = data.stream_icon
            }
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val parsed = parseJson<Data>(data)
        val folder = if (parsed.stream_type == "live") "live" else "movie"
        val ext = if (parsed.stream_type == "live") "ts" else "mp4"
        val finalUrl = "${mainUrl.removeSuffix("/")}/$folder/$user/$pass/${parsed.stream_id}.$ext"

        callback.invoke(
            newExtractorLink(
                parsed.name,
                this.name,
                finalUrl
            ) {
                this.referer = ""
            }
        )
        return true
    }

    private fun Stream.toSearchResponse(type: TvType) = newLiveSearchResponse(
        name = this.name,
        url = Data(name=name, stream_id=stream_id, stream_icon=stream_icon, stream_type=stream_type).toJson(),
        type = type
    ) { this.posterUrl = this@toSearchResponse.stream_icon }

    private inline fun <reified T> safeParse(json: String): T {
        return try {
            if (json.trim().startsWith("[")) parseJson<T>(json) else parseJson<T>("[]")
        } catch (e: Exception) {
            parseJson<T>("[]")
        }
    }
}