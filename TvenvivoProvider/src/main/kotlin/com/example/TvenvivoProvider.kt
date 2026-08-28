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
        val html = safeAppGet(mainUrl) ?: run { Log.e("Tvenvivo", "getMainPage: failed to get HTML"); return null }
        val channels = extractChannelsFromHtml(html)
        if (channels.isEmpty()) Log.w("Tvenvivo", "getMainPage: 0 canales extraídos (selectors changed?)")
        val uniqueChannels = channels.distinctBy { (_, link, _) -> link }
        val channelResults = uniqueChannels.mapNotNull { (titleRaw, link, img) ->
            val title = titleRaw.replace("Ver ", "").replace(" en vivo", "").trim()
            if (nowAllowed.any { title.contains(it, ignoreCase = true) }) return@mapNotNull null
            newTvSeriesSearchResponse(name = title, url = fixUrl(link)) {
                this.type = TvType.Live
                this.posterUrl = fixUrl(img)
            }
        }
        Log.d("Tvenvivo", "getMainPage: ${channelResults.size} canales")
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
        val html = safeAppGet(mainUrl) ?: run { Log.e("Tvenvivo", "Search: failed to get HTML"); return emptyList() }
        val channels = extractChannelsFromHtml(html)
        if (channels.isEmpty()) Log.w("Tvenvivo", "Search: 0 canales (getMainPage also failing?)")
        if (query.isBlank()) return emptyList()
        val filtered = channels.filterNot { (titleRaw, _, _) ->
            val shouldFilter = nowAllowed.any { titleRaw.contains(it, ignoreCase = true) } || titleRaw.isBlank()
            shouldFilter
        }
        val matched = filtered.filter { (titleRaw, _, _) ->
            titleRaw.contains(query, ignoreCase = true)
        }
        Log.d("Tvenvivo", "Search: ${matched.size} resultados para '$query'")
        return matched.mapNotNull { (titleRaw, linkRaw, imgRaw) ->
            val title = titleRaw.replace("Ver ", "").replace(" en vivo", "").trim()
            newLiveSearchResponse(name = title, url = fixUrl(linkRaw), type = TvType.Live) {
                this.posterUrl = fixUrl(imgRaw)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = safeAppGet(url) ?: run { Log.e("Tvenvivo", "load: failed to get HTML $url"); return null }
        val doc = Jsoup.parse(html)
        val title = doc.selectFirst("h1.font-bold")?.text()
            ?: doc.selectFirst("h1")?.text()
            ?: doc.selectFirst("meta[property='og:title']")?.attr("content")
            ?: doc.selectFirst("title")?.text()
            ?: run { Log.w("Tvenvivo", "load: title not found $url"); "Canal Desconocido" }
        val cleanTitle = title.replace(Regex("""(?i)\bVer\s+"""), "").replace(Regex("""(?i)\s*en\s+vivo(\s*hd)?"""), "").replace(Regex("""\s*\|\s*.*"""), "").trim()
        val poster = doc.selectFirst("div.info-logo img")?.attr("src") ?: doc.selectFirst("meta[name='og:image']")?.attr("content") ?: doc.selectFirst("section img[src*='/imge/']")?.attr("src") ?: doc.selectFirst("meta[property='og:image']")?.attr("content") ?: ""
        val description = doc.selectFirst("section.info div.info-card")?.text() ?: doc.selectFirst("meta[property='og:description']")?.attr("content") ?: doc.selectFirst("meta[name=description]")?.attr("content") ?: ""
        if (poster.isBlank()) Log.w("Tvenvivo", "load: poster not found for $cleanTitle")
        Log.d("Tvenvivo", "load: $cleanTitle")
        val episodes = listOf(newEpisode(data = url) { this.name = "En Vivo"; this.posterUrl = fixUrlNull(poster) })
        return newTvSeriesLoadResponse(name = cleanTitle, url = url, type = TvType.Live, episodes = episodes) {
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

            // Obtener cookies de la página del canal (targetUrl)
            val channelPageResp = withTimeoutOrNull(25000L) {
                app.get(targetUrl, headers = mainHeaders, interceptor = cfKiller)
            }
            val channelCookies = channelPageResp?.cookies ?: emptyMap()
            Log.d("Tvenvivo", "channelPageResp: ${channelPageResp?.code ?: "null"} cookies: ${channelCookies.size}")
            if (channelCookies.isEmpty()) {
                Log.w("Tvenvivo", "channel page no cookies, will try live/core.php")
            }

            return coroutineScope {
                for ((displayIdx, rawUrl) in finalLinks.withIndex()) {
                    if (tryLoadOption(targetUrl, rawUrl, displayIdx, mainHeaders, channelCookies, callback)) {
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
                    // 1. Pedir live/core.php (u otro) CON cookies de la página del canal
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
                    // Capturar cookies del live/core.php (incluyendo Set-Cookie headers crudos)
                    val playerCookies = if (playerResponse.cookies.isNotEmpty()) {
                        mainCookies + playerResponse.cookies
                    } else {
                        // Intentar extraer Set-Cookie headers manualmente
                        val setCookieHeaders = playerResponse.headers?.names()?.filter { it.lowercase() == "set-cookie" }
                            ?.flatMap { name -> playerResponse.headers.values(name) } ?: emptyList()
                        if (setCookieHeaders.isNotEmpty()) {
                            Log.d("Tvenvivo", "Found Set-Cookie headers: ${setCookieHeaders.size}")
                            val parsed = setCookieHeaders.associate {
                                val parts = it.split(";")[0].split("=")
                                parts[0] to parts[1]
                            }
                            mainCookies + parsed
                        } else mainCookies
                    }
                    Log.d("Tvenvivo", "playerResponse: ${playerResponse.code} cookies: ${playerResponse.cookies.size} setCookieHeaders: ${playerResponse.headers?.names()?.filter { it.lowercase() == "set-cookie" }?.size ?: 0} -> combined: ${playerCookies.size}")
                    val playerHtml = playerResponse.text

                    if (playerHtml.isBlank()) return@withTimeout false
                    val parsed = Jsoup.parse(playerHtml)
                    val pageTitle = parsed.title().lowercase()
                    if (pageTitle.contains("pagina no encontrada") || pageTitle.contains("página no encontrada") || pageTitle.contains("404")) {
                        Log.w("Tvenvivo", "Logs: Opción ${displayIndex + 1} fail rápido - página no encontrada")
                        return@withTimeout false
                    }

                    // 2. Extraer iframe src del live/core.php (apunta a stream.php)
                    val internalIframe = Regex("""<iframe[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE).find(playerHtml)?.groupValues?.get(1)
                        ?.let { it.replace("&", "&") }
                    Log.d("Tvenvivo", "internalIframe detected: ${internalIframe ?: "null"}")

                    if (internalIframe == null || internalIframe.isBlank()) {
                        Log.w("Tvenvivo", "No iframe found in playerHtml len=${playerHtml.length}")
                        return@withTimeout false
                    }

                    // 3. Pedir stream.php CON headers/cookies correctos
                    val iframeUrl = fixUrl(internalIframe)

                    // Función para intentar un dominio
                    suspend fun tryIframeDomain(domainUrl: String): String? {
                        val iframeHeaders = mainHeaders.toMutableMap().apply {
                            put("Referer", targetUrl)
                            put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                            put("Accept-Language", "es-ES,es;q=0.5")
                            put("Sec-Fetch-Site", "cross-site")
                            put("Sec-Fetch-Mode", "navigate")
                            put("Sec-Fetch-Dest", "iframe")
                            put("Sec-Fetch-Storage-Access", "none")
                            put("Sec-GPC", "1")
                            put("Upgrade-Insecure-Requests", "1")
                            put("sec-ch-ua", "\"Chromium\";v=\"152\", \"Not?A_Brand\";v=\"24\", \"Brave\";v=\"152\"")
                            put("sec-ch-ua-mobile", "?0")
                            put("sec-ch-ua-platform", "\"Windows\"")
                        }
                        // Primero SIN cfKiller (puede romper TLS con este dominio)
                        var iframeResp = withTimeoutOrNull(15000L) {
                            app.get(
                                domainUrl,
                                timeout = 15000L,
                                headers = iframeHeaders,
                                cookies = playerCookies
                            )
                        }
                        Log.d("Tvenvivo", "stream.php (no cfKiller): code=${iframeResp?.code ?: "null"} len=${iframeResp?.text?.length ?: 0} url=$domainUrl")
                        if (iframeResp == null || !iframeResp.isSuccessful) {
                            // Fallback CON cfKiller
                            iframeResp = withTimeoutOrNull(15000L) {
                                app.get(
                                    domainUrl,
                                    timeout = 15000L,
                                    headers = iframeHeaders,
                                    cookies = playerCookies,
                                    interceptor = cfKiller
                                )
                            }
                            Log.d("Tvenvivo", "stream.php (cfKiller): code=${iframeResp?.code ?: "null"} len=${iframeResp?.text?.length ?: 0}")
                        }
                        return if (iframeResp != null && iframeResp.isSuccessful) iframeResp.text else null
                    }

                    // Intentar dominio principal primero
                    var iframeHtml = tryIframeDomain(iframeUrl)
                    
                    // Si falla, probar dominio alternativo (deportes.ksdjugfssddeports.com)
                    if (iframeHtml == null && internalIframe.contains("regionales.saohgdassregions")) {
                        val altUrl = internalIframe.replace("regionales.saohgdassregions.com", "deportes.ksdjugfssddeports.com")
                        Log.d("Tvenvivo", "Trying alt domain: $altUrl")
                        val altHtml = tryIframeDomain(altUrl)
                        if (altHtml != null) {
                            Log.d("Tvenvivo", "Alt domain succeeded")
                            iframeHtml = altHtml
                        }
                    }

                    if (iframeHtml == null) {
                        Log.w("Tvenvivo", "iframe request failed on all domains url=$iframeUrl")
                        return@withTimeout false
                    }
                    Log.d("Tvenvivo", "iframeHtml len=${iframeHtml.length}")

                    // 4. Parsear var src = "playlist.php?id=...&sig=..." del JS ofuscado
                    val playlistUrl = Regex("""var\s+src\s*=\s*["']([^"']*playlist\.php[^"']*)["']""", RegexOption.IGNORE_CASE)
                        .find(iframeHtml)?.groupValues?.get(1)
                        ?.replace("\\/", "/")
                    if (playlistUrl == null) {
                        Log.w("Tvenvivo", "playlist.php URL not found in iframeHtml snippet=${iframeHtml.take(500)}")
                        return@withTimeout false
                    }
                    Log.d("Tvenvivo", "playlist.php URL: $playlistUrl")

                    // 5. Pedir playlist.php (¡este SÍ devuelve el m3u8 real!)
                    val playlistHeaders = mainHeaders.toMutableMap().apply {
                        put("Referer", iframeUrl)
                        put("Accept", "*/*")
                        put("Accept-Language", "es-ES,es;q=0.5")
                        put("Sec-Fetch-Site", "same-origin")
                        put("Sec-Fetch-Mode", "cors")
                        put("Sec-Fetch-Dest", "empty")
                    }
                    val playlistResp = withTimeoutOrNull(20000L) {
                        app.get(
                            fixUrl(playlistUrl),
                            timeout = 20000L,
                            headers = playlistHeaders,
                            cookies = playerCookies,
                            interceptor = cfKiller
                        )
                    }
                    if (playlistResp == null || !playlistResp.isSuccessful) {
                        Log.w("Tvenvivo", "playlist.php request failed: ${playlistResp?.code ?: "timeout/null"} url=$playlistUrl")
                        return@withTimeout false
                    }
                    val m3u8Content = playlistResp.text
                    Log.d("Tvenvivo", "playlist.php response len=${m3u8Content.length} isM3U8=${m3u8Content.contains("#EXTM3U")}")

                    // La respuesta de playlist.php YA ES el m3u8
                    if (!m3u8Content.contains("#EXTM3U")) {
                        Log.w("Tvenvivo", "playlist.php no devolvió m3u8 válido snippet=${m3u8Content.take(200)}")
                        return@withTimeout false
                    }
                    val m3u8Url = fixUrl(playlistUrl)

                    // Fallback: probar mirror conocido (deportes.ksdjugfssddeports.com)
                    var finalM3u8 = m3u8Url
                    if (finalM3u8 == null && internalIframe != null) {
                        val altUrl = if (internalIframe.contains("regionales.saohgdassregions")) {
                            internalIframe.replace("regionales.saohgdassregions.com", "deportes.ksdjugfssddeports.com")
                        } else if (internalIframe.contains("deportes.ksdjugfssddeports")) {
                            internalIframe
                        } else null
                        if (altUrl != null) {
                            Log.d("Tvenvivo", "Trying alt domain: $altUrl")
                            val altHeaders = mainHeaders.toMutableMap().apply {
                                put("Referer", targetUrl)
                                put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                                put("Accept-Language", "es-ES,es;q=0.5")
                                put("Sec-Fetch-Site", "cross-site")
                                put("Sec-Fetch-Mode", "navigate")
                                put("Sec-Fetch-Dest", "iframe")
                                put("Sec-Fetch-Storage-Access", "none")
                                put("Sec-GPC", "1")
                                put("Upgrade-Insecure-Requests", "1")
                                put("sec-ch-ua", "\"Chromium\";v=\"152\", \"Not?A_Brand\";v=\"24\", \"Brave\";v=\"152\"")
                                put("sec-ch-ua-mobile", "?0")
                                put("sec-ch-ua-platform", "\"Windows\"")
                            }
                            // Primero SIN cfKiller
                            var altResp = withTimeoutOrNull(15000L) {
                                app.get(altUrl, timeout = 15000L, headers = altHeaders, cookies = playerCookies)
                            }
                            Log.d("Tvenvivo", "alt stream.php (no cfKiller): code=${altResp?.code ?: "null"} len=${altResp?.text?.length ?: 0}")
                            if (altResp == null || !altResp.isSuccessful) {
                                altResp = withTimeoutOrNull(15000L) {
                                    app.get(altUrl, timeout = 15000L, headers = altHeaders, cookies = playerCookies, interceptor = cfKiller)
                                }
                                Log.d("Tvenvivo", "alt stream.php (cfKiller): code=${altResp?.code ?: "null"} len=${altResp?.text?.length ?: 0}")
                            }
                            if (altResp != null && altResp.isSuccessful) {
                                val altM3u8 = extractM3u8FromHtml(altResp.text)
                                if (altM3u8 != null) finalM3u8 = altM3u8
                                Log.d("Tvenvivo", "Alt domain extractM3u8: ${altM3u8 ?: "null"}")
                            } else {
                                Log.w("Tvenvivo", "Alt domain request failed: ${altResp?.code ?: "null"}")
                            }
                        }
                    }

                    if (!finalM3u8.isNullOrEmpty()) {
                        Log.d("Tvenvivo", "Logs: ¡Éxito! M3U8: $finalM3u8")

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
                                url = finalM3u8,
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
                return found
            }
        }
        // Buscar en window['xxx']='BASE64' - decodificar y buscar m3u8 en el decoded
        try {
            val b64Regex = Regex("""window\[['"][^'"]+['"]\]\s*=\s*['"]([A-Za-z0-9+/=]{100,})['"]""")
            for (m in b64Regex.findAll(html)) {
                val b64 = m.groupValues[1]
                try {
                    val decoded = String(android.util.Base64.decode(b64, android.util.Base64.DEFAULT), Charsets.UTF_8)
                    // Buscar m3u8 en el JS decodificado
                    val innerPatterns = listOf(
                        """(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""",
                        """['"]([^"']*\.m3u8[^"']*)['"]""",
                        """\.m3u8""",
                    )
                    for (ip in innerPatterns) {
                        val im = Regex(ip, RegexOption.IGNORE_CASE).find(decoded)
                        if (im != null) {
                            val found = if (ip.contains("https")) im.groupValues[1] else {
                                val ctxStart = maxOf(0, im.range.first - 100)
                                val ctxEnd = minOf(decoded.length, im.range.last + 100)
                                val ctx = decoded.substring(ctxStart, ctxEnd)
                                Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""").find(ctx)?.groupValues?.get(1)
                            }
                            if (found != null) return found.replace("\\/", "/")
                        }
                    }
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
        // Buscar patrones de API/endpoint
        val apiPatterns = listOf(
            """/api/[^"'\s]*\.m3u8[^"'\s]*""",
            """/stream/[^"'\s]*\.m3u8[^"'\s]*""",
            """/live/[^"'\s]*\.m3u8[^"'\s]*""",
        )
        for (ap in apiPatterns) {
            val m = Regex(ap, RegexOption.IGNORE_CASE).find(html)
            if (m != null) {
                val found = m.value
                if (found.startsWith("/")) return "https://regionales.saohgdassregions.com$found"
                if (found.startsWith("http")) return found
            }
        }
        return null
    }

}