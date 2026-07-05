package com.horis.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import java.net.URLEncoder
import kotlin.random.Random
import okhttp3.Interceptor
import okhttp3.Response

class JioHotstarProvider : MainAPI() {
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime, TvType.AsianDrama)
    override var lang = "en"
    override var mainUrl = "https://net52.cc"
    override var name = "JioHotstar"
    override val hasMainPage = true

    private val ott = "hs"

    private val androidHeaders = mapOf(
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        "Accept-Language" to "en-IN,en-US;q=0.9,en;q=0.8",
        "Cache-Control" to "max-age=0",
        "Connection" to "keep-alive",
        "sec-ch-ua" to "\"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"144\", \"Android WebView\";v=\"144\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Android\"",
        "Sec-Fetch-Dest" to "document",
        "Sec-Fetch-Mode" to "navigate",
        "Sec-Fetch-Site" to "same-origin",
        "Sec-Fetch-User" to "?1",
        "Upgrade-Insecure-Requests" to "1",
        "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/144.0.7559.132 Safari/537.36 /OS.Gatu v3.0",
        "X-Requested-With" to "XMLHttpRequest"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val apiBase = resolveApiUrl()

        val response = app.get(
            "$apiBase/newtv/main.php",
            headers = buildNewTvHeaders(ott, mapOf("Page" to "all", "Recentplay" to "", "Watchlist" to "", "Usertoken" to ""))
        ).parsed<NewTvMainResponse>()

        val imgReferer = response.img_referer ?: apiBase
        val items = response.post.orEmpty().map { category ->
            val ids = category.ids?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }.orEmpty()
            val template = response.imgcdn_v.takeUnless { it.isNullOrBlank() } ?: response.imgcdn_h
            val results = ids.mapNotNull { id ->
                newAnimeSearchResponse("", NewTvId(id).toJson()) {
                    posterUrl = buildPosterUrl(template, id) ?: buildVerticalPosterUrl(id, ott)
                    posterHeaders = mapOf("Referer" to imgReferer)
                }
            }
            HomePageList(category.cate.orEmpty(), results, isHorizontalImages = false)
        }

        return newHomePageResponse(items, hasNext = items.isNotEmpty())
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val apiBase = resolveApiUrl()
        val data = app.get(
            "$apiBase/newtv/search.php?s=${URLEncoder.encode(query, "UTF-8")}",
            headers = buildNewTvHeaders(ott)
        ).parsed<NewTvSearchResponse>()

        val imgReferer = data.img_referer ?: apiBase
        val template = data.detailsimgcdn ?: data.imgcdn

        return data.searchResult.orEmpty().map { item ->
            newAnimeSearchResponse(item.t, NewTvId(item.id).toJson()) {
                posterUrl = buildPosterUrl(template, item.id) ?: buildVerticalPosterUrl(item.id, ott)
                posterHeaders = mapOf("Referer" to imgReferer)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val apiBase = resolveApiUrl()
        val id = parseJson<NewTvId>(url).id

        val rawResponse = retryOnDbError {
            val text = app.get(
                "$apiBase/newtv/post.php?id=$id",
                headers = buildNewTvHeaders(ott, mapOf("Lastep" to "", "Usertoken" to ""))
            ).text
            checkDbError(text)
            text
        }
        val data = JSONParser.parse(rawResponse, NewTvPostResponse::class)

        val title = data.title ?: id
        val playbackId = data.main_id ?: id
        val cast = data.cast?.split(",")?.map { it.trim() }?.map { ActorData(Actor(it)) } ?: emptyList()
        val genre = data.genre?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
        val rating = data.match?.replace("IMDb ", "")
        val runTime = convertRuntimeToMinutes(data.runtime ?: "")
        val isSeries = data.type == "t" || data.episodes?.any { it != null } == true
        val suggest = data.suggest?.map {
            newAnimeSearchResponse("", NewTvId(it.id).toJson()) {
                posterUrl = buildVerticalPosterUrl(it.id, ott)
                posterHeaders = mapOf("Referer" to apiBase)
            }
        }

        if (!isSeries) {
            return newMovieLoadResponse(title, url, TvType.Movie, NewTvLoadData(title, playbackId).toJson()) {
                posterUrl = buildPosterUrl(data.main_poster, id) ?: buildVerticalPosterUrl(id, ott)
                backgroundPosterUrl = buildBackgroundPosterUrl(id, ott)
                posterHeaders = mapOf("Referer" to apiBase)
                plot = data.desc; year = data.year?.toIntOrNull(); tags = genre
                actors = cast; this.score = Score.from10(rating); duration = runTime
                recommendations = suggest
            }
        }

        val episodes = arrayListOf<Episode>()

        if (data.episodes.isNullOrEmpty()) {
            if (data.type != "t") episodes.add(newEpisode(NewTvLoadData(title, playbackId)) { name = title })
        } else {
            val selectedSeasonIdx = data.season?.indexOfFirst { it.selected == true }?.takeIf { it >= 0 }
            val selectedSeasonId = selectedSeasonIdx?.let { data.season?.getOrNull(it)?.id } ?: data.nextPageSeason
            val selectedSeasonNumber = selectedSeasonIdx?.plus(1)

            data.episodes.filterNotNull().mapTo(episodes) {
                newEpisode(NewTvLoadData(title, it.id.orEmpty())) {
                    this.name = it.t
                    episode = it.ep?.toIntOrNull() ?: it.epNum?.replace("E", "").orEmpty().toIntOrNull()
                    season = selectedSeasonNumber ?: it.sNum?.replace("S", "").orEmpty().toIntOrNull()
                    posterUrl = buildPosterUrl(data.ep_poster, it.id.orEmpty()) ?: buildVerticalPosterUrl(it.id.orEmpty(), ott)
                    this.runTime = it.timeVal?.replace("m", "").orEmpty().toIntOrNull()
                    description = it.ep_desc
                }
            }

            if (data.nextPageShow == 1 && !selectedSeasonId.isNullOrBlank())
                episodes.addAll(getEpisodes(title, selectedSeasonId, 2, data.ep_poster, selectedSeasonNumber))

            data.season?.forEachIndexed { index, season ->
                if (season.id != selectedSeasonId && !season.id.isNullOrBlank())
                    episodes.addAll(getEpisodes(title, season.id, 1, data.ep_poster, index + 1))
            }
        }

        if (data.type == "t" && episodes.isEmpty() && !data.season.isNullOrEmpty()) {
            data.season.forEachIndexed { index, season ->
                if (!season.id.isNullOrBlank())
                    episodes.addAll(getEpisodes(title, season.id, 1, data.ep_poster, index + 1))
            }
        }

        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
            posterUrl = buildPosterUrl(data.main_poster, id) ?: buildVerticalPosterUrl(id, ott)
            backgroundPosterUrl = buildBackgroundPosterUrl(id, ott)
            posterHeaders = mapOf("Referer" to apiBase)
            plot = data.desc; year = data.year?.toIntOrNull(); tags = genre
            actors = cast; this.score = Score.from10(rating); duration = runTime
            recommendations = suggest
        }
    }

    private suspend fun getEpisodes(
        title: String, sid: String, page: Int,
        epPoster: String? = null, seasonNumber: Int? = null
    ): List<Episode> {
        val apiBase = resolveApiUrl()
        val episodes = arrayListOf<Episode>()
        var pg = page
        while (true) {
            val rawEp = retryOnDbError {
                val text = app.get(
                    "$apiBase/newtv/episodes.php",
                    params = mapOf("id" to sid, "page" to pg.toString()),
                    headers = buildNewTvHeaders(ott)
                ).text
                checkDbError(text)
                text
            }
            val data = JSONParser.parse(rawEp, NewTvEpisodesResponse::class)

            data.episodes.orEmpty().mapTo(episodes) {
                newEpisode(NewTvLoadData(title, it.id.orEmpty())) {
                    name = it.t
                    episode = it.ep?.toIntOrNull() ?: it.epNum?.replace("E", "").orEmpty().toIntOrNull()
                    season = seasonNumber ?: it.sNum?.replace("S", "").orEmpty().toIntOrNull()
                    posterUrl = buildPosterUrl(epPoster, it.id.orEmpty()) ?: buildVerticalPosterUrl(it.id.orEmpty(), ott)
                    this.runTime = it.timeVal?.replace("m", "").orEmpty().toIntOrNull()
                    description = it.ep_desc
                }
            }

            if (data.nextPageShow != 1) break
            pg++
        }
        return episodes
    }

    override suspend fun loadLinks(
        data: String, isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit
    ): Boolean {
        val apiBase = resolveApiUrl()
        val loadData = parseJson<NewTvLoadData>(data)
        val id = loadData.id
        val title = loadData.title
        val cookie = bypass(mainUrl)
        val videoHeaders = androidHeaders + mapOf("Cookie" to "hd=on; t_hash_t=$cookie")

        // New flow: play.php → playlist.php
        val playlistResult = getPlaylistUrl(mainUrl, ott, id, title, cookie, apiBase)
        if (playlistResult != null) {
            val (m3u8Url, tracks) = playlistResult
            for (track in tracks) {
                if (track.kind == "captions" && !track.file.isNullOrBlank()) {
                    val subUrl = if (track.file.startsWith("http")) track.file
                                 else "https:${track.file.removePrefix("/")}"
                    val subFile = newSubtitleFile(track.label ?: track.language ?: "unknown", subUrl)
                    subFile.headers = mapOf("Referer" to "$mainUrl/")
                    subtitleCallback(subFile)
                }
            }
            Log.d("JioHotstar", "loadLinks new flow SUCCESS: $m3u8Url")
            callback.invoke(newExtractorLink(name, name, m3u8Url, type = ExtractorLinkType.M3U8) {
                this.referer = "$mainUrl/mobile/home?app=1"
                this.headers = videoHeaders
            })
            return true
        }

        // Fallback to old player.php flow
        Log.d("JioHotstar", "loadLinks: fallback to player.php id=$id")
        val rawPlayer = retryOnDbError {
            val text = app.get(
                "$apiBase/newtv/player.php?id=$id",
                headers = buildNewTvHeaders(ott, mapOf("Usertoken" to "", "Referer" to "https://net52.cc"))
            ).text
            checkDbError(text)
            text
        }
        val response = JSONParser.parse(rawPlayer, NewTvPlayerResponse::class)

        if (response.status != "ok" && response.status != "otp" || response.video_link.isNullOrBlank()) return false

        val m3u8Referer = response.referer ?: apiBase
        kotlinx.coroutines.delay(Random.nextLong(1000L, 3000L))
        callback.invoke(newExtractorLink(name, name, response.video_link, type = ExtractorLinkType.M3U8) {
            this.referer = m3u8Referer
            this.headers = videoHeaders
        })
        Log.d("JioHotstar", "loadLinks SUCCESS (player.php fallback): video_link=${response.video_link}")
        return true
    }

    @Suppress("ObjectLiteralToLambda")
    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                if (request.url.toString().contains(".m3u8")) {
                    val newRequest = request.newBuilder()
                        .header("Cookie", "hd=on")
                        .build()
                    return chain.proceed(newRequest)
                }
                return chain.proceed(request)
            }
        }
    }
}
