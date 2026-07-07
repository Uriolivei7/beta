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
            val playHash = getPlayHash(id)
            if (playHash.isNotBlank()) {
                Log.e("PV", "Got play hash: ${playHash.take(60)}")
                currentBypassToken = playHash
            }
        }
        val cookieRaw = if (currentBypassToken.length > 10) currentBypassToken else ""
        // Ensure 5-part format: h1::h2::ts::ep::m
        val cookie5 = if (cookieRaw.contains("::ep::")) cookieRaw
            else cookieRaw + "::${System.currentTimeMillis() / 1000}::ep::m"
        val cookieEscaped = URLEncoder.encode(cookie5, "UTF-8")
        val cookieHeader = mapOf("Cookie" to "t_hash_t=$cookieEscaped; ott=$ott; hd=on")

        // ---- PRIMARY: mobile/hls -> s23.nm-cdn9.top (full content JPG frames) ----
        if (cookie5.length > 10) {
            try {
                val inParam = cookie5.substringBefore("::ep") + "::ep::m"
                val mobileHeaders = mapOf(
                    "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
                    "X-Requested-With" to "app.netmirror.netmirrornew",
                    "Referer" to "$mainUrl/mobile/home?app=1"
                ) + cookieHeader

                // Fetch master with q=720p to get rewritten token in video URLs
                val masterUrl = "$mainUrl/mobile/hls/$id.m3u8?q=720p&in=$inParam&hd=on&lang=eng"
                val masterResp = app.get(masterUrl, headers = mobileHeaders).text
                Log.e("PV", "mobile/hls raw=${masterResp.take(500)}")

                // Extract CDN hostname from audio URI + rewritten token from video variant URL
                val cdnMatch = Regex("""URI="https://([^/]+)/files/""").find(masterResp)
                val inMatch = Regex("""\?in=([^&\s]+)""").find(masterResp)
                if (cdnMatch != null && inMatch != null) {
                    val cdnHost = cdnMatch.groupValues[1]
                    val rewrittenToken = inMatch.groupValues[1]
                    Log.e("PV", "CDN: $cdnHost Rewritten token: $rewrittenToken")

                    // Extract audio group lines from master response
                    val audioLines = Regex("""^#EXT-X-MEDIA:TYPE=AUDIO,.*""", RegexOption.MULTILINE)
                        .findAll(masterResp).map { it.value }.joinToString("\n")

                    // Extract all video variant lines and rewrite s21→CDN
                    val variantRegex = Regex("""(#EXT-X-STREAM-INF:.*)\n(https://s\d+\.freecdn\d*\.top/files/\d+/\w+/.+)""")
                    val variants = variantRegex.findAll(masterResp).map { match ->
                        val streamInf = match.groupValues[1]
                        var url = match.groupValues[2]
                        // Rewrite freecdn hostname + internal ID to CDN hostname + content ID
                        url = url.replace(Regex("""https://s\d+\.freecdn\d*\.top/files/\d+/"""), "https://$cdnHost/files/$id/")
                        // Add rewritten token if not present
                        if (!url.contains("in=")) url += "?in=$rewrittenToken"
                        streamInf to url
                    }.toList()

                    if (variants.isEmpty()) {
                        Log.w("PV", "No video variants found in master response, using fallback 720p")
                        val sb = StringBuilder()
                        sb.appendLine("#EXTM3U")
                        sb.appendLine("#EXT-X-VERSION:3")
                        if (audioLines.isNotBlank()) sb.appendLine(audioLines)
                        sb.appendLine("#EXT-X-STREAM-INF:BANDWIDTH=2000000,RESOLUTION=1280x720,CODECS=\"avc1.64001f,mp4a.40.2\",AUDIO=\"aac\"")
                        sb.append("https://$cdnHost/files/$id/720p/720p.m3u8?in=$rewrittenToken")
                        setCustomMaster(id, sb.toString())
                    } else {
                        val sb = StringBuilder()
                        sb.appendLine("#EXTM3U")
                        sb.appendLine("#EXT-X-VERSION:3")
                        if (audioLines.isNotBlank()) sb.appendLine(audioLines)
                        for ((streamInf, url) in variants) {
                            sb.appendLine(streamInf)
                            sb.append(url)
                        }
                        val customMaster = sb.toString()
                        Log.e("PV", "Custom master with ${variants.size} variants:\\n${customMaster.take(500)}")
                        setCustomMaster(id, customMaster)
                    }

                    val cmHeaders = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
                        "X-Requested-With" to "app.netmirror.netmirrornew",
                        "Referer" to "$mainUrl/mobile/home?app=1",
                        "Cookie" to "hd=on; t_hash_t=$cookieEscaped"
                    )

                    // Pass mobile/hls URL with __cm=1 — interceptor returns custom master
                    val cmUrl = "$mainUrl/mobile/hls/$id.m3u8?in=$inParam&hd=on&__cm=1"
                    callback.invoke(newExtractorLink(name, name, cmUrl, type = ExtractorLinkType.M3U8) {
                        this.headers = cmHeaders
                        this.referer = "$mainUrl/mobile/home?app=1"
                        this.quality = getQualityFromName("720p")
                    })
                    return true
                }
            } catch (e: Exception) {
                Log.e("PV", "mobile/hls s23 failed: ${e.message}")
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
                            if (cookie5.length > 10 && file.contains("in=unknown::ep")) {
                                file = file.replace("in=unknown::ep", "in=${cookie5.substringBefore("::ep")}::ep")
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

    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        Log.e("PV", "getVideoInterceptor called for ${extractorLink.url.take(100)}")
        return try {
            m3u8CdnFixInterceptor()
        } catch (e: Exception) {
            Log.e("PV", "getVideoInterceptor failed: ${e.message}")
            null
        }
    }

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
