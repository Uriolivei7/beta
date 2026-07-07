package com.horis.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import java.net.URLEncoder
import android.util.Log
import okhttp3.FormBody
import okhttp3.Interceptor

class PrimevideoProvider : MainAPI() {
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override var lang = "en"
    override var mainUrl = "https://net52.cc"
    override var name = "PrimeVideo"
    override val hasMainPage = true

    private val ott = "pv"

    private fun pvPoster(id: String): String = "https://imgcdn.kim/pv/v/$id.jpg"
    private fun pvBg(id: String): String = "https://imgcdn.kim/pv/h/$id.jpg"
    private fun pvEpPoster(id: String): String = "https://imgcdn.kim/pvepimg/150/$id.jpg"

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        val response = app.get(
            "$apiBase/newtv/main.php",
            headers = buildNewTvHeaders(ott, mapOf("Page" to "all", "Recentplay" to "", "Watchlist" to "", "Usertoken" to ""))
        ).parsed<NewTvMainResponse>()
        val imgReferer = response.img_referer ?: apiBase
        val items = response.post.orEmpty().map { category ->
            val ids = category.ids?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }.orEmpty()
            val results = ids.mapNotNull { id ->
                newAnimeSearchResponse("", NewTvId(id).toJson()) {
                    posterUrl = pvPoster(id)
                    posterHeaders = mapOf("Referer" to imgReferer)
                }
            }
            HomePageList(category.cate.orEmpty(), results, isHorizontalImages = false)
        }
        return newHomePageResponse(items, hasNext = items.isNotEmpty())
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        val data = app.get(
            "$apiBase/newtv/search.php?s=${URLEncoder.encode(query, "UTF-8")}",
            headers = buildNewTvHeaders(ott)
        ).parsed<NewTvSearchResponse>()
        val imgReferer = data.img_referer ?: apiBase
        return data.searchResult.orEmpty().map { item ->
            newAnimeSearchResponse(item.t, NewTvId(item.id).toJson()) {
                posterUrl = pvPoster(item.id)
                posterHeaders = mapOf("Referer" to imgReferer)
            }
        }
    }

    private fun extractSeasonNumber(s: String?): Int? {
        if (s.isNullOrBlank()) return null
        return Regex("(\\d+)").find(s)?.groupValues?.get(1)?.toIntOrNull()
    }

    override suspend fun load(url: String): LoadResponse? {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        val id = parseJson<NewTvId>(url).id

        val rawResponse = app.get(
            "$apiBase/newtv/post.php?id=$id",
            headers = buildNewTvHeaders(ott, mapOf("Lastep" to "", "Usertoken" to ""))
        ).text
        Log.d("Primevideo", "RAW post response: $rawResponse")
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
                posterUrl = pvPoster(it.id)
                posterHeaders = mapOf("Referer" to apiBase)
            }
        }

        if (!isSeries) {
            return newMovieLoadResponse(title, url, TvType.Movie, NewTvLoadData(title, playbackId).toJson()) {
                posterUrl = pvPoster(id)
                backgroundPosterUrl = pvBg(id)
                posterHeaders = mapOf("Referer" to apiBase)
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
            val selectedSeasonNum = extractSeasonNumber(data.season?.find { it.selected == true }?.s)

            data.episodes.filterNotNull().mapTo(episodes) {
                newEpisode(NewTvLoadData(title, it.id.orEmpty())) {
                    name = it.t
                    episode = it.ep?.toIntOrNull() ?: it.epNum?.replace("E", "").orEmpty().toIntOrNull()
                    season = selectedSeasonNum ?: extractSeasonNumber(it.s)
                    posterUrl = pvEpPoster(it.id.orEmpty())
                    this.runTime = it.info?.getOrNull(2)?.replace("m", "")?.toIntOrNull()
                    description = it.ep_desc
                }
            }

            if (data.nextPageShow == 1 && !selectedSeasonId.isNullOrBlank())
                episodes.addAll(getEpisodes(title, selectedSeasonId, 2, selectedSeasonNum))

            data.season?.forEach { season ->
                if (season.id != selectedSeasonId && !season.id.isNullOrBlank())
                    episodes.addAll(getEpisodes(title, season.id, 1, extractSeasonNumber(season.s)))
            }
        }

        if (data.type == "t" && episodes.isEmpty() && !data.season.isNullOrEmpty()) {
            data.season.forEach { season ->
                if (!season.id.isNullOrBlank())
                    episodes.addAll(getEpisodes(title, season.id, 1, extractSeasonNumber(season.s)))
            }
        }

        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
            posterUrl = pvPoster(id)
            backgroundPosterUrl = pvBg(id)
            posterHeaders = mapOf("Referer" to apiBase)
            plot = data.desc; year = data.year?.toIntOrNull(); this.tags = tags
            actors = cast; this.score = Score.from10(rating); duration = runTime
            recommendations = suggest
            contentRating = data.ua ?: data.certification ?: data.age
        }
    }

    private suspend fun getEpisodes(
        title: String, sid: String, page: Int, seasonNumber: Int? = null
    ): List<Episode> {
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        val episodes = arrayListOf<Episode>()
        val seenIds = mutableSetOf<String>()
        var pg = page
        while (true) {
            val rawEp = app.get(
                "$apiBase/newtv/episodes.php",
                params = mapOf("id" to sid, "page" to pg.toString()),
                headers = buildNewTvHeaders(ott)
            ).text
            Log.d("Primevideo", "RAW episodes page=$pg: $rawEp")
            val data = JSONParser.parse(rawEp, NewTvEpisodesResponse::class)

            data.episodes.orEmpty().forEach { ep ->
                if (ep.id.isNullOrBlank() || ep.id in seenIds) return@forEach
                seenIds.add(ep.id)
                episodes.add(newEpisode(NewTvLoadData(title, ep.id.orEmpty())) {
                    name = ep.t
                    episode = ep.ep?.toIntOrNull() ?: ep.epNum?.replace("E", "").orEmpty().toIntOrNull()
                    season = seasonNumber ?: extractSeasonNumber(ep.s) ?: extractSeasonNumber(ep.sNum)
                    posterUrl = pvEpPoster(ep.id.orEmpty())
                    this.runTime = ep.info?.getOrNull(2)?.replace("m", "")?.toIntOrNull()
                    description = ep.ep_desc
                })
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
        Log.e("PV", "loadLinks id=$id apiBase=$apiBase")

        val token = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (token.length > 10) {
            Log.e("PV", "Got bypass token: ${token.take(60)}")
            currentBypassToken = token
        } else {
            Log.e("PV", "bypass failed, trying play hash")
            val playHash = getPlayHash(id)
            if (playHash.isNotBlank()) {
                Log.e("PV", "Got play hash: ${playHash.take(60)}")
                currentBypassToken = playHash
            }
        }
        val cookieValue = if (currentBypassToken.length > 10) currentBypassToken.replace("::ep::99", "::ep::m") else ""
        val cookieStr = if (cookieValue.length > 10) "t_hash_t=$cookieValue; ott=$ott; hd=on" else ""
        val cookieHeader = if (cookieStr.isNotBlank()) mapOf("Cookie" to cookieStr) else emptyMap()
        val urlToken = if (currentBypassToken.length > 10) currentBypassToken.substringBefore("::ep") else ""

        // Browser headers matching cncverse decompiled provider-level headers
        val browserHeaders = mapOf(
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
        ) + cookieHeader

        // ---- PRIMARY: scrape net52.cc content page for direct M3U8 URLs ----
        val pageUrls = listOf(
            "$mainUrl/netflix/$id", "$mainUrl/movie/$id", "$mainUrl/tv/$id",
            "$mainUrl/watch/$id", "$mainUrl/home?cat=search&search=$id"
        )
        for (pageUrl in pageUrls) {
            try {
                val html = app.get(pageUrl, headers = browserHeaders).text
                Log.e("PVSCRAPE", "page=$pageUrl len=${html.length} first200=${html.take(200)}")
                val m3u8Regex = Regex("""https?://[^"'\s<>]+\.m3u8[^"'\s<>]*""")
                val matches = m3u8Regex.findAll(html).map { it.value }.toList().distinct()
                if (matches.isNotEmpty()) {
                    Log.e("PVSCRAPE", "Found ${matches.size} M3U8 URLs on $pageUrl")
                    for (m3u8Url in matches) {
                        val finalUrl = if (urlToken.length > 10 && m3u8Url.contains("in=unknown::ep"))
                            m3u8Url.replace("in=unknown::ep", "in=$urlToken::ep") else m3u8Url
                        callback.invoke(newExtractorLink(name, name, finalUrl, type = ExtractorLinkType.M3U8) {
                            this.headers = browserHeaders; this.referer = "$mainUrl/"; this.quality = getQualityFromName(finalUrl)
                        })
                    }
                    return true
                }
                val relRegex = Regex("""["'](/hls/[^"'\s<>?]+\.m3u8[^"'\s<>]*)["']""")
                val relMatches = relRegex.findAll(html).map { "$mainUrl${it.groupValues[1]}" }.toList().distinct()
                if (relMatches.isNotEmpty()) {
                    Log.e("PVSCRAPE", "Found ${relMatches.size} relative M3U8 paths on $pageUrl")
                    for (m3u8Url in relMatches) {
                        val finalUrl = if (urlToken.length > 10 && m3u8Url.contains("in=unknown::ep"))
                            m3u8Url.replace("in=unknown::ep", "in=$urlToken::ep") else m3u8Url
                        callback.invoke(newExtractorLink(name, name, finalUrl, type = ExtractorLinkType.M3U8) {
                            this.headers = browserHeaders; this.referer = "$mainUrl/"; this.quality = getQualityFromName(finalUrl)
                        })
                    }
                    return true
                }
                val dataRegex = Regex("""data-(?:src|url|video|stream)="([^"]+\.m3u8[^"]*)"""")
                val dataMatches = dataRegex.findAll(html).map { it.groupValues[1] }.toList().distinct()
                if (dataMatches.isNotEmpty()) {
                    Log.e("PVSCRAPE", "Found ${dataMatches.size} data-attr M3U8 on $pageUrl")
                    for (m3u8Url in dataMatches) {
                        val finalUrl = if (m3u8Url.startsWith("http")) m3u8Url else "$mainUrl$m3u8Url"
                        callback.invoke(newExtractorLink(name, name, finalUrl, type = ExtractorLinkType.M3U8) {
                            this.headers = browserHeaders; this.referer = "$mainUrl/"; this.quality = "auto"
                        })
                    }
                    return true
                }
            } catch (e: Exception) {
                Log.e("PVSCRAPE", "page $pageUrl error: ${e.message}")
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
                Log.e("PV", "playlist raw=${plRaw.take(500)}")
                val items = tryParseJsonList<PlaylistItem>(plRaw)
                if (!items.isNullOrEmpty()) {
                    var count = 0
                    for (item in items) {
                        for (source in item.sources.orEmpty()) {
                            var file = source.file ?: continue
                            val quality = getQualityFromName(file.substringAfter("q=", "").substringBefore("&"))
                            val referer = "$mainUrl/mobile/home?app=1"
                            if (urlToken.length > 10 && file.contains("in=unknown::ep")) {
                                file = file.replace("in=unknown::ep", "in=$urlToken::ep")
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
                    Log.e("PV", "playlist $plUrl returned $count sources")
                    if (count > 0) return true
                }
            } catch (e: Exception) {
                Log.e("PV", "playlist $plUrl error: ${e.message}")
            }
        }

        // ---- FALLBACK 2: player.php ----
        for (u in listOf("$apiBase/newtv/player.php?id=$id", "$mainUrl/newtv/player.php?id=$id")) {
            try {
                val playerHeaders = buildNewTvHeaders(ott, mapOf("Referer" to apiBase)) + cookieHeader
                val resp = app.get(u, headers = playerHeaders).parsed<NewTvPlayerResponse>()
                Log.e("PV", "player $u -> status=${resp.status} link=${resp.video_link?.take(60)}")
                if ((resp.status == "ok" || resp.status == "otp") && resp.video_link != null) {
                    callback.invoke(newExtractorLink(name, name, resp.video_link, type = ExtractorLinkType.M3U8) {
                        this.referer = resp.referer ?: apiBase
                        this.headers = buildNewTvHeaders(ott, mapOf("Referer" to (resp.referer ?: apiBase))) + cookieHeader
                    })
                    return true
                }
            } catch (e: Exception) {
                Log.e("PV", "player $u error: ${e.message}")
            }
        }

        Log.e("PV", "loadLinks FAILED id=$id")
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
                    return h.removePrefix("in=").substringBefore("::ep")
                }
            } catch (_: Exception) {}
        }
        return ""
    }
}
