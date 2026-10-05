package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.nodes.Document
import java.net.URLDecoder
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "DonghuaLife"
private const val MOVIE_PREFIX = "pelis:"

private data class LdInfo(
    val title: String? = null,
    val plot: String? = null,
    val poster: String? = null,
    val year: Int? = null,
    val tags: List<String> = emptyList(),
)

private data class Ep(
    val number: Int,
    val title: String,
    val poster: String?,
)

private data class Season(
    val slug: String,
    val number: Int,
    val episodes: List<Ep>,
)

private data class Src(
    val label: String,
    val provider: String,
    val token: String,
    val priority: Int,
)

private fun JSONObject.strOrNull(key: String): String? {
    if (!has(key) || isNull(key)) return null
    return optString(key).trim().takeIf { it.isNotEmpty() }
}

private fun JSONObject.intOrNull(key: String): Int? {
    if (!has(key) || isNull(key)) return null
    return optInt(key)
}

private fun jsonArrayOf(json: String?, key: String): JSONArray? {
    if (json.isNullOrBlank()) return null
    return try {
        val obj = JSONObject(json)
        if (obj.has(key)) obj.optJSONArray(key) else null
    } catch (e: Exception) {
        Log.w(TAG, "JSON inválido ($key): ${e.message}")
        null
    }
}

private fun JSONObject.optArray(key: String): JSONArray? {
    if (!has(key) || isNull(key)) return null
    return optJSONArray(key)
}

class DonghualifeProvider : MainAPI() {
    override var mainUrl = "https://donghualife.com"
    override var name = "DonghuaLife"
    override val hasMainPage = true
    override var lang = "mx"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val browserHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
    )

    private val jsonHeaders = mapOf(
        "Content-Type" to "application/json",
        "Accept" to "application/json, text/plain, */*",
        "Origin" to mainUrl,
    )

    private val vttStore = ConcurrentHashMap<String, String>()

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private fun unescapePayload(raw: String): String = raw.replace("\\\"", "\"")

    private fun extractBalanced(raw: String, key: String, open: Char, close: Char): String? {
        val marker = "\"$key\":"
        val keyStart = raw.indexOf(marker)
        if (keyStart < 0) return null
        val start = raw.indexOf(open, keyStart + marker.length)
        if (start < 0) return null
        var depth = 0
        var inString = false
        var escaped = false
        var i = start
        while (i < raw.length) {
            val c = raw[i]
            if (inString) {
                when {
                    escaped -> escaped = false
                    c == '\\' -> escaped = true
                    c == '"' -> inString = false
                }
            } else {
                when (c) {
                    '"' -> inString = true
                    open -> depth++
                    close -> {
                        depth--
                        if (depth == 0) return raw.substring(start, i + 1)
                    }
                }
            }
            i++
        }
        return null
    }

    private fun absoluteImage(src: String?): String? {
        if (src.isNullOrBlank()) return null
        if (src.startsWith("http", true)) return src
        val decoded = if (src.contains("/_next/image")) {
            Regex("[?&]url=([^&]+)").find(src)?.groupValues?.get(1)
                ?.let { runCatching { URLDecoder.decode(it, "UTF-8") }.getOrNull() } ?: src
        } else src
        return fixUrl(decoded)
    }

    private fun parseLdJson(doc: Document): LdInfo? {
        val raw = doc.selectFirst("script[type=application/ld+json]")?.data()?.trim() ?: return null
        return try {
            val obj = JSONObject(raw)
            val genres = when (val g = obj.opt("genre")) {
                is JSONArray -> (0 until g.length()).mapNotNull { g.optString(it).trim().takeIf { s -> s.isNotEmpty() } }
                is String -> listOf(g.trim()).filter { it.isNotEmpty() }
                else -> emptyList()
            }
            LdInfo(
                title = obj.strOrNull("name"),
                plot = obj.strOrNull("description"),
                poster = obj.strOrNull("image"),
                year = obj.strOrNull("datePublished")?.take(4)?.takeIf { s -> s.all { it.isDigit() } }?.toIntOrNull(),
                tags = genres,
            )
        } catch (e: Exception) {
            Log.w(TAG, "JSON-LD inválido: ${e.message}")
            null
        }
    }

    private suspend fun fetchDoc(url: String): Document? = try {
        app.get(url, headers = browserHeaders, timeout = 30L).document
    } catch (e: Exception) {
        Log.w(TAG, "GET $url falló: ${e.message}")
        null
    }

    // ------------------------------------------------------------------
    // Listados
    // ------------------------------------------------------------------

    private fun parseCards(doc: Document?): List<SearchResponse> {
        if (doc == null) return emptyList()
        val out = mutableListOf<SearchResponse>()
        for (a in doc.select("a.poster-card[href]")) {
            val href = a.attr("abs:href")
            if (href.isBlank()) continue
            val img = a.selectFirst("img")
            val title = img?.attr("alt")?.takeIf { it.isNotBlank() }
                ?: a.selectFirst("p")?.text()?.trim()?.takeIf { it.isNotBlank() }
                ?: a.text().trim()
            if (title.isBlank()) continue
            val poster = absoluteImage(img?.attr("src"))
            val slug = href.substringAfterLast("/").substringBefore("?")
            if (slug.isBlank()) continue
            if (href.contains("/peliculas/")) {
                out.add(newMovieSearchResponse(title, MOVIE_PREFIX + slug, TvType.AnimeMovie) {
                    this.posterUrl = poster
                })
            } else {
                out.add(newAnimeSearchResponse(title, slug, TvType.Anime) { this.posterUrl = poster })
            }
        }
        return out
    }

    private fun parseLatestEpisodes(doc: Document?): List<SearchResponse> {
        if (doc == null) return emptyList()
        val out = mutableListOf<SearchResponse>()
        val seen = mutableSetOf<String>()
        for (a in doc.select("a[href^=/watch/]")) {
            val watchId = a.attr("href").removePrefix("/watch/").substringBefore("?")
            if (watchId.isBlank() || !seen.add(watchId)) continue
            val img = a.selectFirst("img")
            val seriesName = img?.attr("alt")?.takeIf { it.isNotBlank() } ?: continue
            val epTitle = a.select("p").lastOrNull()?.text()?.trim()?.takeIf { it.isNotEmpty() }
            val poster = absoluteImage(img?.attr("src"))
            val title = if (epTitle.isNullOrBlank() || epTitle.equals(seriesName, true)) seriesName else "$seriesName - $epTitle"
            out.add(newAnimeSearchResponse(title, watchId, TvType.Anime) { this.posterUrl = poster })
        }
        return out
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? = coroutineScope {
        val series = async { fetchDoc("$mainUrl/series?page=$page") }
        val recent = async { fetchDoc("$mainUrl/series?page=$page&sort=latest") }
        val movies = async { fetchDoc("$mainUrl/peliculas?page=$page") }
        val home = async { if (page == 1) fetchDoc("$mainUrl/") else null }

        val lists = mutableListOf<HomePageList>()
        val suffix = if (page == 1) "" else " p.$page"

        parseCards(series.await()).takeIf { it.isNotEmpty() }
            ?.let { lists.add(HomePageList("Popular$suffix", it)) }
        parseCards(recent.await()).takeIf { it.isNotEmpty() }
            ?.let { lists.add(HomePageList("Recientes$suffix", it)) }
        parseCards(movies.await()).takeIf { it.isNotEmpty() }
            ?.let { lists.add(HomePageList("Películas$suffix", it)) }
        parseLatestEpisodes(home.await()).takeIf { it.isNotEmpty() }
            ?.let { lists.add(HomePageList("Últimos episodios", it)) }

        if (lists.isEmpty()) null else newHomePageResponse(lists, page < 12)
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        val q = query.trim()
        if (q.isEmpty()) return null
        val enc = java.net.URLEncoder.encode(q, "UTF-8")
        return coroutineScope {
            val series = async { parseCards(fetchDoc("$mainUrl/series?q=$enc")) }
            val movies = async { parseCards(fetchDoc("$mainUrl/peliculas?q=$enc")) }
            (series.await() + movies.await()).distinctBy { it.url }
        }
    }

    // ------------------------------------------------------------------
    // Detalle
    // ------------------------------------------------------------------

    private fun parseEpisodes(array: JSONArray?): List<Ep> {
        if (array == null) return emptyList()
        val out = mutableListOf<Ep>()
        for (i in 0 until array.length()) {
            val obj = array.optJSONObject(i) ?: continue
            val number = obj.intOrNull("number") ?: (i + 1)
            val title = obj.strOrNull("title") ?: "Episodio $number"
            out.add(Ep(number, title, obj.strOrNull("image")))
        }
        return out
    }

    private suspend fun seasonEpisodes(seriesSlug: String, slug: String, initial: JSONArray?): List<Ep> {
        val inline = parseEpisodes(initial)
        if (inline.isNotEmpty()) return inline
        return try {
            val body = app.get(
                "$mainUrl/api/series/$seriesSlug/seasons/$slug/episodes",
                headers = jsonHeaders + ("Referer" to "$mainUrl/series/$seriesSlug"),
                timeout = 30L,
            ).text
            parseEpisodes(jsonArrayOf(body, "episodes"))
        } catch (e: Exception) {
            Log.w(TAG, "episodios de $slug fallaron: ${e.message}")
            emptyList()
        }
    }

    private suspend fun loadSeriesEpisodes(seriesSlug: String, seasonsJson: String): List<Season> {
        val arr = try {
            JSONArray(seasonsJson)
        } catch (e: Exception) {
            Log.w(TAG, "seasons inválido: ${e.message}")
            return emptyList()
        }

        val raw = mutableListOf<Triple<String, JSONArray?, Boolean>>()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val slug = o.strOrNull("slug") ?: continue
            raw.add(Triple(slug, o.optArray("initialEpisodes"), o.optBoolean("isSpecial", false)))
        }
        if (raw.isEmpty()) return emptyList()

        val numeric = raw.mapNotNull { it.first.substringAfterLast('-').toIntOrNull() }.filter { it > 0 }
        val base = (numeric.maxOrNull() ?: raw.size).let { if (it > 0) it else raw.size }
        val numbers = mutableListOf<Int>()
        var specials = 0
        raw.forEachIndexed { idx, item ->
            val parsed = item.first.substringAfterLast('-').toIntOrNull()
            numbers.add(
                when {
                    parsed != null && parsed > 0 -> parsed
                    item.third -> base + 1 + specials++
                    else -> base + 1 + idx
                }
            )
        }

        val loaded = coroutineScope {
            raw.map { item -> async { seasonEpisodes(seriesSlug, item.first, item.second) } }.awaitAll()
        }

        return raw.mapIndexed { idx, item -> Season(item.first, numbers[idx], loaded.getOrElse(idx) { emptyList() }) }
    }

    override suspend fun load(url: String): LoadResponse? {
        val doc = fetchDoc(url) ?: return null
        val ld = parseLdJson(doc)

        val rawUrl = url.substringBefore("?")
        val slug = rawUrl.substringAfterLast("/").substringBefore("?")
        val isMovie = rawUrl.contains("/peliculas/")

        val title = ld?.title?.takeIf { it.isNotEmpty() }
            ?: doc.selectFirst("h1")?.text()?.trim()?.takeIf { it.isNotEmpty() }
            ?: "Sin título"
        val plot = ld?.plot?.takeIf { it.isNotEmpty() }
            ?: doc.selectFirst("meta[name=description]")?.attr("content")?.trim()
        val poster = absoluteImage(ld?.poster)
            ?: absoluteImage(doc.selectFirst("meta[property=og:image]")?.attr("content"))
        val tags = ld?.tags?.takeIf { it.isNotEmpty() }

        if (rawUrl.contains("/watch/")) {

            val watchId = rawUrl.substringAfter("/watch/").substringBefore("?")
            val epTitle = doc.selectFirst("h1")?.text()?.trim()?.takeIf { it.isNotEmpty() } ?: title
            return newMovieLoadResponse(epTitle, watchId, TvType.AnimeMovie, watchId) {
                this.posterUrl = poster
                this.plot = plot
                this.year = ld?.year
                this.tags = tags
            }
        }

        if (isMovie) {
            val movieData = MOVIE_PREFIX + slug
            return newMovieLoadResponse(title, movieData, TvType.AnimeMovie, movieData) {
                this.posterUrl = poster
                this.plot = plot
                this.year = ld?.year
                this.tags = tags
            }
        }

        val payload = unescapePayload(doc.html())
        val seasonsJson = extractBalanced(payload, "seasons", '[', ']')
        if (seasonsJson.isNullOrBlank()) {
            Log.w(TAG, "sin temporadas en $slug")
            return newMovieLoadResponse(title, slug, TvType.AnimeMovie, MOVIE_PREFIX + slug) {
                this.posterUrl = poster
                this.plot = plot
                this.year = ld?.year
                this.tags = tags
            }
        }

        val seasons = loadSeriesEpisodes(slug, seasonsJson)
        val episodes = mutableListOf<Episode>()
        val used = mutableSetOf<String>()
        for (season in seasons) {
            for ((idx, ep) in season.episodes.sortedBy { it.number }.withIndex()) {
                val watchId = "${season.slug}-${ep.number}"
                if (!used.add(watchId)) continue
                episodes.add(newEpisode(watchId) {
                    this.name = ep.title
                    this.episode = ep.number
                    this.season = season.number
                    this.posterUrl = absoluteImage(ep.poster)
                })
            }
        }

        if (episodes.isEmpty()) {
            return newMovieLoadResponse(title, slug, TvType.AnimeMovie, MOVIE_PREFIX + slug) {
                this.posterUrl = poster
                this.plot = plot
                this.year = ld?.year
                this.tags = tags
            }
        }

        episodes.sortWith(compareBy({ it.season ?: 0 }, { it.episode ?: 0 }))
        return newTvSeriesLoadResponse(title, slug, TvType.Anime, episodes) {
            this.posterUrl = poster
            this.plot = plot
            this.year = ld?.year
            this.tags = tags
        }
    }

    // ------------------------------------------------------------------
    // Fuentes
    // ------------------------------------------------------------------

    private suspend fun postJson(path: String, json: String, referer: String): String? = try {
        app.post(
            "$mainUrl$path",
            headers = jsonHeaders + ("Referer" to referer),
            requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
            timeout = 30L,
        ).text
    } catch (e: Exception) {
        Log.d(TAG, "POST $path falló: ${e.message}")
        null
    }

    private fun urlFrom(body: String?): String? = try {
        body?.let { JSONObject(it).strOrNull("url") }
    } catch (e: Exception) {
        null
    }

    private suspend fun resolveSource(token: String, referer: String): String? {
        val direct = urlFrom(postJson("/api/player/source", """{"token":"$token"}""", referer))
        if (!direct.isNullOrBlank()) return direct

        val refreshed = try {
            postJson("/api/player/refresh", """{"token":"$token"}""", referer)
                ?.let { JSONObject(it).strOrNull("token") }
        } catch (e: Exception) {
            null
        } ?: return null
        Log.d(TAG, "token refrescado, reintentando")

        return urlFrom(postJson("/api/player/source", """{"token":"$refreshed"}""", referer))
    }

    private fun okruQuality(okName: String?): Int = when (okName?.lowercase()) {
        "full" -> Qualities.P1080.value
        "hd" -> Qualities.P720.value
        "sd" -> Qualities.P480.value
        "low" -> Qualities.P360.value
        "lowest", "mobile" -> Qualities.P240.value
        else -> Qualities.Unknown.value
    }

    private suspend fun emitOkru(videoUrl: String, callback: (ExtractorLink) -> Unit): Boolean {
        val videoId = Regex("""ok\.ru/(?:videoembed|video)/(\d+)""").find(videoUrl)?.groupValues?.getOrNull(1)
            ?: return false
        val embedUrl = "https://ok.ru/videoembed/$videoId"

        val metadata = try {
            val doc = app.get(embedUrl, referer = "https://ok.ru/", headers = browserHeaders, timeout = 30L).document
            val raw = doc.selectFirst("[data-options]")?.attr("data-options")
            if (raw.isNullOrBlank()) null
            else JSONObject(raw).optJSONObject("flashvars")?.optJSONObject("metadata")
        } catch (e: Exception) {
            Log.w(TAG, "ok.ru $videoId falló: ${e.message}")
            null
        }

        var emitted = false
        metadata?.strOrNull("hlsManifestUrl")?.let { hls ->
            callback(newExtractorLink(name, "ok.ru", hls, ExtractorLinkType.M3U8) {
                this.referer = embedUrl
                this.quality = Qualities.P1080.value
            })
            Log.d(TAG, "ok.ru $videoId -> HLS")
            emitted = true
        }

        if (!emitted) {
            val videos = metadata?.optArray("videos")
            if (videos != null) {
                for (i in videos.length() - 1 downTo 0) {
                    val v = videos.optJSONObject(i) ?: continue
                    val u = v.strOrNull("url") ?: continue
                    val vn = v.strOrNull("name")
                    callback(newExtractorLink(name, "ok.ru ${vn ?: ""}".trim(), u, ExtractorLinkType.VIDEO) {
                        this.referer = embedUrl
                        this.quality = okruQuality(vn)
                    })
                    emitted = true
                }
            }
        }
        if (!emitted) Log.w(TAG, "ok.ru $videoId: sin URLs")
        return emitted
    }

    private suspend fun emitSubtitles(
        episodeId: String?,
        pageUrl: String,
        subtitleCallback: (SubtitleFile) -> Unit,
    ) {
        if (episodeId.isNullOrBlank()) return
        try {
            val listBody = app.get(
                "$mainUrl/api/subtitles?episodeId=$episodeId",
                headers = jsonHeaders + ("Referer" to pageUrl),
                timeout = 30L,
            ).text
            val subs = jsonArrayOf(listBody, "subtitles") ?: return
            Log.d(TAG, "subtítulos: ${subs.length()}")
            for (i in 0 until subs.length()) {
                val s = subs.optJSONObject(i) ?: continue
                val id = s.strOrNull("id") ?: continue
                val label = s.strOrNull("language") ?: "es"
                val name = s.strOrNull("label")
                val subName = if (name.isNullOrBlank()) label else "$label ($name)"

                if (!vttStore.containsKey(id)) {
                    val content = try {
                        app.get(
                            "$mainUrl/api/subtitles/$id",
                            headers = jsonHeaders + ("Referer" to pageUrl),
                            timeout = 30L,
                        ).let { JSONObject(it.text).strOrNull("content") }
                    } catch (e: Exception) {
                        Log.w(TAG, "subtítulo $id falló: ${e.message}")
                        null
                    } ?: continue
                    val vtt = AssToVtt.convert(content)
                    if (vtt.isBlank()) continue
                    vttStore[id] = vtt
                }
                subtitleCallback(newSubtitleFile(subName, "$mainUrl/__sub/$id.vtt"))
            }
        } catch (e: Exception) {
            Log.w(TAG, "subtítulos no disponibles: ${e.message}")
        }
    }

    private fun parseSources(payload: String): List<Src> {
        val arrJson = extractBalanced(payload, "sources", '[', ']') ?: return emptyList()
        val arr = try {
            JSONArray(arrJson)
        } catch (e: Exception) {
            Log.w(TAG, "sources inválido: ${e.message}")
            return emptyList()
        }
        val out = mutableListOf<Src>()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val token = o.strOrNull("token") ?: continue
            val provider = (o.strOrNull("provider") ?: o.strOrNull("label") ?: "Fuente").lowercase()
            val label = o.strOrNull("label") ?: o.strOrNull("name") ?: provider
            out.add(Src(label, provider, token, o.intOrNull("priority") ?: Int.MAX_VALUE))
        }
        return out
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val isMovie = data.startsWith(MOVIE_PREFIX)
        val pageUrl = if (isMovie) "$mainUrl/peliculas/${data.removePrefix(MOVIE_PREFIX)}" else "$mainUrl/watch/$data"

        val doc = fetchDoc(pageUrl) ?: return false
        val payload = unescapePayload(doc.html())

        val sources = parseSources(payload)
        if (sources.isEmpty()) {
            Log.w(TAG, "sin fuentes en $pageUrl")
            loadExtractor(pageUrl, pageUrl, subtitleCallback, callback)
            return false
        }

        val episodeId = Regex("\"episodeId\":\"([^\"]+)\"").find(payload)?.groupValues?.getOrNull(1)
        emitSubtitles(episodeId, pageUrl, subtitleCallback)

        var found = false
        for (src in sources.sortedBy { it.priority }) {
            val url = resolveSource(src.token, pageUrl)
            if (url.isNullOrBlank()) {
                Log.w(TAG, "no se pudo resolver ${src.label}")
                continue
            }

            if (url.contains("ok.ru")) {
                if (emitOkru(url, callback)) found = true
            } else if (url.contains(".m3u8")) {
                callback(newExtractorLink(name, src.label, url, ExtractorLinkType.M3U8) {
                    this.referer = pageUrl
                    this.headers = mapOf(
                        "User-Agent" to browserHeaders["User-Agent"]!!,
                        "Referer" to pageUrl,
                    )
                })
                found = true
            } else {
                val collected = mutableListOf<ExtractorLink>()
                val collector: (ExtractorLink) -> Unit = { link -> collected.add(link) }
                loadExtractor(url, pageUrl, subtitleCallback, collector)
                if (collected.isEmpty()) {
                    Log.w(TAG, "sin links para ${src.label} ($url)")
                } else {
                    for (link in collected) {
                        callback(newExtractorLink(name, "${src.label} ${link.name}".trim(), link.url) {
                            this.referer = link.referer ?: pageUrl
                            this.quality = link.quality
                            this.type = if (link.isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            this.headers = link.headers
                        })
                    }
                    found = true
                }
            }
        }
        Log.d(TAG, "loadLinks $pageUrl -> $found (${sources.size} fuentes)")
        return found
    }

    // ------------------------------------------------------------------
    // Interceptor: subtítulos internos (.vtt) + cabeceras de ok.ru
    // ------------------------------------------------------------------

    @Suppress("ObjectLiteralToLambda")
    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        val referer = extractorLink.referer
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val url = request.url.toString()

                if (url.contains("/__sub/") && url.contains(".vtt")) {
                    val id = url.substringAfter("/__sub/").substringBefore(".vtt")
                    val vtt = vttStore[id]
                    if (vtt != null) {
                        return Response.Builder()
                            .request(request)
                            .protocol(Protocol.HTTP_1_1)
                            .code(200)
                            .message("OK")
                            .header("Content-Type", "text/vtt")
                            .body(vtt.toResponseBody("text/vtt".toMediaTypeOrNull()))
                            .build()
                    }
                }

                if (url.contains("okcdn.ru") || url.contains("ok.ru/")) {
                    val newReq = request.newBuilder()
                        .header("User-Agent", browserHeaders["User-Agent"]!!)
                        .header("Referer", referer ?: "https://ok.ru/")
                        .header("Accept", "*/*")
                        .build()
                    return chain.proceed(newReq)
                }

                return chain.proceed(request)
            }
        }
    }
}

private object AssToVtt {
    fun convert(ass: String): String {
        if (ass.isBlank()) return ""

        var playResX = 192
        var playResY = 108
        var section = ""
        val styles = HashMap<String, Int>()
        val cues = StringBuilder()

        fun time(v: String): String {
            val parts = v.trim().split(":")
            if (parts.size < 2) return "00:00:00.000"
            val h = parts[0].toIntOrNull() ?: 0
            val m = parts[1].toIntOrNull() ?: 0
            val s = parts.getOrNull(2)?.toFloatOrNull() ?: 0f
            return "%02d:%02d:%06.3f".format(h, m, s)
        }

        fun numpadFrom(x: Float, y: Float): Int {
            val col = when {
                x < 0.33f -> 1
                x < 0.66f -> 2
                else -> 3
            }
            val row = when {
                y > 0.66f -> 1
                y > 0.33f -> 4
                else -> 7
            }
            return row + col - 1
        }

        for (rawLine in ass.split("\n")) {
            val line = rawLine.replace("\r", "").trim()
            if (line.isEmpty()) continue

            if (line.startsWith("[") && line.endsWith("]")) {
                section = line.removePrefix("[").removeSuffix("]").lowercase()
                continue
            }

            when {
                section.contains("script info") -> {
                    if (line.startsWith("PlayResX", true)) {
                        playResX = line.substringAfter(':').trim().toIntOrNull() ?: playResX
                    } else if (line.startsWith("PlayResY", true)) {
                        playResY = line.substringAfter(':').trim().toIntOrNull() ?: playResY
                    }
                }

                section.contains("styles") -> {
                    if (line.startsWith("Style:", true)) {
                        val parts = line.substringAfter(':').split(",")
                        if (parts.size > 18) {
                            styles[parts[0].trim()] = (parts[18].trim().toIntOrNull() ?: 2).coerceIn(1, 9)
                        }
                    }
                }

                section.contains("events") -> {
                    if (!line.startsWith("Dialogue:", true)) continue
                    val parts = line.substringAfter(':').split(",", limit = 10)
                    if (parts.size < 10) continue
                    val start = time(parts[1])
                    val end = time(parts[2])
                    val styleName = parts[3].trim()
                    val text = parts[9]

                    val anOverride = Regex("""\\an(\d)""").find(text)?.groupValues?.getOrNull(1)?.toIntOrNull()
                    val posOverride = Regex("""\\pos\(\s*([\d.]+)\s*,\s*([\d.]+)\s*\)""").find(text)?.let { m ->
                        val x = m.groupValues[1].toFloatOrNull()
                        val y = m.groupValues[2].toFloatOrNull()
                        if (x != null && y != null) {
                            numpadFrom((x / playResX).coerceIn(0f, 1f), (y / playResY).coerceIn(0f, 1f))
                        } else null
                    }

                    val align = anOverride ?: posOverride ?: styles[styleName] ?: 2

                    var clean = Regex("""\{[^}]*}""").replace(text) { "" }
                    clean = clean.replace("\\N", "\n").replace("\\n", "\n").replace("\\h", " ").trim()
                    if (clean.isEmpty()) continue
                    if (align != 2) clean = "{an$align}$clean"

                    cues.append(start).append(" --> ").append(end).append("\n")
                    cues.append(clean).append("\n\n")
                }
            }
        }

        if (cues.isBlank()) return ""
        return "WEBVTT\n\n$cues"
    }
}