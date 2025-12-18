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
        name.contains("Peliculas", true) -> setOf(TvType.Movie)
        name.contains("Series", true) -> setOf(TvType.TvSeries)
        else -> setOf(TvType.Live)
    }

    private val apiURL = "${mainUrl.removeSuffix("/")}/player_api.php?username=$user&password=$pass"
    private val headers = mapOf("User-Agent" to "IPTVSmarters")

    private suspend fun callApi(action: String): String {
        return app.get("$apiURL&action=$action", headers = headers).body.string()
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val home = mutableListOf<HomePageList>()

        val isMovie = name.contains("Peliculas", true)
        val isSeries = name.contains("Series", true)

        val (actionStream, actionCat, tvType) = when {
            isMovie -> Triple("get_vod_streams", "get_vod_categories", TvType.Movie)
            isSeries -> Triple("get_series", "get_series_categories", TvType.TvSeries)
            else -> Triple("get_live_streams", "get_live_categories", TvType.Live)
        }

        val categories = safeParse<List<Category>>(callApi(actionCat))
        val streams = safeParse<List<Stream>>(callApi(actionStream))

        val grouped = streams.groupBy { it.category_id }

        categories.forEach { cat ->
            val list = grouped[cat.category_id]?.map { it.toSearchResponse(tvType) }
            if (!list.isNullOrEmpty()) {
                home.add(HomePageList(cat.category_name, list))
            }
        }

        return newHomePageResponse(home, hasNext = false)
    }

    override suspend fun load(url: String): LoadResponse {
        val data = parseJson<Data>(url)
        val poster = data.stream_icon

        if (name.contains("Series", true)) {
            val seriesDataRaw = callApi("get_series_info&series_id=${data.series_id}")
            val seriesInfo = parseJson<SeriesInfoResponse>(seriesDataRaw)

            val episodesList = mutableListOf<Episode>()
            seriesInfo.episodes?.forEach { (seasonNum, episodes) ->
                episodes.forEach { ep ->
                    val episodeData = Data(
                        name = ep.title ?: "Episodio ${ep.episode_num}",
                        stream_id = ep.id,
                        stream_type = "series",
                        series_id = data.series_id,
                        container_extension = ep.container_extension
                    ).toJson()

                    episodesList.add(newEpisode(episodeData) {
                        this.name = ep.title
                        this.season = seasonNum.toIntOrNull()
                        this.episode = ep.episode_num?.toIntOrNull()
                        this.posterUrl = ep.info?.movie_image
                    })
                }
            }

            return newTvSeriesLoadResponse(data.name, url, TvType.TvSeries, episodesList) {
                this.posterUrl = poster
                this.plot = seriesInfo.info?.plot
            }
        }

        return if (data.stream_type == "live") {
            newLiveStreamLoadResponse(data.name, url, url) { this.posterUrl = poster }
        } else {
            newMovieLoadResponse(data.name, url, TvType.Movie, url) { this.posterUrl = poster }
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val parsed = parseJson<Data>(data)
        val isLive = parsed.stream_type == "live"
        val isSeries = parsed.stream_type == "series"

        val folder = when {
            isLive -> "live"
            isSeries -> "series"
            else -> "movie"
        }

        val ext = parsed.container_extension ?: if (isLive) "ts" else "mp4"
        val id = parsed.stream_id ?: parsed.series_id
        val finalUrl = "${mainUrl.removeSuffix("/")}/$folder/$user/$pass/$id.$ext"

        callback.invoke(newExtractorLink(parsed.name, this.name, finalUrl) { this.referer = "" })
        return true
    }

    private fun Stream.toSearchResponse(type: TvType) = newLiveSearchResponse(
        name = this.name,
        url = Data(
            name = this.name,
            stream_id = this.stream_id ?: this.series_id ?: 0,
            stream_icon = this.stream_icon,
            stream_type = when(type) {
                TvType.Live -> "live"
                TvType.TvSeries -> "series"
                else -> "movie"
            },
            series_id = this.series_id,
            category_id = this.category_id,
            container_extension = this.container_extension
        ).toJson(),
        type = type
    ) {
        this.posterUrl = this@toSearchResponse.stream_icon }

    private inline fun <reified T> safeParse(json: String): T {
        return try {
            if (json.trim().startsWith("[")) parseJson<T>(json) else parseJson<T>("[]")
        } catch (e: Exception) {
            parseJson<T>("[]")
        }
    }
}