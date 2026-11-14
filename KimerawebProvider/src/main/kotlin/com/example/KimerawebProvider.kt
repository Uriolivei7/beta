package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.app
import com.lagradost.api.Log
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import kotlinx.serialization.Serializable
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.json.JSONObject
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import java.net.URLEncoder

@Serializable
data class KimeraTitle(
    val id: Int,
    val name: String,
    val type: String,
    val slug: String,
    val url: String,
    val cover_url: String?,
    val horizontal_cover_url: String?,
    val trailer_url: String?,
    val release_date: String?,
    val description: String?,
    val tags: List<KimeraTag>,
    val status: KimeraStatus?,
    val alternate_names: List<KimeraAltName>?,
    val episodes_count: Int?,
)

@Serializable
data class KimeraStatus(val id: Int, val name: String?)
@Serializable
data class KimeraTag(val id: Int, val name: String)
@Serializable
data class KimeraAltName(val name: String)
@Serializable
data class KimeraContent(
    val seasons: List<KimeraSeason>?,
    val has_next: Boolean,
    val next_page: Int?,
)
@Serializable
data class KimeraSeason(
    val id: Int,
    val content_id: Int,
    val name: String?,
    val number: Double?,
    val short_tag: String?,
    val date: String?,
    val items: List<KimeraEpisode>?,
)
@Serializable
data class KimeraEpisode(
    val created_at: String?,
    val id: Int,
    val name: String?,
    val number: Double?,
    val season_tag: String?,
    val season_id: Int?,
    val url: String?,
    val thumbnail_url: String?,
)

class KimerawebProvider : MainAPI() {
    override var mainUrl = "https://kimeraweb.com"
    override var name = "Kimera"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val usesWebView = false
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Movie, TvType.Anime)

    override val mainPage = mainPageOf(
        "series" to "Series",
        "animes" to "Animes",
        "donghuas" to "Donghuas",
        "dramas" to "Dramas",
        "movies" to "Peliculas",
    )

    private val browserHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36",
        "Accept-Language" to "es-ES,es;q=0.9",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
    )
    private val apiHeaders = browserHeaders + mapOf(
        "Accept" to "application/json",
        "X-Requested-With" to "XMLHttpRequest",
    )

    private fun isInternal(href: String?): Boolean =
        href != null && href.startsWith("/") && !href.startsWith("//")

    private fun figureCard(el: Element): SearchResponse? {
        val a = el.selectFirst("a[href]") ?: return null
        val hrefAttr = a.attr("href")
        if (!isInternal(hrefAttr)) return null
        val kind = hrefAttr.trimStart('/').substringBefore("/", "series").ifBlank { "series" }
        val image = a.selectFirst("img[data-src]") ?: return null
        val poster = fixUrlNull(image.attr("data-src"))
        val caption = el.selectFirst("figcaption")?.text()?.trim()
            ?: image.attr("alt")?.trim()
            ?: return null
        val url = fixUrl(hrefAttr)
        return when (kind) {
            "movies" -> newMovieSearchResponse(caption, url, TvType.Movie) { this.posterUrl = poster }
            "series", "dramas" -> newTvSeriesSearchResponse(caption, url, TvType.TvSeries) { this.posterUrl = poster }
            "animes", "donghuas" -> newAnimeSearchResponse(caption, url, TvType.Anime) { this.posterUrl = poster }
            else -> newAnimeSearchResponse(caption, url, TvType.Anime) { this.posterUrl = poster }
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "$mainUrl/${request.data}/"
        val doc = app.get(url, headers = browserHeaders, timeout = 45L).document
        val items = doc.select("figure:has(> a[href]):has(img[data-src])").mapNotNull { figureCard(it) }
            .distinctBy { it.url }
        return newHomePageResponse(request.name, items, hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        val encoded = URLEncoder.encode(query, "UTF-8")
        val url = "$mainUrl/catalog?q=$encoded"
        val doc = app.get(url, headers = browserHeaders, timeout = 30L).document
        val items = doc.select("figure:has(> a[href]):has(img[data-src])").mapNotNull { figureCard(it) }
            .distinctBy { it.url }
        return items.takeIf { it.isNotEmpty() }
    }

    private fun fixUrlNull(href: String?): String? =
        href?.takeIf { it.startsWith("http") || it.startsWith("//") }
            ?: href?.takeIf { it.startsWith("/") }?.let { mainUrl + it }

    private fun fixUrl(href: String?): String = fixUrlNull(href) ?: href.orEmpty()

    override suspend fun load(url: String): LoadResponse? {
        return try {
        val doc = app.get(url, headers = browserHeaders, timeout = 45L).document
        val titleDataEl = doc.selectFirst("script[data-element='title-data']")
        val titleJson = titleDataEl?.html()?.let { tryParseJson<KimeraTitle>(it) }
        val titleElement = doc.selectFirst("h1") ?: return null
        val title = titleElement.text().trim()
        val isMovie = url.contains("/movies/")
        val id = Regex("""-(\d+)/?$""").find(url.removeSuffix("/"))?.groupValues?.get(1)?.toIntOrNull()
            ?: titleJson?.id ?: return null
        val poster = fixUrlNull(doc.selectFirst("div[data-action='view-image'] img[data-src]")?.attr("data-src"))
        val backdrop = fixUrlNull(doc.selectFirst("div.image-radial-gdr img[data-src]")?.attr("data-src"))
        Log.d("Kimera", "load POSTER=$poster BACKDROP=$backdrop TITLE=$title")
        Log.d("Kimera", "load POSTER_EL=${doc.selectFirst("div[data-action='view-image'] img[data-src]")?.attr("outerHTML")?.take(200)}")
        val synopsis = doc.selectFirst("h2:containsOwn(Sinopsis)")?.nextElementSibling()?.selectFirst("p")?.text()?.trim()
            ?: doc.selectFirst("p.flex.column.wt-100.ht-100")?.text()?.trim()
        val tags = titleJson?.tags?.mapNotNull { it.name }?.takeIf { it.isNotEmpty() }
            ?: doc.select("a[href^='/catalog?tag=']").map { it.text().trim() }.filter { it.isNotBlank() }
        val status = when (titleJson?.status?.name?.lowercase()) {
            "completo", "finalizado" -> ShowStatus.Completed
            "en emision", "en emision", "emitiendo", "publicacion", "publicacion" -> ShowStatus.Ongoing
            else -> null
        }
        val year = parseYear(titleJson?.release_date)
        val trailerJson = titleJson?.trailer_url?.takeIf { it != "null" && it.isNotBlank() }
        val trailerUrl = trailerJson?.let { fixUrlNull(it) ?: it }

        val episodesList = if (isMovie) null else try {
            val seasonsJson = app.get("$mainUrl/api/titles/$id/content?sort=asc&page=1", headers = apiHeaders, timeout = 45L).text
            val data = tryParseJson<KimeraContent>(seasonsJson) ?: return null
            val all = mutableListOf<Episode>()
            for (season in data.seasons.orEmpty()) {
                val seasonNum = season.number?.toInt()
                    ?: Regex("""(\d+)""").find(season.short_tag ?: "")?.groupValues?.get(1)?.toIntOrNull()
                    ?: Regex("""(\d+)""").find(season.name ?: "")?.groupValues?.get(1)?.toIntOrNull()
                    ?: 1
                for (ep in season.items.orEmpty()) {
                    val epNum = ep.number?.toInt() ?: continue
                    val epUrl = ep.url?.let { mainUrl + it } ?: continue
                    val posterUrl = fixUrlNull(ep.thumbnail_url)
                    all.add(newEpisode(epUrl) {
                        this.name = ep.name ?: "Episodio $epNum"
                        this.season = seasonNum
                        this.episode = epNum
                        this.posterUrl = posterUrl
                    })
                }
            }
            all.sortedWith(compareBy({ it.season ?: 0 }, { it.episode ?: 0 }))
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("Kimera", "Error cargando episodios id=$id: ${e.message}")
            emptyList()
        }

        if (isMovie) {
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = synopsis
                this.tags = tags
                year?.let { this.year = it }
                trailerUrl?.let { addTrailer(it) }
            }
        }
        val eps = episodesList ?: return null
        if (eps.isEmpty()) return null
        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, eps) {
            this.posterUrl = poster
            this.backgroundPosterUrl = backdrop
            this.plot = synopsis
            this.tags = tags
            status?.let { this.showStatus = it }
            year?.let { this.year = it }
            trailerUrl?.let { addTrailer(it) }
        }
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("Kimera", "load error $url: ${e::class.simpleName}: ${e.message}")
            null
        }
    }

    private fun parseYear(releaseDate: String?): Int? {
        if (releaseDate.isNullOrBlank()) return null
        val match = Regex("""(\d{4})""").find(releaseDate)
        return match?.groupValues?.get(1)?.toIntOrNull()
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val watchUrl = if (data.contains("/movies/") && !data.contains("/watch")) {
            data.removeSuffix("/") + "/watch"
        } else data
        val doc = try {
            app.get(watchUrl, headers = browserHeaders + ("Referer" to mainUrl), timeout = 45L).document
        } catch (_: Exception) {
            return false
        }
        val scriptEl = doc.selectFirst("script#content-data")
        val json = scriptEl?.html()?.let { JSONObject(it) } ?: return false
        val playback = json.optJSONArray("playback") ?: return false
        var count = 0
        for (i in 0 until playback.length()) {
            val catObj = playback.optJSONObject(i) ?: continue
            val langName = catObj.optString("name", "").trim().ifBlank { "Servidor" }
            val opts = catObj.optJSONArray("playback_options") ?: continue
            for (j in 0 until opts.length()) {
                val opt = opts.optJSONObject(j) ?: continue
                val failed = opt.optBoolean("failed", false)
                if (failed) continue
                val ready = opt.optBoolean("ready", true)
                if (!ready) continue
                val videoUrl = opt.optString("url")?.takeIf { it.isNotBlank() && it != "null" } ?: continue
                val serverName = opt.optString("name", "").trim().ifBlank { "Kmv" }
                val type = opt.optString("type", "hls_storage")
                val languageSuffix = if (langName != "Default") " ($langName)" else ""
                callback(newExtractorLink(
                    name,
                    "Kimera - $serverName$languageSuffix",
                    videoUrl,
                    if (type.contains("hls", ignoreCase = true)) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO,
                ) {
                    this.referer = mainUrl
                    this.headers = mapOf(
                        "User-Agent" to browserHeaders["User-Agent"].orEmpty(),
                        "Referer" to mainUrl,
                    )
                    this.quality = Qualities.Unknown.value
                })
                count++
            }
        }
        Log.e("Kimera", "loadLinks $data -> emitidos=$count")
        return count > 0
    }

    // ------------------------------------------------------------------
    // Interceptor: el CDN sirve init+segmentos fMP4 con un prefijo `<!--IGCF1` (22 bytes).
    // Lo quitamos para que ExoPlayer obtenga un MP4 fragment limpio reproducible.
    // ------------------------------------------------------------------

    @Suppress("ObjectLiteralToLambda")
    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val url = request.url.toString()
                Log.d("Kimera", "[intercept] path=${request.url.encodedPath}")
                if (request.url.host != "cloudcdn.sbs") { Log.d("Kimera", "[intercept] no es cloudcdn.sbs (${request.url.host})"); return chain.proceed(request) }
                if (!request.url.encodedPath.startsWith("/media/stream/")) { Log.d("Kimera", "[intercept] no es /media/stream/*.html"); return chain.proceed(request) }
                val response = chain.proceed(request)
                return try {
                    val body = response.body ?: return response
                    val bytes = body.bytes()
                    if (bytes.size < 22) { Log.w("Kimera", "[intercept] body too small len=${bytes.size}"); return response }
                    val head = String(bytes, 0, 9, Charsets.UTF_8)
                    if (head != "<!--IGCF1") { Log.w("Kimera", "[intercept] no prefix head=${bytes.take(16).joinToString(",") { "%02X".format(it) }}"); return response }
                    val stripped = bytes.copyOfRange(22, bytes.size)
                    Log.d("Kimera", "[intercept] strip ok len=${bytes.size}->${stripped.size} type=${request.url.query}")
                    val contentType = "application/octet-stream".toMediaTypeOrNull()
                    response.newBuilder()
                        .removeHeader("Content-Length")
                        .removeHeader("Content-Type")
                        .header("Content-Length", stripped.size.toString())
                        .header("Content-Type", "application/octet-stream")
                        .body(okhttp3.ResponseBody.create(contentType, stripped))
                        .build()
                } catch (e: Exception) {
                    Log.w("Kimera", "[intercept] error: ${e.message}")
                    response
                }
            }
        }
    }
}
