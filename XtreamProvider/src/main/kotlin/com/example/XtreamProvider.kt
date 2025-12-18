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

    override val supportedTypes = when {
        name.contains("Peliculas", ignoreCase = true) -> setOf(TvType.Movie)
        name.contains("Series", ignoreCase = true) -> setOf(TvType.TvSeries)
        else -> setOf(TvType.Live)
    }

    private val apiURL = "${mainUrl.removeSuffix("/")}/player_api.php?username=$user&password=$pass"
    private val headers = mapOf("User-Agent" to "IPTVSmarters")
    private var searchCache = mutableListOf<Stream>()

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val home = mutableListOf<HomePageList>()

        val (actionStream, actionCat, tvType) = when {
            name.contains("Peliculas", ignoreCase = true) -> Triple("get_vod_streams", "get_vod_categories", TvType.Movie)
            name.contains("Series", ignoreCase = true) -> Triple("get_series", "get_series_categories", TvType.TvSeries)
            else -> Triple("get_live_streams", "get_live_categories", TvType.Live)
        }

        val catsRaw = app.get("$apiURL&action=$actionCat", headers = headers).text
        val streamsRaw = app.get("$apiURL&action=$actionStream", headers = headers).text

        val categories = safeParse<List<Category>>(catsRaw)
        val streams = safeParse<List<Stream>>(streamsRaw)

        searchCache.clear()
        searchCache.addAll(streams)

        val grouped = streams.groupBy { it.category_id }

        categories.forEach { cat ->
            val list = grouped[cat.category_id]?.map { it.toSearchResponse(tvType) }
            if (!list.isNullOrEmpty()) {
                home.add(HomePageList(cat.category_name, list))
            }
        }

        return newHomePageResponse(home, hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val tvType = when {
            name.contains("Peliculas", ignoreCase = true) -> TvType.Movie
            name.contains("Series", ignoreCase = true) -> TvType.TvSeries
            else -> TvType.Live
        }

        return searchCache.filter { it.name.contains(query, ignoreCase = true) }.map {
            it.toSearchResponse(tvType)
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val data = parseJson<Data>(url)
        val isLive = data.stream_type == "live" || name.contains("Vivo", true)

        return if (isLive) {
            newLiveStreamLoadResponse(data.name, url, url) {
                this.posterUrl = data.stream_icon
            }
        } else {
            newMovieLoadResponse(data.name, url, TvType.Movie, url) {
                this.posterUrl = data.stream_icon
            }
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val parsed = parseJson<Data>(data)

        val isMovieOrSeries = name.contains("Peliculas", true) || name.contains("Series", true)
        val folder = if (isMovieOrSeries) "movie" else "live"
        val ext = if (isMovieOrSeries) "mp4" else "ts"

        val finalUrl = "${mainUrl.removeSuffix("/")}/$folder/$user/$pass/${parsed.stream_id}.$ext"

        callback.invoke(
            newExtractorLink(parsed.name, this.name, finalUrl) {
                this.referer = ""
            }
        )
        return true
    }

    private fun Stream.toSearchResponse(type: TvType) = newLiveSearchResponse(
        name = this.name,
        url = Data(
            name = name,
            stream_id = stream_id,
            stream_icon = stream_icon,
            stream_type = stream_type ?: if (type == TvType.Live) "live" else "movie",
            category_id = category_id
        ).toJson(),
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