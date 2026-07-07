package com.horis.example

import android.util.Log
import com.horis.example.entities.HomePageData
import com.horis.example.entities.PostCategory
import com.horis.example.entities.SearchData
import com.horis.example.entities.PostData
import com.horis.example.entities.EpisodesData
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.APIHolder.unixTime
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import java.net.URLEncoder
import okhttp3.Interceptor

class NetflixProvider : MainAPI() {
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override var lang = "en"
    override var mainUrl = "https://net52.cc"
    override var name = "Netflix"
    override val hasMainPage = true
    override val usesWebView = true

    private val ott = "nf"

    private fun getCookie(): Map<String, String> {
        return mapOf("ott" to ott, "hd" to "on")
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d("NF", "getMainPage page=$page")
        val raw = app.get(
            "$mainUrl/tv/nf/homepage.php",
            cookies = getCookie(),
            referer = "$mainUrl/home",
        )
        Log.d("NF", "homepage code=${raw.code} body=${raw.text.take(300)}")
        val data = raw.parsedSafe<HomePageData>() ?: return null

        val items = data.post?.mapNotNull { cat ->
            val name = cat.cate
            val ids = cat.ids.split(",").filter { it.isNotBlank() }
            val results = ids.mapNotNull { id ->
                newAnimeSearchResponse("", NewTvId(id).toJson()) {
                    posterUrl = buildVerticalPosterUrl(id, ott)
                    posterHeaders = mapOf("Referer" to "$mainUrl/home")
                }
            }
            if (results.isEmpty()) return@mapNotNull null
            HomePageList(name, results, isHorizontalImages = false)
        }.orEmpty()

        return newHomePageResponse(items, hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("NF", "search query=$query")
        val raw = app.get(
            "$mainUrl/search.php?s=$query&t=$unixTime",
            referer = "$mainUrl/tv/home",
            cookies = getCookie()
        )
        Log.d("NF", "search code=${raw.code} body=${raw.text.take(300)}")
        val data = raw.parsedSafe<SearchData>() ?: return emptyList()

        return data.searchResult.map {
            newAnimeSearchResponse(it.t, NewTvId(it.id).toJson()) {
                posterUrl = buildVerticalPosterUrl(it.id, ott)
                posterHeaders = mapOf("Referer" to "$mainUrl/home")
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val id = parseJson<NewTvId>(url).id
        Log.d("NF", "load id=$id")

        val raw = app.get(
            "$mainUrl/post.php?id=$id&t=$unixTime",
            headers = mapOf("X-Requested-With" to "XMLHttpRequest"),
            referer = "$mainUrl/tv/home",
            cookies = getCookie()
        )
        Log.d("NF", "post code=${raw.code} body=${raw.text.take(500)}")
        val data = raw.parsedSafe<PostData>() ?: return null

        val title = data.title
        val cast = data.cast?.split(",")?.map { it.trim() }?.map { ActorData(Actor(it)) } ?: emptyList()
        val genre = data.genre?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
        val rating = data.match?.replace("IMDb ", "")
        val runTime = convertRuntimeToMinutes(data.runtime ?: "")
        val suggest = data.suggest?.map {
            newAnimeSearchResponse("", NewTvId(it.id).toJson()) {
                posterUrl = buildVerticalPosterUrl(it.id, ott)
                posterHeaders = mapOf("Referer" to "$mainUrl/home")
            }
        }

        val episodes = arrayListOf<Episode>()

        if (data.episodes.firstOrNull() == null) {
            episodes.add(newEpisode(NewTvLoadData(title, id)) { name = title })
        } else {
            data.episodes.filterNotNull().mapTo(episodes) {
                newEpisode(NewTvLoadData(title, it.id)) {
                    name = it.t
                    episode = it.ep.replace("E", "").toIntOrNull()
                    season = it.s.replace("S", "").toIntOrNull()
                    posterUrl = buildVerticalPosterUrl(it.id, ott)
                    this.runTime = it.time.replace("m", "").toIntOrNull()
                }
            }

            if (data.nextPageShow == 1) {
                episodes.addAll(getEpisodes(title, url, data.nextPageSeason!!, 2))
            }

            data.season?.dropLast(1)?.forEach {
                episodes.addAll(getEpisodes(title, url, it.id, 1))
            }
        }

        val type = if (data.episodes.firstOrNull() == null) TvType.Movie else TvType.TvSeries

        return newTvSeriesLoadResponse(title, url, type, episodes) {
            posterUrl = buildVerticalPosterUrl(id, ott)
            backgroundPosterUrl = buildBackgroundPosterUrl(id, ott)
            posterHeaders = mapOf("Referer" to "$mainUrl/home")
            plot = data.desc; year = data.year.toIntOrNull(); tags = genre
            actors = cast; this.score = Score.from10(rating); duration = runTime
            this.contentRating = data.ua; recommendations = suggest
        }
    }

    private suspend fun getEpisodes(
        title: String, eid: String, sid: String, page: Int
    ): List<Episode> {
        val episodes = arrayListOf<Episode>()
        var pg = page
        while (true) {
            val raw = app.get(
                "$mainUrl/episodes.php?s=$sid&series=$eid&t=$unixTime&page=$pg",
                headers = mapOf("X-Requested-With" to "XMLHttpRequest"),
                referer = "$mainUrl/tv/home",
                cookies = getCookie()
            )
            Log.d("NF", "getEpisodes page=$pg code=${raw.code} body=${raw.text.take(300)}")
            val data = raw.parsedSafe<EpisodesData>() ?: break
            data.episodes?.mapTo(episodes) {
                newEpisode(NewTvLoadData(title, it.id)) {
                    name = it.t
                    episode = it.ep.replace("E", "").toIntOrNull()
                    season = it.s.replace("S", "").toIntOrNull()
                    posterUrl = buildVerticalPosterUrl(it.id, ott)
                    this.runTime = it.time.replace("m", "").toIntOrNull()
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
        val title = loadData.title
        Log.e("NF", "loadLinks id=$id apiBase=$apiBase")

        // Get bypass token FIRST — pass as cookie to playlist.php so server returns real in= hash
        val token = try { bypass(mainUrl) } catch (e: Exception) { Log.e("NF", "bypass fail: ${e.message}"); "" }
        Log.e("NF", "token=${token.take(60)}")
        val extraHeaders = mutableMapOf("Referer" to mainUrl)
        if (token.isNotBlank()) extraHeaders["Cookie"] = "nf_cookie=$token"

        // Primary flow: playlist.php → Source[] (matching cncverse decompiled code)
        val playlistHeaders = buildNewTvHeaders(ott, extraHeaders)
        val playlistUrls = listOf("$apiBase/newtv/playlist.php?id=$id", "$mainUrl/playlist.php?id=$id")
        val hlsBases = listOf(apiBase.trimEnd('/'), mainUrl.trimEnd('/'), "https://net11.cc").distinct()
        for (plUrl in playlistUrls) {
            try {
                val plRaw = app.get(plUrl, headers = playlistHeaders).text
                Log.e("NF", "playlist raw=${plRaw.take(500)}")
                val items = tryParseJsonList<PlaylistItem>(plRaw)
                if (!items.isNullOrEmpty()) {
                    for (item in items) {
                        // Process sources → try each base URL for relative paths
                        for (source in item.sources.orEmpty()) {
                            val file = source.file ?: continue
                            val quality = getQualityFromName(file.substringAfter("q=", "").substringBefore("&"))
                            if (file.startsWith("http")) {
                                callback.invoke(newExtractorLink(name, name, file, type = ExtractorLinkType.M3U8) {
                                    headers = playlistHeaders; referer = "$mainUrl/mobile/home?app=1"; this.quality = quality
                                })
                            } else {
                                for (base in hlsBases) {
                                    callback.invoke(newExtractorLink(name, name, "$base$file", type = ExtractorLinkType.M3U8) {
                                        headers = playlistHeaders; referer = "$mainUrl/mobile/home?app=1"; this.quality = quality
                                    })
                                }
                            }
                        }
                        // Process tracks → subtitle files
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
                    if (items.any { it.sources?.isNotEmpty() == true }) return true
                }
            } catch (e: Exception) {
                Log.e("NF", "playlist $plUrl error: ${e.message}")
            }
        }

        // Fallback: player.php (original 12-attempt flow)
        val rawTokenHash = getRawTokenHash()
        val rthEncoded = java.net.URLEncoder.encode(rawTokenHash, "UTF-8")
        val encodedToken = java.net.URLEncoder.encode(token, "UTF-8")
        val attempts = buildList {
            add(Triple("cookie", "$apiBase/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Cookie" to "nf_cookie=$token", "Referer" to apiBase))))
            add(Triple("cookie", "$apiBase/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            add(Triple("fallback", "$mainUrl/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Referer" to mainUrl))))
            add(Triple("q-token", "$apiBase/newtv/player.php?id=$id&token=$encodedToken",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            add(Triple("q-usertoken", "$apiBase/newtv/player.php?id=$id&usertoken=$encodedToken",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            add(Triple("q-hash", "$apiBase/newtv/player.php?id=$id&hash=$encodedToken",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            add(Triple("q-h", "$apiBase/newtv/player.php?id=$id&h=$encodedToken",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            add(Triple("rth-cookie", "$apiBase/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Cookie" to "nf_cookie=$rawTokenHash", "Referer" to apiBase))))
            add(Triple("rth-h", "$apiBase/newtv/player.php?id=$id&h=$rthEncoded",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
        }
        for ((label, url, headers) in attempts) {
            try {
                val raw = app.get(url, headers = headers).text
                val resp = tryParseJson<NewTvPlayerResponse>(raw)
                Log.e("NF", "$label -> status=${resp?.status} link=${resp?.video_link?.take(60)}")
                if (resp != null && resp.video_link != null) {
                    val ref = resp.referer ?: apiBase
                    Log.e("PLAYURL", resp.video_link)
                    callback.invoke(newExtractorLink(name, name, resp.video_link, type = ExtractorLinkType.M3U8) {
                        this.referer = ref
                        this.headers = buildNewTvHeaders(ott, mapOf("Referer" to ref))
                    })
                    return true
                }
            } catch (e: Exception) {
                Log.e("NF", "$label error: ${e.message}")
            }
        }
        Log.e("NF", "loadLinks FAILED id=$id")
        return false
    }

    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? = m3u8CdnFixInterceptor()
}