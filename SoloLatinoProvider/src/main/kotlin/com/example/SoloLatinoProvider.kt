package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.network.CloudflareKiller
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import org.jsoup.Jsoup
import kotlin.collections.ArrayList
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull

//yeji
class SoloLatinoProvider : MainAPI() {
    override var mainUrl = "https://sololatino.net"
    override var name = "SoloLatino"
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.Cartoon,
    )

    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true

    private val baseHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.5",
        "sec-ch-ua" to "\"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "document",
        "sec-fetch-mode" to "navigate",
        "sec-fetch-site" to "none",
        "sec-gpc" to "1",
        "Referer" to mainUrl
    )

    private suspend fun safeAppGet(
        url: String,
        retries: Int = 3,
        delayMs: Long = 5000L,
        timeoutMs: Long = 30000L
    ): String? {
        for (i in 0 until retries) {
            try {
                Log.d("SoloLatino", "safeAppGet - Intento ${i + 1}/$retries para URL: $url")
                val res = app.get(url, timeout = timeoutMs, headers = baseHeaders)
                Log.d("SoloLatino", "safeAppGet - HTTP ${res.code} para URL: $url")
                when {
                    res.isSuccessful -> return res.text
                    res.code == 429 -> {
                        Log.w("SoloLatino", "safeAppGet - Rate limit 429, esperando 10s para: $url")
                        delay(10000L)
                    }
                    else -> Log.w("SoloLatino", "safeAppGet - HTTP ${res.code} no exitoso para: $url")
                }
            } catch (e: Exception) {
                Log.e("SoloLatino", "safeAppGet - Error intento ${i + 1}: ${e.message}", e)
            }
            if (i < retries - 1) delay(delayMs)
        }
        Log.e("SoloLatino", "safeAppGet - Fallaron todos los intentos para URL: $url")
        return null
    }

    private suspend fun safeAppGetDoc(url: String, timeoutMs: Long = 30000L) =
        app.get(url, timeout = timeoutMs, headers = baseHeaders).document

    private fun extractBestSrcset(srcsetAttr: String?): String? {
        if (srcsetAttr.isNullOrBlank()) return null
        val sources = srcsetAttr.split(",").map { it.trim().split(" ") }
        var bestUrl: String? = null
        var bestMetric = 0
        for (source in sources) {
            if (source.size >= 2) {
                val currentUrl = source[0]
                val descriptor = source[1]
                val widthMatch = Regex("""(\d+)w""").find(descriptor)
                val densityMatch = Regex("""(\d+)x""").find(descriptor)
                if (widthMatch != null) {
                    val width = widthMatch.groupValues[1].toIntOrNull()
                    if (width != null && width > bestMetric) { bestMetric = width; bestUrl = currentUrl }
                } else if (densityMatch != null) {
                    val density = densityMatch.groupValues[1].toIntOrNull()
                    if (density != null && density * 100 > bestMetric) { bestMetric = density * 100; bestUrl = currentUrl }
                }
            } else if (source.isNotEmpty() && source.size == 1) {
                if (bestUrl == null || bestMetric == 0) { bestUrl = source[0]; bestMetric = 1 }
            }
        }
        return bestUrl
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d("SoloLatino", "DEBUG: Iniciando getMainPage, página: $page, solicitud: ${request.name}")
        val items = ArrayList<HomePageList>()
        val urls = listOf(
            Pair("Series", "$mainUrl/series"),
            Pair("Animes", "$mainUrl/animes"),
            Pair("Películas", "$mainUrl/peliculas"),
        )

        val homePageLists = urls.map { (name, url) ->
            val tvType = when (name) {
                "Series" -> TvType.TvSeries
                "Animes" -> TvType.Anime
                "Películas" -> TvType.Movie
                else -> TvType.Others
            }
            val html = safeAppGet(url)
            if (html == null) {
                Log.e("SoloLatino", "getMainPage - No se pudo obtener HTML para $url")
                return@map null
            }
            val doc = Jsoup.parse(html)
            val homeItems = doc.select("div.card").mapNotNull { card ->
                val title = card.selectFirst("span.card__title")?.text()
                val link = card.selectFirst("a")?.attr("href")
                val img = card.selectFirst("img.card__poster")?.attr("src") ?: ""
                Log.d("SoloLatino", "DEBUG: título=$title, link=$link, img=$img")
                if (title != null && link != null) {
                    newAnimeSearchResponse(title, fixUrl(link)) {
                        this.type = tvType
                        this.posterUrl = img
                    }
                } else {
                    Log.w("SoloLatino", "ADVERTENCIA: Elemento incompleto para URL: $url")
                    null
                }
            }
            Log.d("SoloLatino", "DEBUG: $name -> ${homeItems.size} items encontrados")
            HomePageList(name, homeItems)
        }.filterNotNull()

        items.addAll(homePageLists)
        Log.d("SoloLatino", "DEBUG: getMainPage finalizado. ${items.size} listas añadidas.")
        return newHomePageResponse(items, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("SoloLatino", "DEBUG: search para query: $query")
        val url = "$mainUrl/buscar?q=$query"
        val html = safeAppGet(url) ?: run {
            Log.e("SoloLatino", "search - No se pudo obtener HTML para: $url")
            return emptyList()
        }
        val doc = Jsoup.parse(html)
        return doc.select("div.card").mapNotNull { card ->
            val title = card.selectFirst("span.card__title")?.text()
            val link = card.selectFirst("a")?.attr("href")
            val img = card.selectFirst("img.card__poster")?.attr("src") ?: ""
            if (title != null && link != null) {
                val isMovie = link.contains("/pelicula/")
                newAnimeSearchResponse(title, fixUrl(link)) {
                    this.type = if (isMovie) TvType.Movie else TvType.TvSeries
                    this.posterUrl = img
                }
            } else null
        }
    }

    data class EpisodeLoadData(val title: String, val url: String)

    override suspend fun load(url: String): LoadResponse? {
        delay(1000L)
        Log.d("SoloLatino", "load - URL: $url")

        var cleanUrl = url
        val urlJsonMatch = Regex("""\{"url":"(https?:\/\/[^"]+)"\}""").find(url)
        if (urlJsonMatch != null) {
            cleanUrl = urlJsonMatch.groupValues[1]
        } else if (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://")) {
            cleanUrl = "https://" + cleanUrl.removePrefix("//")
        }

        if (cleanUrl.isBlank()) {
            Log.e("SoloLatino", "load - ERROR: URL en blanco.")
            return null
        }

        val html = safeAppGet(cleanUrl) ?: run {
            Log.e("SoloLatino", "load - No se pudo obtener HTML para: $cleanUrl")
            return null
        }
        val doc = Jsoup.parse(html)

        val tvType = if (cleanUrl.contains("/pelicula/")) TvType.Movie else TvType.TvSeries

        // Título: h1 dentro del área de detalle, o del <title>
        val title = doc.selectFirst("div.flex-1 h1")?.text()
            ?: doc.selectFirst("title")?.text()?.substringBefore("—")?.trim()
            ?: doc.selectFirst("title")?.text()?.substringBefore("|")?.trim()
            ?: ""

        // Descripción
        val description = doc.selectFirst("p.text-sm.leading-relaxed")?.text() ?: ""

        // Géneros
        val tags = doc.select("div.flex.flex-wrap.items-center a[href*='/genero/']").map { it.text() }

        // Rating TMDB
        val averageScore = doc.selectFirst("span.rating-badge__val")?.text()?.toDoubleOrNull()

        // Duración (para películas, puede estar en los detail-fields)
        val durationMain = doc.select("div.detail-field").firstOrNull { field ->
            field.selectFirst("dt")?.text()?.contains("duración", ignoreCase = true) == true
        }?.selectFirst("dd")?.text()
            ?.replace(Regex("(?i) min\\.?"), "")?.trim()?.toIntOrNull()

        // Año — primer span con 4 dígitos en la fila de metadata
        val year = doc.selectFirst("div.flex.flex-wrap.items-center.gap-3 span")?.text()
            ?.let { Regex("""\d{4}""").find(it)?.value?.toIntOrNull() }

        // Póster
        val poster = doc.selectFirst("div.flex-shrink-0 img")?.attr("src") ?: ""

        // Fondo desde detail-hero__bg style="background-image:url('...')"
        val backgroundPoster = doc.selectFirst("div.detail-hero__bg")
            ?.attr("style")
            ?.let { Regex("""url\('([^']+)'\)""").find(it)?.groupValues?.get(1) }
            ?: poster

        // Episodios — nueva estructura Laravel
        val episodes = if (tvType == TvType.TvSeries) {
            doc.select("div[data-season-panel] a.ep-item").mapNotNull { element ->
                val epUrl = fixUrl(element.attr("href"))
                val epTitle = element.selectFirst("p.text-sm.font-semibold")?.text() ?: ""
                val epNum = element.selectFirst("p.ep-num")?.text() // "E1", "E2"
                val episodeNumber = epNum?.removePrefix("E")?.toIntOrNull()
                val seasonPanel = element.parents().firstOrNull { it.hasAttr("data-season-panel") }
                val seasonNumber = seasonPanel?.attr("data-season-panel")?.toIntOrNull()
                val epPoster = element.selectFirst("img.ep-thumb")?.attr("src") ?: ""
                // Descripción del episodio: párrafo line-clamp-2
                val epDesc = element.selectFirst("p.line-clamp-2")?.text() ?: ""
                // Fecha: último párrafo con color #404060
                val epDate = element.select("p.text-xs").lastOrNull()?.text() ?: ""
                Log.d("SoloLatino", "load - EP S${seasonNumber}E${episodeNumber}: $epTitle | fecha=$epDate")
                if (epUrl.isNotBlank() && epTitle.isNotBlank()) {
                    newEpisode(epUrl) {
                        this.name = epTitle
                        this.season = seasonNumber
                        this.episode = episodeNumber
                        this.posterUrl = epPoster
                        this.description = buildString {
                            if (epDesc.isNotBlank()) append(epDesc)
                            if (epDate.isNotBlank()) {
                                if (isNotEmpty()) append("\n")
                                append("📅 $epDate")
                            }
                        }.ifBlank { null }
                    }
                } else null
            }
        } else listOf()

        Log.d("SoloLatino", "load - ${episodes.size} episodios encontrados para $cleanUrl")

        // Recomendaciones — misma estructura card del mainPage
        val recommendations = doc.select("div.scroll-row div.card").mapNotNull { card ->
            val recLink = card.selectFirst("a")?.attr("href")
            val recTitle = card.selectFirst("span.card__title")?.text()
            val recImg = card.selectFirst("img.card__poster")?.attr("src") ?: ""
            if (recTitle != null && recLink != null) {
                newAnimeSearchResponse(recTitle, fixUrl(recLink)) {
                    this.posterUrl = recImg
                    this.type = if (recLink.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
                }
            } else null
        }

        return when (tvType) {
            TvType.TvSeries -> newTvSeriesLoadResponse(name = title, url = cleanUrl, type = tvType, episodes = episodes) {
                this.posterUrl = poster; this.backgroundPosterUrl = backgroundPoster
                this.plot = description; this.tags = tags; this.recommendations = recommendations
                this.year = year; this.duration = durationMain
                this.score = averageScore?.let { Score.from10(it) }
            }
            TvType.Movie -> newMovieLoadResponse(name = title, url = cleanUrl, type = tvType, dataUrl = cleanUrl) {
                this.posterUrl = poster; this.backgroundPosterUrl = backgroundPoster
                this.plot = description; this.tags = tags; this.recommendations = recommendations
                this.year = year; this.duration = durationMain
                this.score = averageScore?.let { Score.from10(it) }
            }
            else -> null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val targetUrl = data.trim()
        Log.d("SoloLatino", "loadLinks - URL objetivo: $targetUrl")

        if (targetUrl.isBlank()) {
            Log.e("SoloLatino", "loadLinks - ERROR: URL en blanco.")
            return false
        }

        val episodeDoc = safeAppGetDoc(targetUrl)

        // Nueva estructura: el player puede estar en cualquier iframe con src http
        val initialIframeSrc = episodeDoc.selectFirst("iframe[src^=http]")?.attr("src")
            ?: episodeDoc.selectFirst("iframe[data-src^=http]")?.attr("data-src")

        if (initialIframeSrc == null) {
            Log.e("SoloLatino", "loadLinks - ERROR: No se encontró iframe en $targetUrl.")
            return false
        }

        val fixedSrc = fixUrl(initialIframeSrc)
        Log.d("SoloLatino", "loadLinks - Iframe principal: $fixedSrc")

        when {
            fixedSrc.contains("embed69.org") -> {
                Log.d("SoloLatino", "BRANCH: embed69.org")
                val embed69Headers = mapOf(
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
                    "Accept" to "*/*",
                    "Accept-Language" to "es-ES,es;q=0.5",
                    "Referer" to fixedSrc,
                    "sec-ch-ua" to "\"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Brave\";v=\"146\"",
                    "sec-ch-ua-mobile" to "?0",
                    "sec-ch-ua-platform" to "\"Windows\"",
                    "sec-fetch-dest" to "empty",
                    "sec-fetch-mode" to "cors",
                    "sec-fetch-site" to "same-origin",
                    "sec-fetch-storage-access" to "none",
                    "sec-gpc" to "1",
                )
                val embedDoc = app.get(fixedSrc, headers = embed69Headers, timeout = 30000L).document
                embedDoc.select("script")
                    .firstOrNull { it.html().contains("dataLink = [") }?.html()
                    ?.substringAfter("dataLink = ")
                    ?.substringBefore(";")?.let { dataLinkJson ->
                        tryParseJson<List<ServersByLang>>(dataLinkJson)?.amap { lang ->
                            val encryptedLinks = lang.sortedEmbeds.mapNotNull { it.link }
                            if (encryptedLinks.isEmpty()) return@amap
                            val body = LinksRequest(encryptedLinks).toJson()
                                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                            val decryptedRes = app.post(
                                "https://embed69.org/api/decrypt",
                                requestBody = body,
                                headers = embed69Headers
                            )
                            val decrypted = tryParseJson<Loadlinks>(decryptedRes.text)
                            if (decrypted?.success == true && decrypted.links.isNotEmpty()) {
                                decrypted.links.amap { link ->
                                    loadSourceNameExtractor(lang.videoLanguage ?: "Latino", fixHostsLinks(link.link), targetUrl, subtitleCallback, callback)
                                }
                            } else {
                                Log.e("SoloLatino", "EMBED69 ERROR: ${decrypted?.reason}")
                            }
                        }
                    } ?: Log.e("SoloLatino", "EMBED69: No se extrajo dataLink JSON.")
            }

            fixedSrc.contains("xupalace.org/video") -> {
                Log.d("SoloLatino", "BRANCH: xupalace.org")
                val regex = """(go_to_player|go_to_playerVast)\('([^']+)'""".toRegex()
                val foundLinks = regex.findAll(safeAppGetDoc(fixedSrc).html()).map { it.groupValues[2] }.toList()
                if (foundLinks.isNotEmpty()) {
                    foundLinks.amap { loadExtractor(fixHostsLinks(it), targetUrl, subtitleCallback, callback) }
                } else {
                    Log.e("SoloLatino", "XUPALACE: No se encontraron links.")
                }
            }

            else -> {
                Log.d("SoloLatino", "BRANCH: Host intermedio: $fixedSrc")
                safeAppGetDoc(fixedSrc).selectFirst("iframe")?.attr("src")?.let { nestedSrc ->
                    loadExtractor(fixHostsLinks(fixUrl(nestedSrc)), targetUrl, subtitleCallback, callback)
                } ?: loadExtractor(fixHostsLinks(fixedSrc), targetUrl, subtitleCallback, callback)
            }
        }

        return true
    }
}

suspend fun loadSourceNameExtractor(
    source: String,
    url: String,
    referer: String? = null,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit,
) {
    loadExtractor(url, referer, subtitleCallback) { link ->
        CoroutineScope(Dispatchers.IO).launch {
            callback.invoke(
                newExtractorLink("$source[${link.source}]", "$source[${link.source}]", link.url) {
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
        .replaceFirst("https://doodstream.com", "https://dood.la")
        .replaceFirst("https://streamtape.com", "https://streamtape.cc")
}

data class Server(
    @JsonProperty("servername") val servername: String? = null,
    @JsonProperty("link") val link: String? = null,
)

data class ServersByLang(
    @JsonProperty("file_id") val fileId: String? = null,
    @JsonProperty("video_language") val videoLanguage: String? = null,
    @JsonProperty("sortedEmbeds") val sortedEmbeds: List<Server> = emptyList(),
)

data class LinksRequest(val links: List<String>)

data class Loadlinks(
    val success: Boolean,
    val links: List<Link>,
    val reason: String? = null
)

data class Link(
    val index: Long,
    val link: String,
)