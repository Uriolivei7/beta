package com.horis.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import java.net.URLEncoder
import okhttp3.Interceptor

class JioHotstarProvider : MainAPI() {
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime, TvType.AsianDrama)
    override var lang = "en"
    override var mainUrl = "https://net52.cc"
    override var name = "JioHotstar"
    override val hasMainPage = true

    private val ott = "hs"

    private val androidHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
        "Accept" to "*/*",
        "Accept-Language" to "en-IN,en-US;q=0.9,en;q=0.8",
        "Connection" to "keep-alive",
        "X-Requested-With" to "app.netmirror.netmirrornew",
        "sec-ch-ua" to "\"Android WebView\";v=\"149\", \"Chromium\";v=\"149\", \"Not)A;Brand\";v=\"24\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Android\"",
        "Cache-Control" to "max-age=0"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        for (base in listOf(apiBase, mainUrl).distinct()) {
            try {
                val response = app.get(
                    "$base/newtv/main.php",
                    headers = buildNewTvHeaders(ott, mapOf("Page" to "all", "Recentplay" to "", "Watchlist" to "", "Usertoken" to ""))
                ).parsed<NewTvMainResponse>()
                val items = response.post.orEmpty().map { category ->
                    val ids = category.ids?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }.orEmpty()
                    val template = response.imgcdn_v.takeUnless { it.isNullOrBlank() } ?: response.imgcdn_h
                    val results = ids.mapNotNull { id ->
                        newAnimeSearchResponse("", NewTvId(id).toJson()) {
                            posterUrl = buildPosterUrl(template, id) ?: buildVerticalPosterUrl(id, ott)
                        }
                    }
                    HomePageList(category.cate.orEmpty(), results, isHorizontalImages = false)
                }
                return newHomePageResponse(items, hasNext = items.isNotEmpty())
            } catch (_: Exception) {}
        }
        return null
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        for (base in listOf(apiBase, mainUrl).distinct()) {
            try {
                val data = app.get(
                    "$base/newtv/search.php?s=${URLEncoder.encode(query, "UTF-8")}",
                    headers = buildNewTvHeaders(ott)
                ).parsed<NewTvSearchResponse>()
                val template = data.detailsimgcdn ?: data.imgcdn
                return data.searchResult.orEmpty().map { item ->
                    newAnimeSearchResponse(item.t, NewTvId(item.id).toJson()) {
                        posterUrl = buildPosterUrl(template, item.id) ?: buildVerticalPosterUrl(item.id, ott)
                    }
                }
            } catch (_: Exception) {}
        }
        return emptyList()
    }

    override suspend fun load(url: String): LoadResponse? {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
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
            }
        }

        if (!isSeries) {
            return newMovieLoadResponse(title, url, TvType.Movie, NewTvLoadData(title, playbackId).toJson()) {
                posterUrl = buildPosterUrl(data.main_poster, id) ?: buildVerticalPosterUrl(id, ott)
                backgroundPosterUrl = buildBackgroundPosterUrl(id, ott)
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
            plot = data.desc; year = data.year?.toIntOrNull(); tags = genre
            actors = cast; this.score = Score.from10(rating); duration = runTime
            recommendations = suggest
        }
    }

    private suspend fun getEpisodes(
        title: String, sid: String, page: Int,
        epPoster: String? = null, seasonNumber: Int? = null
    ): List<Episode> {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
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
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        val loadData = parseJson<NewTvLoadData>(data)
        val id = loadData.id
        Log.d("JioHotstar", "loadLinks id=$id apiBase=$apiBase")

        // 1. Get bypass cookie (t_hash_t)
        val cookie = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (cookie.length <= 10) {
            Log.d("JioHotstar", "bypass failed")
            return false
        }
        currentBypassToken = cookie
        Log.d("JioHotstar", "Bypass cookie: ${cookie.take(60)}")

        // 2. Get user token (OTP-based auth)
        val userToken = try { getNewTvUserToken(apiBase, ott) } catch (_: Exception) { "" }
        Log.d("JioHotstar", "User token: ${userToken.take(60)}")

        val inParam = when {
            userToken.length > 10 -> userToken
            else -> cookie
        }

        val masterHeaders = buildNewTvHeaders(ott, mapOf(
            "Referer" to "$mainUrl/mobile/home?app=1"
        ))
        val cookieHeader = mapOf("Cookie" to "t_hash_t=$cookie; hd=on; ott=$ott")

        // 3. Fetch mobile/hls master playlist
        val masterUrl = "$mainUrl/mobile/hls/$id.m3u8?q=720p&in=$inParam&hd=on&lang=eng"
        Log.d("JioHotstar", "Fetching master: $masterUrl")
        val masterResp = app.get(masterUrl, headers = masterHeaders + cookieHeader)
        val masterText = masterResp.text
        Log.d("JioHotstar", "mobile/hls status=${masterResp.code} body=${masterText.take(500)}")

        if (!masterText.startsWith("#EXT")) {
            Log.d("JioHotstar", "Invalid master response")
            return false
        }

        // 4. Parse master → ExtractorLinks
        val lines = masterText.lines().map { it.trimEnd() }
        var foundAny = false
        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            when {
                line.contains("#EXT-X-MEDIA:TYPE=AUDIO") -> {
                    val next = lines.getOrNull(i + 1) ?: ""
                    if (next.startsWith("http")) {
                        callback(newExtractorLink(name, name, next, type = ExtractorLinkType.M3U8) {
                            headers = masterHeaders + cookieHeader
                            referer = "$mainUrl/mobile/home?app=1"
                        })
                        foundAny = true; i += 2; continue
                    }
                }
                line.contains("#EXT-X-MEDIA:TYPE=SUBTITLES") -> {
                    val uri = Regex("""URI="([^"]+)"""").find(line)?.groupValues?.get(1)
                    if (uri != null) {
                        val lang = Regex("""LANGUAGE="([^"]+)"""").find(line)?.groupValues?.get(1) ?: "Unknown"
                        subtitleCallback(newSubtitleFile(lang, if (uri.startsWith("http")) uri else "$mainUrl$uri"))
                    }
                }
                line.startsWith("#EXT-X-STREAM-INF:") && i + 1 < lines.size -> {
                    i++; val urlLine = lines[i]
                    val bw = Regex("""BANDWIDTH=(\d+)""").find(line)?.groupValues?.get(1)?.toIntOrNull()
                    val quality = when { bw != null && bw >= 5_000_000 -> getQualityFromName("1080p")
                        bw != null && bw >= 2_000_000 -> getQualityFromName("720p")
                        bw != null && bw >= 800_000 -> getQualityFromName("480p")
                        else -> getQualityFromName("360p") }
                    val videoUrl = urlLine.replace(Regex("https://[^/]+"), "https://s23.nm-cdn9.top")
                        .replace(Regex("[?&]in=[^&\n\r]*"), "")
                    callback(newExtractorLink(name, "$quality", videoUrl, type = ExtractorLinkType.M3U8) {
                        headers = masterHeaders + mapOf("Cookie" to "t_hash_t=$cookie; hd=on")
                        referer = "$mainUrl/mobile/home?app=1"; this.quality = quality
                    })
                    foundAny = true; i++; continue
                }
            }
            i++
        }

        // 5. Fallback: player.php
        if (!foundAny) {
            for (u in listOf("$apiBase/newtv/player.php?id=$id", "$mainUrl/newtv/player.php?id=$id")) {
                try {
                    val resp = tryParseJson<NewTvPlayerResponse>(app.get(u, headers = masterHeaders + cookieHeader).text)
                    if (resp?.video_link != null && (resp.status == "ok" || resp.status == "otp")) {
                        callback(newExtractorLink(name, name, resp.video_link, type = ExtractorLinkType.M3U8) {
                            headers = masterHeaders + cookieHeader; referer = resp.referer ?: apiBase
                        })
                        foundAny = true
                    }
                } catch (_: Exception) {}
            }
        }

        Log.d("JioHotstar", "loadLinks result=$foundAny id=$id")
        return foundAny
    }

    @Suppress("ObjectLiteralToLambda")
    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        return m3u8CdnFixInterceptor()
    }
}
