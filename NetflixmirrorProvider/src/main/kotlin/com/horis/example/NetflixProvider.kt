package com.horis.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import java.net.URLEncoder
import kotlin.random.Random
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class NetflixProvider : MainAPI() {
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override var lang = "en"
    override var mainUrl = "https://net52.cc"
    override var name = "Netflix"
    override val hasMainPage = true
    override val usesWebView = true

    private val ott = "nf"

    private val androidHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/144.0.7559.132 Safari/537.36 /OS.Gatu v3.0",
        "Accept" to "*/*",
        "Connection" to "keep-alive",
        "X-Requested-With" to "app.netmirror.netmirrornew"
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
                    posterUrl = buildVerticalPosterUrl(id, ott)
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
                posterUrl = buildVerticalPosterUrl(item.id, ott)
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
        Log.d("NetflixProvider", "RAW post response: $rawResponse")
        val data = JSONParser.parse(rawResponse, NewTvPostResponse::class)

        val title = data.title ?: id
        val playbackId = data.main_id ?: id
        val cast = data.cast?.split(",")?.map { it.trim() }?.map { ActorData(Actor(it)) } ?: emptyList()
        val genre = data.genre?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
        val languages = data.lang?.map { it.l.orEmpty() }?.filter { it.isNotBlank() }?.distinct()
        val director = data.moredetails?.find { it.k == "Director" }?.v
        val studio = data.moredetails?.find { it.k == "Studio" }?.v
        val writer = data.moredetails?.find { it.k == "Writer" }?.v
        val thisMovieIs = data.moredetails?.find { it.k == "This Movie Is" }?.v
        Log.d("NetflixProvider", "lang=${languages} director=${director} studio=${studio} writer=${writer} thisMovieIs=${thisMovieIs}")
        val imdbFromDetails = data.moredetails?.find { it.k == "IMDB Rating" }?.v
        val rating = when {
            data.match?.startsWith("IMDb ") == true -> data.match?.replace("IMDb ", "")
            data.match?.contains("%") == true -> {
                val pct = data.match?.replace(Regex("[^0-9]"), "")?.toFloatOrNull()
                if (pct != null) String.format("%.1f", pct / 10f) else null
            }
            else -> data.match ?: imdbFromDetails
        }
        val runTime = convertRuntimeToMinutes(data.runtime ?: "")
        val isSeries = data.type == "t" || data.episodes?.any { it != null } == true
        val extraPlot = buildList {
            //if (!director.isNullOrBlank()) add("Director: $director")
            //if (!writer.isNullOrBlank()) add("Writer: $writer")
            if (!studio.isNullOrBlank()) add(" - Studio: $studio")
        }.takeIf { it.isNotEmpty() }?.joinToString("\n")
        val languagesText = if (!languages.isNullOrEmpty()) " -- Audio: ${languages.joinToString(", ")}" else null
        val tags = buildList {
            if (!genre.isNullOrEmpty()) addAll(genre)
            if (!thisMovieIs.isNullOrBlank()) addAll(thisMovieIs.split(",").map { it.trim() }.filter { it.isNotEmpty() })
        }.takeIf { it.isNotEmpty() }
        val fullPlot = buildList {
            data.desc?.let { add(it) }
            languagesText?.let { add(it) }
            extraPlot?.let { add(it) }
        }.takeIf { it.isNotEmpty() }?.joinToString("\n\n")

        val suggest = data.suggest?.map {
            newAnimeSearchResponse("", NewTvId(it.id).toJson()) {
                posterUrl = buildVerticalPosterUrl(it.id, ott)
                posterHeaders = mapOf("Referer" to apiBase)
            }
        }

        if (!isSeries) {
            return newMovieLoadResponse(title, url, TvType.Movie, NewTvLoadData(title, playbackId).toJson()) {
                posterUrl = buildVerticalPosterUrl(id, ott)
                backgroundPosterUrl = buildBackgroundPosterUrl(id, ott)
                posterHeaders = mapOf("Referer" to apiBase)
                plot = fullPlot; year = data.year?.toIntOrNull(); this.tags = tags
                actors = cast; this.score = Score.from10(rating); duration = runTime
                recommendations = suggest
                contentRating = data.ua ?: data.certification ?: data.age
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
                Log.d("NetflixProvider", "episode id=${it.id} t=${it.t} info=${it.info}")
                newEpisode(NewTvLoadData(title, it.id.orEmpty())) {
                    this.name = it.t
                    episode = it.ep?.toIntOrNull() ?: it.epNum?.replace("E", "").orEmpty().toIntOrNull()
                    season = selectedSeasonNumber ?: it.sNum?.replace("S", "").orEmpty().toIntOrNull()
                    posterUrl = buildPosterUrl(data.ep_poster, it.id.orEmpty()) ?: buildVerticalPosterUrl(it.id.orEmpty(), ott)
                    this.runTime = it.info?.getOrNull(2)?.replace("m", "")?.toIntOrNull()
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
            posterUrl = buildVerticalPosterUrl(id, ott)
            backgroundPosterUrl = buildBackgroundPosterUrl(id, ott)
            posterHeaders = mapOf("Referer" to apiBase)
            plot = fullPlot; year = data.year?.toIntOrNull(); this.tags = tags
            actors = cast; this.score = Score.from10(rating); duration = runTime
            recommendations = suggest
            contentRating = data.ua ?: data.certification ?: data.age
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
            val rawEp = app.get(
                "$apiBase/newtv/episodes.php",
                params = mapOf("id" to sid, "page" to pg.toString()),
                headers = buildNewTvHeaders(ott)
            ).text
            Log.d("NetflixProvider", "RAW episodes page=$pg: $rawEp")
            val data = JSONParser.parse(rawEp, NewTvEpisodesResponse::class)

            data.episodes.orEmpty().mapTo(episodes) {
                Log.d("NetflixProvider", "getEpisodes id=${it.id} t=${it.t} info=${it.info}")
                newEpisode(NewTvLoadData(title, it.id.orEmpty())) {
                    name = it.t
                    episode = it.ep?.toIntOrNull() ?: it.epNum?.replace("E", "").orEmpty().toIntOrNull()
                    season = seasonNumber ?: it.sNum?.replace("S", "").orEmpty().toIntOrNull()
                    posterUrl = buildPosterUrl(epPoster, it.id.orEmpty()) ?: buildVerticalPosterUrl(it.id.orEmpty(), ott)
                    this.runTime = it.info?.getOrNull(2)?.replace("m", "")?.toIntOrNull()
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
        // Force fresh bypass to see verify.php response body
        NetflixMirrorStorage.clearCookie()
        val cookie = bypass(mainUrl)

        // Probe: various jjoii values
        val uHeaders = androidHeaders + mapOf("Cookie" to cookie, "Referer" to "$mainUrl/", "Origin" to mainUrl)
        // verify.php response body
        try {
            val vResp = app.post("$mainUrl/verify.php", headers = mapOf("Origin" to "https://net22.cc", "Referer" to "https://net22.cc/verify2"),
                requestBody = FormBody.Builder().add("g-recaptcha-response", "test").build())
            Log.d("NetflixProvider", "verify body: ${vResp.text.take(300)}")
        } catch (e: Exception) {
            Log.d("NetflixProvider", "verify probe failed: ${e.message}")
        }
        // userver with jjoii=episode_id
        try {
            val uResp = app.post("https://userver.net52.cc/?jjoii=$id", headers = uHeaders)
            Log.d("NetflixProvider", "userver(ep_id) code=${uResp.code} body=${uResp.text.take(300)}")
        } catch (e: Exception) {
            Log.d("NetflixProvider", "userver(ep_id) failed: ${e.message}")
        }
        // userver with jjoii=t_hash_t token (URL decoded)
        val tHashValue = cookie.split(";").firstOrNull { it.trim().startsWith("t_hash_t=") }?.substringAfter("=")?.trim()
        if (tHashValue != null) {
            val decoded = java.net.URLDecoder.decode(tHashValue, "UTF-8")
            try {
                val uResp = app.post("https://userver.net52.cc/?jjoii=$tHashValue", headers = uHeaders)
                Log.d("NetflixProvider", "userver(t_hash_raw) code=${uResp.code} body=${uResp.text.take(300)}")
            } catch (e: Exception) { Log.d("NetflixProvider", "userver(t_hash_raw) failed: ${e.message}") }
            try {
                val uResp = app.post("https://userver.net52.cc/?jjoii=$decoded", headers = uHeaders)
                Log.d("NetflixProvider", "userver(t_hash_dec) code=${uResp.code} body=${uResp.text.take(300)}")
            } catch (e: Exception) { Log.d("NetflixProvider", "userver(t_hash_dec) failed: ${e.message}") }
            val firstPart = decoded.split("::").first()
            try {
                val uResp = app.post("https://userver.net52.cc/?jjoii=$firstPart", headers = uHeaders)
                Log.d("NetflixProvider", "userver(t_hash_first) code=${uResp.code} body=${uResp.text.take(300)}")
            } catch (e: Exception) { Log.d("NetflixProvider", "userver(t_hash_first) failed: ${e.message}") }
            try {
                val rb = okhttp3.RequestBody.create("application/json".toMediaTypeOrNull()!!, """{"id":"$id","ott":"$ott","t_hash":"$decoded"}""")
                val uResp = app.post("https://userver.net52.cc/", headers = uHeaders, requestBody = rb)
                Log.d("NetflixProvider", "userver(empty+t_hash) code=${uResp.code} body=${uResp.text.take(300)}")
            } catch (e: Exception) { Log.d("NetflixProvider", "userver(empty+t_hash) failed: ${e.message}") }
        }
        // userver with base64(id) as jjoii
        try {
            val b64 = android.util.Base64.encodeToString(id.toByteArray(), android.util.Base64.NO_WRAP)
            val uResp = app.post("https://userver.net52.cc/?jjoii=$b64", headers = uHeaders)
            Log.d("NetflixProvider", "userver(b64) code=${uResp.code} body=${uResp.text.take(300)}")
        } catch (e: Exception) { Log.d("NetflixProvider", "userver(b64) failed: ${e.message}") }

        // New flow: mobile/hls/ID.m3u8 with t_hash_t as in param
        val tHashValue2 = cookie.split(";").firstOrNull { it.trim().startsWith("t_hash_t=") }?.substringAfter("=")?.trim()
        if (tHashValue2 != null) {
            val decodedHash = java.net.URLDecoder.decode(tHashValue2, "UTF-8")
            val hlsUrl = "$mainUrl/mobile/hls/$id.m3u8?in=$decodedHash&hd=on&lang=eng"
            Log.d("NetflixProvider", "Trying mobile/hls: $hlsUrl")
            try {
                val resp = app.get(hlsUrl, headers = androidHeaders + mapOf(
                    "Cookie" to cookie,
                    "Referer" to "$mainUrl/mobile/home?app=1",
                    "Origin" to mainUrl
                ))
                val body = resp.text
                Log.d("NetflixProvider", "mobile/hls FULL response:\n$body")
                // Parse video URL from master playlist (prefer 720p)
                val videoUrl = Regex("https://[^\n\r]+720p[^\n\r]*\\.m3u8[^\n\r]*").find(body)?.value
                    ?: Regex("https://[^\n\r]+480p[^\n\r]*\\.m3u8[^\n\r]*").find(body)?.value
                if (videoUrl != null) {
                    Log.d("NetflixProvider", "Video URL found: $videoUrl")
                    val videoHeaders = androidHeaders + mapOf(
                        "Cookie" to cookie,
                        "Referer" to "$mainUrl/mobile/home?app=1"
                    )
                    callback.invoke(newExtractorLink(name, name, videoUrl, type = ExtractorLinkType.M3U8) {
                        this.headers = videoHeaders
                    })
                    return true
                }
                // Fallback: pass master URL directly
                Log.d("NetflixProvider", "No video URL found, using master: $hlsUrl")
                val masterHeaders = androidHeaders + mapOf(
                    "Cookie" to cookie,
                    "Referer" to "$mainUrl/mobile/home?app=1"
                )
                callback.invoke(newExtractorLink(name, name, hlsUrl, type = ExtractorLinkType.M3U8) {
                    this.headers = masterHeaders
                })
                return true
            } catch (e: Exception) {
                Log.d("NetflixProvider", "mobile/hls failed: ${e.message}")
            }
        }

        // Fallback: play.php → playlist.php
        val playlistResult = getPlaylistUrl(mainUrl, ott, id, title, cookie, apiBase)
        if (playlistResult != null) {
            val (m3u8Url, tracks) = playlistResult
            for (track in tracks) {
                if (track.kind == "captions" && !track.file.isNullOrBlank()) {
                    val subUrl = if (track.file.startsWith("http")) track.file
                                 else "https:${track.file.removePrefix("/")}"
                    val subFile = newSubtitleFile(track.label ?: track.language ?: "unknown", subUrl)
                    subFile.headers = mapOf("Referer" to m3u8Url)
                    subtitleCallback(subFile)
                }
            }
            val m3u8Domain = Regex("https://([^/]+)/").find(m3u8Url)?.groupValues?.get(1) ?: mainUrl
            val videoHeaders = androidHeaders + mapOf(
                "Cookie" to cookie,
                "Referer" to m3u8Url,
                "Origin" to "https://$m3u8Domain"
            )
            Log.d("NetflixProvider", "loadLinks new flow SUCCESS: $m3u8Url")
            callback.invoke(newExtractorLink(name, name, m3u8Url, type = ExtractorLinkType.M3U8) {
                this.headers = videoHeaders
            })
            return true
        }

        // Fallback to old player.php flow
        Log.d("NetflixProvider", "loadLinks: fallback to player.php id=$id")
        val rawPlayer = retryOnDbError {
            val text = app.get(
                "$apiBase/newtv/player.php?id=$id",
                headers = buildNewTvHeaders(ott, mapOf("Usertoken" to "", "Referer" to "https://net52.cc"))
            ).text
            checkDbError(text)
            text
        }
        Log.d("NetflixProvider", "loadLinks RAW player response: $rawPlayer")
        val response = JSONParser.parse(rawPlayer, NewTvPlayerResponse::class)

        Log.d("NetflixProvider", "loadLinks parsed: status=${response.status}, video_link=${response.video_link}, referer=${response.referer}")
        if (response.status != "ok" && response.status != "otp" || response.video_link.isNullOrBlank()) {
            Log.e("NetflixProvider", "loadLinks FAILED: status=${response.status} video_link=${response.video_link}")
            return false
        }

        val m3u8Referer = response.referer ?: apiBase
        val videoHeaders = androidHeaders + mapOf("Cookie" to cookie, "Referer" to m3u8Referer)
        kotlinx.coroutines.delay((1000L..3000L).random())
        Log.e("PLAYURL", response.video_link)
        callback.invoke(newExtractorLink(name, name, response.video_link, type = ExtractorLinkType.M3U8) {
            this.referer = m3u8Referer
            this.headers = videoHeaders
        })
        Log.d("NetflixProvider", "loadLinks SUCCESS (player.php fallback): video_link=${response.video_link}")
        return true
    }

    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        return null
    }
}