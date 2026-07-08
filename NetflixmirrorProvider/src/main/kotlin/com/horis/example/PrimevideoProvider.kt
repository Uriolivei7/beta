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

    init {
        Log.e("PV", "PrimevideoProvider init called")
    }

    private fun pvPoster(id: String): String = "https://imgcdn.kim/pv/v/$id.jpg"
    private fun pvBg(id: String): String = "https://imgcdn.kim/pv/h/$id.jpg"
    private fun pvEpPoster(id: String): String = "https://imgcdn.kim/pvepimg/150/$id.jpg"

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val cookie = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (cookie.length <= 10) { Log.e("PV", "getMainPage: bypass failed"); return null }
        Log.e("PV", "getMainPage cookie=${cookie.take(40)}...")

        val cookies = mobileCookies(cookie, ott)
        val mHeaders = mobileHeaders(ott, cookie, mapOf("Referer" to "$mainUrl/mobile/home?app=1"))

        try {
            val doc = app.get(
                "$mainUrl/mobile/home?app=1",
                headers = mHeaders,
                cookies = cookies,
                referer = "$mainUrl/mobile/home?app=1"
            ).document

            val items = doc.select(".tray-container, #top10").mapNotNull { tray ->
                val name = tray.select("h2, span").text().ifBlank { return@mapNotNull null }
                val results = tray.select("article, .top10-post").mapNotNull { el ->
                    val id = el.selectFirst("a")?.attr("data-post")?.ifBlank { null } ?: el.attr("data-post").ifBlank { null } ?: return@mapNotNull null
                    newAnimeSearchResponse("", NewTvId(id).toJson()) {
                        posterUrl = pvPoster(id)
                        posterHeaders = mapOf("Referer" to "$mainUrl/home")
                    }
                }
                if (results.isEmpty()) return@mapNotNull null
                HomePageList(name, results, isHorizontalImages = false)
            }
            Log.e("PV", "getMainPage: ${items.size} categories")
            return newHomePageResponse(items, hasNext = false)
        } catch (e: Exception) {
            Log.e("PV", "getMainPage failed: ${e.message}")
            return null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.e("PV", "search called query=$query")
        val cookie = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (cookie.length <= 10) { Log.e("PV", "search: bypass failed"); return emptyList() }

        val cookies = mobileCookies(cookie, ott)
        val mHeaders = mobileHeaders(ott, cookie, mapOf("Referer" to "$mainUrl/home"))

        try {
            val url = "$mainUrl/mobile/pv/search.php?s=${URLEncoder.encode(query, "UTF-8")}&t=${System.currentTimeMillis()}"
            val data = app.get(url, headers = mHeaders, cookies = cookies, referer = "$mainUrl/home")
                .parsed<MobileSearchData>()
            return data.searchResult.orEmpty().map { item ->
                newAnimeSearchResponse(item.t, NewTvId(item.id).toJson()) {
                    posterUrl = pvPoster(item.id)
                    posterHeaders = mapOf("Referer" to "$mainUrl/home")
                }
            }
        } catch (e: Exception) {
            Log.e("PV", "search failed: ${e.message}")
            return emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val id = parseJson<NewTvId>(url).id
        val cookie = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (cookie.length <= 10) { Log.e("PV", "load: bypass failed"); return null }

        val cookies = mobileCookies(cookie, ott)
        val mHeaders = mobileHeaders(ott, cookie, mapOf("Referer" to "$mainUrl/home"))

        val rawResponse = app.get(
            "$mainUrl/mobile/pv/post.php?id=$id&t=${System.currentTimeMillis()}",
            headers = mHeaders,
            cookies = cookies,
            referer = "$mainUrl/home"
        ).text
        Log.d("PV", "RAW mobile post response: $rawResponse")
        val data = JSONParser.parse(rawResponse, MobilePostData::class)

        val title = data.title ?: id
        val cast = data.cast?.split(",")?.map { it.trim() }?.map { ActorData(Actor(it)) } ?: emptyList()
        val genre = data.genre?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
        val rating = data.match?.replace("IMDb ", "")
        val runTime = convertRuntimeToMinutes(data.runtime ?: "")
        val isSeries = data.episodes?.any { it != null } == true

        val suggest = data.suggest?.map {
            newAnimeSearchResponse("", NewTvId(it.id).toJson()) {
                posterUrl = pvPoster(it.id)
            }
        }

        if (!isSeries) {
            return newMovieLoadResponse(title, url, TvType.Movie, NewTvLoadData(title, id).toJson()) {
                posterUrl = pvPoster(id)
                backgroundPosterUrl = pvBg(id)
                plot = data.desc; year = data.year?.toIntOrNull(); this.tags = genre
                actors = cast; this.score = Score.from10(rating); duration = runTime
                recommendations = suggest
                contentRating = data.ua
            }
        }

        val episodes = arrayListOf<Episode>()

        if (data.episodes.isNullOrEmpty()) {
            episodes.add(newEpisode(NewTvLoadData(title, id)) { name = title })
        } else {
            val selectedSeasonId = data.season?.find { it.sele == "true" || it.sele == "1" }?.id
                ?: data.nextPageSeason

            data.episodes.filterNotNull().mapTo(episodes) {
                newEpisode(NewTvLoadData(title, it.id)) {
                    name = it.t
                    episode = it.ep.replace("E", "").toIntOrNull()
                    season = it.s.replace("S", "").toIntOrNull()
                    posterUrl = pvEpPoster(it.id)
                    this.runTime = it.time.replace("m", "").toIntOrNull()
                    description = it.complate.ifBlank { null }
                }
            }

            if (data.nextPageShow == 1 && !selectedSeasonId.isNullOrBlank())
                episodes.addAll(getEpisodes(title, selectedSeasonId, 2))

            data.season?.forEach { season ->
                if (season.id != selectedSeasonId && !season.id.isNullOrBlank())
                    episodes.addAll(getEpisodes(title, season.id, 1))
            }
        }

        if (episodes.isEmpty() && !data.season.isNullOrEmpty()) {
            data.season.forEach { season ->
                if (!season.id.isNullOrBlank())
                    episodes.addAll(getEpisodes(title, season.id, 1))
            }
        }

        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
            posterUrl = pvPoster(id)
            backgroundPosterUrl = pvBg(id)
            plot = data.desc; year = data.year?.toIntOrNull(); this.tags = genre
            actors = cast; this.score = Score.from10(rating); duration = runTime
            recommendations = suggest
            contentRating = data.ua
        }
    }

    private suspend fun getEpisodes(
        title: String, sid: String, page: Int
    ): List<Episode> {
        val episodes = arrayListOf<Episode>()
        val cookie = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (cookie.length <= 10) return episodes
        val cookies = mobileCookies(cookie, ott)
        val mHeaders = mobileHeaders(ott, cookie, mapOf("Referer" to "$mainUrl/home"))
        var pg = page
        while (true) {
            val rawEp = app.get(
                "$mainUrl/mobile/pv/episodes.php?s=$sid&series=$title&t=${System.currentTimeMillis()}&page=$pg",
                headers = mHeaders,
                cookies = cookies,
                referer = "$mainUrl/home"
            ).text
            Log.d("PV", "RAW episodes page=$pg: $rawEp")
            val data = JSONParser.parse(rawEp, MobileEpisodesData::class)

            data.episodes.orEmpty().mapTo(episodes) {
                newEpisode(NewTvLoadData(title, it.id)) {
                    name = it.t
                    episode = it.ep.replace("E", "").toIntOrNull()
                    season = it.s.replace("S", "").toIntOrNull()
                    posterUrl = pvEpPoster(it.id)
                    this.runTime = it.time.replace("m", "").toIntOrNull()
                    description = it.complate.ifBlank { null }
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
        Log.e("PV", "loadLinks id=$id apiBase=$apiBase")

        val token = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (token.length > 10) {
            Log.e("PV", "Got bypass token: ${token.take(60)}")
            currentBypassToken = token
        } else {
            Log.e("PV", "bypass failed, trying play hash")
            val playHash = getPlayHash(id, apiBase)
            if (playHash.isNotBlank()) {
                Log.e("PV", "Got play hash: ${playHash.take(60)}")
                currentBypassToken = playHash
            }
        }
        val cookie5 = if (currentBypassToken.length > 10) currentBypassToken else ""
        // Construir inParam con formato h1::playHash::ts::ep::m:
        // - h1 del bypass cookie, hash2 de play.php para que server reescriba
        val h1 = cookie5.substringBefore("::")
        val playHash = if (token.length > 10) getPlayHash(id, apiBase) else ""
        if (playHash.length > 10) {
            Log.e("PV", "Got playHash for id=$id: ${playHash.take(40)}")
        }
        val ts = System.currentTimeMillis() / 1000
        val hash2 = if (playHash.length > 10) playHash
            else cookie5.substringAfter("::").substringBefore("::")
        val inParam = "$h1::$hash2::$ts::ep::m"
        val cookieEscaped = URLEncoder.encode(cookie5, "UTF-8")
        val cookieHeader = mapOf("Cookie" to "t_hash_t=$cookieEscaped; ott=$ott; hd=on")

        var foundAnyLink = false

        // ---- PRIMARY: mobile/hls (server-provided master, hp=yes) ----
        if (cookie5.length > 10) {
            try {
                val masterUrl = "$mainUrl/mobile/hls/$id.m3u8?in=$inParam&hd=on&lang=eng"
                val masterResp = app.get(masterUrl, headers = newTvBaseHeaders, cookies = mapOf("t_hash_t" to cookie5, "hd" to "on", "ott" to "pv")).text
                Log.e("PV", "mobile/hls raw=${masterResp.take(2000)}")

                if (masterResp.startsWith("#EXT")) {
                    // Parse master: audio stays on s23, video freecdn→s23
                    val lines = masterResp.lines().map { it.trimEnd() }.toMutableList()
                    val fixedMaster = buildString {
                        var i = 0
                        while (i < lines.size) {
                            val line = lines[i]
                            if (line.startsWith("#EXT-X-STREAM-INF:") && i + 1 < lines.size) {
                                appendLine(line)
                                i++
                                val urlLine = lines[i]
                                if (urlLine.contains("freecdn")) {
                                    val quality = Regex("/(\\d+p)/").find(urlLine)?.groupValues?.get(1) ?: "720p"
                                    val rewritten = urlLine
                                        .replace(Regex("https://[^/]+"), "https://s23.nm-cdn9.top")
                                        .replace(Regex("/files/\\d+/"), "/files/$id/")
                                        .replace("in=unknown::ep", "in=$inParam")
                                    Log.e("PV", "rewrote video: ${urlLine.take(80)} → ${rewritten.take(80)}")
                                    appendLine(rewritten)
                                } else {
                                    appendLine(urlLine)
                                }
                            } else if (line.contains("freecdn")) {
                                val quality = Regex("/(\\d+p)/").find(line)?.groupValues?.get(1) ?: "720p"
                                val rewritten = line
                                    .replace(Regex("https://[^/]+"), "https://s23.nm-cdn9.top")
                                    .replace(Regex("/files/\\d+/"), "/files/$id/")
                                    .replace("in=unknown::ep", "in=$inParam")
                                appendLine(rewritten)
                            } else {
                                appendLine(line)
                            }
                            i++
                        }
                    }
                    val hasVideo = fixedMaster.contains("#EXT-X-STREAM-INF:")
                    if (hasVideo) {
                        Log.e("PV", "Server master OK, fixed CDN")
                        Log.e("PV", "fixedMaster=${fixedMaster.take(2000)}")
                        setCustomMaster(id, fixedMaster)

                        val cmUrl = "$mainUrl/mobile/hls/$id.m3u8?in=$inParam&hd=on&__cm=1"
                        val cmHeaders = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
                            "X-Requested-With" to "app.netmirror.netmirrornew",
                            "Referer" to "$mainUrl/mobile/home?app=1"
                        )
                        callback.invoke(newExtractorLink(name, name, cmUrl, type = ExtractorLinkType.M3U8) {
                            this.headers = cmHeaders + cookieHeader
                            this.referer = "$mainUrl/mobile/home?app=1"
                            this.quality = getQualityFromName("720p")
                        })
                        foundAnyLink = true
                        Log.e("PV", "mobile/hls master returned for id=$id")
                        return true
                    } else {
                        Log.e("PV", "mobile/hls response has no video variants")
                    }
                } else {
                    Log.e("PV", "mobile/hls response invalid: ${masterResp.take(200)}")
                }
            } catch (e: Exception) {
                Log.e("PV", "mobile/hls failed: ${e.message}")
            }
        }

        // ---- FALLBACK: player.php (preview, solo si s23 falló) ----
        if (!foundAnyLink) {
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
                        foundAnyLink = true
                    }
                } catch (e: Exception) {
                    Log.e("PV", "player $u error: ${e.message}")
                }
            }
        }

        // ---- FALLBACK: playlist.php (solo si todo lo demás falló) ----
        if (!foundAnyLink) {
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
                                if (cookie5.length > 10 && file.contains("in=unknown::ep")) {
                                    file = file.replace("in=unknown::ep", "in=$inParam")
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
                        if (count > 0) foundAnyLink = true
                    }
                } catch (e: Exception) {
                    Log.e("PV", "playlist $plUrl error: ${e.message}")
                }
            }
        }

        Log.e("PV", "loadLinks result=$foundAnyLink id=$id")
        return foundAnyLink
    }

    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        val linkUrl = extractorLink.url
        Log.e("PV", "getVideoInterceptor called for ${linkUrl.take(120)}")
        Log.e("PV", "getVideoInterceptor referer=${extractorLink.referer?.take(80)} headers=${extractorLink.headers?.map { "${it.key}=${it.value.take(60)}" }}")
        return try {
            m3u8CdnFixInterceptor()
        } catch (e: Exception) {
            Log.e("PV", "getVideoInterceptor failed: ${e.message}")
            null
        }
    }

    private suspend fun getPlayHash(id: String, apiBase: String): String {
        val domains = mutableListOf(mainUrl.trimEnd('/'), apiBase.trimEnd('/'))
        domains.addAll(listOf("https://net11.cc", "https://net52.cc", "https://net22.cc"))
        if (currentBypassToken.length <= 10) return ""
        for (domain in domains) {
            for (path in listOf("/play.php", "/newtv/play.php")) {
                try {
                    val resp = app.get(
                        "$domain$path?id=$id",
                        headers = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
                            "X-Requested-With" to "app.netmirror.netmirrornew",
                            "Referer" to "$domain/mobile/home?app=1"
                        ),
                        cookies = mapOf("t_hash_t" to currentBypassToken, "hd" to "on", "ott" to "pv")
                    )
                    val text = resp.text.trim()
                    Log.e("PV", "play.php GET $domain$path raw=${text.take(200)}")
                    if (text.startsWith("<") || text.length < 10) continue
                    val parts = text.split("::")
                    return if (parts.size >= 2) parts[1] else text
                } catch (_: Exception) {}
            }
            try {
                val resp = app.post(
                    "$domain/play.php",
                    requestBody = FormBody.Builder().add("id", id).build(),
                    headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
                        "X-Requested-With" to "app.netmirror.netmirrornew",
                        "Referer" to "$domain/mobile/home?app=1",
                        "Accept" to "*/*",
                        "Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8"
                    ),
                    cookies = mapOf("t_hash_t" to currentBypassToken, "hd" to "on", "ott" to "pv")
                )
                val text = resp.text.trim()
                Log.e("PV", "play.php POST $domain raw=${text.take(200)}")
                if (text.startsWith("<") || text.length < 10) continue
                val parsed = tryParseJson<PlayHashResponse>(text)
                val h = parsed?.h?.removePrefix("in=")?.substringBefore("::ep")
                if (h != null && h.length > 10) {
                    val parts = h.split("::")
                    return if (parts.size >= 2) parts[1] else h
                }
                if (text.length < 100) {
                    val parts = text.split("::")
                    return if (parts.size >= 2) parts[1] else text
                }
            } catch (_: Exception) {}
        }
        return ""
    }
}
