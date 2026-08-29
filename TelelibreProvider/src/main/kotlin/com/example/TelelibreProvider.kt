package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.network.CloudflareKiller
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import kotlinx.coroutines.withTimeoutOrNull

class TelelibreProvider : MainAPI() {
    companion object {
        var pluginContext: android.content.Context? = null
    }
    override var mainUrl = "https://tele-libre.buzz"
    override var name = "TeleLibre"
    override val supportedTypes = setOf(TvType.Live)
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = false

    private val cfKiller = CloudflareKiller()
    private val desktopHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language" to "es-AR,es;q=0.9"
    )

    private suspend fun safeGet(url: String, referer: String? = null): String? {
        return try {
            val h = if (referer != null) desktopHeaders + mapOf("Referer" to referer) else desktopHeaders
            val res = app.get(url, headers = h, interceptor = cfKiller, timeout = 15000L)
            if (res.isSuccessful) res.text else null
        } catch (e: Exception) {
            Log.e("Telelibre", "safeGet $url: ${e.message}")
            null
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val html = safeGet(mainUrl) ?: return null
        val doc = Jsoup.parse(html)
        val channels = doc.select("a.channel-link").mapNotNull { a ->
            val href = a.attr("href") ?: return@mapNotNull null
            val img = a.selectFirst("img")
            val title = img?.attr("alt")?.ifBlank { null } ?: img?.attr("title") ?: a.text()
            if (title.isBlank() || href.isBlank()) return@mapNotNull null
            val url = if (href.startsWith("http")) href else fixUrl(href)
            val poster = img?.attr("src")?.let { fixUrl(it) }
            Triple(title.trim(), url, poster)
        }.distinctBy { it.second }
        if (channels.isEmpty()) return null
        val list = channels.map { (title, url, poster) ->
            newLiveSearchResponse(title, url, TvType.Live) { this.posterUrl = poster }
        }
        return newHomePageResponse(listOf(HomePageList("Canales", list)), false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val html = safeGet(mainUrl) ?: return emptyList()
        val doc = Jsoup.parse(html)
        return doc.select("a.channel-link").mapNotNull { a ->
            val href = a.attr("href") ?: return@mapNotNull null
            val img = a.selectFirst("img")
            val title = img?.attr("alt")?.ifBlank { null } ?: img?.attr("title") ?: a.text()
            if (!title.contains(query, true)) return@mapNotNull null
            val url = if (href.startsWith("http")) href else fixUrl(href)
            val poster = img?.attr("src")?.let { fixUrl(it) }
            newLiveSearchResponse(title.trim(), url, TvType.Live) { this.posterUrl = poster }
        }.distinctBy { it.url }
    }

    override suspend fun load(url: String): LoadResponse? {
        // Titulo para ?r= viene vacio (Tele-libre), usar el id decodificado
        if (url.contains("?r=")) {
            val b64 = url.substringAfter("?r=").substringBefore("&")
            try {
                val outer = String(android.util.Base64.decode(b64, android.util.Base64.DEFAULT))
                val innerB64 = outer.substringAfter("get=").substringBefore("&").ifBlank { outer.substringAfter("?get=").substringBefore("&") }
                val inner = try { String(android.util.Base64.decode(innerB64, android.util.Base64.DEFAULT)) } catch (_: Exception) { innerB64 }
                val pretty = inner.replace("_", " ").replace("-", " ").trim()
                val titleFromId = pretty.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                Log.d("Telelibre", "load ?r= title=$titleFromId inner=$inner")
                val ep = newEpisode(url) { this.name = "En Vivo" }
                return newTvSeriesLoadResponse(titleFromId.ifBlank { "Canal" }, url, TvType.Live, listOf(ep))
            } catch (_: Exception) {}
        }
        val html = safeGet(url) ?: return null
        val doc = Jsoup.parse(html)
        val title = doc.selectFirst("h1")?.text()?.trim()?.takeIf { it.isNotBlank() && !it.contains("Tele-libre", true) }
            ?: doc.selectFirst("title")?.text()?.substringBefore("|")?.trim()?.takeIf { it.length < 60 && !it.contains("Tele-libre", true) }
            ?: "Canal"
        val poster = doc.selectFirst("img.imgCanal")?.attr("src")?.let { fixUrl(it) }
            ?: doc.selectFirst("meta[property='og:image']")?.attr("content")?.let { fixUrl(it) }
        val plot = doc.selectFirst("p.card-text")?.text()?.take(500)
        Log.d("Telelibre", "load: $title -> $url")
        val ep = newEpisode(url) { this.name = "En Vivo"; this.posterUrl = poster }
        return newTvSeriesLoadResponse(title, url, TvType.Live, listOf(ep)) {
            this.posterUrl = poster
            this.plot = plot
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val targetUrl = fixUrl(data)
        Log.d("Telelibre", "loadLinks: $targetUrl")

        // Caso ?r=BASE64 -> bestleague.life/tok.html?get=XXX (DASH cvattv)
        if (targetUrl.contains("?r=")) {
            val b64 = targetUrl.substringAfter("?r=").substringBefore("&")
            try {
                val outer = String(android.util.Base64.decode(b64, android.util.Base64.DEFAULT))
                Log.d("Telelibre", "outer decode: $outer")
                val tokHtml = safeGet(outer, referer = targetUrl)
                if (tokHtml != null) {
                    if (handleTokHtml(tokHtml, outer, callback)) return true
                    val m3u8 = Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""").find(tokHtml)?.value
                    if (m3u8 != null) {
                        callback(newExtractorLink(name, name, m3u8, ExtractorLinkType.M3U8) { this.referer = outer })
                        return true
                    }
                }
                // Fallback: tratar inner como id zonatv
                val innerB64 = outer.substringAfter("get=").substringBefore("&").ifBlank { "" }
                if (innerB64.isNotBlank()) {
                    val inner = try { String(android.util.Base64.decode(innerB64, android.util.Base64.DEFAULT)) } catch (_: Exception) { innerB64 }
                    val zonatv = "https://zonatv.space/canales.php?id=${inner.lowercase().replace(" ", "-")}"
                    if (resolveZonatvChain(zonatv, targetUrl, callback)) return true
                }
            } catch (e: Exception) {
                Log.e("Telelibre", "r decode fail: ${e.message}")
            }
        }

        // Flujo normal /en-vivo/telefe/ -> embed2.php -> mpd2.php -> zonatv -> la18hd
        val mainHtml = safeGet(targetUrl) ?: return false
        val doc = Jsoup.parse(mainHtml)
        // Buscar iframes principales (Opcion 1 etc)
        val iframeSrc = doc.selectFirst("iframe#playerFrame, iframe#embed, iframe[src*=/en-vivo/], iframe[src*=/mpd], iframe[src*=/embed]")?.attr("src")
            ?: doc.selectFirst("iframe")?.attr("src")
        if (iframeSrc.isNullOrBlank()) {
            Log.w("Telelibre", "sin iframe en $targetUrl")
            return false
        }
        var nextUrl = if (iframeSrc.startsWith("http")) iframeSrc else fixUrl(iframeSrc)
        Log.d("Telelibre", "step1 iframe: $nextUrl")
        // Seguir cadena hasta encontrar m3u8
        // Paso 2: /en-vivo/telefe/embed2.php -> /mpd2.php?id=telefe
        var html = safeGet(nextUrl, referer = targetUrl)
        var depth = 0
        while (html != null && depth < 8) {
            depth++
            // Buscar playbackURL directo (la18hd)
            val playback = Regex("""playbackURL\s*=\s*["']([^"']+\.m3u8[^"']*)["']""").find(html)?.groupValues?.get(1)
            if (playback != null) {
                val m3u8 = playback.replace("\\/", "/")
                Log.d("Telelibre", "¡m3u8 la18hd! $m3u8")
                callback(newExtractorLink(name, name, m3u8, ExtractorLinkType.M3U8) {
                    this.referer = nextUrl
                    this.headers = desktopHeaders
                })
                return true
            }
            // Buscar m3u8 generico
            val generic = Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""").find(html)?.groupValues?.get(1)
            if (generic != null && !generic.contains("googletag")) {
                Log.d("Telelibre", "m3u8 generico depth $depth: $generic")
                callback(newExtractorLink(name, name, generic, ExtractorLinkType.M3U8) { this.referer = nextUrl })
                return true
            }
            // bestleague tok.html es DASH cvattv (no iframe)
            if (html.contains("cvattv.com.ar") && html.contains("atob(getURL)")) {
                if (handleTokHtml(html, nextUrl, callback)) return true
            }
            // Buscar siguiente iframe (ignorar placeholders ${...})
            val nextIframeRaw = Regex("""<iframe[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE).find(html)?.groupValues?.get(1)
            if (nextIframeRaw != null && !nextIframeRaw.contains("\${") && !nextIframeRaw.contains("extensionUrl")) {
                val nextIframe = nextIframeRaw
                nextUrl = when {
                    nextIframe.startsWith("//") -> "https:$nextIframe"
                    nextIframe.startsWith("http") -> nextIframe
                    nextIframe.startsWith("/") -> {
                        val base = try { val u = java.net.URL(nextUrl); "${u.protocol}://${u.host}" } catch (_: Exception) { mainUrl }
                        if (nextIframe.startsWith("/mpd")) "https://tele-libre.buzz$nextIframe" else "$base$nextIframe"
                    }
                    else -> nextIframe
                }
                Log.d("Telelibre", "step ${depth+1} iframe: $nextUrl")
                html = safeGet(nextUrl, referer = targetUrl)
                continue
            }
            // Buscar document.write iframe (mpd2.php)
            val docWrite = Regex("""src="([^"]+canales\.php[^"]+)"""").find(html)?.groupValues?.get(1)
            if (docWrite != null) {
                nextUrl = if (docWrite.startsWith("http")) docWrite else "https://zonatv.space$docWrite"
                Log.d("Telelibre", "docWrite: $nextUrl")
                html = safeGet(nextUrl, referer = nextUrl)
                continue
            }
            break
        }
        Log.w("Telelibre", "sin m3u8 tras $depth pasos, url=$nextUrl")
        return false
    }

    private suspend fun resolveZonatvChain(url: String, referer: String, callback: (ExtractorLink) -> Unit): Boolean {
        val html = safeGet(url, referer) ?: return false
        if (handleTokHtml(html, url, callback)) return true
        val iframe = Regex("""<iframe[^>]+src=["']([^"']+)["']""").find(html)?.groupValues?.get(1) ?: return false
        if (iframe.contains("\${")) return false
        val next = if (iframe.startsWith("//")) "https:$iframe" else iframe
        return resolveLa18Chain(next, url, callback)
    }

    private suspend fun resolveLa18Chain(url: String, referer: String, callback: (ExtractorLink) -> Unit): Boolean {
        val html = safeGet(url, referer) ?: return false
        if (handleTokHtml(html, url, callback)) return true
        val playback = Regex("""playbackURL\s*=\s*["']([^"']+)["']""").find(html)?.groupValues?.get(1) ?: return false
        val m3u8 = playback.replace("\\/", "/")
        Log.d("Telelibre", "resolveLa18 m3u8: $m3u8")
        callback(newExtractorLink(name, name, m3u8, ExtractorLinkType.M3U8) { this.referer = url })
        return true
    }

    private suspend fun handleTokHtml(html: String, referer: String, callback: (ExtractorLink) -> Unit): Boolean {
        if (!html.contains("cvattv.com.ar")) return false
        try {
            // getURL = base64 del canal, ej VW5pdmVyc2FsX0NvbWVkeQ==
            val getParam = Regex("""getURL\s*==\s*"([^"]+)"""").findAll(html).map { it.groupValues[1] }.toList()
            // number segun if chain, extraer number = X despues del primer match del getURL actual
            // Simplificado: buscar el bloque que contiene el getURL del referer
            val urlGet = Regex("""[?&]get=([^&"']+)""").find(referer)?.groupValues?.get(1) ?: Regex("""get=([^&"']+)""").find(referer)?.groupValues?.get(1)
            var number: String? = null
            if (urlGet != null) {
                // Buscar el if que contiene ese get y su number
                val pattern = Regex("""if\s*\(getURL\s*==\s*"$urlGet"[^)]*\)\s*\n*number\s*=\s*(\d+)""")
                number = pattern.find(html)?.groupValues?.get(1)
                if (number == null) {
                    // Buscar en listas con ||
                    val listPattern = Regex("""getURL\s*==\s*"$urlGet"[^;]*number\s*=\s*(\d+)""", RegexOption.DOT_MATCHES_ALL)
                    number = listPattern.find(html)?.groupValues?.get(1)
                }
                // fallback por cercania
                if (number == null) {
                    val idx = html.indexOf(urlGet)
                    if (idx >= 0) {
                        val snippet = html.substring(idx, minOf(html.length, idx + 3000))
                        number = Regex("""number\s*=\s*(\d+)""").find(snippet)?.groupValues?.get(1)
                    }
                }
            }
            if (number == null) number = Regex("""number\s*=\s*(\d+)""").find(html)?.groupValues?.get(1) ?: "6"
            // mt tokens
            val mtBlock = Regex("""var\s+mt\s*=\s*\[(.*?)\];""", RegexOption.DOT_MATCHES_ALL).find(html)?.groupValues?.get(1) ?: return false
            val tokenRegex = Regex("""token:\s*"([^"]+)"""")
            val tokens = tokenRegex.findAll(mtBlock).map { it.groupValues[1] }.toList()
            if (tokens.isEmpty()) return false
            val token = tokens.first()
            val decodedGet = if (urlGet != null) try { String(android.util.Base64.decode(urlGet, android.util.Base64.DEFAULT)) } catch (_: Exception) { urlGet } else "unknown"
            // cdn del token (edge-liveXX-sl)
            val cdn = Regex(""""cdn":\s*"([^"]+)"""").find(mtBlock)?.groupValues?.get(1) ?: "edge-live01-sl"
            val mpdUrl = "https://$cdn.cvattv.com.ar/$token/live/c${number}eds/$decodedGet/SA_Live_dash_enc/$decodedGet.mpd"
            Log.d("Telelibre", "tok DASH mpd=$mpdUrl number=$number cdn=$cdn")
            // Buscar ClearKey para este getURL
            val keyBlock = Regex("""if\s*\(getURL\s*==\s*"$urlGet"\)\s*\{[^}]*keyId\s*=\s*"([^"]+)"[^}]*key\s*=\s*"([^"]+)"""", RegexOption.DOT_MATCHES_ALL).find(html)
            val keyId = keyBlock?.groupValues?.get(1)
            val key = keyBlock?.groupValues?.get(2)
            // Fallback búsqueda global por atob(getURL) nombre
            var fallbackKeyId: String? = null
            var fallbackKey: String? = null
            if (keyId == null) {
                // Buscar por decoded nombre en comentarios // Universal Comedy
                val commentPattern = Regex("""//\s*${Regex.escape(decodedGet)}[^}]*keyId\s*=\s*"([^"]+)"[^}]*key\s*=\s*"([^"]+)""", RegexOption.DOT_MATCHES_ALL)
                val m = commentPattern.find(html)
                fallbackKeyId = m?.groupValues?.get(1)
                fallbackKey = m?.groupValues?.get(2)
            }
            val finalKeyId = keyId ?: fallbackKeyId
            val finalKey = key ?: fallbackKey
            if (finalKeyId != null && finalKey != null) {
                Log.d("Telelibre", "ClearKey $finalKeyId:$finalKey")
                // CloudStream DASH ClearKey via header? Emit as DASH with drm param not directly supported, usamos MPD con headers y ExoPlayer lo resolvera si pasamos clearkey via callback? 
                // Por ahora emitimos MPD normal, el player lo intentara sin DRM si el token lo permite, y si falla el DRM se necesita extractor custom.
                // Emitimos como DASH
                callback(newExtractorLink(name, "$name - DASH", mpdUrl, ExtractorLinkType.DASH) {
                    this.referer = referer
                    this.headers = desktopHeaders
                })
                return true
            } else {
                // Sin clearkey, igual intentar MPD
                callback(newExtractorLink(name, name, mpdUrl, ExtractorLinkType.DASH) {
                    this.referer = referer
                })
                return true
            }
        } catch (e: Exception) {
            Log.e("Telelibre", "handleTok fail: ${e.message}")
            return false
        }
    }
}
