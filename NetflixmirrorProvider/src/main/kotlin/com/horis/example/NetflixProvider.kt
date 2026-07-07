package com.horis.example

import android.util.Log
import com.lagradost.cloudstream3.*
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
    override val usesWebView = false

    private val ott = "nf"

    private val androidHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
        "Accept" to "*/*",
        "X-Requested-With" to "app.netmirror.netmirrornew",
        "Cache-Control" to "max-age=0"
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
        val apiBase = try { resolveApiUrl() } catch (_: Exception) { mainUrl }
        val loadData = parseJson<NewTvLoadData>(data)
        val id = loadData.id
        val title = loadData.title
        Log.e("NF", "loadLinks id=$id apiBase=$apiBase")

        val token = try { bypass(mainUrl) } catch (e: Exception) { Log.e("NF", "bypass fail: ${e.message}"); "" }
        Log.e("NF", "token=${token.take(60)}")

        // Also try raw token_hash from checknewtv.php (might be the auth token itself)
        val rawTokenHash = getRawTokenHash()
        Log.e("NF", "rawTokenHash=${rawTokenHash.take(60)}")

        val rthEncoded = java.net.URLEncoder.encode(rawTokenHash, "UTF-8")
        val encodedToken = java.net.URLEncoder.encode(token, "UTF-8")
        val attempts = buildList {
            // with bypass() token
            add(Triple("cookie", "$apiBase/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Cookie" to "nf_cookie=$token", "Referer" to apiBase))))
            add(Triple("usertoken", "$apiBase/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Usertoken" to token, "Referer" to apiBase))))
            add(Triple("fallback-cookie", "$mainUrl/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Cookie" to "nf_cookie=$token", "Referer" to mainUrl))))
            add(Triple("fallback-plain", "$mainUrl/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Referer" to mainUrl))))
            add(Triple("q-token", "$apiBase/newtv/player.php?id=$id&token=$encodedToken",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            add(Triple("q-usertoken", "$apiBase/newtv/player.php?id=$id&usertoken=$encodedToken",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            add(Triple("q-hash", "$apiBase/newtv/player.php?id=$id&hash=$encodedToken",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            add(Triple("q-h", "$apiBase/newtv/player.php?id=$id&h=$encodedToken",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            // with rawTokenHash from checknewtv.php
            add(Triple("rth-cookie", "$apiBase/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Cookie" to "nf_cookie=$rawTokenHash", "Referer" to apiBase))))
            add(Triple("rth-usertoken", "$apiBase/newtv/player.php?id=$id",
                buildNewTvHeaders(ott, mapOf("Usertoken" to rawTokenHash, "Referer" to apiBase))))
            add(Triple("rth-h", "$apiBase/newtv/player.php?id=$id&h=$rthEncoded",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
            add(Triple("rth-token", "$apiBase/newtv/player.php?id=$id&token=$rthEncoded",
                buildNewTvHeaders(ott, mapOf("Referer" to apiBase))))
        }

        for ((label, url, headers) in attempts) {
            try {
                val raw = retryOnDbError {
                    val t = app.get(url, headers = headers).text
                    checkDbError(t); t
                }
                val resp = tryParseJson<NewTvPlayerResponse>(raw)
                Log.e("NF", "$label -> status=${resp?.status} usertoken=${resp?.usertoken} link=${resp?.video_link?.take(60)}")
                if (resp != null && resp.video_link != null) {
                    val ref = resp.referer ?: apiBase
                    Log.e("PLAYURL", resp.video_link)
                    callback.invoke(newExtractorLink(name, name, resp.video_link, type = ExtractorLinkType.M3U8) {
                        this.referer = ref
                        this.headers = androidHeaders + mapOf("Referer" to ref)
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

    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? = null
}