package com.example

import android.util.Base64
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element
import java.net.URL
import java.util.regex.Pattern
import kotlinx.coroutines.delay
import kotlin.random.Random

class PlushdProvider : MainAPI() {
    override var mainUrl = "https://tioplus.app"
    override var name = "PlusHD"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Anime,
        TvType.Cartoon,
        TvType.Movie,
        TvType.AsianDrama,
    )

    override val mainPage = mainPageOf(
        "series" to "Series",
        "animes" to "Animes",
        "peliculas" to "Peliculas",
        "doramas" to "Doramas",
    )

    private fun base64Encode(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }


    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data}/$page").document
        val home = document.select(".articlesList article").filter { !it.selectFirst("a")?.attr("target").equals("_blank") }
            .mapNotNull { it.toSearchResult() }
        return newHomePageResponse(
            list = HomePageList(
                name = request.name,
                list = home,
                isHorizontalImages = false
            ),
            hasNext = true
        )
    }

    private fun Element.toSearchResult(): SearchResponse? {
        var title = this.selectFirst("a h2")?.text() ?: return null
        val link = this.selectFirst("a.itemA")?.attr("href") ?: return null
        val img = this.selectFirst("picture img")?.attr("data-src")

        val yearRegex = Regex("""\s*\((\d{4})\)$""")
        val match = yearRegex.find(title)
        val year = match?.groupValues?.get(1)?.toIntOrNull()

        if (match != null) {
            title = title.replace(yearRegex, "").trim()
        }

        val searchType = when {
            link.contains("/serie") -> TvType.TvSeries
            link.contains("/anime") -> TvType.Anime
            link.contains("/pelicula") -> TvType.Movie
            link.contains("/dorama") -> TvType.AsianDrama
            else -> TvType.Movie
        }

        return when (searchType) {
            TvType.Movie -> newMovieSearchResponse(title, link, searchType) {
                this.posterUrl = img
                this.year = year
            }
            else -> newTvSeriesSearchResponse(title, link, searchType) {
                this.posterUrl = img
                this.year = year
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/api/search/$query").document
        val results = document.select("article.item").mapNotNull { it.toSearchResult() }
        return results
    }

    data class MainTemporadaElement(
        val title: String? = null,
        val image: String? = null,
        val season: Int? = null,
        val episode: Int? = null
    )

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document

        val tvType = when {
            url.contains("/serie") -> TvType.TvSeries
            url.contains("/anime") -> TvType.Anime
            url.contains("/pelicula") -> TvType.Movie
            url.contains("/dorama") -> TvType.AsianDrama
            else -> TvType.TvSeries
        }

        var title = doc.selectFirst(".slugh1")?.text() ?: ""
        val yearRegex = Regex("""\s*\((\d{4})\)$""")
        val match = yearRegex.find(title)
        val year = match?.groupValues?.get(1)?.toIntOrNull()

        if (match != null) {
            title = title.replace(yearRegex, "").trim()
        }

        val backimage = doc.selectFirst(".bg")?.attr("style")?.let {
            Regex("url\\(\"?(.*?)\"?\\)").find(it)?.groupValues?.get(1)
        } ?: doc.selectFirst("head meta[property=og:image]")?.attr("content") ?: ""

        var verticalPoster = doc.select(".poster img, .data img").firstNotNullOfOrNull {
            val src = it.attr("data-src").ifBlank { it.attr("src") }

            if (src.isNotBlank() &&
                !src.contains("nGfjgUlES2WuYrHXNNF4fbGe2Eq") &&
                src.contains("tmdb.org") &&
                !src.contains("/episodes/") &&
                !src.contains("/seasons/")
            ) {
                src
            } else null
        }?.replace("original", "w342")

        if (verticalPoster.isNullOrBlank()) {
            verticalPoster = doc.selectFirst(".poster img")?.attr("src")?.replace("original", "w342") ?: backimage
        }

        val description = doc.selectFirst("div.description")?.text() ?: ""
        val tags = doc.select("div.home__slider .genres:contains(Generos) a").map { it.text() }
        val epi = ArrayList<Episode>()

        if (tvType != TvType.Movie) {
            val script = doc.select("script").firstOrNull { it.html().contains("seasonsJson = ") }?.html()
            if (!script.isNullOrEmpty()) {
                val jsonRegex = Regex("seasonsJson\\s*=\\s*(\\{[^;]*\\});")
                val matchJson = jsonRegex.find(script)
                val jsonscript = matchJson?.groupValues?.get(1)

                if (!jsonscript.isNullOrEmpty()) {
                    try {
                        val seasonsMap = parseJson<Map<String, List<MainTemporadaElement>>>(jsonscript)
                        seasonsMap.values.forEach { list ->
                            list.forEach { info ->
                                val epTitle = info.title
                                val seasonNum = info.season
                                val epNum = info.episode
                                val img = info.image
                                val realimg = if (img.isNullOrEmpty()) null else "https://image.tmdb.org/t/p/w342${img.replace("\\/", "/")}"
                                val epurl = "$url/season/$seasonNum/episode/$epNum"
                                if (epTitle != null && seasonNum != null && epNum != null) {
                                    epi.add(newEpisode(epurl) {
                                        this.name = epTitle
                                        this.season = seasonNum
                                        this.episode = epNum
                                        this.posterUrl = realimg
                                    })
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("PlushdProvider", "Error al parsear seasonsJson: ${e.message}")
                    }
                }
            }
        }

        return when (tvType) {
            TvType.TvSeries, TvType.Anime, TvType.AsianDrama -> {
                newTvSeriesLoadResponse(title, url, tvType, epi) {
                    this.posterUrl = verticalPoster
                    this.backgroundPosterUrl = backimage
                    this.plot = description
                    this.tags = tags
                    this.year = year
                }
            }
            TvType.Movie -> {
                newMovieLoadResponse(title, url, tvType, url) {
                    this.posterUrl = verticalPoster
                    this.backgroundPosterUrl = backimage
                    this.plot = description
                    this.tags = tags
                    this.year = year
                }
            }
            else -> null
        }
    }

    private fun fixPelisplusHostsLinks(url: String): String {
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
            .replace("https://sblanh.com", "https://lvturbo.com")
            .replaceFirst("https://emturbovid.com", "https://turbovidhls.com")
    }

    private val REGEX_LINK = Pattern.compile(
        "^(https?:)?//(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)$"
    )

    private fun fetchUrls(text: String): List<String> {
        val urls = ArrayList<String>()
        val regex = Regex("""(https?://[^\s"']+)""")
        regex.findAll(text).forEach { urls.add(it.value) }
        return urls
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        var linksFound = false

        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
            "Referer" to "$mainUrl/",
            "Cache-Control" to "no-cache, no-store, must-revalidate",
            "Pragma" to "no-cache"
        )

        val doc = app.get(data, headers = headers).document

        val serverItems = doc.select("div ul.subselect li")
        Log.d("PlushdProvider", "=== loadLinks: ${serverItems.size} servidores encontrados en página ===")

        val loggingSubtitleCallback: (SubtitleFile) -> Unit = { file ->
            Log.d("PlushdProvider", "Subtítulo encontrado. URL: ${file.url}")
            subtitleCallback.invoke(file)
        }

        serverItems.toList().forEachIndexed { index, serverLi ->
            val tag = "PlushdProvider-Server"
            try {
                val serverData = serverLi.attr("data-server")
                if (serverData.isNullOrEmpty()) {
                    Log.w(tag, "[#$index] data-server vacío, saltando")
                    return@forEachIndexed
                }

                val decoded = String(Base64.decode(serverData, Base64.DEFAULT))
                Log.d(tag, "[#$index] decoded: ${decoded.take(120)}")

                val isPlayerPath = !REGEX_LINK.matcher(decoded).matches()
                val url = if (isPlayerPath) {
                    "$mainUrl/player/${base64Encode(serverData.toByteArray())}"
                } else {
                    decoded
                }
                Log.d(tag, "[#$index] usará ${if (isPlayerPath) "PLAYER" else "DIRECT"}: ${url.take(120)}")

                val videoUrl = if (url.contains("/player/")) {
                    val playerHeaders = headers + mapOf("Referer" to data)
                    Log.d(tag, "[#$index] fetcheando player page: $url")
                    val playerDoc = app.get(url, headers = playerHeaders).document
                    Log.d(tag, "[#$index] HTML player page: ${playerDoc.html().length} chars")
                    extractUrlFromPlayerPage(playerDoc, index)
                } else {
                    url
                }

                if (videoUrl.isBlank()) {
                    Log.w(tag, "[#$index] videoUrl en blanco después de extracción")
                    return@forEachIndexed
                }
                Log.d(tag, "[#$index] videoUrl raw: ${videoUrl.take(120)}")

                val fixedLink = fixPelisplusHostsLinks(videoUrl)
                    .replace(Regex("""([a-zA-Z0-9]{0,8}[a-zA-Z0-9_-]+)=https://ww3.pelisplus.to.*"""), "")
                Log.d(tag, "[#$index] fixedLink: ${fixedLink.take(120)}")

                if (fixedLink.isBlank()) {
                    Log.w(tag, "[#$index] fixedLink en blanco después de fixPelisplusHostsLinks")
                    return@forEachIndexed
                }

                val processed = tryHandleDirectVideo(fixedLink, data, callback, loggingSubtitleCallback)
                if (processed) {
                    linksFound = true
                    Log.d(tag, "[#$index] OK (directo)")
                } else {
                    val extractorReferer = try {
                        val urlObject = URL(fixedLink)
                        urlObject.protocol + "://" + urlObject.host + "/"
                    } catch (e: Exception) {
                        Log.e(tag, "[#$index] Error al parsear URL para Referer: ${e.message}")
                        url
                    }
                    Log.d(tag, "[#$index] extractorReferer: $extractorReferer")

                    loadExtractor(
                        url = fixedLink,
                        referer = extractorReferer,
                        subtitleCallback = loggingSubtitleCallback,
                        callback = callback
                    )
                    linksFound = true
                    Log.d(tag, "[#$index] OK (loadExtractor)")
                }
            } catch (e: Exception) {
                Log.e(tag, "[#$index] Error: ${e.message}")
            }

            delay(Random.nextLong(800L, 2500L))
        }

        Log.d("PlushdProvider", "=== loadLinks FIN: linksFound=$linksFound ===")
        return linksFound
    }

    private suspend fun tryHandleDirectVideo(
        url: String,
        referer: String,
        callback: (ExtractorLink) -> Unit,
        subtitleCallback: (SubtitleFile) -> Unit
    ): Boolean {
        return try {
            when {
                url.contains("turbovidhls.com") || url.contains("emturbovid.com") -> {
                    Log.d("PlushdProvider-Direct", "turbovid detectado, extrayendo M3U8 directo de: $url")
                    val page = app.get(url, headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                        "Referer" to referer
                    )).document
                    val m3u8raw = page.selectFirst("#video_player")?.attr("data-hash") ?: ""
                    if (m3u8raw.isNotBlank()) {
                        val m3u8Host = if (m3u8raw.startsWith("http")) m3u8raw else "https://cdn2.turboviplay.com/$m3u8raw"
                        Log.d("PlushdProvider-Direct", "M3U8 raw: ${m3u8raw.take(100)}")
                        Log.d("PlushdProvider-Direct", "M3U8 final: ${m3u8Host.take(100)}")

                        val m3u8Headers = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                            "Referer" to "https://turbovidhls.com/",
                            "Origin" to "https://turbovidhls.com"
                        )
                        try {
                            val m3u8Content = app.get(m3u8Host, headers = m3u8Headers).text
                            if (m3u8Content.trimStart().startsWith("#EXTM3U") || m3u8Content.contains(".ts") || m3u8Content.contains(".m3u8")) {
                                Log.d("PlushdProvider-Direct", "M3U8 válido, ${m3u8Content.length} chars")
                                Log.d("PlushdProvider-Direct", "Primeras líneas M3U8: ${m3u8Content.take(300)}")

                                val variantUrl = Regex("""https?://[^\s"'`,;]+\.m3u8[^\s"'`,;]*""")
                                    .findAll(m3u8Content).map { it.value }.toList().firstOrNull()

                                if (variantUrl != null) {
                                    Log.d("PlushdProvider-Direct", "Verificando variante: ${variantUrl.take(100)}")
                                    val variantContent = app.get(variantUrl, headers = mapOf(
                                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                        "Referer" to m3u8Host,
                                        "Origin" to "https://turbovidhls.com"
                                    )).text
                                    if (variantContent.trimStart().startsWith("#EXTM3U") || variantContent.contains("#EXTINF")) {
                                        Log.d("PlushdProvider-Direct", "Variante válida, ${variantContent.take(200)}")
                                    } else {
                                        Log.w("PlushdProvider-Direct", "Variante no es playlist: ${variantContent.take(200)}")
                                    }
                                }

                                val videoUrl = variantUrl ?: m3u8Host
                                Log.d("PlushdProvider-Direct", "Video URL final: ${videoUrl.take(100)}")

                                @Suppress("DEPRECATION")
                                val link = ExtractorLink(
                                    source = "TurboVid",
                                    name = "TurboVid - Latino",
                                    url = videoUrl,
                                    referer = "https://turbovidhls.com/",
                                    quality = Qualities.Unknown.value,
                                    type = ExtractorLinkType.VIDEO
                                )
                                link.headers = mapOf(
                                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                    "Referer" to "https://turbovidhls.com/",
                                    "Origin" to "https://turbovidhls.com"
                                )
                                callback.invoke(link)
                                true
                            } else {
                                Log.w("PlushdProvider-Direct", "M3U8 response no es playlist: ${m3u8Content.take(200)}")
                                false
                            }
                        } catch (e: Exception) {
                            Log.e("PlushdProvider-Direct", "Error al verificar M3U8: ${e.message}")
                            false
                        }
                    } else {
                        Log.w("PlushdProvider-Direct", "No se encontró data-hash en turbovid")
                        false
                    }
                }
                url.contains("#") && (
                    url.contains("rpmstream.live") ||
                    url.contains("upns.pro") ||
                    url.contains("strp2p.com") ||
                    url.contains("4meplayer.pro") ||
                    url.contains("pelisplusto")
                ) -> {
                    Log.d("PlushdProvider-Direct", "SPA hash URL detectada: ${url.take(100)}")
                    tryExtractFromSpaPage(url, referer, callback, subtitleCallback)
                }
                else -> false
            }
        } catch (e: Exception) {
            Log.e("PlushdProvider-Direct", "Error: ${e.message}")
            false
        }
    }

    private suspend fun tryExtractFromSpaPage(
        url: String,
        referer: String,
        callback: (ExtractorLink) -> Unit,
        subtitleCallback: (SubtitleFile) -> Unit
    ): Boolean {
        Log.d("PlushdProvider-SPA", "SPA ocultado (requiere JS, no extraíble): ${url.take(80)}")
        return true
    }

    private suspend fun extractUrlFromPlayerPage(playerDoc: org.jsoup.nodes.Document, index: Int = -1): String {
        val tag = "PlushdProvider-Player"
        Log.d(tag, "[#$index] HTML total: ${playerDoc.html().length} chars")

        val strategies = listOf(
            "window.onload" to { doc: org.jsoup.nodes.Document ->
                val script = doc.selectFirst("script:containsData(window.onload)")?.data() ?: ""
                fetchUrls(script).firstOrNull() ?: ""
            },
            "iframe[src]" to { doc: org.jsoup.nodes.Document ->
                doc.selectFirst("iframe[src]")?.attr("src") ?: ""
            },
            "script.mp4" to { doc: org.jsoup.nodes.Document ->
                doc.select("script").firstOrNull { it.data().contains("https://") && it.data().contains(".mp4") }
                    ?.let { fetchUrls(it.data()).firstOrNull() } ?: ""
            },
            "script.m3u8" to { doc: org.jsoup.nodes.Document ->
                doc.select("script").firstOrNull { it.data().contains("https://") && it.data().contains("m3u8") }
                    ?.let { fetchUrls(it.data()).firstOrNull() } ?: ""
            },
            "video source[src]" to { doc: org.jsoup.nodes.Document ->
                doc.selectFirst("video source[src], video[src]")?.attr("src") ?: ""
            },
            "a[href]" to { doc: org.jsoup.nodes.Document ->
                doc.selectFirst("a[href$=\".mp4\"], a[href$=\".m3u8\"]")?.attr("href") ?: ""
            },
        )

        for ((name, extract) in strategies) {
            val url = extract(playerDoc)
            if (url.isNotBlank()) {
                Log.d(tag, "[#$index] Estrategia '$name' OK: ${url.take(100)}")
                return url
            } else {
                Log.d(tag, "[#$index] Estrategia '$name' no encontró URL")
            }
        }

        Log.w(tag, "[#$index] Ninguna estrategia encontró URL")
        return ""
    }

}