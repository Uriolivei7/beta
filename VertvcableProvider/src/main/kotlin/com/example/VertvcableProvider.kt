package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URLEncoder

class VertvcableProvider : MainAPI() {
    override var mainUrl = "https://www.vertvcable.com"
    override var name = "VerTvCable"
    override val supportedTypes = setOf(TvType.Live)
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = false

    private val desktopHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9"
    )

    override val mainPage = mainPageOf(
        "/" to "Canales",
        "/category/cine/" to "Cine",
        "/category/peliculas/" to "Películas",
        "/category/deportes/" to "Deportes",
        "/category/infantil/" to "Infantil",
        "/category/musica/" to "Música",
    )

    private suspend fun safeGet(url: String, referer: String? = null): String? {
        return try {
            val h = if (referer != null) desktopHeaders + mapOf("Referer" to referer) else desktopHeaders
            val res = app.get(url, headers = h, timeout = 15L)
            if (res.isSuccessful) res.text else null
        } catch (e: Exception) {
            Log.e("VerTvCable", "safeGet $url: ${e.message}")
            null
        }
    }

    private fun parseCards(doc: org.jsoup.nodes.Document): List<SearchResponse> {
        // Cards Appyn: div.bav > a (poster real en img[data-src] por lazyload)
        return doc.select("div.bav a[href]").mapNotNull { a ->
            val href = a.attr("abs:href")
            if (href.isBlank()) return@mapNotNull null
            val title = a.selectFirst("span.title")?.text()?.trim()?.takeIf { it.isNotBlank() }
                ?: a.attr("title")?.trim()?.takeIf { it.isNotBlank() }
                ?: a.selectFirst("img")?.attr("alt")?.trim()?.takeIf { it.isNotBlank() }
                ?: return@mapNotNull null
            val img = a.selectFirst("img")
            val poster = img?.attr("data-src")?.trim()?.takeIf { it.isNotBlank() && !it.startsWith("data:") }
                ?: img?.attr("src")?.trim()?.takeIf { it.isNotBlank() && !it.startsWith("data:") }
                ?.let { fixUrl(it) }
            newLiveSearchResponse(title, href, TvType.Live) { this.posterUrl = poster }
        }.distinctBy { it.url }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val base = if (request.data == "/") "$mainUrl/" else "$mainUrl${request.data}"
        val url = if (page <= 1) base else "$base/page/$page/"
        val html = safeGet(url) ?: return null
        val doc = Jsoup.parse(html, url)
        val items = parseCards(doc)
        if (items.isEmpty()) {
            Log.w("VerTvCable", "getMainPage '${request.name}' p$page: 0 items")
            return null
        }
        val hasNext = doc.select("link[rel=next]").isNotEmpty() || items.size >= 20
        Log.d("VerTvCable", "getMainPage '${request.name}' p$page: ${items.size} canales")
        return newHomePageResponse(HomePageList(request.name, items), hasNext)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=${URLEncoder.encode(query, "UTF-8")}"
        val html = safeGet(url) ?: return emptyList()
        val doc = Jsoup.parse(html, url)
        val items = parseCards(doc)
        Log.d("VerTvCable", "search '$query': ${items.size}")
        return items
    }

    private fun cleanTitle(t: String): String {
        var s = t.trim()
        s = s.replace(Regex("\\s+en VIVO\\s*$", RegexOption.IGNORE_CASE), "")
        s = s.substringBefore("|").trim()
        return s.ifBlank { t.trim() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = safeGet(url) ?: return null
        val doc = Jsoup.parse(html, url)
        val title = cleanTitle(
            doc.selectFirst("h1")?.text()?.trim()?.takeIf { it.isNotBlank() }
                ?: doc.selectFirst("meta[property='og:title']")?.attr("content")?.trim()?.takeIf { it.isNotBlank() }
                ?: "Canal"
        )
        val poster = doc.selectFirst("meta[property='og:image']")?.attr("content")?.let { fixUrl(it) }
        val plot = doc.selectFirst("meta[name='description']")?.attr("content")
            ?: doc.selectFirst("meta[property='og:description']")?.attr("content")
        val pills = doc.select(".vtc-pill[data-post]").size
        Log.d("VerTvCable", "load: $title -> $url (opciones=$pills)")
        val ep = newEpisode(url) { this.name = "En Vivo"; this.posterUrl = poster }
        return newTvSeriesLoadResponse(title, url, TvType.Live, listOf(ep)) {
            this.posterUrl = poster
            this.plot = plot
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val targetUrl = fixUrl(data)
        Log.d("VerTvCable", "loadLinks: $targetUrl")
        val mainHtml = safeGet(targetUrl) ?: return false
        val doc = Jsoup.parse(mainHtml, targetUrl)
        val pills = doc.select(".vtc-pill[data-post]")
        if (pills.isEmpty()) {
            Log.w("VerTvCable", "sin pills en $targetUrl")
            return false
        }
        var ok = false
        for (pill in pills) {
            val post = pill.attr("data-post")
            val idx = pill.attr("data-index").ifBlank { "0" }
            val nonce = pill.attr("data-nonce")
            val label = pill.text().trim().ifBlank { "Opción ${idx.toIntOrNull()?.plus(1) ?: idx}" }
            if (post.isBlank() || nonce.isBlank()) continue
            Log.d("VerTvCable", "ajax post=$post index=$idx ($label)")
            val ajaxText = try {
                app.post(
                    "$mainUrl/wp-admin/admin-ajax.php",
                    headers = mapOf(
                        "User-Agent" to (desktopHeaders["User-Agent"] ?: ""),
                        "Referer" to targetUrl,
                        "Origin" to mainUrl,
                        "X-Requested-With" to "XMLHttpRequest",
                        "Accept" to "application/json, */*",
                    ),
                    data = mapOf(
                        "action" to "vtc_get_stream_url",
                        "post_id" to post,
                        "stream_index" to idx,
                        "nonce" to nonce,
                    ),
                    timeout = 15L
                ).text
            } catch (e: Exception) {
                Log.e("VerTvCable", "ajax fail ($label): ${e.message}")
                continue
            }
            try {
                val dataObj = JSONObject(ajaxText).optJSONObject("data") ?: continue
                val embedUrl = dataObj.optString("url")
                val type = dataObj.optString("type").uppercase()
                val ajaxLabel = dataObj.optString("label").ifBlank { label }
                if (embedUrl.isBlank()) continue
                Log.d("VerTvCable", "ajax OK ($ajaxLabel): type=$type url=${embedUrl.take(100)}")
                when (type) {
                    "EMBED" -> if (resolveEmbed(embedUrl, targetUrl, ajaxLabel, subtitleCallback, callback)) ok = true
                    "HLS", "MP4" -> {
                        val lt = if (type == "HLS") ExtractorLinkType.M3U8 else INFER_TYPE
                        callback(newExtractorLink(name, "$name [$ajaxLabel]", embedUrl, lt) { this.referer = targetUrl })
                        ok = true
                    }
                    "DASH" -> {
                        callback(newExtractorLink(name, "$name [$ajaxLabel]", embedUrl, ExtractorLinkType.DASH) { this.referer = targetUrl })
                        ok = true
                    }
                    else -> if (resolveEmbed(embedUrl, targetUrl, ajaxLabel, subtitleCallback, callback)) ok = true
                }
            } catch (e: Exception) {
                Log.e("VerTvCable", "ajax parse fail ($label): ${e.message}")
            }
        }
        if (!ok) Log.w("VerTvCable", "sin links en ${pills.size} opciones")
        return ok
    }

    // embed page -> iframe /ver/?id= (config url + clearkey) u otro host
    private suspend fun resolveEmbed(embedUrl: String, referer: String, label: String, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val html = safeGet(embedUrl, referer) ?: return false
        // 1. Config Clappr/Shaka con ClearKey: const config = {"url":"...mpd","clearkey":{kid:key}}
        val cfgRaw = Regex("""const config\s*=\s*(\{.*?\});""", RegexOption.DOT_MATCHES_ALL).find(html)?.groupValues?.get(1)
        if (cfgRaw != null) {
            try {
                val cfg = JSONObject(cfgRaw)
                var mpd = cfg.optString("url")
                if (mpd.startsWith("//")) mpd = "https:$mpd"
                if (mpd.contains(".mpd")) {
                    val keys = cfg.optJSONObject("clearkey")
                    Log.d("VerTvCable", "DASH mpd=$mpd keys=${keys?.length() ?: 0} ($label)")
                    if (keys != null && keys.length() > 0) {
                        val kid = keys.keys().next()
                        val key = keys.optString(kid)
                        callback(newDrmExtractorLink(name, "$name [$label]", mpd, ExtractorLinkType.DASH,
                            java.util.UUID.fromString("e2719d58-a985-b3c9-781a-059057b03bac")) {
                            this.referer = embedUrl
                            this.headers = desktopHeaders + mapOf("Referer" to embedUrl)
                            this.kty = "oct"
                            this.kid = hexToB64Url(kid)
                            this.key = hexToB64Url(key)
                        })
                    } else {
                        Log.w("VerTvCable", "MPD sin keys, emitiendo sin DRM")
                        callback(newExtractorLink(name, "$name [$label]", mpd, ExtractorLinkType.DASH) {
                            this.referer = embedUrl
                            this.headers = desktopHeaders + mapOf("Referer" to embedUrl)
                        })
                    }
                    return true
                }
            } catch (e: Exception) {
                Log.e("VerTvCable", "config parse fail: ${e.message}")
            }
        }
        // 2. m3u8/mp4 directo en el HTML
        val direct = Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*|https?://[^"'\s<>]+\.mp4[^"'\s<>]*)""").find(html)?.value
        if (direct != null) {
            val lt = if (direct.contains(".m3u8")) ExtractorLinkType.M3U8 else INFER_TYPE
            Log.d("VerTvCable", "directo ($label): ${direct.take(120)}")
            callback(newExtractorLink(name, "$name [$label]", direct, lt) { this.referer = embedUrl })
            return true
        }
        // 3. Seguir iframe (ej. /ver/?id= o host externo)
        val frame = Regex("""<iframe[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE).find(html)?.groupValues?.get(1)
        if (frame != null && !frame.contains("\${")) {
            val next = when {
                frame.startsWith("//") -> "https:$frame"
                frame.startsWith("http") -> frame
                frame.startsWith("/") -> try {
                    val u = java.net.URL(embedUrl); "${u.protocol}://${u.host}$frame"
                } catch (_: Exception) { mainUrl + frame }
                else -> frame
            }
            Log.d("VerTvCable", "iframe ($label): $next")
            if (next == embedUrl) return false
            // Si es /ver/ u otra página del mismo flujo, reintentar resolve (1 nivel)
            if (!next.contains("youtube.com") && !next.contains("youtu.be")) {
                val html2 = safeGet(next, embedUrl) ?: return false
                // Reintentar config/m3u8 sobre el HTML anidado sin recursión infinita
                val cfg2 = Regex("""const config\s*=\s*(\{.*?\});""", RegexOption.DOT_MATCHES_ALL).find(html2)?.groupValues?.get(1)
                if (cfg2 != null) {
                    try {
                        val cfg = JSONObject(cfg2)
                        var mpd = cfg.optString("url")
                        if (mpd.startsWith("//")) mpd = "https:$mpd"
                        if (mpd.contains(".mpd")) {
                            val keys = cfg.optJSONObject("clearkey")
                            if (keys != null && keys.length() > 0) {
                                val kid = keys.keys().next()
                                callback(newDrmExtractorLink(name, "$name [$label]", mpd, ExtractorLinkType.DASH,
                                    java.util.UUID.fromString("e2719d58-a985-b3c9-781a-059057b03bac")) {
                                    this.referer = next
                                    this.headers = desktopHeaders + mapOf("Referer" to next)
                                    this.kty = "oct"
                                    this.kid = hexToB64Url(kid)
                                    this.key = hexToB64Url(key)
                                })
                            } else {
                                callback(newExtractorLink(name, "$name [$label]", mpd, ExtractorLinkType.DASH) {
                                    this.referer = next
                                    this.headers = desktopHeaders + mapOf("Referer" to next)
                                })
                            }
                            return true
                        }
                    } catch (_: Exception) {}
                }
                val direct2 = Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*|https?://[^"'\s<>]+\.mp4[^"'\s<>]*)""").find(html2)?.value
                if (direct2 != null) {
                    val lt = if (direct2.contains(".m3u8")) ExtractorLinkType.M3U8 else INFER_TYPE
                    callback(newExtractorLink(name, "$name [$label]", direct2, lt) { this.referer = next })
                    return true
                }
            }
            // Iframe externo (YouTube etc): delegar a extractores CS3
            return try {
                loadExtractor(next, embedUrl, subtitleCallback, callback)
            } catch (_: Exception) { false }
        }
        Log.w("VerTvCable", "sin stream en embed ($label)")
        return false
    }

    private fun hexToB64Url(hex: String): String {
        val clean = hex.trim().removePrefix("0x")
        // Solo convierte si es hex válido de 16 bytes (32 chars); si no, se pasa tal cual
        if (clean.length != 32 || clean.any { it !in '0'..'9' && it !in 'a'..'f' && it !in 'A'..'F' }) {
            return clean
        }
        val bytes = ByteArray(16) { i ->
            clean.substring(i * 2, i * 2 + 2).toInt(16).toByte()
        }
        return android.util.Base64.encodeToString(
            bytes,
            android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
        )
    }
}
