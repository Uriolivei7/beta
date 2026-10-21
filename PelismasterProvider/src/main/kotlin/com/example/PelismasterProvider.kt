package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.CancellationException
import org.json.JSONObject
import org.jsoup.nodes.Document

class PelismasterProvider : MainAPI() {

    companion object {
        const val TAG = "PelisMaster"
        const val PLAYER_API = "https://api-encrypted.moviemaster.lat"
        const val MAX_SERVERS = 12
        const val SEARCH_PAGES = 3
    }

    override var mainUrl = "https://pelismaster.com"
    override var name = "PelisMaster"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = false
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override val mainPage = mainPageOf(
        "$mainUrl/movies" to "Películas",
        "$mainUrl/series" to "Series",
        "$mainUrl/movies?sort=trending" to "Tendencias",
        "$mainUrl/series?sort=popular" to "Populares",
        "$mainUrl/movies?sort=release" to "Estrenos",
        "$mainUrl/movies?sort=rating" to "Mejor valoradas",
    )

    private fun enc(s: String): String = java.net.URLEncoder.encode(s, "UTF-8")

    private fun unescape(s: String): String = s
        .replace("&quot;", "\"")
        .replace("&#039;", "'")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&amp;", "&")

    private fun cleanTitle(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        var t = raw.trim()
        val marks = listOf(" Online Gratis", " online gratis", " - PelisMaster")
        for (m in marks) {
            val i = t.indexOf(m)
            if (i > 0) t = t.substring(0, i)
        }
        if (t.startsWith("Ver ")) t = t.substring(4).trim()
        return t.trim()
    }

    private fun parseCards(doc: Document): List<SearchResponse> =
        doc.select("a.cd[href]").mapNotNull { a ->
            val href = a.attr("href")
            if (href.isBlank()) return@mapNotNull null
            val title = a.selectFirst("span.cd-t")?.text()?.trim()?.takeIf { it.isNotEmpty() }
                ?: return@mapNotNull null
            val poster = a.selectFirst("img")?.attr("src")?.takeIf { it.isNotBlank() }
            val url = fixUrl(href)
            if (href.contains("/series/")) {
                newTvSeriesSearchResponse(title, url, TvType.TvSeries) { this.posterUrl = poster }
            } else {
                newMovieSearchResponse(title, url, TvType.Movie) { this.posterUrl = poster }
            }
        }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        if (page > 1) return null
        return try {
            val doc = app.get(request.data, timeout = 40L).document
            val items = parseCards(doc)
            Log.d(TAG, "getMainPage '${request.name}': ${items.size} items")
            if (items.isEmpty()) null else newHomePageResponse(HomePageList(request.name, items, true), false)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "getMainPage fallo: ${e.message}")
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val q = query.trim()
        if (q.isBlank()) return emptyList()
        return try {
            val urls = (1..SEARCH_PAGES).map { p -> "$mainUrl/search?q=${enc(q)}&page=$p" }
            val docs = urls.amap { u -> app.get(u, timeout = 40L).document }
            val out = ArrayList<SearchResponse>()
            val seen = HashSet<String>()
            for (doc in docs) {
                for (c in parseCards(doc)) {
                    if (seen.add(c.url)) out.add(c)
                }
            }
            Log.d(TAG, "search '$q' -> ${out.size}")
            out
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "search fallo: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val u = url.trim()
        if (u.isBlank()) return null
        return try {
            val doc = app.get(u, timeout = 40L).document
            val title = cleanTitle(
                doc.selectFirst("meta[property=og:title]")?.attr("content")
                    ?: doc.selectFirst("h1")?.text()
            )
            val poster = doc.selectFirst("meta[property=og:image]")?.attr("content")
            val plot = doc.selectFirst("meta[property=og:description]")?.attr("content")
            val year = doc.selectFirst("p.meta a[href*='/year/']")?.text()?.trim()?.toIntOrNull()

            if (u.contains("/episode/")) {
                Log.d(TAG, "load episodio '$title'")
                newMovieLoadResponse(title, u, TvType.TvSeries, u) {
                    this.posterUrl = poster
                    this.plot = plot
                }
            } else if (u.contains("/series/")) {
                val eps = loadEpisodes(u, doc)
                Log.d(TAG, "load serie '$title': ${eps.size} episodios")
                newTvSeriesLoadResponse(title, u, TvType.TvSeries, eps) {
                    this.posterUrl = poster
                    this.plot = plot
                    this.year = year
                }
            } else {
                Log.d(TAG, "load pelicula '$title'")
                newMovieLoadResponse(title, u, TvType.Movie, u) {
                    this.posterUrl = poster
                    this.plot = plot
                    this.year = year
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "load '$u' fallo: ${e.message}")
            null
        }
    }

    private suspend fun loadEpisodes(seriesUrl: String, firstDoc: Document): List<Episode> {
        val eps = ArrayList<Episode>()
        val seasonHrefs = firstDoc.select("a[href*='/season/']")
            .map { it.attr("href") }
            .filter { it.isNotBlank() }
            .distinct()
        if (seasonHrefs.isEmpty()) {
            eps.addAll(parseEpisodes(firstDoc, 1))
            return eps
        }
        for (href in seasonHrefs) {
            val season = Regex("/season/(\\d+)").find(href)?.groupValues?.get(1)?.toIntOrNull() ?: 1
            val doc = try {
                if (href == seriesUrl) firstDoc else app.get(fixUrl(href), timeout = 40L).document
            } catch (e: Exception) {
                Log.w(TAG, "temporada $season fallo: ${e.message}")
                continue
            }
            eps.addAll(parseEpisodes(doc, season))
        }
        return eps
    }

    private fun parseEpisodes(doc: Document, season: Int): List<Episode> =
        doc.select("a.ep[href]").mapNotNull { a ->
            val href = a.attr("href")
            val n = Regex("/episode/(\\d+)").find(href)?.groupValues?.get(1)?.toIntOrNull()
                ?: return@mapNotNull null
            val poster = a.selectFirst("span.ep-img img")?.attr("src")
            val label = a.selectFirst("span.ep-t")?.text()?.trim()?.takeIf { it.isNotEmpty() }
            val duration = a.selectFirst("span.ep-d")?.text()?.trim()
            newEpisode(fixUrl(href)) {
                this.episode = n
                this.season = season
                this.name = label ?: "Episodio $n"
                this.posterUrl = poster
                if (!duration.isNullOrBlank()) {
                    Regex("(\\d+)\\s*h").find(duration)?.groupValues?.get(1)?.toIntOrNull()?.let { h ->
                        this.runTime = h * 60
                    }
                    Regex("(\\d+)\\s*min").find(duration)?.groupValues?.get(1)?.toIntOrNull()?.let { m ->
                        this.runTime = m
                    }
                }
            }
        }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "Solicitando links para: $data")
        return try {
            val doc = app.get(data, timeout = 40L).document
            val rawEmbed = doc.selectFirst("[data-embed]")?.attr("data-embed")
            if (rawEmbed.isNullOrBlank()) {
                Log.e(TAG, "No se encontro el player (data-embed)")
                return false
            }
            val embed = unescape(rawEmbed)
            val passUrl = Regex("data-pass=\"([^\"]+)\"").find(embed)?.groupValues?.get(1)
            val src = Regex("src=\"([^\"]+)\"").find(embed)?.groupValues?.get(1)
            if (passUrl.isNullOrBlank() || src.isNullOrBlank()) {
                Log.e(TAG, "Player incompleto (pass/src)")
                return false
            }
            val qs = buildQuery(src)
            if (qs == null) {
                Log.e(TAG, "No se pudo armar la consulta del player: $src")
                return false
            }

            var servers: List<JSONObject>? = null
            var pass = ""
            for (attempt in 0..1) {
                pass = fetchPass(passUrl, data, attempt > 0)
                if (pass.isBlank()) continue
                val res = fetchServers(qs, pass, data)
                if (res.second.isNotEmpty()) {
                    servers = res.second
                    break
                }
                Log.w(TAG, "servers no disponibles (HTTP ${res.first}), reintentando con pass renovado")
            }
            if (servers.isNullOrEmpty()) {
                Log.e(TAG, "El API no devolvio servidores")
                return false
            }

            val ordered = servers
                .sortedWith(
                    compareBy<JSONObject> { langPriority(it.optString("lang", "")) }
                        .thenBy { if (it.optString("mode", "").equals("directo", true)) 0 else 1 }
                )
                .take(MAX_SERVERS * 2)
            val qualities = ordered.amap { s -> detectQuality(s.optString("video", "")) }
            var found = false
            var emitted = 0
            for ((i, s) in ordered.withIndex()) {
                if (emitted >= MAX_SERVERS) break
                val video = s.optString("video", "")
                if (video.isBlank()) continue
                val q = qualities.getOrNull(i) ?: continue
                val label = s.optString("name", "Servidor").takeIf { it.isNotBlank() } ?: "Servidor"
                val lang = s.optString("lang", "").takeIf { it.isNotBlank() }
                val type = s.optString("type", "hls")
                val extType = if (type.equals("mp4", true) || video.contains(".mp4")) {
                    ExtractorLinkType.VIDEO
                } else {
                    ExtractorLinkType.M3U8
                }
                val name = if (lang != null) "$label [$lang]" else label
                Log.d(TAG, "link: $name ($type, ${q.name})")
                callback(
                    newExtractorLink(name, name, video, extType) {
                        this.quality = q.value
                        this.referer = data
                    }
                )
                found = true
                emitted++
            }
            Log.d(TAG, "loadLinks -> $found links")
            found
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "loadLinks error: ${e.message}")
            false
        }
    }

    private fun buildQuery(src: String): String? {
        val path = src.substringAfter("/embed/", "").substringBefore("?").trim('/')
        val parts = path.split("/")
        if (parts.size < 2) return null
        val type = parts[0]
        val tmdb = parts[1]
        if (!tmdb.matches(Regex("\\d+"))) return null
        val qs = StringBuilder("tmdb=$tmdb&type=$type")
        if (type == "tv" && parts.size >= 4) {
            qs.append("&season=${parts[2]}&episode=${parts[3]}")
        }
        return qs.toString()
    }

    private suspend fun fetchPass(passUrl: String, referer: String, renew: Boolean): String {
        val url = if (passUrl.startsWith("/")) "$mainUrl$passUrl" else passUrl
        val full = if (renew) {
            if (url.contains("?")) "$url&renew=1" else "$url?renew=1"
        } else {
            url
        }
        return try {
            val body = app.get(
                full,
                referer = referer,
                headers = mapOf(
                    "X-Requested-With" to "XMLHttpRequest",
                    "Accept" to "application/json",
                )
            ).text
            JSONObject(body).optString("pass", "")
        } catch (e: Exception) {
            Log.w(TAG, "player-pass fallo: ${e.message}")
            ""
        }
    }

    private suspend fun fetchServers(qs: String, pass: String, referer: String): Pair<Int, List<JSONObject>> {
        val url = "$PLAYER_API/servers?$qs" + if (pass.isNotBlank()) "&pass=$pass" else ""
        return try {
            val resp = app.get(url, referer = referer, headers = mapOf("Accept" to "application/json"))
            val body = resp.text
            if (resp.code != 200) {
                Log.w(TAG, "servers -> HTTP ${resp.code}: ${body.take(120)}")
                return resp.code to emptyList()
            }
            val arr = try {
                JSONObject(body).optJSONArray("servers")
            } catch (e: Exception) {
                null
            }
            if (arr == null || arr.length() == 0) {
                Log.w(TAG, "servers -> sin lista: ${body.take(120)}")
                resp.code to emptyList()
            } else {
                val list = ArrayList<JSONObject>()
                for (i in 0 until arr.length()) {
                    val o = arr.optJSONObject(i) ?: continue
                    list.add(o)
                }
                Log.d(TAG, "servers -> ${list.size} (${list.map { it.optString("lang", "") }.distinct()})")
                resp.code to list
            }
        } catch (e: Exception) {
            Log.e(TAG, "servers fallo: ${e.message}")
            0 to emptyList()
        }
    }

    private fun langPriority(lang: String): Int = when {
        lang.equals("LAT", true) || lang.contains("Latino", true) -> 0
        lang.equals("ESP", true) || lang.contains("Castellano", true) -> 1
        lang.equals("SUB", true) || lang.contains("Subtitul", true) -> 2
        else -> 3
    }

    private suspend fun detectQuality(videoUrl: String): Qualities? {
        if (videoUrl.isBlank()) return null
        return try {
            val resp = app.get(videoUrl, timeout = 20L)
            val body = resp.text
            if (resp.code != 200 || !body.contains("#EXTM3U")) {
                Log.w(TAG, "servidor caido (${resp.code}), se omite: ${videoUrl.take(70)}")
                return null
            }
            val res = Regex("RESOLUTION=(\\d+)x(\\d+)").findAll(body)
                .map { it.groupValues[2].toIntOrNull() ?: 0 }
                .maxOrNull() ?: 0
            when {
                res >= 2160 -> Qualities.P2160
                res >= 1440 -> Qualities.P1440
                res >= 1080 -> Qualities.P1080
                res >= 720 -> Qualities.P720
                res >= 480 -> Qualities.P480
                res >= 360 -> Qualities.P360
                res >= 240 -> Qualities.P240
                else -> Qualities.Unknown
            }
        } catch (e: Exception) {
            Log.w(TAG, "servidor caido: ${e.message}")
            null
        }
    }
}
