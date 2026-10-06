package com.example

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URLDecoder
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "DonghuaLife"

private fun watchUrl(id: String): String = "https://donghualife.com/watch/$id"
private fun seriesUrl(slug: String): String = "https://donghualife.com/series/$slug"
private fun movieUrl(slug: String): String = "https://donghualife.com/peliculas/$slug"

private val GENRE_SECTIONS = listOf(
    "Acción",
    "Aventura",
    "Cultivo",
    "Animación",
    "Fantasía",
    "Romance",
    "Drama",
    "Artes marciales",
)

private fun encGenre(name: String): String =
    java.net.URLEncoder.encode(name, "UTF-8").replace("+", "%20")

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

private data class SeasonRaw(
    val slug: String,
    val initial: JSONArray?,
    val special: Boolean,
    val count: Int,
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
    companion object {
        var pluginContext: android.content.Context? = null
    }

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

    private suspend fun fetchDoc(url: String): Document? {
        val t0 = System.currentTimeMillis()
        return try {
            val text = app.get(url, headers = browserHeaders, timeout = 30L).text

            val doc = Jsoup.parse(text, url)
            Log.d(TAG, "GET ok ${System.currentTimeMillis() - t0}ms len=${text.length} $url")
            doc
        } catch (e: Exception) {
            Log.w(TAG, "GET falló ${System.currentTimeMillis() - t0}ms $url -> ${e.javaClass.simpleName}: ${e.message}")
            null
        }
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
                out.add(newMovieSearchResponse(title, movieUrl(slug), TvType.AnimeMovie) {
                    this.posterUrl = poster
                })
            } else {
                out.add(newAnimeSearchResponse(title, seriesUrl(slug), TvType.Anime) { this.posterUrl = poster })
            }
        }
        return out
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? = coroutineScope {
        val t0 = System.currentTimeMillis()
        Log.d(TAG, "getMainPage page=$page")
val series = async { fetchDoc("$mainUrl/series?page=$page") }
        val recent = async { fetchDoc("$mainUrl/series?page=$page&sort=latest") }
        val movies = async { fetchDoc("$mainUrl/peliculas?page=$page") }

        val genreDocs = GENRE_SECTIONS.map { g -> g to async { fetchDoc("$mainUrl/genres/${encGenre(g)}?page=$page") } }

        val lists = mutableListOf<HomePageList>()
        val suffix = if (page == 1) "" else " p.$page"

        val seriesList = parseCards(series.await())
        val recentList = parseCards(recent.await())
        val movieList = parseCards(movies.await())
        val genreLists = genreDocs.map { (genre, deferred) -> genre to parseCards(deferred.await()) }

        seriesList.takeIf { it.isNotEmpty() }
            ?.let { lists.add(HomePageList("Popular$suffix", it)) }
        recentList.takeIf { it.isNotEmpty() }
            ?.let { lists.add(HomePageList("Recientes$suffix", it)) }
        movieList.takeIf { it.isNotEmpty() }
            ?.let { lists.add(HomePageList("Películas$suffix", it)) }
        genreLists.forEach { (genre, items) ->
            items.takeIf { it.isNotEmpty() }
                ?.let { lists.add(HomePageList("$genre$suffix", it)) }
        }

        Log.d(
            TAG,
            "getMainPage page=$page ${System.currentTimeMillis() - t0}ms | " +
                "popular=${seriesList.size} recientes=${recentList.size} pelis=${movieList.size} listas=${lists.size} | " +
                "generos=${genreLists.joinToString { "${it.first}=${it.second.size}" }}"
        )
        if (lists.isEmpty()) {
            Log.w(TAG, "getMainPage page=$page sin listas")
            null
        } else newHomePageResponse(lists, page < 12)
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        val q = query.trim()
        Log.d(TAG, "search() llamado: '$q'")
        if (q.isEmpty()) return null
        val enc = java.net.URLEncoder.encode(q, "UTF-8")
        return try {
            coroutineScope {
                val series = async { parseCards(fetchDoc("$mainUrl/series?q=$enc")) }
                val movies = async { parseCards(fetchDoc("$mainUrl/peliculas?q=$enc")) }
                val s = series.await()
                val m = movies.await()
                val out = (s + m).distinctBy { it.url }
                Log.d(TAG, "search '$q' -> ${s.size} series, ${m.size} peliculas, ${out.size} total")
                if (out.isEmpty()) null else out
            }
        } catch (e: CancellationException) {
            Log.w(TAG, "search '$q' cancelada: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "search '$q' falló: ${e.message}")
            null
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

    private suspend fun seasonEpisodes(seriesSlug: String, slug: String, initial: JSONArray?, expected: Int): List<Ep> {
        val inline = parseEpisodes(initial)

        if (inline.isNotEmpty() && (expected <= 0 || inline.size >= expected)) {
            Log.d(TAG, "temporada $slug: inline ${inline.size}/$expected (sin API)")
            return inline
        }
        Log.d(TAG, "temporada $slug: inline ${inline.size}/$expected -> consultando API")
        val t0 = System.currentTimeMillis()
        return try {
            val body = app.get(
                "$mainUrl/api/series/$seriesSlug/seasons/$slug/episodes",
                headers = jsonHeaders + ("Referer" to "$mainUrl/series/$seriesSlug"),
                timeout = 30L,
            ).text
            val api = parseEpisodes(jsonArrayOf(body, "episodes"))
            Log.d(TAG, "temporada $slug: API devolvió ${api.size}/${expected} en ${System.currentTimeMillis() - t0}ms")
            if (api.isEmpty()) inline else api
        } catch (e: Exception) {
            Log.w(TAG, "episodios de $slug fallaron: ${e.message} (inline ${inline.size})")
            inline
        }
    }

    private suspend fun loadSeriesEpisodes(seriesSlug: String, seasonsJson: String): List<Season> {
        val arr = try {
            JSONArray(seasonsJson)
        } catch (e: Exception) {
            Log.w(TAG, "seasons inválido: ${e.message}")
            return emptyList()
        }

        val raw = mutableListOf<SeasonRaw>()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val slug = o.strOrNull("slug") ?: continue
            raw.add(SeasonRaw(slug, o.optArray("initialEpisodes"), o.optBoolean("isSpecial", false), o.optInt("episodeCount", 0)))
        }
        if (raw.isEmpty()) return emptyList()

        fun seasonNumber(slug: String): Int? {
            val parts = slug.split('-')
            for (i in parts.indices.reversed()) {
                val n = parts[i].toIntOrNull() ?: continue
                if (n > 0) return n
            }
            return null
        }

        val numeric = raw.mapNotNull { seasonNumber(it.slug) }
        val base = (numeric.maxOrNull() ?: raw.size).let { if (it > 0) it else raw.size }
        val numbers = mutableListOf<Int>()
        var specials = 0
        raw.forEachIndexed { idx, item ->
            numbers.add(
                when {
                    item.special -> base + 1 + specials++
                    else -> seasonNumber(item.slug) ?: (base + 1 + idx)
                }
            )
        }

        val loaded = coroutineScope {
            raw.map { item -> async { seasonEpisodes(seriesSlug, item.slug, item.initial, item.count) } }.awaitAll()
        }

        return raw.mapIndexed { idx, item -> Season(item.slug, numbers[idx], loaded.getOrElse(idx) { emptyList() }) }
    }

    private suspend fun episodeResponse(doc: Document?, watchId: String): LoadResponse? {
        if (doc == null) {
            Log.w(TAG, "no se pudo cargar el episodio $watchId")
            return null
        }
        val ld = parseLdJson(doc)
        val h1 = doc.selectFirst("h1")?.text()?.trim()?.takeIf { it.isNotEmpty() }
        val epTitle = h1 ?: ld?.title ?: "Episodio"
        return newMovieLoadResponse(epTitle, watchId, TvType.AnimeMovie, watchUrl(watchId)) {
            this.posterUrl = absoluteImage(ld?.poster)
                ?: absoluteImage(doc.selectFirst("meta[property=og:image]")?.attr("content"))
            this.plot = ld?.plot ?: doc.selectFirst("meta[name=description]")?.attr("content")?.trim()
            this.tags = ld?.tags
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val t0 = System.currentTimeMillis()
        val rawUrl = url.substringBefore("?")
        val slug = rawUrl.substringAfterLast("/")

        val watchId = when {
            rawUrl.contains("/watch/") -> rawUrl.substringAfter("/watch/")
            !rawUrl.contains('/') && !rawUrl.contains("://") && Regex("-").containsMatchIn(rawUrl) -> rawUrl
            else -> null
        }
        if (watchId != null) {
            Log.d(TAG, "load($url) -> Episodio $watchId")
            val res = episodeResponse(fetchDoc(watchUrl(watchId)), watchId)
            Log.d(TAG, "load($url) -> Episodio ${if (res != null) "ok" else "FALLÓ"} ${System.currentTimeMillis() - t0}ms")
            return res
        }

        val pageUrl = when {
            rawUrl.contains("://") -> rawUrl
            slug.contains('-') -> "$mainUrl/watch/$slug"
            else -> "$mainUrl/${rawUrl.trimStart('/')}"
        }
        Log.d(TAG, "load($url) -> pagina $pageUrl")
        val doc = fetchDoc(pageUrl) ?: run {
            Log.w(TAG, "load($url) -> pagina vacía/indisponible ${System.currentTimeMillis() - t0}ms")
            return null
        }
        val ld = parseLdJson(doc)
        val isMovie = pageUrl.contains("/peliculas/")
        Log.d(TAG, "load pagina=$pageUrl | jsonld=${ld != null} titulo=${ld?.title ?: "(sin json-ld)"}")

        val title = ld?.title?.takeIf { it.isNotEmpty() }
            ?: doc.selectFirst("h1")?.text()?.trim()?.takeIf { it.isNotEmpty() }
            ?: "Sin título"
        val plot = ld?.plot?.takeIf { it.isNotEmpty() }
            ?: doc.selectFirst("meta[name=description]")?.attr("content")?.trim()
        val poster = absoluteImage(ld?.poster)
            ?: absoluteImage(doc.selectFirst("meta[property=og:image]")?.attr("content"))
        val tags = ld?.tags?.takeIf { it.isNotEmpty() }

        if (isMovie) {
            return newMovieLoadResponse(title, slug, TvType.AnimeMovie, pageUrl) {
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
            return newMovieLoadResponse(title, slug, TvType.AnimeMovie, pageUrl) {
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
                val id = "${season.slug}-${ep.number}"
                if (!used.add(id)) continue

                episodes.add(newEpisode(watchUrl(id)) {
                    this.name = ep.title
                    this.episode = ep.number
                    this.season = season.number
                    this.posterUrl = absoluteImage(ep.poster)
                })
            }
        }

        if (episodes.isEmpty()) {
            return newMovieLoadResponse(title, slug, TvType.AnimeMovie, pageUrl) {
                this.posterUrl = poster
                this.plot = plot
                this.year = ld?.year
                this.tags = tags
            }
        }

        episodes.sortWith(compareBy({ it.season ?: 0 }, { it.episode ?: 0 }))
        Log.d(
            TAG,
            "load serie=$slug ${System.currentTimeMillis() - t0}ms | temporadas=${seasons.size} " +
                "(${seasons.joinToString { "T${it.number}:${it.slug}=${it.episodes.size}" }}) episodios=${episodes.size}"
        )
        return newTvSeriesLoadResponse(title, seriesUrl(slug), TvType.Anime, episodes) {
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
        val t0 = System.currentTimeMillis()
        val direct = urlFrom(postJson("/api/player/source", """{"token":"$token"}""", referer))
        if (!direct.isNullOrBlank()) {
            Log.d(TAG, "source ok ${System.currentTimeMillis() - t0}ms -> ${direct.take(90)}")
            return direct
        }

        val refreshed = try {
            postJson("/api/player/refresh", """{"token":"$token"}""", referer)
                ?.let { JSONObject(it).strOrNull("token") }
        } catch (e: Exception) {
            null
        } ?: run {
            Log.w(TAG, "source falló y refresh no devolvió token (${System.currentTimeMillis() - t0}ms)")
            return null
        }
        Log.d(TAG, "token refrescado, reintentando")

        val retried = urlFrom(postJson("/api/player/source", """{"token":"$refreshed"}""", referer))
        Log.d(
            TAG,
            if (retried.isNullOrBlank()) "source falló tras refresh (${System.currentTimeMillis() - t0}ms)"
            else "source ok tras refresh ${System.currentTimeMillis() - t0}ms -> ${retried.take(90)}"
        )
        return retried
    }

    private fun okruQuality(okName: String?): Int = when (okName?.lowercase()) {
        "full" -> Qualities.P1080.value
        "hd" -> Qualities.P720.value
        "sd" -> Qualities.P480.value
        "low" -> Qualities.P360.value
        "lowest", "mobile" -> Qualities.P240.value
        else -> Qualities.Unknown.value
    }

    private fun dailymotionQuality(key: String): Int = when (key.lowercase()) {
        "1080", "hd1080" -> Qualities.P1080.value
        "720", "hd720" -> Qualities.P720.value
        "480", "sd" -> Qualities.P480.value
        "380", "360" -> Qualities.P360.value
        "240", "low" -> Qualities.P240.value
        else -> Qualities.Unknown.value
    }

    private fun dailymotionId(url: String): String? =
        Regex("""[?&]video=([A-Za-z0-9]{6,})""").find(url)?.groupValues?.getOrNull(1)
            ?: Regex("""dailymotion\.com/(?:embed/)?video/([A-Za-z0-9]{6,})""").find(url)
                ?.groupValues?.getOrNull(1)

    private fun canonicalDailymotion(url: String): String? =
        dailymotionId(url)?.let { "https://www.dailymotion.com/video/$it" }

    private suspend fun emitDailymotion(
        videoUrl: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val id = dailymotionId(videoUrl) ?: return false
        val referer = "https://www.dailymotion.com/embed/video/$id"

        val meta = try {
            app.get(
                "https://www.dailymotion.com/player/metadata/video/$id",
                headers = browserHeaders + ("Referer" to referer),
                timeout = 30L,
            ).text
        } catch (e: Exception) {
            Log.w(TAG, "dailymotion $id metadata falló (${e.message}), reintento sin headers")
            try {
                app.get("https://www.dailymotion.com/player/metadata/video/$id", timeout = 30L).text
            } catch (e2: Exception) {
                Log.w(TAG, "dailymotion $id metadata falló de nuevo: ${e2.message}")
                null
            }
        } ?: return false

        val root = try {
            JSONObject(meta)
        } catch (e: Exception) {
            Log.w(TAG, "dailymotion $id metadata no es JSON (${e.message}): ${meta.take(160)}")
            null
        } ?: return false

        try {
            val data = root.optJSONObject("subtitles")?.optJSONObject("data")
            if (data != null) {
                for (key in data.keys()) {
                    val entry = data.optJSONObject(key) ?: continue
                    val arr = entry.optJSONArray("urls")
                    val url = when {
                        arr == null || arr.length() == 0 -> null

                        arr.optJSONObject(0) != null -> arr.optJSONObject(0).strOrNull("url")
                        else -> arr.optString(0).takeIf { it.isNotBlank() }
                    } ?: continue
                    val label = entry.strOrNull("label") ?: key
                    subtitleCallback(SubtitleFile(label, url))
                    Log.d(TAG, "dailymotion $id -> sub $label")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "dailymotion $id subtítulos: ${e.message}")
        }

        val qualities = root.optJSONObject("qualities")
        if (qualities == null) {
            Log.w(TAG, "dailymotion $id: sin qualities")
            return false
        }

        var emitted = false
        for (key in qualities.keys()) {
            val arr = qualities.optJSONArray(key)
            if (arr == null) {

                val single = qualities.optJSONObject(key)?.strOrNull("url")
                if (!single.isNullOrBlank()) {
                    emitDmLink(key, single, referer, callback)
                    emitted = true
                }
                continue
            }
            for (i in 0 until arr.length()) {
                val item = arr.optJSONObject(i) ?: continue
                val u = item.strOrNull("url") ?: continue
                emitDmLink(key, u, referer, callback)
                emitted = true
            }
        }
        Log.d(TAG, "dailymotion $id -> ${if (emitted) "OK" else "0 URLs"}")
        return emitted
    }

    private suspend fun emitDmLink(
        key: String,
        url: String,
        referer: String,
        callback: (ExtractorLink) -> Unit,
    ) {
        val isHls = url.contains(".m3u8")
        callback(newExtractorLink(name, "Dailymotion ${key.uppercase()}".trim(), url, if (isHls) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO) {
            this.referer = referer
            this.headers = browserHeaders + ("Referer" to referer)
            this.quality = if (key.equals("auto", true)) Qualities.Unknown.value else dailymotionQuality(key)
        })
    }

    private suspend fun emitRumble(
        videoUrl: String,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {

        val resp = try {
            app.get(
                videoUrl,
                headers = browserHeaders + ("Referer" to "https://rumble.com/"),
                timeout = 30L,
            )
        } catch (e: Exception) {
            Log.w(TAG, "rumble embed falló (${e.message}), reintento sin headers: ${videoUrl.take(90)}")
            try {
                app.get(videoUrl, timeout = 30L)
            } catch (e2: Exception) {
                Log.w(TAG, "rumble embed falló de nuevo: ${e2.message}")
                null
            }
        }
        if (resp != null) {
            val body = try {
                resp.text
            } catch (e: Exception) {
                Log.w(TAG, "rumble body falló: ${e.message}")
                ""
            }

            Log.d(TAG, "rumble embed -> code=${resp.code} len=${body.length} ${videoUrl.take(80)}")
            val clean = body.replace("\\/", "/")
            if (extractRumbleLinks(clean, callback)) return true

            if (!videoUrl.contains("/embed/")) {
                Regex("""https://rumble\.com/embed/[^"'\s\\]+""").find(clean)?.value?.let { embed ->
                    Log.d(TAG, "rumble: reintentando con embed $embed")
                    return emitRumble(embed, callback)
                }
            }
        }

        Log.d(TAG, "rumble: probando WebView para ${videoUrl.take(80)}")
        val wvHtml = renderViaWebView(videoUrl)?.replace("\\/", "/")
        if (wvHtml != null) {
            Log.d(TAG, "rumble WebView -> len=${wvHtml.length}")
            if (extractRumbleLinks(wvHtml, callback)) return true
        }
        Log.d(TAG, "rumble -> 0 URLs")
        return false
    }

    private suspend fun extractRumbleLinks(
        clean: String,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        Regex("""https://rumble\.com/hls-vod/[^"'\s\\]+?playlist\.m3u8""").findAll(clean)
            .map { it.value }.distinct().forEach { u ->
                callback(newExtractorLink(name, "Rumble HLS", u, ExtractorLinkType.M3U8) {
                    this.referer = "https://rumble.com/"
                    this.headers = browserHeaders + ("Referer" to "https://rumble.com/")
                    this.quality = Qualities.Unknown.value
                })
                Log.d(TAG, "rumble -> HLS OK")
                return true
            }

        var emitted = false
        Regex("""https://[^"'\s\\]+\.rumble\.cloud[^"'\s\\]*?\.mp4[^"'\s\\]*""").findAll(clean)
            .map { it.value }.distinct().forEach { u ->
                callback(newExtractorLink(name, "Rumble MP4", u, ExtractorLinkType.VIDEO) {
                    this.referer = "https://rumble.com/"
                    this.headers = browserHeaders + ("Referer" to "https://rumble.com/")
                    this.quality = Qualities.Unknown.value
                })
                emitted = true
            }
        if (emitted) Log.d(TAG, "rumble -> MP4 OK")
        return emitted
    }

    private suspend fun renderViaWebView(pageUrl: String): String? = withContext(Dispatchers.Main) {
        val appCtx = pluginContext?.applicationContext ?: run {
            Log.w(TAG, "WebView sin contexto")
            return@withContext null
        }
        var webView: WebView? = null
        val mainHandler = android.os.Handler(android.os.Looper.getMainLooper())
        try {
            val htmlDeferred = CompletableDeferred<String?>()
            var latestHtml: String? = null
            webView = WebView(appCtx)
            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE
                userAgentString = browserHeaders["User-Agent"]
            }
            webView.addJavascriptInterface(object {
                @android.webkit.JavascriptInterface
                fun onHtml(html: String) {
                    latestHtml = html
                    if (!htmlDeferred.isCompleted && html.contains("hls-vod")) {
                        htmlDeferred.complete(html)
                    }
                }
            }, "NativeBridge")
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    
                    fun poll(left: Int) {
                        if (htmlDeferred.isCompleted) return
                        view?.evaluateJavascript(
                            "(function(){NativeBridge.onHtml(document.documentElement.outerHTML);})();",
                            null,
                        )
                        if (left > 0) mainHandler.postDelayed({ poll(left - 1) }, 2000)
                    }
                    mainHandler.postDelayed({ poll(10) }, 3000)
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?,
                ) {
                    Log.w(TAG, "WebView error $errorCode $description url=${failingUrl?.take(80)}")
                }

                override fun onReceivedHttpError(
                    view: WebView?,
                    request: android.webkit.WebResourceRequest?,
                    errorResponse: android.webkit.WebResourceResponse?,
                ) {
                    val url = request?.url?.toString() ?: "?"
                    if (!url.contains("favicon.ico")) {
                        Log.w(TAG, "WebView HTTP ${errorResponse?.statusCode} -> ${url.take(100)}")
                    }
                }
            }
            webView.loadUrl(pageUrl)
            try {
                withTimeout(26000L) { htmlDeferred.await() }
            } catch (e: TimeoutCancellationException) {
                Log.w(TAG, "WebView timeout, devolviendo último HTML (len=${latestHtml?.length ?: 0})")
                latestHtml
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "WebView falló: ${e::class.simpleName}: ${e.message}")
            null
        } finally {
            try {
                webView?.destroy()
            } catch (_: Exception) {
            }
        }
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
        val t0 = System.currentTimeMillis()
        val raw = data.substringBefore("?")
        // CS3 nos pasa la url ABSOLUTA que emitimos (fixUrl solo rompe las relativas)
        val pageUrl = when {
            raw.contains("://") -> raw
            raw.contains("/watch/") -> watchUrl(raw.substringAfter("/watch/"))
            raw.contains("/peliculas/") -> movieUrl(raw.substringAfter("/peliculas/"))
            else -> watchUrl(raw)
        }
        Log.d(TAG, "loadLinks data='$data' -> pagina $pageUrl")

        val doc = fetchDoc(pageUrl) ?: run {
            Log.w(TAG, "loadLinks $pageUrl -> pagina no disponible ${System.currentTimeMillis() - t0}ms")
            return false
        }
        val payload = unescapePayload(doc.html())

        val sources = parseSources(payload)
        Log.d(
            TAG,
            "loadLinks fuentes=${sources.size} [${sources.joinToString { "${it.label}(${it.provider})" }}]"
        )
        if (sources.isEmpty()) {
            Log.w(TAG, "sin fuentes en $pageUrl, delegando a loadExtractor")
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
                else Log.w(TAG, "ok.ru sin manifest: $url")
            } else if (url.contains("dailymotion.com")) {
                // El extractor de CS3 NO matchea geo.dailymotion.com: su getVideoId() saca el id
                // solo del path (/video/{id}) y nunca del query ?video=, asi que devuelve null
                // -> 0 links. Resolvemos aqui y, de fallback, pasamos la URL canonica.
                if (emitDailymotion(url, subtitleCallback, callback)) found = true
                else {
                    val canonical = canonicalDailymotion(url) ?: url
                    Log.w(TAG, "dailymotion: emitDailymotion sin resultado, probando loadExtractor con $canonical")
                    val collected = mutableListOf<ExtractorLink>()
                    val collector: (ExtractorLink) -> Unit = { link -> collected.add(link) }
                    loadExtractor(canonical, pageUrl, subtitleCallback, collector)
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
            } else if (url.contains("rumble.com")) {
                // CS3 no trae extractor de Rumble -> loadExtractor solo encuentra el
                // RumbleExtractor del plugin. Extraemos el HLS aqui y lo dejamos de fallback.
                if (emitRumble(url, callback)) found = true
                else {
                    Log.w(TAG, "rumble: emitRumble sin resultado, probando loadExtractor")
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
        Log.d(
            TAG,
            "loadLinks $pageUrl -> ${if (found) "OK" else "SIN LINKS"} " +
                "(${sources.size} fuentes) ${System.currentTimeMillis() - t0}ms"
        )
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