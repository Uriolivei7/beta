package com.horis.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import java.net.URLEncoder
import okhttp3.Interceptor

class PrimevideoProvider : MainAPI() {
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override var lang = "en"
    override var mainUrl = "https://net52.cc"
    override var name = "PrimeVideo"
    override val hasMainPage = true

    private val ott = "pv"

    init {
        Log.e("Netmirror", "PrimevideoProvider init called")
    }

    private fun pvPoster(id: String): String = "https://imgcdn.kim/pv/v/$id.jpg"
    private fun pvBg(id: String): String = "https://imgcdn.kim/pv/h/$id.jpg"
    private fun pvEpPoster(id: String): String = "https://imgcdn.kim/pvepimg/150/$id.jpg"

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val cookie = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (cookie.length <= 10) { Log.e("Netmirror", "getMainPage: bypass failed"); return null }
        Log.e("Netmirror", "getMainPage cookie=${cookie.take(40)}...")

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
            Log.e("Netmirror", "getMainPage: ${items.size} categories")
            return newHomePageResponse(items, hasNext = false)
        } catch (e: Exception) {
            Log.e("Netmirror", "getMainPage failed: ${e.message}")
            return null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.e("Netmirror", "search called query=$query")
        val cookie = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (cookie.length <= 10) { Log.e("Netmirror", "search: bypass failed"); return emptyList() }

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
            Log.e("Netmirror", "search failed: ${e.message}")
            return emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val id = parseJson<NewTvId>(url).id
        val cookie = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (cookie.length <= 10) { Log.e("Netmirror", "load: bypass failed"); return null }

        val cookies = mobileCookies(cookie, ott)
        val mHeaders = mobileHeaders(ott, cookie, mapOf("Referer" to "$mainUrl/home"))

        val rawResponse = app.get(
            "$mainUrl/mobile/pv/post.php?id=$id&t=${System.currentTimeMillis()}",
            headers = mHeaders,
            cookies = cookies,
            referer = "$mainUrl/home"
        ).text
        Log.d("Netmirror", "RAW mobile post response: $rawResponse")
        val data = fromJson<MobilePostData>(rawResponse)

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
            Log.d("Netmirror", "RAW episodes page=$pg: $rawEp")
            val data = fromJson<MobileEpisodesData>(rawEp)

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
        Log.d("Netmirror", "loadLinks id=$id apiBase=$apiBase")

        // 1. Get bypass cookie (t_hash_t)
        val cookie = try { bypass(mainUrl) } catch (_: Exception) { "" }
        if (cookie.length <= 10) {
            Log.d("Netmirror", "bypass failed")
            return false
        }
        currentBypassToken = cookie
        Log.d("Netmirror", "Bypass cookie: ${cookie.take(60)}")

        // 2. Get user token (OTP-based auth) — per the new decompiled flow
        val userToken = try { getNewTvUserToken(apiBase, ott) } catch (_: Exception) { "" }
        Log.d("Netmirror", "User token: ${userToken.take(60)}")

        // 3. Build in= parameter: use userToken if available, else cookie
        val inParam = when {
            userToken.length > 10 -> userToken
            else -> cookie
        }

        val masterHeaders = buildNewTvHeaders(ott, mapOf(
            "Referer" to "$mainUrl/mobile/home?app=1"
        ))
        val cookieHeader = mapOf("Cookie" to "t_hash_t=$cookie; hd=on; ott=$ott")

        // 4. Fetch mobile/hls master playlist (matches decompiled loadLinks flow)
        val masterUrl = "$mainUrl/mobile/hls/$id.m3u8?q=720p&in=$inParam&hd=on&lang=eng"
        Log.d("Netmirror", "Fetching master: $masterUrl")
        val masterResp = app.get(masterUrl, headers = masterHeaders + cookieHeader)
        val masterText = masterResp.text
        Log.d("Netmirror", "mobile/hls status=${masterResp.code} body=${masterText.take(500)}")

        if (!masterText.startsWith("#EXT")) {
            Log.d("Netmirror", "Invalid master response")
            return false
        }

        // 5. Parse master playlist → ExtractorLinks
        val lines = masterText.lines().map { it.trimEnd() }
        var foundAny = false
        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            when {
                // Audio: keep as-is (already on s23 CDN)
                line.contains("#EXT-X-MEDIA:TYPE=AUDIO") -> {
                    val next = lines.getOrNull(i + 1) ?: ""
                    if (next.startsWith("http")) {
                        callback(newExtractorLink(name, name, next, type = ExtractorLinkType.M3U8) {
                            headers = masterHeaders + cookieHeader
                            referer = "$mainUrl/mobile/home?app=1"
                        })
                        foundAny = true
                        i += 2; continue
                    }
                }
                // Subtitles
                line.contains("#EXT-X-MEDIA:TYPE=SUBTITLES") -> {
                    val uriMatch = Regex("""URI="([^"]+)"""").find(line)
                    val uri = uriMatch?.groupValues?.get(1)
                    if (uri != null) {
                        val langMatch = Regex("""LANGUAGE="([^"]+)"""").find(line)
                        val lang = langMatch?.groupValues?.get(1) ?: "Unknown"
                        val fullUri = if (uri.startsWith("http")) uri else "$mainUrl$uri"
                        subtitleCallback(newSubtitleFile(lang, fullUri))
                    }
                }
                // Video variant: rewrite CDN from freecdn → s23.nm-cdn9
                line.startsWith("#EXT-X-STREAM-INF:") && i + 1 < lines.size -> {
                    i++
                    val urlLine = lines[i]
                    val bwMatch = Regex("""BANDWIDTH=(\d+)""").find(line)
                    val quality = when (bwMatch?.groupValues?.get(1)?.toIntOrNull()) {
                        in 5_000_000..Int.MAX_VALUE -> getQualityFromName("1080p")
                        in 2_000_000..4_999_999 -> getQualityFromName("720p")
                        in 800_000..1_999_999 -> getQualityFromName("480p")
                        else -> getQualityFromName("360p")
                    }
                    // Rewrite CDN: freecdn → s23, remove in= param (Cookie auth)
                    val videoUrl = urlLine
                        .replace(Regex("https://[^/]+"), "https://s23.nm-cdn9.top")
                        .replace(Regex("[?&]in=[^&\n\r]*"), "")
                    Log.d("Netmirror", "Video variant: $videoUrl quality=$quality")
                    callback(newExtractorLink(name, "$quality", videoUrl, type = ExtractorLinkType.M3U8) {
                        headers = masterHeaders + mapOf("Cookie" to "t_hash_t=$cookie; hd=on")
                        referer = "$mainUrl/mobile/home?app=1"
                        this.quality = quality
                    })
                    foundAny = true
                    i++; continue
                }
            }
            i++
        }

        // 6. Fallback: player.php
        if (!foundAny) {
            for (u in listOf("$apiBase/newtv/player.php?id=$id", "$mainUrl/newtv/player.php?id=$id")) {
                try {
                    val respRaw = app.get(u, headers = masterHeaders + cookieHeader).text
                    Log.d("Netmirror", "player.php fallback raw=${respRaw.take(300)}")
                    val resp = tryParseJson<NewTvPlayerResponse>(respRaw)
                    if (resp?.video_link != null && (resp.status == "ok" || resp.status == "otp")) {
                        callback(newExtractorLink(name, name, resp.video_link, type = ExtractorLinkType.M3U8) {
                            headers = masterHeaders + cookieHeader
                            referer = resp.referer ?: apiBase
                        })
                        foundAny = true
                    }
                } catch (_: Exception) {}
            }
        }

        // 7. Fallback: playlist.php
        if (!foundAny) {
            for (plUrl in listOf("$mainUrl/newtv/playlist.php?id=$id", "$apiBase/newtv/playlist.php?id=$id")) {
                try {
                    val items = tryParseJsonList<PlaylistItem>(
                        app.get(plUrl, headers = masterHeaders + cookieHeader).text
                    ) ?: continue
                    for (item in items) {
                        for (source in item.sources.orEmpty()) {
                            val file = source.file ?: continue
                            callback(newExtractorLink(name, source.label ?: "Unknown", file, type = ExtractorLinkType.M3U8) {
                                headers = masterHeaders + cookieHeader
                                referer = "$mainUrl/"
                                quality = getQualityFromName(source.label ?: "")
                            })
                            foundAny = true
                        }
                        for (track in item.tracks.orEmpty()) {
                            val tf = track.file ?: continue
                            subtitleCallback(newSubtitleFile(track.label ?: "Unknown", tf))
                        }
                    }
                } catch (_: Exception) {}
            }
        }

        Log.d("Netmirror", "loadLinks result=$foundAny id=$id")
        return foundAny
    }

    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        val linkUrl = extractorLink.url
        Log.e("Netmirror", "getVideoInterceptor called for ${linkUrl.take(120)}")
        Log.e("Netmirror", "getVideoInterceptor referer=${extractorLink.referer?.take(80)} headers=${extractorLink.headers?.map { "${it.key}=${it.value.take(60)}" }}")
        return try {
            m3u8CdnFixInterceptor()
        } catch (e: Exception) {
            Log.e("Netmirror", "getVideoInterceptor failed: ${e.message}")
            null
        }
    }

}
