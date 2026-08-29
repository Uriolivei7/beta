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
        val html = safeGet(url) ?: return null
        val doc = Jsoup.parse(html)
        val title = doc.selectFirst("h1")?.text()?.trim()
            ?: doc.selectFirst("title")?.text()?.substringBefore("|")?.trim()
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

        // Caso ?r=BASE64 -> bestleague.top (Universal etc)
        if (targetUrl.contains("?r=")) {
            val b64 = targetUrl.substringAfter("?r=").substringBefore("&")
            try {
                val outer = String(android.util.Base64.decode(b64, android.util.Base64.DEFAULT))
                Log.d("Telelibre", "outer decode: $outer")
                // outer = https://bestleague.top/tok.html?get=XXXX
                val innerB64 = outer.substringAfter("get=").substringBefore("&")
                val inner = try { String(android.util.Base64.decode(innerB64, android.util.Base64.DEFAULT)) } catch (_: Exception) { innerB64 }
                Log.d("Telelibre", "inner: $inner")
                // Probar bestleague -> suele redirigir a zonatv/la18hd similar
                val tokHtml = safeGet(outer, referer = targetUrl)
                if (tokHtml != null) {
                    // buscar m3u8 directo o iframe
                    val m3u8 = Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""").find(tokHtml)?.value
                    if (m3u8 != null) {
                        Log.d("Telelibre", "m3u8 en tok.html: $m3u8")
                        callback(newExtractorLink(name, "$name", m3u8, ExtractorLinkType.M3U8) { this.referer = outer })
                        return true
                    }
                    val iframe = Regex("""<iframe[^>]+src=["']([^"']+)["']""").find(tokHtml)?.groupValues?.get(1)
                    if (iframe != null) {
                        val iframeUrl = if (iframe.startsWith("http")) iframe else "https://bestleague.top$iframe"
                        Log.d("Telelibre", "iframe tok: $iframeUrl")
                        return resolveLa18Chain(iframeUrl, outer, callback)
                    }
                }
                // Fallback: tratar inner como id para zonatv
                // ej Universal_Comedy -> probar zonatv
                val zonatv = "https://zonatv.space/canales.php?id=${inner.lowercase().replace(" ", "-")}"
                Log.d("Telelibre", "fallback zonatv: $zonatv")
                if (resolveZonatvChain(zonatv, targetUrl, callback)) return true
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
            // Buscar siguiente iframe
            val nextIframe = Regex("""<iframe[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE).find(html)?.groupValues?.get(1)
            if (nextIframe != null) {
                nextUrl = when {
                    nextIframe.startsWith("//") -> "https:$nextIframe"
                    nextIframe.startsWith("http") -> nextIframe
                    nextIframe.startsWith("/") -> {
                        val base = nextUrl.substringBefore("/", nextUrl).let { 
                            // extraer origen
                            try { val u = java.net.URL(nextUrl); "${u.protocol}://${u.host}" } catch (_: Exception) { mainUrl }
                        }
                        if (nextIframe.startsWith("/mpd")) "https://tele-libre.buzz$nextIframe" else "$base$nextIframe"
                    }
                    else -> nextIframe
                }
                Log.d("Telelibre", "step ${depth+1} iframe: $nextUrl")
                // Manejo especial mpd2.php?id=xxx -> construye zonatv
                if (nextIframe.contains("mpd2.php")) {
                    // mpd2.php redirige a zonatv.space/canales.php?id=xxx
                    // lo parseamos igual
                }
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
        val iframe = Regex("""<iframe[^>]+src=["']([^"']+)["']""").find(html)?.groupValues?.get(1) ?: return false
        val next = if (iframe.startsWith("//")) "https:$iframe" else iframe
        return resolveLa18Chain(next, url, callback)
    }

    private suspend fun resolveLa18Chain(url: String, referer: String, callback: (ExtractorLink) -> Unit): Boolean {
        val html = safeGet(url, referer) ?: return false
        val playback = Regex("""playbackURL\s*=\s*["']([^"']+)["']""").find(html)?.groupValues?.get(1) ?: return false
        val m3u8 = playback.replace("\\/", "/")
        Log.d("Telelibre", "resolveLa18 m3u8: $m3u8")
        callback(newExtractorLink(name, name, m3u8, ExtractorLinkType.M3U8) { this.referer = url })
        return true
    }
}
