package com.horis.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import java.net.URLEncoder
import okhttp3.FormBody
import okhttp3.Interceptor

class  NetflixProvider : MainAPI() {
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override var lang = "en"
    override var mainUrl = "https://net52.cc"
    override var name = "Netflix"
    override val hasMainPage = true

    private val ott = "nf"

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
                    val results = ids.mapNotNull { id ->
                        newAnimeSearchResponse("", NewTvId(id).toJson()) {
                            posterUrl = buildVerticalPosterUrl(id, ott)
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
                return data.searchResult.orEmpty().map { item ->
                    newAnimeSearchResponse(item.t, NewTvId(item.id).toJson()) {
                        posterUrl = buildVerticalPosterUrl(item.id, ott)
                    }
                }
            } catch (_: Exception) {}
        }
        return emptyList()
    }

    override suspend fun load(url: String): LoadResponse? {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        val id = parseJson<NewTvId>(url).id

        val rawResponse = app.get(
            "$apiBase/newtv/post.php?id=$id",
            headers = buildNewTvHeaders(ott, mapOf("Lastep" to "", "Usertoken" to ""))
        ).text
        Log.d("NetflixProvider", "RAW post response: $rawResponse")
        val data = JSONParser.parse(rawResponse, NewTvPostResponse::class)

        val title = data.title ?: id
        val playbackId = data.main_id ?: id
        val cast = data.cast?.split(",")?.map { it.trim() }?.map { ActorData(Actor(it)) } ?: emptyList()
        val genre = data.genre?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
        val languages = data.lang?.map { it.l.orEmpty() }?.filter { it.isNotBlank() }?.distinct()
        val rating = when {
            data.match?.startsWith("IMDb ") == true -> data.match?.replace("IMDb ", "")
            data.match?.contains("%") == true -> {
                val pct = data.match?.replace(Regex("[^0-9]"), "")?.toFloatOrNull()
                if (pct != null) String.format("%.1f", pct / 10f) else null
            }
            else -> data.match
        }
        val runTime = convertRuntimeToMinutes(data.runtime ?: "")
        val isSeries = data.type == "t" || data.episodes?.any { it != null } == true
        val tags = buildList {
            if (!genre.isNullOrEmpty()) addAll(genre)
        }.takeIf { it.isNotEmpty() }

        val suggest = data.suggest?.map {
            newAnimeSearchResponse("", NewTvId(it.id).toJson()) {
                posterUrl = buildVerticalPosterUrl(it.id, ott)
            }
        }

        if (!isSeries) {
            return newMovieLoadResponse(title, url, TvType.Movie, NewTvLoadData(title, playbackId).toJson()) {
                posterUrl = buildVerticalPosterUrl(id, ott)
                backgroundPosterUrl = buildBackgroundPosterUrl(id, ott)
                plot = data.desc; year = data.year?.toIntOrNull(); this.tags = tags
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

            data.episodes.filterNotNull().mapTo(episodes) {
                newEpisode(NewTvLoadData(title, it.id.orEmpty())) {
                    name = it.t
                    episode = it.ep?.toIntOrNull() ?: it.epNum?.replace("E", "").orEmpty().toIntOrNull()
                    season = (selectedSeasonIdx ?: 0) + 1
                    posterUrl = buildPosterUrl(data.ep_poster, it.id.orEmpty()) ?: buildVerticalPosterUrl(it.id.orEmpty(), ott)
                    this.runTime = it.info?.getOrNull(2)?.replace("m", "")?.toIntOrNull()
                    description = it.ep_desc
                }
            }

            if (data.nextPageShow == 1 && !selectedSeasonId.isNullOrBlank())
                episodes.addAll(getEpisodes(title, selectedSeasonId, 2, data.ep_poster))

            data.season?.forEach { season ->
                if (season.id != selectedSeasonId && !season.id.isNullOrBlank())
                    episodes.addAll(getEpisodes(title, season.id, 1, data.ep_poster))
            }
        }

        if (data.type == "t" && episodes.isEmpty() && !data.season.isNullOrEmpty()) {
            data.season.forEach { season ->
                if (!season.id.isNullOrBlank())
                    episodes.addAll(getEpisodes(title, season.id, 1, data.ep_poster))
            }
        }

        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
            posterUrl = buildVerticalPosterUrl(id, ott)
            backgroundPosterUrl = buildBackgroundPosterUrl(id, ott)
            plot = data.desc; year = data.year?.toIntOrNull(); this.tags = tags
            actors = cast; this.score = Score.from10(rating); duration = runTime
            recommendations = suggest
            contentRating = data.ua ?: data.certification ?: data.age
        }
    }

    private suspend fun getEpisodes(
        title: String, sid: String, page: Int, epPoster: String? = null
    ): List<Episode> {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
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
                newEpisode(NewTvLoadData(title, it.id.orEmpty())) {
                    name = it.t
                    episode = it.ep?.toIntOrNull() ?: it.epNum?.replace("E", "").orEmpty().toIntOrNull()
                    season = it.sNum?.replace("S", "").orEmpty().toIntOrNull()
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
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        val loadData = parseJson<NewTvLoadData>(data)
        val id = loadData.id
        Log.e("NF", "loadLinks id=$id apiBase=$apiBase")

        val token = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (token.length > 10) {
            Log.e("NF", "Got bypass token: ${token.take(60)}")
            currentBypassToken = token
        } else {
            Log.e("NF", "bypass failed, trying play hash")
            val playHash = getPlayHash(id)
            if (playHash.isNotBlank()) {
                Log.e("NF", "Got play hash: ${playHash.take(60)}")
                currentBypassToken = playHash
            }
        }

        val cookieRaw = if (currentBypassToken.length > 10) currentBypassToken else ""
        val cookieEscaped = if (cookieRaw.length > 10) URLEncoder.encode(cookieRaw, "UTF-8") else ""
        val cookieHeader = if (cookieEscaped.length > 10) mapOf("Cookie" to "t_hash_t=$cookieEscaped; ott=nf; hd=on") else emptyMap()

        // ---- PRIMARY: mobile/hls -> s23.nm-cdn9.top (full content JPG frames) ----
        if (cookieRaw.length > 10) {
            try {
                val inParam = cookieRaw.substringBefore("::ep") + "::ep::99"
                val mobileHeaders = mapOf(
                    "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
                    "X-Requested-With" to "app.netmirror.netmirrornew",
                    "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
                    "sec-ch-ua" to "\"Android WebView\";v=\"149\", \"Chromium\";v=\"149\", \"Not)A;Brand\";v=\"24\"",
                    "sec-ch-ua-mobile" to "?0",
                    "sec-ch-ua-platform" to "\"Android\"",
                    "Sec-Fetch-Dest" to "empty",
                    "Sec-Fetch-Mode" to "cors",
                    "Sec-Fetch-Site" to "same-origin",
                    "Referer" to "$mainUrl/mobile/home?app=1"
                ) + cookieHeader

                val mobileUrl = "$mainUrl/mobile/hls/$id.m3u8?q=720p&in=$inParam&hd=on&lang=eng"
                val mobileResp = app.get(mobileUrl, headers = mobileHeaders).text
                Log.e("NF", "mobile/hls raw=${mobileResp.take(500)}")

                val inMatch = Regex("""https://s21\.freecdn4\.top/files/\d+/\w+/[\w.]+\.m3u8\?in=([^&\s]+)""").find(mobileResp)
                if (inMatch != null) {
                    val rewrittenToken = inMatch.groupValues[1]
                    Log.e("NF", "Rewritten token from server: $rewrittenToken")

                    val s23Headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
                        "X-Requested-With" to "app.netmirror.netmirrornew",
                        "Referer" to "$mainUrl/mobile/home?app=1",
                        "Cookie" to "hd=on; t_hash_t=$cookieEscaped"
                    )

                    for (q in listOf("1080p", "720p")) {
                        val s23Url = "https://s23.nm-cdn9.top/files/$id/$q/$q.m3u8?in=$rewrittenToken"
                        callback.invoke(newExtractorLink(name, name, s23Url, type = ExtractorLinkType.M3U8) {
                            this.headers = s23Headers
                            this.referer = "$mainUrl/mobile/home?app=1"
                            this.quality = getQualityFromName(q)
                        })
                    }
                    return true
                }
            } catch (e: Exception) {
                Log.e("NF", "mobile/hls s23 failed: ${e.message}")
            }
        }

        // ---- FALLBACK 1: playlist.php (cncverse API) ----
        val playlistHeaders = buildNewTvHeaders(ott, mapOf("Referer" to mainUrl)) + cookieHeader
        val playlistUrls = listOf("$mainUrl/newtv/playlist.php?id=$id", "$apiBase/newtv/playlist.php?id=$id",
            "$mainUrl/playlist.php?id=$id")
        val hlsBases = listOf(mainUrl.trimEnd('/'))
        for (plUrl in playlistUrls) {
            try {
                val plRaw = app.get(plUrl, headers = playlistHeaders).text
                Log.e("NF", "playlist raw=${plRaw.take(500)}")
                val items = tryParseJsonList<PlaylistItem>(plRaw)
                if (!items.isNullOrEmpty()) {
                    var count = 0
                    for (item in items) {
                        for (source in item.sources.orEmpty()) {
                            var file = source.file ?: continue
                            val quality = getQualityFromName(file.substringAfter("q=", "").substringBefore("&"))
                            val referer = "$mainUrl/mobile/home?app=1"
                            if (cookieRaw.length > 10 && file.contains("in=unknown::ep")) {
                                file = file.replace("in=unknown::ep", "in=${cookieRaw.substringBefore("::ep")}::ep")
                            }
                            if (file.startsWith("http")) {
                                callback.invoke(newExtractorLink(name, name, file, type = ExtractorLinkType.M3U8) {
                                    this.headers = playlistHeaders; this.referer = referer; this.quality = quality
                                }); count++
                            } else {
                                for (base in hlsBases) {
                                    callback.invoke(newExtractorLink(name, name, "$base$file", type = ExtractorLinkType.M3U8) {
                                        this.headers = playlistHeaders; this.referer = referer; this.quality = quality
                                    }); count++
                                }
                            }
                        }
                        for (track in item.tracks.orEmpty()) {
                            val trackFile = track.file ?: continue
                            if (trackFile.startsWith("http")) {
                                subtitleCallback.invoke(newSubtitleFile(track.label ?: "Unknown", trackFile) {
                                    headers = mapOf("Referer" to "$mainUrl/")
                                })
                            } else {
                                for (base in hlsBases) {
                                    subtitleCallback.invoke(newSubtitleFile(track.label ?: "Unknown", "$base$trackFile") {
                                        headers = mapOf("Referer" to "$mainUrl/")
                                    })
                                }
                            }
                        }
                    }
                    Log.e("NF", "playlist $plUrl returned $count sources")
                    if (count > 0) return true
                }
            } catch (e: Exception) {
                Log.e("NF", "playlist $plUrl error: ${e.message}")
            }
        }

        // ---- FALLBACK 2: player.php ----
        for (u in listOf("$apiBase/newtv/player.php?id=$id", "$mainUrl/newtv/player.php?id=$id")) {
            try {
                val playerHeaders = buildNewTvHeaders(ott, mapOf("Referer" to apiBase)) + cookieHeader
                val resp = app.get(u, headers = playerHeaders).parsed<NewTvPlayerResponse>()
                Log.e("NF", "player $u -> status=${resp.status} link=${resp.video_link?.take(60)}")
                if ((resp.status == "ok" || resp.status == "otp") && resp.video_link != null) {
                    callback.invoke(newExtractorLink(name, name, resp.video_link, type = ExtractorLinkType.M3U8) {
                        this.referer = resp.referer ?: apiBase
                        this.headers = buildNewTvHeaders(ott, mapOf("Referer" to (resp.referer ?: apiBase))) + cookieHeader
                    })
                    return true
                }
            } catch (e: Exception) {
                Log.e("NF", "player $u error: ${e.message}")
            }
        }

        Log.e("NF", "loadLinks FAILED id=$id")
        return false
    }

    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? = m3u8CdnFixInterceptor()

    private suspend fun getPlayHash(id: String): String {
        val domains = listOf("https://net11.cc", "https://net52.cc", "https://net22.cc")
        for (domain in domains) {
            try {
                val resp = app.post(
                    "$domain/play.php",
                    requestBody = FormBody.Builder().add("id", id).build(),
                    headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
                        "Accept" to "*/*",
                        "Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8"
                    )
                )
                val parsed = tryParseJson<PlayHashResponse>(resp.text)
                val h = parsed?.h ?: continue
                if (h.length > 10) {
                    // Extract hash up to ::ep to match in=unknown::ep format
                    return h.removePrefix("in=").substringBefore("::ep")
                }
            } catch (_: Exception) {}
        }
        return ""
    }
}
