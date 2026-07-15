package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.network.CloudflareKiller
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import android.util.Log
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RetrotveProvider : MainAPI() {
    override var mainUrl = "https://retrotve.com"
    override var name = "RetroTVE"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val usesWebView = true
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Movie,
        TvType.Cartoon
    )

    override val mainPage = mainPageOf(
        "/category/animacion/?tr_post_type=2" to "Series Animadas",
        "/category/liveaction/" to "Series Live Action",
        "/category/animacion/?tr_post_type=1" to "Películas Animadas",
        "/peliculas/" to "Películas"
    )

    private val cfKiller = CloudflareKiller()
    private var wvResolved = false
    private var sessionCookies = mapOf<String, String>()

    private val baseHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
        "Referer" to mainUrl
    )

    @android.annotation.SuppressLint("SetJavaScriptEnabled")
    private suspend fun resolveChallengeWithWebView(): Boolean {
        val ctx = retrotveAppContext ?: run {
            Log.e("RetrotveProvider", "resolveChallenge: appContext null")
            return false
        }
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                var resumed = false
                fun done(v: Boolean) { if (!resumed) { resumed = true; cont.resume(v) } }
                try {
                    CookieManager.getInstance().setAcceptCookie(true)
                    CookieManager.getInstance().removeAllCookies(null)
                    val view = WebView(ctx.applicationContext).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        settings.cacheMode = WebSettings.LOAD_DEFAULT
                        settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36"
                    }
                    var pageFinishedCount = 0
                    var lastTitle = ""
                    view.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            pageFinishedCount++
                            Log.d("RetrotveProvider", "resolveChallenge: onPageFinished #$pageFinishedCount url=${url.take(80)}")
                            view.evaluateJavascript("(function(){ return document.title; })()") { titleRaw ->
                                if (resumed || titleRaw == null) return@evaluateJavascript
                                val t = titleRaw.trim('"').trim('\'')
                                lastTitle = t
                                Log.d("RetrotveProvider", "resolveChallenge: title=$t, pageFinished=$pageFinishedCount")
                                if (t.isNotEmpty() && !t.contains("momento") && !t.contains("just a moment") && !t.contains("Please wait") && pageFinishedCount >= 2 && t != "null") {
                                    Log.d("RetrotveProvider", "resolveChallenge: REAL PAGE LOADED! Title=$t")
                                    val cookies = CookieManager.getInstance().getCookie(mainUrl) ?: ""
                                    sessionCookies = if (cookies.isNotEmpty()) {
                                        cookies.split(";").mapNotNull { kv ->
                                            val parts = kv.trim().split("=", limit = 2)
                                            if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
                                        }.toMap()
                                    } else emptyMap()
                                    wvResolved = true
                                    view.destroy()
                                    done(true)
                                }
                            }
                        }
                    }
                    val handler = android.os.Handler(android.os.Looper.getMainLooper())
                    val poll = object : Runnable {
                        var attempts = 0
                        override fun run() {
                            if (resumed) return
                            if (attempts++ > 120) {
                                Log.e("RetrotveProvider", "resolveChallenge: timeout (60s) after $pageFinishedCount page loads. Last title=$lastTitle")
                                view.destroy()
                                done(false)
                                return
                            }
                            val cookies = CookieManager.getInstance().getCookie(mainUrl)
                            Log.d("RetrotveProvider", "resolveChallenge: attempt $attempts, pageFinished=$pageFinishedCount, cookies=$cookies, lastTitle=$lastTitle")
                            handler.postDelayed(this, 500)
                        }
                    }
                    view.loadUrl(mainUrl)
                    handler.post(poll)
                } catch (e: Exception) {
                    Log.e("RetrotveProvider", "resolveChallenge error: ${e.message}")
                    done(false)
                }
            }
        }
    }

    private suspend fun safeGet(url: String): org.jsoup.nodes.Document? {
        if (!wvResolved) {
            Log.d("RetrotveProvider", "safeGet: WebView not resolved, resolving...")
            val ok = resolveChallengeWithWebView()
            if (!ok) {
                Log.e("RetrotveProvider", "safeGet: WebView challenge resolution failed")
                return null
            }
            Log.d("RetrotveProvider", "safeGet: WebView resolved, cookies=${sessionCookies.keys}")
            delay(1000)
        }
        try {
            val resp = app.get(url, headers = baseHeaders, timeout = 30000L, interceptor = cfKiller, cookies = sessionCookies)
            val text = resp.text
            val title = Regex("<title>(.*?)</title>", RegexOption.IGNORE_CASE).find(text)?.groupValues?.get(1) ?: "no-title"
            Log.d("RetrotveProvider", "safeGet: url=$url, code=${resp.code}, title=$title, len=${text.length}")
            if (text.contains("Un momento") || text.contains("spinner") || text.contains("window.location.reload")) {
                Log.w("RetrotveProvider", "safeGet: still on challenge page after WebView. Clearing and retrying WebView...")
                wvResolved = false
                sessionCookies = emptyMap()
                return null
            }
            if (text.length < 500) {
                Log.w("RetrotveProvider", "safeGet: response suspiciously short (${text.length} chars)")
            }
            return resp.document
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "safeGet error: ${e.message}")
            return null
        }
    }

    private fun logDocumentDebug(tag: String, doc: org.jsoup.nodes.Document?) {
        if (doc == null) {
            Log.e("RetrotveProvider", "$tag: document is NULL")
            return
        }
        val html = doc.html()
        Log.d("RetrotveProvider", "$tag: doc title=${doc.title()}, html len=${html.length}")
        if (html.length > 200) {
            Log.d("RetrotveProvider", "$tag: first 500 chars: ${html.take(500)}")
        } else {
            Log.d("RetrotveProvider", "$tag: FULL html=$html")
        }
        Log.d("RetrotveProvider", "$tag: selectors test -> .MovieList: ${doc.select(".MovieList").size}, .TPostMv: ${doc.select(".TPostMv").size}, .TpRwCont: ${doc.select(".TpRwCont").size}, a[href*='/serie/']: ${doc.select("a[href*='/serie/']").size}, a[href*='/pelicula/']: ${doc.select("a[href*='/pelicula/']").size}")
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (page == 1) {
            "$mainUrl${request.data}"
        } else {
            "$mainUrl${request.data.removeSuffix("/")}/page/$page/"
        }
        Log.d("RetrotveProvider", "getMainPage: URL = $url")
        
        val document = safeGet(url)
        if (document == null) {
            Log.e("RetrotveProvider", "getMainPage: safeGet returned null for $url")
            if (!wvResolved) {
                val retry = safeGet(url)
                if (retry != null) {
                    logDocumentDebug("getMainPage_retry", retry)
                    val home = parseListing(retry)
                    return newHomePageResponse(
                        list = HomePageList(name = request.name, list = home, isHorizontalImages = false),
                        hasNext = home.size >= 20
                    )
                }
            }
            return newHomePageResponse(emptyList(), false)
        }
        logDocumentDebug("getMainPage", document)
        val home = parseListing(document)
        Log.d("RetrotveProvider", "getMainPage: found ${home.size} items")
        return newHomePageResponse(
            list = HomePageList(name = request.name, list = home, isHorizontalImages = false),
            hasNext = home.size >= 20
        )
    }

    private fun parseListing(doc: org.jsoup.nodes.Document): List<SearchResponse> {
        val results = ArrayList<SearchResponse>()
        val seenLinks = mutableSetOf<String>()
        doc.select(".TpRwCont .MovieList > li, .TpRwCont .MovieList .TPostMv, section .MovieList > li").forEach { element ->
            val linkElement = element.selectFirst("a[href*='/serie/'], a[href*='/pelicula/']")
            val link = linkElement?.attr("href") ?: return@forEach
            if (seenLinks.contains(link)) return@forEach
            seenLinks.add(link)
            val title = element.selectFirst(".Title")?.text() ?: element.selectFirst("h3")?.text() ?: return@forEach
            val poster = fixPosterUrl(element.selectFirst(".Image img")?.attr("src")
                ?: element.selectFirst(".Image img")?.attr("data-src"))
            if (title.isNotEmpty() && title.length > 2) {
                val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
                results.add(newTvSeriesSearchResponse(title, link, tvType) { this.posterUrl = poster })
            }
        }
        return results
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("RetrotveProvider", "search: query = $query")
        val document = safeGet("$mainUrl/?s=$query")
        if (document == null) {
            Log.e("RetrotveProvider", "search: safeGet returned null")
            return emptyList()
        }
        logDocumentDebug("search", document)
        val results = ArrayList<SearchResponse>()
        val seenLinks = mutableSetOf<String>()
        document.select("section .MovieList > li, section .TPostMv, .TpRwCont main .MovieList > li").forEach { element ->
            val link = element.selectFirst("a[href*='/serie/'], a[href*='/pelicula/']")?.attr("href") ?: return@forEach
            if (!link.contains("/serie/") && !link.contains("/pelicula/")) return@forEach
            if (seenLinks.contains(link)) return@forEach
            seenLinks.add(link)
            val title = element.selectFirst(".Title")?.text() ?: element.selectFirst("h3")?.text() ?: return@forEach
            val poster = fixPosterUrl(element.selectFirst(".Image img")?.attr("src")
                ?: element.selectFirst(".Image img")?.attr("data-src"))
            if (title.isEmpty() || title.length < 3) return@forEach
            val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
            results.add(newTvSeriesSearchResponse(title, link, tvType) { this.posterUrl = poster })
        }
        Log.d("RetrotveProvider", "search: ${results.size} resultados")
        return results
    }

    private fun extractEpisodePoster(row: Element): String? {
        row.selectFirst(".MvTbImg img")?.let { img ->
            val src = img.attr("src")
            if (src.isNotBlank()) return if (src.startsWith("//")) "https:$src" else src
        }
        row.selectFirst(".MvTbImg span.cnv")?.let { span ->
            val encoded = span.text()
            val decoded = encoded
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&nbsp;", " ")
            try {
                val doc = Jsoup.parse(decoded)
                val src = doc.selectFirst("img")?.attr("src")
                if (!src.isNullOrBlank()) return if (src.startsWith("//")) "https:$src" else src
            } catch (_: Exception) {}
        }
        return null
    }

    private fun fixPosterUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        return if (url.startsWith("//")) "https:$url" else url
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("RetrotveProvider", "load: url = $url")
        val document = safeGet(url)
        if (document == null) {
            Log.e("RetrotveProvider", "load: safeGet returned null for $url")
            return null
        }
        logDocumentDebug("load", document)
        
        val title = document.selectFirst("h1.Title, h1")?.text()
        if (title == null) {
            Log.e("RetrotveProvider", "load: no title found. Tried selectors: h1.Title, h1")
            return null
        }
        val poster = fixPosterUrl(document.selectFirst(".TPost.Single .Image img")?.attr("src")
            ?: document.selectFirst(".TPost.Single .Image img")?.attr("data-src")
            ?: document.selectFirst(".TPostBg img")?.attr("src")
            ?: document.selectFirst("meta[property='og:image']")?.attr("content"))
        val description = document.selectFirst(".Description p")?.text()
        val year = document.selectFirst(".Year")?.text()?.filter { it.isDigit() }?.take(4)?.toIntOrNull()
        val isMovie = url.contains("/pelicula/")
        val episodes = ArrayList<Episode>()
        
        if (!isMovie) {
            document.select(".Wdgt.AABox").forEach { seasonBox ->
                val seasonTitle = seasonBox.selectFirst(".Title.AA-Season span")?.text()
                    ?: seasonBox.selectFirst(".Title")?.text()
                val seasonNum = seasonTitle?.filter { it.isDigit() }?.toIntOrNull() ?: 1
                seasonBox.select(".TPTblCn tbody tr").forEach { row ->
                    val episodeUrl = row.selectFirst("a[href*='/seriestv/']")?.attr("href") ?: return@forEach
                    val episodeName = row.selectFirst(".MvTbTtl a")?.text() ?: "Episodio"
                    val episodeNum = row.selectFirst(".Num")?.text()?.toIntOrNull()
                    val posterUrl = extractEpisodePoster(row)
                    episodes.add(newEpisode(episodeUrl) {
                        this.name = episodeName
                        this.episode = episodeNum ?: episodes.size + 1
                        this.season = seasonNum
                        this.posterUrl = posterUrl
                    })
                }
            }
            if (episodes.isEmpty()) {
                Log.w("RetrotveProvider", "load: no episodes with .Wdgt.AABox. Trying .TPTblCn")
                document.select(".TPTblCn tbody tr").forEach { row ->
                    val episodeUrl = row.selectFirst("a[href*='/seriestv/']")?.attr("href") ?: return@forEach
                    val episodeName = row.selectFirst(".MvTbTtl a")?.text() ?: "Episodio"
                    val episodeNum = row.selectFirst(".Num")?.text()?.toIntOrNull()
                    val posterUrl = extractEpisodePoster(row)
                    episodes.add(newEpisode(episodeUrl) {
                        this.name = episodeName
                        this.episode = episodeNum ?: episodes.size + 1
                        this.posterUrl = posterUrl
                    })
                }
            }
        }
        
        val recommendations = document.select(".TpSbList li, .Relacionados li").mapNotNull { element ->
            val recUrl = element.selectFirst("a[href]")?.attr("href") ?: return@mapNotNull null
            if (!recUrl.contains("/serie/") && !recUrl.contains("/pelicula/")) return@mapNotNull null
            val recTitle = element.selectFirst(".Title")?.text() ?: return@mapNotNull null
            val recPoster = fixPosterUrl(element.selectFirst("img")?.attr("src") ?: element.selectFirst("img")?.attr("data-src"))
            val recType = if (recUrl.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
            newTvSeriesSearchResponse(recTitle, recUrl, recType) { this.posterUrl = recPoster }
        }
        
        return if (isMovie) {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster; this.plot = description; this.year = year; this.recommendations = recommendations
            }
        } else {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster; this.plot = description; this.year = year; this.recommendations = recommendations
            }
        }
    }

    private suspend fun extractSendvid(url: String, referer: String, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        try {
            val embedUrl = if (url.contains("/embed/")) url else url.replace("/?", "/embed/?")
            SendvidExtractor().getUrl(embedUrl, referer, subtitleCallback, callback)
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "Sendvid error: ${e.message}")
        }
    }

    private suspend fun extractTokyoVideo(url: String, referer: String, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        try {
            val resp = app.get(url, referer = referer)
            val doc = resp.document
            val source = doc.selectFirst("video source[src]")
            if (source != null) {
                callback(newExtractorLink("TokyoVideo", "TokyoVideo", source.attr("src")) {
                    this.referer = "https://www.tokyvideo.com/"; this.quality = 1080
                })
            } else {
                loadExtractor(url, referer, subtitleCallback, callback)
            }
        } catch (e: Exception) {
            loadExtractor(url, referer, subtitleCallback, callback)
        }
    }

    private suspend fun extractFilemoon(url: String, referer: String, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        try {
            val page = app.get(url, referer = referer).text
            val sources1 = Regex("""sources\s*:\s*\[?\s*\{?\s*file\s*:\s*"([^"]+)"\s*\}?\s*\]?""").findAll(page).toList()
            if (sources1.isNotEmpty()) {
                sources1.forEach { m -> callback(newExtractorLink("Filemoon", "Filemoon", m.groupValues[1].replace("\\/", "/")) { this.referer = "https://filemoon.to/"; this.quality = 1080 }) }
                return
            }
            val sources2 = Regex("""sources\s*:\s*\[?\s*\{?\s*file\s*:\s*'([^']+)'\s*\}?\s*\]?""").findAll(page).toList()
            if (sources2.isNotEmpty()) {
                sources2.forEach { m -> callback(newExtractorLink("Filemoon", "Filemoon", m.groupValues[1].replace("\\/", "/")) { this.referer = "https://filemoon.to/"; this.quality = 1080 }) }
                return
            }
            val directUrls = Regex("""https?://[^"'\s<>]+\.(?:mp4|m3u8)[^"'\s<>]*""").findAll(page).toList()
            if (directUrls.isNotEmpty()) {
                directUrls.forEach { m -> callback(newExtractorLink("Filemoon", "Filemoon", m.value) { this.referer = "https://filemoon.to/"; this.quality = 1080 }) }
                return
            }
            loadExtractor(url.substringBeforeLast("/"), referer, subtitleCallback, callback)
        } catch (e: Exception) {
            loadExtractor(url, referer, subtitleCallback, callback)
        }
    }

    private suspend fun extractVKVideo(url: String, referer: String, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        try {
            val fixedUrl = url.replace("vkvideo.ru", "vk.com")
            val page = app.get(fixedUrl, referer = referer, timeout = 60L).text
            val mp4 = Regex(""""mp4_720"\s*:\s*"([^"]+)"""").find(page)
            if (mp4 != null) callback(newExtractorLink("VKVideo", "VKVideo 720p", mp4.groupValues[1].replace("\\/", "/")) { this.referer = "https://vk.com/"; this.quality = 720 })
            val hls = Regex(""""hls_ondemand"\s*:\s*"([^"]+)"""").find(page)
            if (hls != null) callback(newExtractorLink("VKVideo", "VKVideo HLS", hls.groupValues[1].replace("\\/", "/")) { this.referer = "https://vk.com/"; this.quality = 1080 })
            val dash = Regex(""""dash_ondemand"\s*:\s*"([^"]+)"""").find(page)
            if (dash != null) callback(newExtractorLink("VKVideo", "VKVideo DASH", dash.groupValues[1].replace("\\/", "/")) { this.referer = "https://vk.com/"; this.quality = 1080; this.type = ExtractorLinkType.DASH })
        } catch (e: Exception) { Log.e("RetrotveProvider", "VKVideo error: ${e.message}") }
    }

    private suspend fun processPlayerPage(
        playerUrl: String, referer: String, serverName: String,
        subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit
    ) {
        try {
            val playerDoc = app.get(playerUrl, referer = referer, headers = baseHeaders).document
            playerDoc.select("iframe[src]").forEach { iframe ->
                val src = iframe.attr("src").let { if (it.startsWith("//")) "https:$it" else it }
                when {
                    src.contains("sendvid.com") -> extractSendvid(src, playerUrl, subtitleCallback, callback)
                    src.contains("filemoon.") -> extractFilemoon(src, playerUrl, subtitleCallback, callback)
                    src.contains("ok.ru") || src.contains("odnoklassniki") -> loadExtractor(src, playerUrl, subtitleCallback, callback)
                    src.contains("vk.com") || src.contains("vkvideo") -> extractVKVideo(src, playerUrl, subtitleCallback, callback)
                    src.contains("tokyvideo.com") -> extractTokyoVideo(src, playerUrl, subtitleCallback, callback)
                    src.contains("yourupload.") -> loadExtractor(src, playerUrl, subtitleCallback, callback)
                    src.contains("uqload.") -> loadExtractor(src, playerUrl, subtitleCallback, callback)
                    src.contains("gdriveplayer") -> loadExtractor(src, playerUrl, subtitleCallback, callback)
                    src.contains("mediafire") -> loadExtractor(src, playerUrl, subtitleCallback, callback)
                    else -> loadExtractor(src, playerUrl, subtitleCallback, callback)
                }
            }
        } catch (e: Exception) { Log.e("RetrotveProvider", "processPlayerPage: ${e.message}") }
    }

    override suspend fun loadLinks(
        data: String, isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = safeGet(data) ?: return false
        logDocumentDebug("loadLinks", document)
        
        val trtype = if (data.contains("/pelicula/")) "1" else "2"
        val allEmbeds = mutableListOf<Pair<String, String>>()
        var correctTrid: String? = null
        
        document.select("iframe[src*='trembed']").forEach { iframe ->
            val src = iframe.attr("src").let { if (it.startsWith("//")) "https:$it" else it }
            val tridMatch = Regex("""trid=(\d+)""").find(src)
            tridMatch?.groupValues?.get(1)?.let { if (correctTrid == null) correctTrid = it }
            allEmbeds.add(src to "Iframe")
        }
        
        document.select(".TPlayerTb[id], .TPlayer[id]").forEach { tab ->
            tab.select("iframe[src]").forEach { iframe ->
                val src = iframe.attr("src").let { if (it.startsWith("//")) "https:$it" else it }
                if (src.contains("retrotve.com") && src.contains("trembed") && src.contains("trid")) {
                    val tridMatch = Regex("""trid=(\d+)""").find(src)
                    tridMatch?.groupValues?.get(1)?.let { if (correctTrid == null) correctTrid = it }
                    if (!allEmbeds.any { it.first == src }) allEmbeds.add(src to tab.attr("id"))
                }
            }
            tab.select("div[data-src*='trembed']").forEach { div ->
                val src = div.attr("data-src").let { if (it.startsWith("//")) "https:$it" else it }
                val tridMatch = Regex("""trid=(\d+)""").find(src)
                tridMatch?.groupValues?.get(1)?.let { if (correctTrid == null) correctTrid = it }
                if (!allEmbeds.any { it.first == src }) allEmbeds.add(src to tab.attr("id"))
            }
        }
        
        correctTrid?.let { trid ->
            for (t in 0..5) {
                val url = "$mainUrl/?trembed=$t&trid=$trid&trtype=$trtype"
                if (!allEmbeds.any { it.first == url }) allEmbeds.add(url to "Opt${t + 1}")
            }
        }
        
        allEmbeds.distinctBy { it.first }.forEach { (url, name) ->
            processPlayerPage(url, data, name, subtitleCallback, callback)
        }
        return true
    }
}

fun getBaseName(url: String): String {
    return url.substringAfter("://").substringBefore("/").substringBefore(".")
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
        .replaceFirst("https://uqload.cx", "https://uqload.com")
        .replaceFirst("https://do7go.com", "https://dood.la")
        .replaceFirst("https://short.ink/", "https://")
}
