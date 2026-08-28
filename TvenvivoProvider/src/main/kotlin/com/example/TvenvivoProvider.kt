package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.network.CloudflareKiller
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.TimeoutCancellationException
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType

class TvenvivoProvider : MainAPI() {
    override var mainUrl = "https://www.tvenvivo2.com/"
    override var name = "TVenVIVO"

    override val supportedTypes = setOf(
        TvType.Live
    )

    override var lang = "mx"

    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true

    private val cfKiller = CloudflareKiller()
    private val successfulOptionUrl = HashMap<String, String>()
    private val nowAllowed = listOf("Red Social", "Donacion", "Donar con Paypal", "Mundo Latam")

    private val infantilCat = setOf(
        "Cartoon Network", "Tooncast", "Disney Channel", "Nick", "Nickelodeon", "FM Hot Kids"
    )

    private val peliculasSeriesCat = setOf(
        "Universal Channel", "Universal Premiere", "Universal Cinema", "TNT", "TNT Series", "TNT Novelas",
        "Star Channel", "Cinemax", "Space", "Syfy", "Warner Channel", "Cinecanal", "FX",
        "AXN", "AMC", "Studio Universal", "Multipremier", "Golden", "Sony", "Panico", "Extrema",
        "USA", "Canal Sony"
    )

    private val educacionCat = setOf(
        "Discovery Channel", "Discovery World", "Discovery Theater", "Discovery Science", "Discovery Familia",
        "Discovery H&H", "Discovery A&E", "ID Investigation",
        "History", "History 2", "Animal Planet", "Nat Geo"
    )

    private val entretenimientoCat = setOf(
        "Telefe", "El Trece", "Television Publica", "Telemundo", "Univision", "Pasiones", "Caracol",
        "RCN", "Latina", "America TV", "Willax TV", "ATV", "Las Estrellas", "Tlnovelas", "Galavision",
        "Azteca", "Canal 5", "Distrito Comedia", "MTV", "E!", "Unicable", "Imagen TV", "Azteca 7",
        "Azteca Uno", "Antena 3", "DW", "FM Hot Movies"
    )

    private val deportesCat = setOf(
        "TUDN", "WWE", "Afizzionados", "Gol Peru", "Gol TV", "TNT Sports", "Fox Sports",
        "TyC Sports", "Movistar", "Dazn", "Bein", "Directv Sports", "ESPN", "Win Sports",
        "Azteca Deportes", "Liga 1", "Sky Sports", "VIX TUDN"
    )

    private val noticiasCat = setOf(
        "Telemundo 51", "CNN", "Noticias", "RTVE"
    )

    private val localLatinoCat = setOf(
        "Canal", "Televisa", "TV Azteca", "TV Publica", "TV Peru"
    )

    private suspend fun safeAppGet(
        url: String,
        timeoutMs: Long = 10000L,
        additionalHeaders: Map<String, String>? = null,
        referer: String? = null
    ): String? {
        val requestHeaders = (additionalHeaders ?: emptyMap()).toMutableMap()
        if (!requestHeaders.containsKey("User-Agent")) {
            requestHeaders["User-Agent"] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"
        }
        if (!referer.isNullOrBlank() && !requestHeaders.containsKey("Referer")) {
            requestHeaders["Referer"] = referer
        }

        for (attempt in 1..2) {
            try {
                val interceptor = if (attempt > 1) cfKiller else null
                val res = app.get(url, timeout = timeoutMs * attempt, headers = requestHeaders, interceptor = interceptor)
                if (res.isSuccessful) return res.text
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("Tvenvivo", "safeAppGet error (intento $attempt): ${e::class.simpleName} - ${e.message}")
            }
        }
        return null
    }

    private fun getCategory(title: String): String {
        val normalizedTitle = title.uppercase().replace(" EN VIVO", "").trim()
        
        return when {
            infantilCat.any { normalizedTitle.contains(it) } -> "Infantil"
            educacionCat.any { normalizedTitle.contains(it) } -> "Educacion"
            noticiasCat.any { normalizedTitle.contains(it) } -> "Noticias"
            entretenimientoCat.any { normalizedTitle.contains(it) } -> "Entretenimiento"
            peliculasSeriesCat.any { normalizedTitle.contains(it) } -> "Peliculas"
            deportesCat.any { normalizedTitle.contains(it) } -> "Deportes"
            localLatinoCat.any { normalizedTitle.contains(it) } -> "Latino"
            else -> "Canales"
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val html = safeAppGet(mainUrl) ?: return null
        Log.d("Tvenvivo", "getMainPage: HTML ${html.length} chars")
        val channels = extractChannelsFromHtml(html)
        Log.d("Tvenvivo", "getMainPage: ${channels.size} canales extraídos")

        val uniqueChannels = channels.distinctBy { (_, link, _) -> link }
        Log.d("Tvenvivo", "getMainPage: ${uniqueChannels.size} únicos")

        val channelResults = uniqueChannels.mapNotNull { (titleRaw, link, img) ->
            val title = titleRaw.replace("Ver ", "").replace(" en vivo", "").trim()
            if (nowAllowed.any { title.contains(it, ignoreCase = true) }) return@mapNotNull null
            newTvSeriesSearchResponse(name = title, url = fixUrl(link)) {
                this.type = TvType.Live
                this.posterUrl = fixUrl(img)
            }
        }

        Log.d("Tvenvivo", "getMainPage: ${channelResults.size} resultados finales")
        return newHomePageResponse(listOf(HomePageList("Todos los Canales", channelResults)), false)
    }

    private fun extractChannelsFromHtml(html: String): List<Triple<String, String, String>> {
        val channels = mutableListOf<Triple<String, String, String>>()
        val doc = Jsoup.parse(html)

        // Nuevo HTML usa a.channel con div.channel-name e img alt; mantener fallback a a.channel-card
        val selectors = listOf("a.channel", "a.channel-card", "a[class*=channel]")
        val seen = mutableSetOf<String>()
        for (sel in selectors) {
            doc.select(sel).forEach { channelCard ->
                val link = channelCard.attr("href")
                if (link.isBlank() || !seen.add(link)) return@forEach
                val imgElement = channelCard.selectFirst("img")
                val nameDiv = channelCard.selectFirst("div.channel-name")?.text()?.trim()
                val pElement = channelCard.selectFirst("p")
                val titleRaw = when {
                    imgElement?.attr("alt")?.isNotBlank() == true -> imgElement.attr("alt")
                    !nameDiv.isNullOrBlank() -> nameDiv
                    pElement?.text()?.isNotBlank() == true -> pElement.text()
                    channelCard.attr("aria-label").isNotBlank() -> channelCard.attr("aria-label").replace(" en vivo", "", true).trim()
                    else -> ""
                }
                val img = imgElement?.attr("src") ?: ""
                if (titleRaw.isNotBlank() && link.isNotBlank()) {
                    channels.add(Triple(titleRaw, link, img))
                }
            }
            if (channels.isNotEmpty()) break
        }

        return channels
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("Tvenvivo", "Search: query='$query'")
        val html = safeAppGet(mainUrl) ?: run {
            Log.d("Tvenvivo", "Search: failed to get HTML")
            return emptyList()
        }
        
        val channels = extractChannelsFromHtml(html)
        Log.d("Tvenvivo", "Search: found ${channels.size} channels")
        
        if (query.isBlank()) {
            Log.d("Tvenvivo", "Search: query is blank, returning empty")
            return emptyList()
        }

        val filtered = channels.filterNot { (titleRaw, _, _) ->
            val shouldFilter = nowAllowed.any { titleRaw.contains(it, ignoreCase = true) } || titleRaw.isBlank()
            if (shouldFilter) Log.d("Tvenvivo", "Search: filtering out '$titleRaw'")
            shouldFilter
        }
        
        val matched = filtered.filter { (titleRaw, _, _) ->
            val matches = titleRaw.contains(query, ignoreCase = true)
            Log.d("Tvenvivo", "Search: '$titleRaw' matches '$query' = $matches")
            matches
        }
        
        Log.d("Tvenvivo", "Search: matched ${matched.size} channels")
        
        return matched.mapNotNull { (titleRaw, linkRaw, imgRaw) ->
            val title = titleRaw.replace("Ver ", "").replace(" en vivo", "").trim()
            newLiveSearchResponse(
                name = title,
                url = fixUrl(linkRaw),
                type = TvType.Live
            ) {
                this.posterUrl = fixUrl(imgRaw)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = safeAppGet(url) ?: return null
        val doc = Jsoup.parse(html)

        val title = doc.selectFirst("h1.font-bold")?.text()
            ?: doc.selectFirst("h1")?.text()
            ?: doc.selectFirst("meta[property='og:title']")?.attr("content")
            ?: doc.selectFirst("title")?.text()
            ?: "Canal Desconocido"

        val cleanTitle = title
            .replace(Regex("""(?i)\bVer\s+"""), "")
            .replace(Regex("""(?i)\s*en\s+vivo(\s*hd)?"""), "")
            .replace(Regex("""\s*\|\s*.*"""), "")
            .trim()

        val poster = doc.selectFirst("div.info-logo img")?.attr("src")
            ?: doc.selectFirst("meta[name='og:image']")?.attr("content")
            ?: doc.selectFirst("section img[src*='/imge/']")?.attr("src")
            ?: doc.selectFirst("meta[property='og:image']")?.attr("content")
            ?: ""

        val description = doc.selectFirst("section.info div.info-card")?.text()
            ?: doc.selectFirst("meta[property='og:description']")?.attr("content")
            ?: doc.selectFirst("meta[name=description]")?.attr("content")
            ?: ""

        val episodes = listOf(
            newEpisode(data = url) {
                this.name = "En Vivo"
                this.posterUrl = fixUrlNull(poster)
            }
        )

        return newTvSeriesLoadResponse(
            name = cleanTitle,
            url = url,
            type = TvType.Live,
            episodes = episodes
        ) {
            this.posterUrl = fixUrlNull(poster)
            this.backgroundPosterUrl = fixUrlNull(poster)
            this.plot = description
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val targetUrl = fixUrl(data)
        Log.d("Tvenvivo", "Logs: Cargando URL base -> $targetUrl")

        try {
            val mainHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9",
                "Sec-Fetch-Dest" to "iframe",
                "Sec-Fetch-Mode" to "navigate",
                "Sec-Fetch-Site" to "same-origin",
                "Sec-Fetch-User" to "?1",
                "Upgrade-Insecure-Requests" to "1"
            )

            val mainPageResponse = withTimeoutOrNull(20000L) { app.get(targetUrl, headers = mainHeaders, interceptor = cfKiller) }
                ?: run {
                    Log.w("Tvenvivo", "Logs: Timeout al cargar página principal")
                    return false
                }
            val mainCookies = mainPageResponse.cookies
            val doc = Jsoup.parse(mainPageResponse.text)

            // Try extracting M3U8 directly from the main page first (fast path)
            val directM3u8 = extractM3u8FromHtml(mainPageResponse.text, strict = false)
            if (directM3u8 != null) {
                Log.d("Tvenvivo", "Logs: M3U8 encontrado directamente en página principal: $directM3u8")
                callback(
                    newExtractorLink(
                        source = this.name,
                        name = "${this.name} - Directo",
                        url = directM3u8,
                        type = ExtractorLinkType.M3U8
                    ) {
                        this.headers = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                            "Referer" to targetUrl
                        )
                    }
                )
                return true
            }

            val optionLinksBuilder = mutableListOf<String>()
            // Nuevo: botones con data-src (live/core.php, live2/core.php, etc.)
            doc.select("button[data-src], a[data-src], [data-src]").forEach {
                val ds = it.attr("data-src")
                if (ds.isNotBlank()) optionLinksBuilder.add(ds)
            }
            // Fallback viejo: a href / iframe src
            doc.select("a[href*=/live], iframe[name=player], iframe[src*=/live], iframe#playerFrame").forEach {
                val v = if (it.tagName() == "iframe") it.attr("src") else it.attr("href")
                if (v.isNotBlank()) optionLinksBuilder.add(v)
            }
            val optionLinks = optionLinksBuilder.filter { it.isNotBlank() && !it.contains("facebook") }.distinct()
            Log.d("Tvenvivo", "Logs: optionLinks raw=${optionLinks.joinToString()}")

            val cachedUrl = successfulOptionUrl[targetUrl]
            val finalLinks = if (cachedUrl != null) {
                val cachedIdx = optionLinks.indexOf(cachedUrl)
                if (cachedIdx > 0) {
                    val link = optionLinks[cachedIdx]
                    listOf(link) + optionLinks.filterIndexed { i, _ -> i != cachedIdx }
                } else optionLinks
            } else optionLinks

            Log.d("Tvenvivo", "Logs: Opciones detectadas: ${finalLinks.size}")

            if (finalLinks.isEmpty()) return false

            return coroutineScope {
                for ((displayIdx, rawUrl) in finalLinks.withIndex()) {
                    if (tryLoadOption(targetUrl, rawUrl, displayIdx, mainHeaders, mainCookies, callback)) {
                        return@coroutineScope true
                    }
                }
                false
            }
        } catch (e: Exception) {
            Log.e("Tvenvivo", "Logs: Error crítico: ${e.message}")
            return false
        }
    }

    private suspend fun tryLoadOption(
        targetUrl: String,
        rawPlayerUrl: String,
        displayIndex: Int,
        mainHeaders: Map<String, String>,
        mainCookies: Map<String, String>,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val playerUrl = fixUrl(rawPlayerUrl)
        return try {
            Log.d("Tvenvivo", "Logs: Probando Opción ${displayIndex + 1} -> $playerUrl")

            val playerHeaders = mainHeaders.toMutableMap().apply {
                put("Referer", targetUrl)
                put("Sec-Fetch-Site", "same-origin")
            }

            val isPhpUrl = playerUrl.contains(".php")
            val requestTimeout = if (isPhpUrl) 30000L else 20000L
            try {
                withTimeout(requestTimeout) {
                    val playerResponse = if (isPhpUrl) {
                        app.get(
                            playerUrl,
                            timeout = requestTimeout,
                            headers = playerHeaders,
                            cookies = mainCookies
                        )
                    } else {
                        app.get(
                            playerUrl,
                            timeout = requestTimeout,
                            headers = playerHeaders,
                            cookies = mainCookies,
                            interceptor = cfKiller
                        )
                    }
                    val playerHtml = playerResponse.text

                    if (playerHtml.isBlank()) return@withTimeout false
                    val parsed = Jsoup.parse(playerHtml)
                    val pageTitle = parsed.title().lowercase()
                    if (pageTitle.contains("pagina no encontrada") || pageTitle.contains("página no encontrada") || pageTitle.contains("404")) {
                        Log.w("Tvenvivo", "Logs: Opción ${displayIndex + 1} fail rápido - página no encontrada")
                        return@withTimeout false
                    }

                    // Genérico: cualquier iframe (ahora deportes.ksdjugfssddeports.com, antes regionales.saohgdasregions.fun)
                    val internalIframe = Regex("""<iframe[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE).find(playerHtml)?.groupValues?.get(1)
                        ?.let { it.replace("&amp;", "&") }

                    val finalHtml = if (internalIframe != null && internalIframe.isNotBlank()) {
                        val iframeUrl = fixUrl(internalIframe)
                        Log.d("Tvenvivo", "Logs: Iframe interno: $iframeUrl")
                        val iframeResp = withTimeoutOrNull(20000L) {
                            app.get(
                                iframeUrl,
                                timeout = 20000L,
                                headers = playerHeaders.toMutableMap().apply { put("Referer", playerUrl) },
                                cookies = mainCookies,
                                interceptor = cfKiller
                            )
                        }
                        if (iframeResp == null || !iframeResp.isSuccessful) {
                            Log.w("Tvenvivo", "Logs: Iframe HTTP ${iframeResp?.code ?: "null"}")
                            return@withTimeout false
                        }
                        val html = iframeResp.text
                        Log.d("Tvenvivo", "Logs: Iframe HTML len=${html.length} snippet=${html.take(500).replace("\n"," ")}")
                        html
                    } else {
                        playerHtml
                    }

                    val m3u8Url = extractM3u8FromHtml(finalHtml)
                    Log.d("Tvenvivo", "Logs: extractM3u8FromHtml returned=${m3u8Url ?: "null"}")

                    if (!m3u8Url.isNullOrEmpty()) {
                        Log.d("Tvenvivo", "Logs: ¡Éxito! M3U8: $m3u8Url")

                        val iframeDomain = try { java.net.URL(internalIframe ?: playerUrl).host } catch (_: Exception) { "" }
                        val streamingHeaders = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                            "Origin" to if (iframeDomain.isNotBlank()) "https://$iframeDomain" else "https://www.tvenvivo2.com",
                            "Referer" to if (internalIframe != null) fixUrl(internalIframe) else playerUrl
                        )
                        Log.d("Tvenvivo", "Logs: streamingHeaders=$streamingHeaders")

                        callback(
                            newExtractorLink(
                                source = this@TvenvivoProvider.name,
                                name = "${this@TvenvivoProvider.name} - Opción ${displayIndex + 1}",
                                url = m3u8Url,
                                type = ExtractorLinkType.M3U8
                            ) {
                                this.headers = streamingHeaders
                            }
                        )

                        successfulOptionUrl[targetUrl] = rawPlayerUrl
                        return@withTimeout true
                    } else {
                        Log.w("Tvenvivo", "Logs: Falló Opción ${displayIndex + 1} - no se encontró M3U8")
                        return@withTimeout false
                    }
                }
            } catch (e: TimeoutCancellationException) {
                Log.w("Tvenvivo", "Logs: Opción ${displayIndex + 1} timeout - ${requestTimeout}ms")
                false
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("Tvenvivo", "Logs: Error opción ${displayIndex + 1}: ${e.message}")
            false
        }
    }

    private fun extractM3u8FromHtml(html: String, strict: Boolean = true): String? {
        val patterns = if (strict) {
            listOf(
                """["'](https?[:\/\/\\]+[^"']+\.m3u8[^"']*)["']""",
                """source\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""",
                """file\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""",
                """var\s+src\s*=\s*["']([^"']+\.m3u8[^"']*)["']"""
            )
        } else {
            listOf(
                """["'](https?[:\/\/\\]+[^"']+\.m3u8[^"']*)["']""",
                """source\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""",
                """file\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""",
                """var\s+src\s*=\s*["']([^"']+\.m3u8[^"']*)["']""",
                """(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""",
                """['"]([^"']+\.m3u8[^"']*)['"]"""
            )
        }

        for (pattern in patterns) {
            val match = Regex(pattern, RegexOption.IGNORE_CASE).find(html)
            if (match != null) {
                val found = match.groupValues[1].replace("\\/", "/")
                Log.d("Tvenvivo", "extractM3u8: pattern '$pattern' -> $found")
                return found
            }
        }
        // Intentar decodificar window['xxx']='BASE64' que contiene m3u8 (tvenvivo2 usa obfuscación)
        try {
            val b64Regex = Regex("""window\[['"][^'"]+['"]\]\s*=\s*['"]([A-Za-z0-9+/=]{80,})['"]""")
            var foundB64 = false
            for (m in b64Regex.findAll(html)) {
                foundB64 = true
                val b64 = m.groupValues[1]
                Log.d("Tvenvivo", "extractM3u8: found window var b64 len=${b64.length}")
                try {
                    val decoded = String(android.util.Base64.decode(b64, android.util.Base64.DEFAULT), Charsets.UTF_8)
                    Log.d("Tvenvivo", "extractM3u8: decoded len=${decoded.length} snippet=${decoded.take(300).replace("\n"," ")}")
                    if (decoded.contains(".m3u8")) {
                        // buscar m3u8 dentro del decoded
                        val inner = Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""").find(decoded)?.groupValues?.get(1)
                            ?: Regex("""['"]([^"']+\.m3u8[^"']*)['"]""").find(decoded)?.groupValues?.get(1)
                        if (inner != null) {
                            Log.d("Tvenvivo", "extractM3u8: decoded window var -> $inner")
                            return inner.replace("\\/", "/")
                        }
                    }
                } catch (e: Exception) {
                    Log.w("Tvenvivo", "extractM3u8: b64 decode error: ${e.message}")
                }
            }
            if (!foundB64) Log.d("Tvenvivo", "extractM3u8: no window var b64 found in html")
        } catch (e: Exception) {
            Log.w("Tvenvivo", "extractM3u8: window var search error: ${e.message}")
        }
        return null
    }

}