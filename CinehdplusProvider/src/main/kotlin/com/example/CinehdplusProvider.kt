package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.net.URLEncoder

private const val TAG = "Cinehdplus"

class CinehdplusProvider : MainAPI() {
    override var mainUrl = "https://cinehdplus.org"
    override var name = "CineHD+"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.Cartoon,
    )

    override val mainPage = mainPageOf(
        "series/" to "Series",
        "peliculas/" to "Películas",
    )

    // ------------------------------------------------------------------
    // Listados: tarjetas <a href="/series-tv-{id}/..."> y "/pelicula-{id}/..."
    // ------------------------------------------------------------------

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val t0 = System.currentTimeMillis()
        val path = if (page <= 1) request.data else "${request.data}page/$page/"
        val document = app.get("$mainUrl/$path").document
        val home = document.select("a[href*=/series-tv-], a[href*=/pelicula-]")
            .mapNotNull { it.toCard() }
            .distinctBy { it.url }
        val hasNext = document.selectFirst("a[href*=/page/${page + 1}/]") != null
        Log.d(TAG, "getMainPage ${request.name} page=$page -> ${home.size} items hasNext=$hasNext ${System.currentTimeMillis() - t0}ms")
        return newHomePageResponse(
            list = HomePageList(
                name = request.name,
                list = home,
                isHorizontalImages = false
            ),
            hasNext = hasNext
        )
    }

    private fun Element.toCard(): SearchResponse? {
        val href = fixUrlNull(this.attr("href")) ?: return null
        if (!href.contains("/series-tv-") && !href.contains("/pelicula-")) return null

        val container = this.closest("div.group")
        val img = this.selectFirst("img") ?: container?.selectFirst("img")
        val title = img?.attr("alt")?.takeIf { it.isNotBlank() }
            ?: this.attr("aria-label").takeIf { it.isNotBlank() }
            ?: this.selectFirst("div.mt-2 > p")?.text()?.trim()?.takeIf { it.isNotBlank() }
            ?: container?.selectFirst("h2, h3")?.text()?.trim()?.takeIf { it.isNotBlank() }
            ?: return null
        val posterUrl = fixUrlNull(img?.attr("src"))
        return if (href.contains("/pelicula-")) {
            newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = posterUrl
            }
        } else {
            newTvSeriesSearchResponse(title, href, TvType.TvSeries) {
                this.posterUrl = posterUrl
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val t0 = System.currentTimeMillis()
        Log.d(TAG, "search() llamado: '$query'")

        val encoded = URLEncoder.encode(query, "UTF-8")
        val document = app.get("$mainUrl/?s=$encoded").document
        val results = document.select("a[href*=/series-tv-], a[href*=/pelicula-]")
            .mapNotNull { it.toCard() }
            .distinctBy { it.url }
        Log.d(TAG, "search '$query' -> ${results.size} resultados ${System.currentTimeMillis() - t0}ms")
        return results
    }

    // ------------------------------------------------------------------
    // Detalle: JSON-LD (TVSeries/Movie/TVEpisode) + episodios en
    // div#season-content-N > a[href*=/episodio-]
    // ------------------------------------------------------------------

    private fun ldJson(doc: Document): JSONObject? = try {
        val raw = doc.selectFirst("script[type=application/ld+json]")?.data()
            ?: return null
        JSONObject(raw)
    } catch (e: Exception) {
        null
    }

    private fun cleanText(s: String?): String? =
        s?.let { Parser.unescapeEntities(it, false).trim() }?.takeIf { it.isNotEmpty() }

    private fun cleanTitle(s: String?): String? {
        var t = cleanText(s) ?: return null
        t = t.replaceFirst(Regex("^Ver\\s+"), "")
        t = t.replaceFirst(Regex("\\s+Online HD.*$"), "")
        t = t.replaceFirst(Regex("\\s+[–-]\\s+CineHDPlus\\s*$"), "")
        t = t.replaceFirst(Regex("\\s+\\(\\d{4}\\)\\s*$"), "")
        return t.takeIf { it.isNotEmpty() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val t0 = System.currentTimeMillis()
        Log.d(TAG, "load($url)")
        val doc = app.get(url).document
        val ld = ldJson(doc)
        val type = ld?.optString("@type").orEmpty()

        val title = cleanText(ld?.optString("name"))
            ?: cleanTitle(doc.selectFirst("head meta[property=og:title]")?.attr("content"))
            ?: return null
        val plot = cleanText(ld?.optString("description"))
            ?: doc.selectFirst("head meta[property=og:description]")?.attr("content")?.trim()
        val poster = fixUrlNull(cleanText(ld?.optString("image")))
            ?: fixUrlNull(doc.selectFirst("head meta[property=og:image]")?.attr("content"))
        val year = ld?.optString("datePublished")?.take(4)?.toIntOrNull()
        val tags = ld?.optJSONArray("genre")?.let { arr ->
            List(arr.length()) { cleanText(arr.optString(it)) }.mapNotNull { it }.takeIf { it.isNotEmpty() }
        }
        val trailer = doc.selectFirst("button[data-url*=youtube.com], iframe[src*=youtube.com]")
            ?.let { it.attr("data-url").ifBlank { it.attr("src") } }
            ?.takeIf { it.isNotBlank() }
            ?.substringBefore("?")
            ?.replaceFirst("https://www.youtube.com/embed/", "https://www.youtube.com/watch?v=")
            .let { if (it?.contains("watch?v=") == true) it else null }
        val recommendations = doc.select("a[href*=/series-tv-], a[href*=/pelicula-]")
            .mapNotNull { it.toCard() }
            .filter { it.url != url && !it.url.contains("/episodio-") }
            .distinctBy { it.url }
            .take(12)


        if (url.contains("/episodio-")) {
            val series = cleanText(ld?.optJSONObject("partOfSeries")?.optString("name"))
            val epName = cleanText(ld?.optString("name"))
            val epNum = ld?.optString("episodeNumber")?.toIntOrNull()
                ?: Regex("-(\\d+)x(\\d+)/?$").find(url)?.groupValues?.getOrNull(2)?.toIntOrNull()
            val epTitle = if (!series.isNullOrBlank() && epNum != null) {
                "$series - Episodio $epNum"
            } else if (!series.isNullOrBlank() && !epName.isNullOrBlank()) {
                "$series - $epName"
            } else {
                epName ?: title
            }
            Log.d(TAG, "load episodio -> $epTitle ${System.currentTimeMillis() - t0}ms")
            return newMovieLoadResponse(epTitle, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = plot
                this.year = year
                this.tags = tags
                this.recommendations = recommendations
                addTrailer(trailer)
            }
        }

        if (url.contains("/pelicula-") || type == "Movie") {
            Log.d(TAG, "load película -> $title ($year) ${System.currentTimeMillis() - t0}ms")
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.backgroundPosterUrl = poster
                this.plot = plot
                this.tags = tags
                this.year = year
                this.recommendations = recommendations
                addTrailer(trailer)
            }
        }

        val episodes = mutableListOf<Episode>()
        val seen = mutableSetOf<String>()
        doc.select("div[id^=season-content-]").forEach { pane ->
            val season = pane.attr("id").removePrefix("season-content-").toIntOrNull() ?: return@forEach
            pane.select("a[href*=/episodio-]").forEach { a ->
                val epUrl = fixUrlNull(a.attr("href")) ?: return@forEach
                if (!seen.add(epUrl)) return@forEach
                val m = Regex("-(\\d+)x(\\d+)/?$").find(epUrl)
                val epNum = m?.groupValues?.getOrNull(2)?.toIntOrNull()
                val epName = a.selectFirst("h3")?.text()?.trim()?.takeIf { it.isNotEmpty() }
                    ?: a.selectFirst("img")?.attr("alt")?.takeIf { it.isNotBlank() }
                    ?: (if (epNum != null) "Episodio $epNum" else null)
                    ?: return@forEach
                episodes.add(newEpisode(fixUrl(epUrl)) {
                    this.name = epName
                    this.season = m?.groupValues?.getOrNull(1)?.toIntOrNull() ?: season
                    this.episode = epNum
                    this.posterUrl = fixUrlNull(a.selectFirst("img")?.attr("src"))
                })
            }
        }
        episodes.sortWith(compareBy({ it.season ?: 0 }, { it.episode ?: 0 }))
        Log.d(TAG, "load serie -> $title ($year) temporadas=${episodes.mapNotNull { it.season }.distinct().size} episodios=${episodes.size} ${System.currentTimeMillis() - t0}ms")

        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
            this.posterUrl = poster
            this.backgroundPosterUrl = poster
            this.plot = plot
            this.tags = tags
            this.year = year
            this.recommendations = recommendations
            addTrailer(trailer)
        }
    }

    // ------------------------------------------------------------------
    // Reproducción: button[data-url="//api.../ir/player.php?h="][data-domain][data-lang]
    // + cadena ir/ intacta (goto.php -> rd.php -> redir_ddh.php -> link b64)
    // ------------------------------------------------------------------

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val t0 = System.currentTimeMillis()
        Log.d(TAG, "loadLinks($data)")
        val doc = app.get(data).document
        val buttons = doc.select("button[data-url]")
        Log.d(TAG, "loadLinks botones=${buttons.size} [${buttons.map { "${it.attr("data-domain")}:${it.attr("data-lang")}" }.distinct().joinToString()}]")
        var found = false
        buttons.amap { btn ->
            var raw = btn.attr("data-url").trim()
            if (raw.isBlank() || raw.contains("youtube.com")) return@amap
            if (raw.startsWith("//")) raw = "https:$raw"
            if (raw.contains(".m3u8")) {
                callback(newExtractorLink(name, btn.attr("data-lang").ifBlank { btn.attr("data-domain") }, raw, ExtractorLinkType.M3U8) {
                    this.referer = "$mainUrl/"
                })
                Log.d(TAG, "loadLinks m3u8 directo OK")
                found = true
                return@amap
            }
            val frame = raw.substringAfter("player.php?h=").substringBefore("&")
            if (frame.isBlank() || frame == raw) {
                Log.w(TAG, "loadLinks botón sin hash: ${raw.take(80)}")
                return@amap
            }
            val lang = btn.attr("data-lang").ifBlank { btn.attr("data-domain").ifBlank { "Latino" } }
            Log.d(TAG, "loadLinks servidor $lang (${btn.attr("data-domain")}) ...")
            if (resolveIrChain(frame, lang, subtitleCallback, callback)) {
                Log.d(TAG, "loadLinks servidor $lang -> OK")
                found = true
            } else {
                Log.w(TAG, "loadLinks servidor $lang -> FALLO")
            }
        }
        Log.d(TAG, "loadLinks $data -> ${if (found) "OK" else "SIN LINKS"} ${System.currentTimeMillis() - t0}ms")
        return found
    }

    private suspend fun resolveIrChain(
        frame: String,
        lang: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val api = mainUrl.replaceFirst("https://", "https://api.")
        return try {
            val d1 = app.get("$api/ir/goto.php?h=$frame").document
            val u1 = d1.selectFirst("form input#url")?.attr("value")
            if (u1.isNullOrBlank()) {
                Log.w(TAG, "ir/ goto sin form: $frame")
                return false
            }
            val d2 = app.post("$api/ir/rd.php", data = mapOf("url" to u1)).document
            val u2 = d2.selectFirst("form input#url")?.attr("value")
            if (u2.isNullOrBlank()) {
                Log.w(TAG, "ir/ rd sin form")
                return false
            }
            val d3 = app.post("$api/ir/redir_ddh.php", data = mapOf("url" to u2, "dl" to "0")).document
            val form = d3.selectFirst("form")
            if (form == null) {
                Log.w(TAG, "ir/ redir sin form")
                return false
            }
            val action = form.attr("action")
            if (action.isBlank()) {
                Log.w(TAG, "ir/ redir sin action")
                return false
            }
            val vid = form.selectFirst("input#vid")?.attr("value")
            val hash = form.selectFirst("input#hash")?.attr("value")
            if (vid.isNullOrBlank() || hash.isNullOrBlank()) {
                Log.w(TAG, "ir/ redir sin vid/hash")
                return false
            }
            val d4 = app.post(action, data = mapOf("vid" to vid, "hash" to hash)).document
            val encoded = d4.selectFirst("script:containsData(link =)")?.html()
                ?.substringAfter("link = '")?.substringBefore("';")
            if (encoded.isNullOrBlank()) {
                Log.w(TAG, "ir/ final sin link b64")
                return false
            }
            val link = base64Decode(encoded)
            Log.d(TAG, "ir/ link -> ${link.take(80)}")
            loadSourceNameExtractor(lang, fixHostsLinks(link), "$mainUrl/", subtitleCallback, callback)
            true
        } catch (e: Exception) {
            Log.w(TAG, "ir/ excepción: ${e.message}")
            false
        }
    }
}

data class LinkData(
    @JsonProperty("movieName") val title: String? = null,
    @JsonProperty("imdbID") val imdbId: String? = null,
    @JsonProperty("tmdbID") val tmdbId: Int? = null,
    @JsonProperty("season") val season: Int? = null,
    @JsonProperty("episode") val episode: Int? = null,
)

suspend fun loadSourceNameExtractor(
    source: String,
    url: String,
    referer: String? = null,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit,
) {
    loadExtractor(url, referer, subtitleCallback) { link ->
        val idiomaLabel = when {
            source.lowercase().contains("lat") -> "Español Latino "
            source.lowercase().contains("es") || source.lowercase().contains("cast") -> "Español de España "
            else -> source
        }

        val calidadLabel = if (link.quality > 0) "${link.quality}p" else ""

        val nombreFinal = "$idiomaLabel [${link.source}] $calidadLabel".trim()

        CoroutineScope(Dispatchers.IO).launch {
            callback.invoke(
                newExtractorLink(
                    nombreFinal,
                    nombreFinal,
                    link.url,
                ) {
                    this.quality = link.quality
                    this.type = link.type
                    this.referer = link.referer
                    this.headers = link.headers
                    this.extractorData = link.extractorData
                }
            )
        }
    }
}

fun fixHostsLinks(url: String): String {
    return url
        .replaceFirst("https://hglink.to", "https://streamwish.to")
        .replaceFirst("https://swdyu.com", "https://streamwish.to")
        .replaceFirst("https://cybervynx.com", "https://streamwish.to")
        .replaceFirst("https://dumbalag.com", "https://streamwish.to")
        .replaceFirst("https://mivalyo.com", "https://vidhidepro.com")
        .replaceFirst("https://dinisglows.com", "https://vidhidepro.com")
        .replaceFirst("https://dhtpre.com", "https://vidhidepro.com")
        .replaceFirst("https://filemoon.link", "https://filemoon.sx")
        .replaceFirst("https://sblona.com", "https://watchsb.com")
        .replaceFirst("https://lulu.st", "https://lulustream.com")
        .replaceFirst("https://uqload.io", "https://uqload.com")
        .replaceFirst("https://do7go.com", "https://dood.la")
}