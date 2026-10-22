package com.example

import android.util.Base64
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.network.CloudflareKiller
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap

class TeleonlineProvider : MainAPI() {
    override var mainUrl = "https://teleonline.org"
    override var name = "Teleonline"
    override val supportedTypes = setOf(TvType.Live)
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    private val cfKiller = CloudflareKiller()
    private val PLAYER_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
    private val maxPagesGeneral = 2

    private fun extractPosterFromStyle(element: org.jsoup.nodes.Element): String? {
        val style = element.attr("style")
        if (style.isBlank()) return null
        return Regex("""url\s*\(?\s*['"]?\s*([^'")\s]+)""", RegexOption.IGNORE_CASE).find(style)?.groupValues?.get(1)
    }

    private data class ChannelInfo(val name: String, val url: String, val poster: String?)

    private fun extractChannels(html: String): List<ChannelInfo> {
        val channels = mutableListOf<ChannelInfo>()
        val doc = Jsoup.parse(html)
        doc.select("article.article-loop").forEach { article ->
            val link = article.selectFirst("a[rel=bookmark]")?.attr("href")
                ?: article.selectFirst("a")?.attr("href")
                ?: return@forEach
            if (link.isBlank() || link.contains("\${")) return@forEach
            val title = article.selectFirst(".entry-title")?.text()?.trim()
                ?: article.selectFirst("a p")?.text()?.trim()
                ?: return@forEach
            if (title.isBlank() || title.contains("\${")) return@forEach

            var poster: String? = null
            val imgEl = article.selectFirst(".article-image")
            if (imgEl != null) poster = extractPosterFromStyle(imgEl)
            if (poster == null) {
                poster = article.selectFirst("img")?.attr("src")?.let {
                    if (it.startsWith("http")) it else null
                }
            }
            if (poster != null && poster.contains("\${")) poster = null

            channels.add(ChannelInfo(title, link, poster))
        }
        return channels
    }

    private suspend fun safeGet(url: String, timeoutSec: Long = 15L, attempts: Int = 2): String? {
        for (attempt in 1..attempts) {
            try {
                val interceptor = if (attempt > 1) cfKiller else null
                val res = app.get(url, timeout = timeoutSec * attempt, interceptor = interceptor)
                if (res.isSuccessful) return res.text
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                Log.e("Teleonline", "safeGet error (intento $attempt): ${e.message}")
            }
        }
        return null
    }

    private val sectionCache = ConcurrentHashMap<String, List<ChannelInfo>>()
    private val sections = listOf(
        "Canales" to "/",
        "Perú" to "/canales/peru/",
        "Argentina" to "/canales/argentina/",
        "México" to "/canales/mexico/",
        "Colombia" to "/canales/colombia/",
        "Chile" to "/canales/chile/",
        "Estados Unidos" to "/canales/estados-unidos/",
        "Brasil" to "/canales/brasil/",
        "Venezuela" to "/canales/venezuela/",
        "España" to "/canales/espana/",
    )

    private suspend fun fetchSectionPage(name: String, path: String, page: Int): List<ChannelInfo> {
        val key = "$name|$page"
        sectionCache[key]?.let { return it }
        val url = if (page == 1) "$mainUrl$path" else "$mainUrl${path}page/$page/"
        val html = safeGet(url) ?: return emptyList()
        val list = extractChannels(html)
        sectionCache[key] = list
        return list
    }

    private suspend fun loadAllSections(page: Int): Map<String, List<ChannelInfo>> {
        val results = sections.map { (name, path) -> name to fetchSectionPage(name, path, page) }.amap { it }
        val map = LinkedHashMap<String, List<ChannelInfo>>()
        for ((name, list) in results) map[name] = list
        return map
    }

    private fun fold(s: String): String =
        java.text.Normalizer.normalize(s.lowercase(), java.text.Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val sectionsOut = mutableListOf<HomePageList>()

        val all = loadAllSections(1)
        for ((name, channels) in all) {
            var clean = channels.filter { it.url.isNotBlank() && !it.url.contains("\${") }
                .distinctBy { it.url }
            if (name == "Canales" && maxPagesGeneral > 1) {
                val extra = fetchSectionPage(name, "/", 2)
                clean = (clean + extra.filter { it.url.isNotBlank() && !it.url.contains("\${") }).distinctBy { it.url }
            }
            if (clean.isNotEmpty()) {
                sectionsOut.add(HomePageList(name, clean.map { it.toSearchResponse() }, isHorizontalImages = true))
            }
        }

        if (sectionsOut.isEmpty()) return null
        return newHomePageResponse(sectionsOut, hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val q = query.trim()
        if (q.isBlank()) return emptyList()
        return coroutineScope {
            val restJob = async { fetchRestSearch(q) }
            val localJob = async { localSearchResults(q) }
            val out = ArrayList<SearchResponse>()
            val seen = HashSet<String>()

            for (post in restJob.await()) {
                val titleObj = post.optJSONObject("title")
                val title = cleanHtml(
                    if (titleObj != null) titleObj.optString("rendered", "") else post.optString("title", "")
                )
                val url = post.optString("link", "").trim()
                if (title.isBlank() || url.isBlank()) continue
                if (!seen.add(url)) continue
                val poster = post.optString("poster", "")
                out.add(newLiveSearchResponse(title, url, TvType.Live) {
                    this.posterUrl = poster.takeIf { p -> p.isNotBlank() } ?: posterForUrl(url)
                })
                if (out.size >= 40) break
            }

            for (ch in localJob.await()) {
                if (out.size >= 40) break
                if (!seen.add(ch.url)) continue
                out.add(ch.toSearchResponse())
            }
            Log.d("Teleonline", "search '$q' -> ${out.size}")
            out
        }
    }

    private suspend fun localSearchResults(q: String): List<ChannelInfo> {
        val qn = fold(q)
        val out = mutableListOf<ChannelInfo>()
        val seen = HashSet<String>()
        for (ronda in 1..2) {
            val all = loadAllSections(ronda)
            for ((_, channels) in all) {
                for (ch in channels) {
                    if (ch.url.isBlank() || ch.url.contains("\${")) continue
                    if (!fold(ch.name).contains(qn)) continue
                    if (!seen.add(ch.url)) continue
                    out.add(ch)
                }
            }
            if (out.size >= 20) break
        }
        return out
    }

    private fun cleanHtml(s: String): String {
        if (s.isBlank()) return ""
        return s.replace(Regex("<[^>]*>"), "")
            .replace("&#8211;", "-").replace("&#8212;", "-")
            .replace("&amp;", "&").replace("&quot;", "\"")
            .replace("&#039;", "'").replace("&nbsp;", " ")
            .trim()
    }

    private suspend fun fetchRestSearch(q: String): List<JSONObject> {
        val url = "$mainUrl/wp-json/wp/v2/posts?search=${enc(q)}&per_page=30&_fields=id,featured_media,link,title"
        var items = emptyList<JSONObject>()
        for (attempt in 1..2) {
            try {
                val res = app.get(url, timeout = 40L)
                if (!res.isSuccessful) {
                    Log.w("Teleonline", "rest search -> HTTP ${res.code} (intento $attempt)")
                } else {
                    val arr = JSONArray(res.text)
                    val list = ArrayList<JSONObject>()
                    for (i in 0 until arr.length()) {
                        val o = arr.optJSONObject(i) ?: continue
                        list.add(o)
                    }
                    Log.d("Teleonline", "rest search -> ${list.size} items (intento $attempt)")
                    items = list
                    break
                }
            } catch (e: Exception) {
                Log.w("Teleonline", "rest search fallo (intento $attempt): ${e.message}")
            }
            if (attempt < 2) delay(1500L)
        }
        if (items.isEmpty()) return items

        val mediaIds = items.mapNotNull { it.optLong("featured_media", 0L).takeIf { id -> id > 0 } }
            .distinct()
        val posters = HashMap<Long, String>()
        for (tanda in mediaIds.chunked(10)) {
            val res = tanda.amap { mid -> mid to fetchMediaUrl(mid) }
            for ((mid, u) in res) {
                if (u.isNotBlank()) posters[mid] = u
            }
        }
        for (it in items) {
            it.put("poster", posters[it.optLong("featured_media", 0L)] ?: "")
        }
        return items
    }

    private suspend fun fetchMediaUrl(mid: Long): String {
        return try {
            val r = app.get("$mainUrl/wp-json/wp/v2/media/$mid?_fields=source_url", timeout = 15L)
            if (r.isSuccessful) JSONObject(r.text).optString("source_url", "") else ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun posterForUrl(url: String): String? {
        for (list in sectionCache.values) {
            for (ch in list) {
                if (ch.url == url) return ch.poster
            }
        }
        return null
    }

    private fun enc(s: String): String = java.net.URLEncoder.encode(s, "UTF-8")

    override suspend fun load(url: String): LoadResponse? {
        try {
            val html = safeGet(url, 20L) ?: return null
            val doc = Jsoup.parse(html)

            val title = doc.selectFirst(".titulo-canal-manual")?.text()
                ?: doc.selectFirst("h1")?.text()
                ?: doc.selectFirst("title")?.text()
                ?: "Canal"

            var poster = doc.selectFirst(".logo-channel img")?.attr("src")
            if (poster.isNullOrBlank()) {
                val jsonLd = Regex(""""logo"\s*:\s*\{[^}]*"url"\s*:\s*"([^"]+)""").find(html)
                if (jsonLd != null) poster = jsonLd.groupValues[1]
            }
            if (poster.isNullOrBlank()) {
                poster = doc.selectFirst("meta[property='og:image']")?.attr("content")
            }

            val desc = doc.selectFirst("meta[property='og:description']")?.attr("content")
                ?: doc.selectFirst("meta[name=description]")?.attr("content")
                ?: ""

            val episodes = listOf(newEpisode(url) {
                this.name = "En Vivo"
                this.posterUrl = poster
            })
            return newTvSeriesLoadResponse(title, url, TvType.Live, episodes) {
                this.posterUrl = poster
                this.plot = desc
            }
        } catch (e: Exception) {
            Log.e("Teleonline", "load error: ${e.message}")
            return null
        }
    }

    override suspend fun loadLinks(
        data: String, isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit
    ): Boolean {
        try {
            val mainHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9"
            )

            val mainHtml = withTimeoutOrNull(20000L) {
                app.get(data, headers = mainHeaders, interceptor = cfKiller)
            }?.text ?: return false

            val b64Pattern = Regex("""(?:atob|btoa)\s*\(\s*['"][A-Za-z0-9+/=]+['"]""")
            val b64Matches = b64Pattern.findAll(mainHtml).toList()
            if (b64Matches.isNotEmpty()) {
                val candidates = ArrayList<Pair<String, String>>()
                for ((idx, match) in b64Matches.withIndex()) {
                    try {
                        val decoded = String(Base64.decode(match.groupValues[1], Base64.DEFAULT))
                        if (decoded.contains(".m3u8")) {
                            val m3u8Url = decoded.replace("\\/", "/").trim()
                            Log.d("Teleonline", "M3U8 from base64 (opción ${idx + 1}): $m3u8Url")
                            candidates.add(m3u8Url to "En Vivo - Opción ${idx + 1}")
                        }
                    } catch (_: Exception) {}
                }
                if (candidates.isNotEmpty()) {
                    return emitAlive(candidates, data, callback)
                }
            }

            val directM3u8 = extractM3u8FromHtml(mainHtml)
            if (directM3u8 != null) {
                return emitAlive(listOf(directM3u8 to "$name - En Vivo"), data, callback)
            }
            val doc = Jsoup.parse(mainHtml)
            val playerUrls = doc.select("iframe[src], a[href*=/live], a[href*=.php], div[data-player] a, [data-src], iframe[src*=/embed]")
                .mapNotNull { el ->
                    when {
                        el.tagName() == "iframe" -> el.attr("src")
                        el.hasAttr("data-src") -> el.attr("data-src")
                        else -> el.attr("href")
                    }
                }
                .filter { it.isNotBlank() && !it.contains("facebook") && !it.contains("twitter") && !it.contains("supportiveinvoicevarnish") }
                .distinct()
                .map { if (it.startsWith("http")) it else "$mainUrl$it" }
                .filter { it.startsWith("http") }

            Log.d("Teleonline", "playerUrls: $playerUrls")

            for ((idx, playerUrl) in playerUrls.withIndex()) {
                try {
                    if (playerUrl.contains("youtube.com") || playerUrl.contains("youtu.be")) {
                        val ytId = Regex("""embed/([^/?]+)""").find(playerUrl)?.groupValues?.get(1)
                        val ytUrl = if (ytId != null) "https://www.youtube.com/watch?v=$ytId" else playerUrl
                        callback(newExtractorLink(name, "YouTube", ytUrl, ExtractorLinkType.M3U8))
                        return true
                    }

                    val playerHtml = withTimeoutOrNull(15000L) {
                        app.get(playerUrl, headers = mainHeaders + mapOf("Referer" to data), interceptor = cfKiller)
                    }?.text ?: continue

                                        val m3u8Url = extractM3u8FromHtml(playerHtml)
                    if (m3u8Url != null) {
                        return emitAlive(listOf(m3u8Url to "$name - Opción ${idx + 1}"), playerUrl, callback)
                    }} catch (_: Exception) {}
            }
            return false
        } catch (e: Exception) {
            Log.e("Teleonline", "loadLinks error: ${e.message}")
            return false
        }
    }

    private suspend fun emitAlive(
        candidates: List<Pair<String, String>>,
        referer: String,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        if (candidates.isEmpty()) return false
        val probes = candidates.amap { (url, label) ->
            val alive = try {
                val r = withTimeoutOrNull(8000L) {
                    app.get(url, headers = mapOf(
                        "User-Agent" to PLAYER_UA,
                        "Referer" to referer
                    ))
                }
                r != null && r.isSuccessful && r.text.contains("#EXTM3U")
            } catch (e: Exception) {
                false
            }
            Triple(url, label, alive)
        }
        var found = false
        for ((url, label, alive) in probes) {
            if (!alive) {
                Log.w("Teleonline", "servidor caido, se omite: ${url.take(90)}")
                continue
            }
            callback(newExtractorLink(name, label, url, ExtractorLinkType.M3U8) {
                this.headers = mapOf(
                    "User-Agent" to PLAYER_UA,
                    "Referer" to referer
                )
            })
            found = true
        }
        Log.d("Teleonline", "links vivos: ${probes.count { it.third }}/${probes.size}")
        return found
    }
    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val host = request.url.host
                // No tocar YouTube/Google (usa sus propios headers)
                if (host.contains("youtube.com") || host.contains("youtu.be") || host.contains("googlevideo.com")) {
                    return chain.proceed(request)
                }
                val builder = request.newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36")
                    .header("Accept", "*/*")
                    .header("Accept-Language", "es-ES,es;q=0.9")
                val referer = extractorLink.headers["Referer"]
                if (!referer.isNullOrBlank()) {
                    builder.header("Referer", referer)
                    try {
                        val u = java.net.URL(referer)
                        builder.header("Origin", "${u.protocol}://${u.host}")
                    } catch (_: Exception) {}
                }
                val response = chain.proceed(builder.build())
                if (response.code !in 200..299) {
                    Log.w("Teleonline", "Intercept ${response.code} -> ${request.url.toString().take(130)}")
                }
                return response
            }
        }
    }

    private fun extractM3u8FromHtml(html: String): String? {
        val patterns = listOf(
            """["'](https?[:\/\/\\]+[^"']+\.m3u8[^"']*)["']""",
            """source\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""",
            """file\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""",
            """var\s+src\s*=\s*["']([^"']+\.m3u8[^"']*)["']""",
            """(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""",
        )
        for (pattern in patterns) {
            val match = Regex(pattern, RegexOption.IGNORE_CASE).find(html)
            if (match != null) {
                return match.groupValues[1].replace("\\/", "/")
            }
        }
        return null
    }

    private fun ChannelInfo.toSearchResponse() = newLiveSearchResponse(name, url, TvType.Live) {
        this.posterUrl = poster
    }
}