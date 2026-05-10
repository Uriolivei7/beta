package com.example

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.lagradost.cloudstream3.DubStatus
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.ShowStatus
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.addEpisodes
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newAnimeLoadResponse
import com.lagradost.cloudstream3.newEpisode
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newMovieSearchResponse
import com.lagradost.cloudstream3.newSubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.Locale

class AnizoneProvider : MainAPI() {

    override var mainUrl = "https://anizone.to"
    override var name = "AniZone"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)
    override var lang = "en"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val hasDownloadSupport = true
    override val usesWebView = true
    override val mainPage = mainPageOf(
        "anime" to "Animes"
    )

    companion object {
        var pluginContext: Context? = null
        private var wv: WebView? = null
        private var resolving = false
        private val wvMutex = Mutex()
    }

    private fun destroyWv() {
        try { wv?.destroy() } catch (_: Exception) {}
        wv = null
    }

    private suspend fun tryDirectGet(url: String): Document? {
        return withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .timeout(8000)
                    .followRedirects(true)
                    .get()
                if (doc.title().contains("anizone", ignoreCase = true) || doc.select("div[wire\\:key]").isNotEmpty() || doc.selectFirst("h1") != null) {
                    Log.e("AniZone", "tryDirectGet OK: ${url.take(60)}... (${doc.text().length} chars)")
                    doc
                } else { Log.e("AniZone", "tryDirectGet: contenido inesperado para $url"); null }
            } catch (e: Exception) {
                Log.e("AniZone", "tryDirectGet fallo para ${url.take(60)}: ${e.message}")
                null
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private suspend fun ensureWebView(): Boolean {
        if (wv != null) { Log.e("AniZone", "ensureWebView: wv existe"); return true }
        val directTest = tryDirectGet("$mainUrl/anime")
        if (directTest != null) { Log.e("AniZone", "ensureWebView: acceso directo OK, sin Cloudflare"); return true }
        val ctx = pluginContext ?: run { Log.e("AniZone", "ensureWebView: pluginContext null"); return false }
        if (resolving) {
            Log.e("AniZone", "ensureWebView: esperando resolucion...")
            for (i in 1..60) { if (wv != null) { Log.e("AniZone", "ensureWebView: resuelto por otro hilo"); return true }; delay(1000) }
            Log.e("AniZone", "ensureWebView: timeout esperando resolucion")
            return false
        }
        resolving = true
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                var resumed = false
                fun done(v: Boolean) { if (!resumed) { resumed = true; resolving = !v; cont.resume(v) } }
                try {
                    Log.e("AniZone", "ensureWebView: creando WebView dialog...")
                    val webView = WebView(ctx.applicationContext).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        settings.cacheMode = WebSettings.LOAD_DEFAULT
                    }
                    val prog = ProgressBar(ctx, null, android.R.attr.progressBarStyleHorizontal).apply {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8)
                    }
                    val layout = LinearLayout(ctx).apply {
                        orientation = LinearLayout.VERTICAL
                        addView(prog)
                        addView(webView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    }
                    val dialog = AlertDialog.Builder(ctx)
                        .setTitle("AniZone - Resuelve Cloudflare")
                        .setView(layout)
                        .setNegativeButton("Cancelar") { _, _ -> destroyWv(); done(false) }
                        .setCancelable(false)
                        .create()

                    webView.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(v: WebView, p: Int) { prog.progress = p }
                    }
                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            if (resumed) return
                            if (url.contains("anizone.to") && !url.contains("/cdn-cgi/")) {
                                view.evaluateJavascript("document.querySelector('div[wire\\\\:key]') !== null") { r ->
                                    if (r == "true") {
                                        Log.e("AniZone", "ensureWebView: Cloudflare resuelto!")
                                        wv = view
                                        dialog.dismiss()
                                        done(true)
                                    }
                                }
                            }
                        }
                    }
                    webView.loadUrl("$mainUrl/anime")
                    dialog.show()
                } catch (e: Exception) {
                    Log.e("AniZone", "ensureWebView error: ${e.message}")
                    done(false)
                }
            }
        }
    }

    private suspend fun wvGet(url: String): Document {
        Log.e("AniZone", "wvGet: $url")
        val direct = tryDirectGet(url)
        if (direct != null) return direct
        Log.e("AniZone", "wvGet: usando WebView como fallback para $url")
        val view = wv ?: throw Exception("WebView null (directo tambien fallo)")
        return wvMutex.withLock {
            withContext(Dispatchers.Main) {
                suspendCoroutine { cont ->
                    var resumed = false
                    val timeoutRunnable = Runnable {
                        if (!resumed) {
                            resumed = true; Log.e("AniZone", "wvGet timeout: $url"); cont.resume(
                                Jsoup.parse("<html><body></body></html>", url)
                            )
                        }
                    }
                    val handler = android.os.Handler(android.os.Looper.getMainLooper())
                    handler.postDelayed(timeoutRunnable, 15000)
                    try {
                        view.stopLoading()
                        view.webViewClient = object : WebViewClient() {
                            override fun onPageFinished(v: WebView, u: String) {
                                if (resumed) return
                                handler.removeCallbacks(timeoutRunnable)
                                v.evaluateJavascript("(function(){return document.documentElement.outerHTML;})()") { html ->
                                    if (!resumed) {
                                        resumed = true
                                        val decoded = try {
                                            JSONObject("{\"h\":$html}").getString("h")
                                        } catch (e: Exception) {
                                            html?.removeSurrounding("\"")?.replace("\\\"", "\"")
                                                ?.replace("\\n", "\n") ?: ""
                                        }
                                        Log.e(
                                            "AniZone",
                                            "wvGet OK: ${url.take(60)}... (${decoded.length} chars)"
                                        )
                                        cont.resume(Jsoup.parse(decoded, url))
                                    }
                                }
                            }
                        }
                        view.loadUrl(url)
                    } catch (e: Exception) {
                        handler.removeCallbacks(timeoutRunnable)
                        if (!resumed) {
                            resumed = true; cont.resume(
                                Jsoup.parse(
                                    "<html><body></body></html>",
                                    url
                                )
                            )
                        }
                        throw e
                    }
                }
            }
        }
    }

    private fun imgSrc(img: Element?): String? {
        if (img == null) return null
        val src = img.attr("src").ifEmpty { img.attr("data-src").ifEmpty { img.attr("data-lazy-src") } }.ifEmpty { null }
        Log.e("AniZone", "imgSrc: ${img.tagName()} src=$src")
        return src
    }

    private fun toResult(post: Element, posterMap: Map<String, String> = emptyMap()) = newMovieSearchResponse(
        post.selectFirst("img")?.attr("alt") ?: post.selectFirst("a")?.text() ?: "",
        post.selectFirst("a")?.attr("href") ?: "",
        TvType.Movie
    ) {
        val src = imgSrc(post.selectFirst("img"))
        this.posterUrl = posterMap[src ?: ""] ?: src
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.e("AniZone", "getMainPage: page=$page cat=${request.data}")
        try { if (!ensureWebView()) return newHomePageResponse(HomePageList(request.name, emptyList()), false) }
        catch (e: Exception) { Log.e("AniZone", "getMainPage ensure: ${e.message}"); return newHomePageResponse(HomePageList(request.name, emptyList()), false) }
        return try {
            val doc = wvGet("$mainUrl/${request.data}")
            val items = doc.select("div[wire:key]")
            Log.e("AniZone", "getMainPage: ${items.size} items")
            items.take(2).forEachIndexed { i, el ->
                val img = el.selectFirst("img")
                Log.e("AniZone", "item[$i]: img=$img src=${imgSrc(img)} a=${el.selectFirst("a")?.attr("href")}")
            }
            val posterUrls = items.mapNotNull { imgSrc(it.selectFirst("img")) }
            val posterMap = if (posterUrls.isNotEmpty() && wv != null) extractPosterDataUris(posterUrls) else emptyMap()
            Log.e("AniZone", "getMainPage: ${posterMap.size}/${posterUrls.size} posters como data URI")
            newHomePageResponse(HomePageList(request.name, items.map { toResult(it, posterMap) }), hasNext = false)
        } catch (e: Exception) {
            Log.e("AniZone", "getMainPage error: ${e.message}")
            destroyWv()
            newHomePageResponse(HomePageList(request.name, emptyList()), false)
        }
    }

    override suspend fun quickSearch(query: String) = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        Log.e("AniZone", "search: $query")
        try { if (!ensureWebView()) return emptyList() }
        catch (e: Exception) { Log.e("AniZone", "search ensure: ${e.message}"); return emptyList() }
        return try {
            val doc = wvGet("$mainUrl/anime?search=$query")
            val items = doc.select("div[wire:key]")
            Log.e("AniZone", "search: ${items.size} items for '$query'")
            items.take(2).forEachIndexed { i, el ->
                val img = el.selectFirst("img")
                Log.e("AniZone", "item[$i]: img=$img src=${imgSrc(img)} a=${el.selectFirst("a")?.attr("href")}")
            }
            val posterUrls = items.mapNotNull { imgSrc(it.selectFirst("img")) }
            val posterMap = if (posterUrls.isNotEmpty() && wv != null) extractPosterDataUris(posterUrls) else emptyMap()
            Log.e("AniZone", "search: ${posterMap.size}/${posterUrls.size} posters como data URI")
            items.mapNotNull { toResult(it, posterMap) }
        } catch (e: Exception) {
            Log.e("AniZone", "search error: ${e.message}")
            destroyWv()
            emptyList()
        }
    }

    private suspend fun extractPosterDataUris(urls: List<String>): Map<String, String> {
        if (urls.isEmpty()) return emptyMap()
        val view = wv ?: return emptyMap()
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                var resumed = false
                val jsonUrls = JSONArray(urls).toString()
                view.evaluateJavascript(
                    "(function(){" +
                    "var u = JSON.parse('${jsonUrls.replace("'", "\\'")}');" +
                    "var r = {}; var p = u.length;" +
                    "u.forEach(function(s){" +
                    "  function done(d, ok){" +
                    "    if(ok){" +
                    "      try{var c=document.createElement('canvas');c.width=d.width;c.height=d.height;c.getContext('2d').drawImage(d,0,0);r[s]=c.toDataURL('image/jpeg',80);}catch(e){r[s]=''}" +
                    "    } else {r[s]=''}" +
                    "    p--; if(p===0){window.__pd=true;window.__p=JSON.stringify(r)}" +
                    "  }" +
                    "  var i=document.querySelector('img[src=\"'+s+'\"]');" +
                    "  if(i&&i.complete&&i.naturalWidth>0){done(i,true)}" +
                    "  else if(i){i.onload=function(){done(i,true)};i.onerror=function(){done(null,false)}}" +
                    "  else{done(null,false)}" +
                    "});" +
                    "})()"
                ) {
                    fun poll(n: Int) {
                        if (!resumed) {
                            if (n <= 0) { resumed = true; cont.resume(emptyMap()); return }
                            view.evaluateJavascript("window.__pd") { d ->
                                if (!resumed) {
                                    if (d == "true") {
                                        view.evaluateJavascript("window.__p") { data ->
                                            if (!resumed) {
                                                resumed = true
                                                val json = data?.removeSurrounding("\"")?.replace("\\\"", "\"")
                                                val map = if (json != null) try {
                                                    val obj = JSONObject(json)
                                                    urls.mapNotNull { url ->
                                                        val v = obj.optString(url, "")
                                                        if (v.isNotEmpty()) url to v else null
                                                    }.toMap()
                                                } catch (e: Exception) { emptyMap() } else emptyMap()
                                                if (map.isNotEmpty()) Log.e("AniZone", "extractPosterDataUris: ${map.size} data URIs")
                                                cont.resume(map)
                                            }
                                        }
                                    } else {
                                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ poll(n - 1) }, 200)
                                    }
                                }
                            }
                        }
                    }
                    poll(50)
                }
            }
        }
    }

    private suspend fun downloadImageAsDataUri(imageUrl: String): String? {
        val view = wv ?: return null
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                var resumed = false
                val safeUrl = imageUrl.replace("'", "\\'")
                view.evaluateJavascript(
                    "(function(){" +
                    "var i=new Image();" +
                    "i.crossOrigin='anonymous';" +
                    "i.onload=function(){" +
                    "var c=document.createElement('canvas');c.width=i.width;c.height=i.height;" +
                    "c.getContext('2d').drawImage(i,0,0);" +
                    "window.__p=c.toDataURL('image/jpeg',80);window.__pd=true};" +
                    "i.onerror=function(){window.__pd=true;window.__p=''};" +
                    "i.src='$safeUrl';" +
                    "})()"
                ) {
                    fun poll(n: Int) {
                        if (!resumed) {
                            if (n <= 0) { resumed = true; cont.resume(null); return }
                            view.evaluateJavascript("window.__pd") { d ->
                                if (!resumed) {
                                    if (d == "true") {
                                        view.evaluateJavascript("window.__p") { data ->
                                            if (!resumed) {
                                                resumed = true
                                                cont.resume(data?.removeSurrounding("\"")?.ifEmpty { null })
                                            }
                                        }
                                    } else {
                                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ poll(n - 1) }, 200)
                                    }
                                }
                            }
                        }
                    }
                    poll(50)
                }
            }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        Log.e("AniZone", "load: $url")
        try { if (!ensureWebView()) throw Exception("No WebView") }
        catch (e: Exception) { throw e }
        val doc = wvGet(url)
        val title = doc.selectFirst("h1")?.text() ?: throw NotImplementedError("No title")
        Log.e("AniZone", "load: title=$title")

        var poster = imgSrc(doc.selectFirst("meta[property=og:image]"))
            ?: imgSrc(doc.selectFirst("main img, .poster img, .card img"))
            ?: imgSrc(doc.selectFirst("img[src*='/storage/'], img[src*='/images/anime/']"))
        if (poster != null && poster.startsWith("$mainUrl/")) {
            Log.e("AniZone", "load: descargando poster via WebView...")
            val dataUri = downloadImageAsDataUri(poster)
            if (dataUri != null) { poster = dataUri; Log.e("AniZone", "load: poster como data URI (${dataUri.take(50)}...)") }
        }
        Log.e("AniZone", "load poster final: ${poster?.take(60)}")

        val eps = doc.select("li[x-data]").mapNotNull { elt ->
            val a = elt.selectFirst("a") ?: return@mapNotNull null
            newEpisode(data = a.attr("href")) {
                this.name = elt.selectFirst("h3")?.text()?.substringAfter(":")?.trim()
                this.season = 0; this.posterUrl = imgSrc(elt.selectFirst("img"))
                this.data = "${a.attr("href")}|||"
                this.date = elt.selectFirst("span[title] span.line-clamp-1")?.text()?.trim()
                    ?.replace(Regex("\\s+"), "")?.ifEmpty { null }?.let {
                    try { SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(it)?.time } catch (e: Exception) { null }
                } ?: 0L
            }
        }
        Log.e("AniZone", "load: ${eps.size} episodios")

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = doc.selectFirst(".sr-only + div")?.text() ?: ""
            this.tags = doc.select("a[wire:navigate][wire:key]").map { it.text() }
            this.year = doc.select("span.inline-block").map { it.text() }.getOrNull(3)?.toIntOrNull()
            this.showStatus = when (doc.select("span.inline-block").map { it.text() }.getOrNull(1)) {
                "Completed" -> ShowStatus.Completed; "Ongoing" -> ShowStatus.Ongoing; else -> null
            }
            addEpisodes(DubStatus.None, eps)
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val episodeUrl = data.split("|||")[0]
        Log.e("AniZone", "loadLinks: $episodeUrl")
        try { if (!ensureWebView()) return false }
        catch (e: Exception) { Log.e("AniZone", "loadLinks ensure: ${e.message}"); return false }
        val doc = wvGet(episodeUrl)
        val sourceName = doc.selectFirst("span.truncate")?.text() ?: ""
        val mediaPlayer = doc.selectFirst("media-player")
        val m3U8 = mediaPlayer?.attr("src") ?: ""
        Log.e("AniZone", "loadLinks: m3u8=$m3U8 source=$sourceName")
        mediaPlayer?.select("track")?.forEach { subtitleCallback(newSubtitleFile(it.attr("label"), it.attr("src"))) }
        callback(newExtractorLink(sourceName, name, m3U8, type = ExtractorLinkType.M3U8) {
            this.referer = episodeUrl; this.quality = 0
        })
        return true
    }
}
