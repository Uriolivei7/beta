package com.example

import android.util.Log
import com.google.gson.Gson
import com.lagradost.cloudstream3.Actor
import com.lagradost.cloudstream3.network.CloudflareKiller
import com.lagradost.cloudstream3.ActorData
import com.lagradost.cloudstream3.Episode
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.LoadResponse.Companion.addImdbId
import com.lagradost.cloudstream3.LoadResponse.Companion.addTMDbId
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.Score
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SearchResponseList
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.addDate
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.base64Decode
import com.lagradost.cloudstream3.fixUrl
import com.lagradost.cloudstream3.fixUrlNull
import com.lagradost.cloudstream3.getQualityFromString
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newEpisode
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newMovieLoadResponse
import com.lagradost.cloudstream3.newMovieSearchResponse
import com.lagradost.cloudstream3.newSubtitleFile
import com.lagradost.cloudstream3.newTvSeriesSearchResponse
import com.lagradost.cloudstream3.newTvSeriesLoadResponse
import com.lagradost.cloudstream3.newSearchResponseList
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.INFER_TYPE
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.nicehttp.NiceResponse
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.nodes.Element


class CinemacityProvider : MainAPI() {
    override var mainUrl = "https://cinemacity.cc"
    override var name = "CinemaCity"
    override var lang = "en"
    override val hasMainPage = true
    override val hasDownloadSupport = false
    override val hasQuickSearch = true
    override val supportedTypes = setOf(
        TvType.Movie, TvType.TvSeries, TvType.Cartoon, TvType.AsianDrama
    )

    private var dynamicCookies: Map<String, String> = emptyMap()
    private val cfKiller = CloudflareKiller()

    private val protectionHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    )

    private suspend fun doRequest(url: String): NiceResponse {
        return app.get(
            url,
            headers = protectionHeaders + ("Referer" to "$mainUrl/"),
            cookies = dynamicCookies,
            interceptor = cfKiller,
            timeout = 120L
        ).also {
            if (it.cookies.isNotEmpty()) dynamicCookies = dynamicCookies + it.cookies
        }
    }

    companion object {
        private const val TMDBIMAGEBASEURL = "https://image.tmdb.org/t/p/original"
        private const val cinemeta_url = "https://aiometadata.elfhosted.com/stremio/b7cb164b-074b-41d5-b458-b3a834e197bb/meta"
    }

    fun parseCredits(jsonText: String?): List<ActorData> {
        if (jsonText.isNullOrBlank()) return emptyList()
        val list = ArrayList<ActorData>()
        val root = JSONObject(jsonText)
        val castArr = root.optJSONArray("cast") ?: return list
        for (i in 0 until castArr.length()) {
            val c = castArr.optJSONObject(i) ?: continue
            val name = c.optString("name").takeIf { it.isNotBlank() } ?: c.optString("original_name").orEmpty()
            val profile = c.optString("profile_path").takeIf { it.isNotBlank() }?.let { "$TMDBIMAGEBASEURL$it" }
            val character = c.optString("character").takeIf { it.isNotBlank() }
            val actor = Actor(name, profile)
            list += ActorData(actor, roleString = character)
        }
        return list
    }

    override val mainPage = mainPageOf(
        "$mainUrl/tv-series/" to "Series",
        "$mainUrl/movies/" to "Movies",
    )

    override suspend fun getMainPage(
        page: Int, request: MainPageRequest
    ): HomePageResponse {
        return try {
            val base = request.data.trimEnd('/')
            val url = if (page > 1) "$base/page/$page/" else "$base/"
            Log.d("CinemacityMP", "getMainPage url: $url")
            val resp = doRequest(url)
            Log.d("CinemacityMP", "Status: ${resp.code}, doc length: ${resp.document.text().length}")
            val home = resp.document.select("div.dar-short_item").mapNotNull { it.toSearchResult() }
            Log.d("CinemacityMP", "Items found: ${home.size}")
            val hasNext = resp.document.select("a[href*='/page/'], .pnext, .next").isNotEmpty()
            Log.d("CinemacityMP", "Has next page: $hasNext")
            newHomePageResponse(request.name, home, hasNext)
        } catch (e: Exception) {
            Log.e("CinemacityMP", "getMainPage failed: ${e.message}", e)
            throw e
        }
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val link = this.select("a").firstOrNull {
            val h = it.attr("href")
            (h.contains("/movies/") || h.contains("/tv-series/")) && !h.contains(Regex("\\.(webp|jpg|png)"))
        } ?: this.selectFirst("h2 a, h3 a, div.title a, span.title a")?.takeIf {
            val h = it.attr("href")
            h.contains("/movies/") || h.contains("/tv-series/")
        } ?: return null

        val rawTitle = link.text().trim()
        if (rawTitle.isBlank()) return null
        val title = rawTitle.split(" (", " S0", " -")[0].trim()
        val href = fixUrlNull(link.attr("href")) ?: return null
        val poster = fixUrlNull(this.selectFirst("img")?.attr("src"))
        val isTv = href.contains("/tv-series/")
        val score = this.selectFirst("span.rating-color, .rating")?.text()
        val date  = this.selectFirst("span a[href*=year]")?.text()?.toIntOrNull()
            ?: Regex("\\((\\d{4})\\)").find(rawTitle)?.groupValues?.get(1)?.toIntOrNull()

        return if (isTv) {
            newTvSeriesSearchResponse(title, href, TvType.TvSeries) {
                this.posterUrl = poster
                this.score = Score.from10(score)
                this.year = date
            }
        } else {
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = poster
                this.score = Score.from10(score)
                this.year = date
            }
        }
    }


    override suspend fun search(query: String): List<SearchResponse>? {
        return try {
            val encoded = java.net.URLEncoder.encode(query, "UTF-8")
            val formData = mapOf("do" to "search", "subaction" to "search", "story" to query)

            // Strategy 1: Quick GET attempt with short timeout (Cloudflare may block)
            val getUrl = "$mainUrl/index.php?do=search&subaction=search&search_start=1&full_search=0&story=$encoded"
            Log.d("CinemacitySearch", "Trying quick GET: $getUrl")
            val quickGet = app.get(
                getUrl,
                headers = protectionHeaders + ("Referer" to "$mainUrl/"),
                cookies = dynamicCookies,
                interceptor = cfKiller,
                timeout = 15L
            )
            Log.d("CinemacitySearch", "Quick GET Status: ${quickGet.code}")
            var resp: NiceResponse = quickGet

            // Strategy 2: POST to main URL (mimics search form submission, bypasses Cloudflare)
            if (resp.code != 200) {
                Log.d("CinemacitySearch", "GET failed ($resp.code), trying POST to mainUrl")
                resp = app.post(
                    mainUrl,
                    headers = protectionHeaders + ("Referer" to "$mainUrl/") + ("X-Requested-With" to "XMLHttpRequest"),
                    cookies = dynamicCookies,
                    interceptor = cfKiller,
                    timeout = 120L,
                    data = formData
                ).also {
                    if (it.cookies.isNotEmpty()) dynamicCookies = dynamicCookies + it.cookies
                }
                Log.d("CinemacitySearch", "POST(mainUrl) Status: ${resp.code}")
            }

            // Parse results
            val results = resp.document.select("div.dar-short_item, div.short-story, div.short, div.search-item, .search-result").mapNotNull { it.toSearchResult() }
            Log.d("CinemacitySearch", "Results found: ${results.size}, doc length: ${resp.document.text().length}")

            if (results.isNotEmpty()) return results

            // Strategy 3: Try search suggest API (different endpoint, may bypass Cloudflare)
            Log.d("CinemacitySearch", "No HTML results, trying search suggest API")
            val suggestUrl = "$mainUrl/engine/ajax/search_find.php?q=$encoded"
            val suggestResp = app.get(
                suggestUrl,
                headers = protectionHeaders + ("Referer" to "$mainUrl/"),
                cookies = dynamicCookies,
                interceptor = cfKiller,
                timeout = 30L
            ).also {
                if (it.cookies.isNotEmpty()) dynamicCookies = dynamicCookies + it.cookies
            }
            Log.d("CinemacitySearch", "Suggest Status: ${suggestResp.code}")
            if (suggestResp.code == 200) {
                val suggestResults = suggestResp.document.select("a").mapNotNull { a ->
                    val href = fixUrlNull(a.attr("href")) ?: return@mapNotNull null
                    if (!href.contains("/movies/") && !href.contains("/tv-series/")) return@mapNotNull null
                    val title = a.text().trim().substringBefore("(").trim()
                    val isTv = href.contains("/tv-series/")
                    if (isTv) newTvSeriesSearchResponse(title, href, TvType.TvSeries)
                    else newMovieSearchResponse(title, href, TvType.Movie)
                }
                Log.d("CinemacitySearch", "Suggest results: ${suggestResults.size}")
                if (suggestResults.isNotEmpty()) return suggestResults
            }

            emptyList()
        } catch (e: Exception) {
            Log.e("CinemacitySearch", "Search failed: ${e.message}", e)
            null
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse>? = search(query)


    override suspend fun load(url: String): LoadResponse {
        Log.d("CinemacityLoad", "load url: $url")
        val page = doRequest(url)
        val doc = page.document
        Log.d("CinemacityLoad", "Status: ${page.code}, doc length: ${doc.text().length}")
        Log.d("CinemacityLoad", "og:title: ${doc.selectFirst("meta[property=og:title]")?.attr("content")}")
        Log.d("CinemacityLoad", "iframes: ${doc.select("iframe").size}")
        doc.select("iframe").forEachIndexed { i, f ->
            Log.d("CinemacityLoad", "  iframe[$i] src=${f.attr("src")}")
        }
        // Check for video containers with data attributes
        val playerDivs = doc.select("[id*=player], [class*=player], [data-player], [data-file]")
        Log.d("CinemacityLoad", "player divs: ${playerDivs.size}")
        playerDivs.forEachIndexed { i, d ->
            Log.d("CinemacityLoad", "  playerDiv[$i] html=${d.html().take(300)}")
            Log.d("CinemacityLoad", "  playerDiv[$i] data-attrs: ${d.attributes().filter { it.key.startsWith("data-") }}")
        }
        // Check for video/embed tags
        Log.d("CinemacityLoad", "video tags: ${doc.select("video").size}")
        Log.d("CinemacityLoad", "embed tags: ${doc.select("embed").size}")
        Log.d("CinemacityLoad", "source tags: ${doc.select("source").size}")
        // Check for links with video extensions
        val videoLinks = doc.select("a[href$=.mp4], a[href$=.m3u8], a[href*=.mp4], a[href*=.m3u8]")
        Log.d("CinemacityLoad", "direct video links: ${videoLinks.size}")
        videoLinks.forEachIndexed { i, a ->
            Log.d("CinemacityLoad", "  videoLink[$i] href=${a.attr("href")}")
        }

        val ogTitle = doc.selectFirst("meta[property=og:title]")?.attr("content").orEmpty()
        val title = ogTitle.substringBefore("(").trim()
        val poster = doc.selectFirst("meta[property=og:image]")?.attr("content").orEmpty()
        val bgposter = doc.selectFirst("div.dar-full_bg a")?.attr("href")
        val trailer = doc.select("div.dar-full_bg.e-cover > div").attr("data-vbg")

        val audioLanguages = doc
            .select("li")
            .firstOrNull {
                it.selectFirst("span")?.text()
                    ?.equals("Audio language", ignoreCase = true) == true
            }
            ?.select("span:eq(1) a")
            ?.map { it.text().trim() }
            ?.filter { it.isNotEmpty() }
            ?.joinToString(", ")

        val descriptions = doc.selectFirst("#about div.ta-full_text1")?.text()


        val recommendation = doc.select("div.ta-rel > div.ta-rel_item").map {
            val recTitle = it.select("a").text().substringBefore("(").trim()
            val recHref = fixUrl(it.selectFirst("> div > a")?.attr("href") ?: "")
            val recPosterUrl = it.selectFirst("div > a")?.attr("href")

            newMovieSearchResponse(recTitle, recHref, TvType.Movie) {
                this.posterUrl = recPosterUrl
            }
        }

        val year = ogTitle.substringAfter("(", "").substringBefore(")").toIntOrNull()
        val contenttype = doc.select("div.dar-full_meta > span:nth-child(5) > a").text()

        val tvtype = if (url.contains("/movies/", true)) TvType.Movie else TvType.TvSeries
        val tmdbmetatype = if (tvtype == TvType.TvSeries) "tv" else "movie"

        var genre: List<String>? = null
        var background: String? = null
        var description: String? = null


        val imdbId = doc
            .select("div.ta-full_rating1 > div")
            .mapNotNull { it.attr("onclick") }
            .firstNotNullOfOrNull { Regex("tt\\d+").find(it)?.value }

        val tmdbId = imdbId?.let { id ->
            runCatching {
                val obj = JSONObject(
                    app.get(
                        "https://api.themoviedb.org/3/find/$id" +
                                "?api_key=1865f43a0549ca50d341dd9ab8b29f49" +
                                "&external_source=imdb_id"
                    ).textLarge
                )

                obj.optJSONArray("movie_results")?.optJSONObject(0)?.optInt("id")?.takeIf { it != 0 }
                    ?: obj.optJSONArray("tv_results")?.optJSONObject(0)?.optInt("id")?.takeIf { it != 0 }
            }.getOrNull()?.toString()
        }

        val logoPath = imdbId?.let {
            "https://live.metahub.space/logo/medium/$it/img"
        }

        val creditsJson = tmdbId?.let {
            runCatching {
                app.get(
                    "https://api.themoviedb.org/3/$tmdbmetatype/$it/credits" +
                            "?api_key=1865f43a0549ca50d341dd9ab8b29f49&language=en-US"
                ).textLarge
            }.getOrNull()
        }

        val castList = parseCredits(creditsJson)
        val typeset = if (tvtype == TvType.TvSeries) "series" else "movie"

        val responseData = imdbId?.takeIf { it.isNotBlank() }?.let {
            val text = app.get("$cinemeta_url/$typeset/$it.json").text
            if (text.startsWith("{")) Gson().fromJson(text, ResponseData::class.java) else null
        }

        responseData?.meta?.let {
            description = it.description ?: descriptions
            background = it.background ?: poster
            genre = it.genres
        }

        val epMetaMap: Map<String, ResponseData.Meta.EpisodeDetails> =
            responseData?.meta?.videos
                ?.filter { it.season != null && it.episode != null }
                ?.associateBy { "${it.season}:${it.episode}" }
                ?: emptyMap()

        // Try to extract Playerjs from atob scripts (standard path)
        val atobScripts = doc.select("script:containsData(atob)")
        Log.d("CinemacityLoad", "atob scripts found: ${atobScripts.size}")
        atobScripts.forEachIndexed { i, s ->
            Log.d("CinemacityLoad", "atob[$i] data length: ${s.data()?.length}, preview: ${s.data()?.take(100)}")
        }

        val playerScript = atobScripts.getOrNull(1)?.data()
        Log.d("CinemacityLoad", "playerScript found: ${playerScript != null}, length: ${playerScript?.length}")

        val fileArray: JSONArray
        val parsedSubtitles: JSONArray

        if (playerScript != null) {
            val b64 = playerScript.substringAfter("atob(\"").substringBefore("\")")
            Log.d("CinemacityLoad", "base64 string length: ${b64.length}, preview: ${b64.take(50)}")
            val decodedPlayer = base64Decode(b64)
            Log.d("CinemacityLoad", "decoded length: ${decodedPlayer?.length}, preview: ${decodedPlayer?.take(200)}")

            val playerJsonStr = decodedPlayer
                ?.substringAfter("new Playerjs(")
                ?.substringBeforeLast(");")
            Log.d("CinemacityLoad", "playerJsonStr length: ${playerJsonStr?.length}, preview: ${playerJsonStr?.take(200)}")

            if (playerJsonStr.isNullOrBlank()) {
                Log.w("CinemacityLoad", "Playerjs parse failed; fallback to iframe detection. decoded: ${decodedPlayer?.take(300)}")
                fileArray = JSONArray()
                parsedSubtitles = JSONArray()
            } else {
                val playerJson = JSONObject(playerJsonStr)
                Log.d("CinemacityLoad", "playerJson keys: ${playerJson.names()}")

                val rawFile = playerJson.opt("file")
                Log.d("CinemacityLoad", "rawFile value: '$rawFile' (type: ${rawFile?.javaClass?.simpleName})")

                fileArray = when {
                    rawFile is JSONArray -> rawFile
                    rawFile is String && rawFile.isNotBlank() -> {
                        val value = rawFile.trim()
                        when {
                            value.startsWith("[") && value.endsWith("]") -> JSONArray(value)
                            value.startsWith("{") && value.endsWith("}") -> JSONArray().apply { put(JSONObject(value)) }
                            else -> JSONArray().apply { put(JSONObject().apply { put("file", value) }) }
                        }
                    }
                    else -> {
                        Log.w("CinemacityLoad", "Playerjs file is empty/null")
                        JSONArray()
                    }
                }

                parsedSubtitles = parseSubtitles(
                    when {
                        playerJson.opt("subtitle") is String ->
                            playerJson.optString("subtitle")
                        fileArray.optJSONObject(0)?.opt("subtitle") is String ->
                            fileArray.optJSONObject(0)?.optString("subtitle")
                        else -> null
                    }
                )
            }
        } else {
            Log.w("CinemacityLoad", "No atob Playerjs script found; checking iframe alternatives")
            fileArray = JSONArray()
            parsedSubtitles = JSONArray()
        }

        // Fallback: extract video URLs from iframes
        if (fileArray.length() == 0) {
            doc.select("iframe").forEach { iframe ->
                val src = iframe.attr("src")
                if (src.isNotBlank()) {
                    Log.d("CinemacityLoad", "Found iframe src: $src")
                    fileArray.put(JSONObject().apply { put("file", src) })
                }
            }
            doc.select("video source, source[src*=m3u8], source[src*=mp4]").forEach { source ->
                val src = source.attr("src")
                if (src.isNotBlank()) {
                    Log.d("CinemacityLoad", "Found direct video source: $src")
                    fileArray.put(JSONObject().apply { put("file", src) })
                }
            }
        }


        val seasonRegex = Regex("Season\\s*(\\d+)", RegexOption.IGNORE_CASE)
        val episodeRegex = Regex("Episode\\s*(\\d+)", RegexOption.IGNORE_CASE)

        val episodeList = mutableListOf<Episode>()

        val movieHrefs: String? = fileArray.optJSONObject(0)
                ?.takeIf { !it.has("folder") }
                ?.optString("file")
                ?.takeIf { it.isNotBlank() }

        val moviejson = movieHrefs?.let {
            JSONObject().apply {
                put("streamUrl", it)
                put("subtitleTracks", parsedSubtitles)
            }.toString()
        }

        if (tvtype == TvType.TvSeries) {
            for (i in 0 until fileArray.length()) {
                val seasonJson = fileArray.getJSONObject(i)

                val seasonNumber = seasonRegex
                    .find(seasonJson.optString("title"))
                    ?.groupValues?.get(1)?.toIntOrNull()
                    ?: continue

                val episodes = seasonJson.optJSONArray("folder") ?: continue
                for (j in 0 until episodes.length()) {
                    val epJson = episodes.getJSONObject(j)

                    val episodeNumber = episodeRegex
                        .find(epJson.optString("title"))
                        ?.groupValues?.get(1)?.toIntOrNull()
                        ?: continue

                    val streamUrls = mutableListOf<String>()

                    epJson.optString("file")
                        .takeIf { it.isNotBlank() }
                        ?.let { streamUrls += it }

                    epJson.optJSONArray("folder")?.let { sources ->
                        for (k in 0 until sources.length()) {
                            sources.optJSONObject(k)
                                ?.optString("file")
                                ?.takeIf { it.isNotBlank() }
                                ?.let { streamUrls += it }
                        }
                    }

                    if (streamUrls.isEmpty()) continue

                    val metaKey = "$seasonNumber:$episodeNumber"
                    val epMeta = epMetaMap[metaKey]

                    val epSubtitleTracks =
                        parseSubtitles(epJson.optString("subtitle"))

                    val epjson = JSONObject().apply {
                        put("streams", JSONArray(streamUrls))
                        put("subtitleTracks", epSubtitleTracks)
                    }.toString()

                    episodeList += newEpisode(epjson) {
                        this.season = seasonNumber
                        this.episode = episodeNumber
                        this.name = epMeta?.title ?: "S${seasonNumber}E${episodeNumber}"
                        this.description = epMeta?.overview
                        this.posterUrl = epMeta?.thumbnail
                        addDate(epMeta?.released)
                    }
                }
            }
            Log.d("CinemacityLoad", "Returning TV series: ${responseData?.meta?.name ?: title}, episodes: ${episodeList.size}")
            return newTvSeriesLoadResponse(
                responseData?.meta?.name ?: title,
                url,
                TvType.TvSeries,
                episodeList
            ) {
                this.backgroundPosterUrl = background ?: bgposter
                this.posterUrl = poster
                //try { this.logoUrl = logoPath } catch(_:Throwable){}
                this.year = year ?: responseData?.meta?.year?.toIntOrNull()
                this.plot = buildString {
                    append(description ?: descriptions)
                    if (!audioLanguages.isNullOrBlank()) {
                        append(" — Audio: ")
                        append(audioLanguages)
                    }
                }
                this.recommendations = recommendation
                this.tags = genre
                this.score = Score.from10(responseData?.meta?.imdbRating)
                this.contentRating = responseData?.meta?.appExtras?.certification
                addImdbId(imdbId)
                addTMDbId(tmdbId)
                addTrailer(trailer)
            }
        }

        Log.d("CinemacityLoad", "Returning movie: ${responseData?.meta?.name ?: title}")

        return newMovieLoadResponse(
            responseData?.meta?.name ?: title,
            url,
            TvType.Movie,
            moviejson ?: "{}"
        ) {
            this.backgroundPosterUrl = background ?: bgposter
            this.posterUrl = poster
            //try { this.logoUrl = logoPath } catch(_:Throwable){}
            this.year = year ?: responseData?.meta?.year?.toIntOrNull()
            this.plot = buildString {
                append(description ?: descriptions)
                if (!audioLanguages.isNullOrBlank()) {
                    append(" — Audio: ")
                    append(audioLanguages)
                }
            }
            this.recommendations = recommendation
            this.tags = genre
            this.contentRating = responseData?.meta?.appExtras?.certification
            this.score = Score.from10(responseData?.meta?.imdbRating)
            addImdbId(imdbId)
            addTMDbId(tmdbId)
            addTrailer(trailer)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("CinemacityLinks", "loadLinks data length: ${data.length}, preview: ${data.take(200)}")
        if (data.isBlank() || data == "null" || data == "{}") {
            Log.w("CinemacityLinks", "data is empty, no streams available")
            return false
        }
        val obj = try { JSONObject(data) } catch (e: Exception) {
            Log.e("CinemacityLinks", "Failed to parse data JSON: ${e.message}")
            return false
        }

        obj.optJSONArray("subtitleTracks")?.let { subs ->
            for (i in 0 until subs.length()) {
                val s = subs.getJSONObject(i)
                subtitleCallback(
                    newSubtitleFile(
                        s.getString("language"),
                        s.getString("subtitleUrl")
                    )
                )
            }
        }

        val streamUrls = mutableListOf<String>()

        obj.optJSONArray("streams")?.let { arr ->
            for (i in 0 until arr.length()) {
                arr.optString(i)
                    .takeIf { it.isNotBlank() }
                    ?.let { streamUrls += it }
            }
        }

        if (streamUrls.isEmpty()) {
            obj.optString("streamUrl")
                .takeIf { it.isNotBlank() }
                ?.let { streamUrls += it }
        }

        if (streamUrls.isEmpty()) return false

        streamUrls.forEach { url ->
            Log.d("Cinemacity", "Cargando link: $url")
            callback(
                newExtractorLink(
                    name,
                    name,
                    url,
                    INFER_TYPE
                ) {
                    this.referer = mainUrl
                    this.quality = extractQuality(url)
                }
            )
        }

        return true
    }


    fun extractQuality(url: String): Int {
        return when {
            url.contains("2160p") -> Qualities.P2160.value
            url.contains("1440p") -> Qualities.P1440.value
            url.contains("1080p") -> Qualities.P1080.value
            url.contains("720p")  -> Qualities.P720.value
            url.contains("480p")  -> Qualities.P480.value
            url.contains("360p")  -> Qualities.P360.value
            else -> Qualities.Unknown.value
        }
    }

    fun parseSubtitles(raw: String?): JSONArray {
        val tracks = JSONArray()
        if (raw.isNullOrBlank()) return tracks

        raw.split(",").forEach { entry ->
            val match = Regex("""\[(.+?)](https?://.+)""").find(entry.trim())
            if (match != null) {
                tracks.put(
                    JSONObject().apply {
                        put("language", match.groupValues[1])
                        put("subtitleUrl", match.groupValues[2])
                    }
                )
            }
        }
        return tracks
    }

}