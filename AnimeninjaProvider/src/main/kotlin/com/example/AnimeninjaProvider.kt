package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.network.CloudflareKiller
import org.jsoup.nodes.Document
import android.util.Log
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.nodes.Element
import org.jsoup.Jsoup
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AnimeOnlineNinjaProvider : MainAPI() {
    override var mainUrl = "https://ww3.animeonline.ninja"
    override var name = "AnimeOnlineNinja"
    override val hasMainPage = true
    override var lang = "es"
    override val hasDownloadSupport = true
    override val hasQuickSearch = true
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    companion object {
        var cfClearance: String = ""
        var pluginContext: Context? = null
        private var wv: WebView? = null
        private var resolving = false
        private val wvMutex = Mutex()
    }

    private fun destroyWv() {
        try { wv?.destroy() } catch (_: Exception) {}
        wv = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    private suspend fun resolveTurnstileWithWebView(url: String): Boolean {
        Log.d("AnimeOnlineNinja", "resolveTurnstileWithWebView: $url")
        if (wv != null) { Log.d("AnimeOnlineNinja", "resolveTurnstileWithWebView: wv existe"); return true }
        val ctx = pluginContext ?: run { Log.e("AnimeOnlineNinja", "resolveTurnstileWithWebView: pluginContext null"); return false }
        if (resolving) {
            Log.d("AnimeOnlineNinja", "resolveTurnstileWithWebView: esperando resolucion...")
            for (i in 1..90) { if (wv != null) { Log.d("AnimeOnlineNinja", "resuelto por otro hilo"); return true }; delay(1000) }
            Log.e("AnimeOnlineNinja", "resolveTurnstileWithWebView: timeout esperando")
            return false
        }
        resolving = true
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                var resumed = false
                fun done(v: Boolean) { if (!resumed) { resumed = true; resolving = false; cont.resume(v) } }
                try {
                    Log.d("AnimeOnlineNinja", "resolveTurnstileWithWebView: creando WebView dialog...")
                    CookieManager.getInstance().setAcceptCookie(true)
                    val webView = WebView(ctx.applicationContext).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        settings.cacheMode = WebSettings.LOAD_DEFAULT
                    }
                    val progressBar = ProgressBar(ctx, null, android.R.attr.progressBarStyleHorizontal).apply {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8)
                    }
                    val layout = LinearLayout(ctx).apply {
                        orientation = LinearLayout.VERTICAL
                        addView(progressBar)
                        addView(webView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    }
                    val dialog = AlertDialog.Builder(ctx)
                        .setTitle("AnimeOnlineNinja - Resuelve Cloudflare")
                        .setView(layout)
                        .setNegativeButton("Cancelar") { _, _ -> destroyWv(); done(false) }
                        .setCancelable(false)
                        .create()

                    webView.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(v: WebView, p: Int) { progressBar.progress = p }
                    }
                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, u: String) {
                            // just logging, cookie polling handles resolution
                            Log.d("AnimeOnlineNinja", "resolveTurnstileWithWebView: onPageFinished $u")
                        }
                    }

                    val handler = android.os.Handler(android.os.Looper.getMainLooper())
                    val cookiePoll = object : Runnable {
                        var attempts = 0
                        override fun run() {
                            if (resumed) return
                            if (attempts++ > 120) {
                                Log.e("AnimeOnlineNinja", "resolveTurnstileWithWebView: timeout esperando cookie")
                                done(false)
                                return
                            }
                            val cookies = CookieManager.getInstance().getCookie(mainUrl)
                            if (cookies != null && cookies.contains("cf_clearance")) {
                                val cf = cookies.split(";").map { it.trim() }.firstOrNull { it.startsWith("cf_clearance=") }
                                if (cf != null) {
                                    cfClearance = cf.removePrefix("cf_clearance=")
                                    Log.d("AnimeOnlineNinja", "resolveTurnstileWithWebView: cf_clearance obtenido")
                                }
                                wv = webView
                                dialog.dismiss()
                                done(true)
                                return
                            }
                            handler.postDelayed(this, 500)
                        }
                    }

                    webView.loadUrl(url)
                    dialog.show()
                    handler.post(cookiePoll)
                } catch (e: Exception) {
                    Log.e("AnimeOnlineNinja", "resolveTurnstileWithWebView error: ${e.message}")
                    done(false)
                }
            }
        }
    }

    private suspend fun wvGet(url: String): Document? {
        Log.d("AnimeOnlineNinja", "wvGet: $url")
        val view = wv ?: run { Log.e("AnimeOnlineNinja", "wvGet: wv is null"); return null }
        return wvMutex.withLock {
            withContext(Dispatchers.Main) {
                suspendCoroutine { cont ->
                    var resumed = false
                    val timeoutRunnable = Runnable {
                        if (!resumed) {
                            resumed = true
                            Log.e("AnimeOnlineNinja", "wvGet timeout: $url")
                            cont.resume(null)
                        }
                    }
                    val handler = android.os.Handler(android.os.Looper.getMainLooper())
                    handler.postDelayed(timeoutRunnable, 20000)
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
                                        Log.d("AnimeOnlineNinja", "wvGet OK: ${url.take(60)}... (${decoded.length} chars)")
                                        cont.resume(Jsoup.parse(decoded, url))
                                    }
                                }
                            }
                        }
                        view.loadUrl(url)
                    } catch (e: Exception) {
                        handler.removeCallbacks(timeoutRunnable)
                        if (!resumed) { resumed = true; cont.resume(null) }
                    }
                }
            }
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
                    "      try{var c=document.createElement('canvas');c.width=d.naturalWidth;c.height=d.naturalHeight;c.getContext('2d').drawImage(d,0,0);r[s]=c.toDataURL('image/jpeg',80);}catch(e){r[s]=''}" +
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
                                                Log.d("AnimeOnlineNinja", "extractPosterDataUris: ${map.size} data URIs")
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
                    "var c=document.createElement('canvas');c.width=i.naturalWidth;c.height=i.naturalHeight;" +
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

    private val cloudflareKiller = CloudflareKiller()
    private var cachedNonce: String? = null
    private var cachedCookies: Map<String, String>? = null

    private fun buildCookies(): Map<String, String> {
        val base = cachedCookies?.toMutableMap() ?: mutableMapOf()
        if (cfClearance.isNotBlank() && !base.containsKey("cf_clearance")) {
            base["cf_clearance"] = cfClearance
            Log.d("AnimeOnlineNinja", "Using user-provided cf_clearance cookie")
        }
        return base
    }

    private val chromeHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
        "sec-ch-ua" to "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "document",
        "sec-fetch-mode" to "navigate",
        "sec-fetch-site" to "none",
        "sec-fetch-user" to "?1",
        "Referer" to mainUrl
    )

    private val ajaxHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
        "Accept" to "application/json, text/javascript, */*; q=0.01",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
        "X-Requested-With" to "XMLHttpRequest",
        "sec-ch-ua" to "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "empty",
        "sec-fetch-mode" to "cors",
        "sec-fetch-site" to "same-origin",
        "Referer" to "$mainUrl/search/?s="
    )

    private suspend fun safeGet(url: String, timeoutMs: Long = 60000L, retries: Int = 3): String? {
        Log.d("AnimeOnlineNinja", "Fetching URL: $url (timeout=${timeoutMs}ms, retries=$retries)")
        val requestCookies = buildCookies()
        if (requestCookies.isNotEmpty()) {
            Log.d("AnimeOnlineNinja", "Using cookies: ${requestCookies.keys}")
        }
        for (i in 0 until retries) {
            try {
                val response = app.get(url, headers = chromeHeaders, timeout = timeoutMs,
                    interceptor = cloudflareKiller, cookies = requestCookies)
                val respCookies = response.cookies
                if (respCookies.isNotEmpty()) {
                    cachedCookies = respCookies
                    Log.d("AnimeOnlineNinja", "Saved cookies: ${respCookies.keys}")
                }
                val html = response.text
                Log.d("AnimeOnlineNinja", "Fetched ${html.length} bytes from $url (attempt ${i + 1})")

                if (html.contains("One moment, please") && html.contains("wsidchk")) {
                    Log.w("AnimeOnlineNinja", "VRF challenge detected, attempting to solve...")
                    val solved = solveVrfChallenge(html, url)
                    if (solved != null) {
                        Log.d("AnimeOnlineNinja", "VRF challenge solved, ${solved.length} bytes")
                        return solved
                    }
                    Log.w("AnimeOnlineNinja", "VRF challenge solve failed")
                }

                if (html.contains("Just a moment") && html.contains("challenges.cloudflare.com")) {
                    Log.w("AnimeOnlineNinja", "Cloudflare Turnstile challenge at $url (attempt ${i + 1})")
                    if (cfClearance.isBlank()) {
                        Log.d("AnimeOnlineNinja", "safeGet: attempting WebView Turnstile resolution")
                        if (resolveTurnstileWithWebView(url)) {
                            Log.d("AnimeOnlineNinja", "safeGet: WebView resolvio Turnstile, reintentando con cookie")
                            continue
                        }
                    }
                    if (i < retries - 1) {
                        delay(5000L)
                        continue
                    }
                    return null
                }
                if (html.length < 10000) {
                    Log.d("AnimeOnlineNinja", "HTML preview (first 600 chars): ${html.take(600)}")
                }
                return html
            } catch (e: Exception) {
                Log.e("AnimeOnlineNinja", "Attempt ${i + 1}/$retries failed for $url: ${e.message}")
                if (i < retries - 1) {
                    delay(3000L)
                }
            }
        }
        Log.e("AnimeOnlineNinja", "All $retries attempts failed for $url")
        return null
    }

    private suspend fun solveVrfChallenge(html: String, originalUrl: String): String? {
        return try {
            val doc = Jsoup.parse(html)
            val script = doc.selectFirst("script:containsData(west=)") ?: return null
            val js = script.data()
            val west = js.substringAfter("west=").substringBefore(",").trim()
            val east = js.substringAfter("east=").substringBefore(",").trim()
            val formAction = doc.selectFirst("form#wsidchk-form")?.attr("action") ?: return null
            val result = simpleEval(west, east) ?: return null
            val host = originalUrl.let { Regex("https?://[^/]+").find(it)?.value ?: mainUrl }
            val solveUrl = "$host$formAction?wsidchk=$result"
            Log.d("AnimeOnlineNinja", "VRF solve URL: $solveUrl")
            val resp = app.get(solveUrl, headers = chromeHeaders, timeout = 30000L)
            val cookies = resp.cookies
            if (cookies.isNotEmpty()) {
                cachedCookies = cookies
                Log.d("AnimeOnlineNinja", "VRF saved cookies: ${cookies.keys}")
            }
            resp.text
        } catch (e: Exception) {
            Log.e("AnimeOnlineNinja", "VRF solve error: ${e.message}")
            null
        }
    }

    private fun simpleEval(west: String, east: String): String? {
        return try {
            val w = west.removeSurrounding("\"").toLongOrNull()
            val e = east.removeSurrounding("\"").toLongOrNull()
            if (w != null && e != null) (w + e).toString()
            else (west + east).replace("\"", "")
        } catch (e: Exception) {
            Log.e("AnimeOnlineNinja", "simpleEval error: ${e.message}")
            null
        }
    }

    private suspend fun getHtml(url: String): String? = safeGet(url)

    private suspend fun getSearchNonce(): String? {
        cachedNonce?.let { return it }
        val html = getHtml("$mainUrl/") ?: return null
        val patterns = listOf(
            Regex("""dooplay[.\s]*ajax_nonce['"]?\s*[:=]\s*['"]([a-f0-9]+)['"]"""),
            Regex("""nonce['"]?\s*[:=]\s*['"]([a-f0-9]+)['"]"""),
            Regex("""id=["']dooplay_nonce["']\s+value=["']([a-f0-9]+)["']"""),
            Regex("""ajax_nonce["']?\s*:\s*["']([a-f0-9]+)["']"""),
            Regex("""var\s+dooplay\s*=\s*\{[^}]*?nonce["']?\s*[:=]\s*["']([a-f0-9]+)["']"""),
        )
        for (pattern in patterns) {
            val match = pattern.find(html)?.groupValues?.get(1)
            if (match != null) {
                cachedNonce = match
                Log.d("AnimeOnlineNinja", "getSearchNonce: found nonce=$match via $pattern")
                return match
            }
        }
        Log.w("AnimeOnlineNinja", "getSearchNonce: no nonce found in main page")
        return null
    }

    override val mainPage = mainPageOf(
        "$mainUrl/inicio/" to "Inicio",
        "$mainUrl/online/" to "Animes",
        "$mainUrl/pelicula/" to "Películas",
        "$mainUrl/genero/audio-latino/" to "Latino",
        "$mainUrl/genero/anime-castellano/" to "Castellano",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (page > 1) "${request.data.trimEnd('/')}/page/$page/" else request.data
        Log.d("AnimeOnlineNinja", "getMainPage: page=$page name=${request.name} url=$url")
        val html = getHtml(url) ?: run {
            Log.w("AnimeOnlineNinja", "getMainPage: html=null for $url")
            return newHomePageResponse(
                list = HomePageList(name = request.name, list = emptyList(), isHorizontalImages = false),
                hasNext = false
            )
        }
        val document = Jsoup.parse(html)

        var items = document.select("article.item").mapNotNull { it.toSearchResult() }
        if (items.isEmpty()) {
            Log.d("AnimeOnlineNinja", "getMainPage: 0 items via article.item, logging HTML classes...")
            document.select("article, div[class], li[class]").take(5).forEach { el ->
                Log.d("AnimeOnlineNinja", "  candidate: <${el.tagName()} class='${el.className()}'>")
            }
        }
        if (items.isEmpty()) {
            Log.d("AnimeOnlineNinja", "getMainPage: HTML snippet: ${document.text().take(300)}")
        }

        val posterUrls = items.mapNotNull { it.posterUrl }
        val posterMap = if (posterUrls.isNotEmpty() && wv != null) {
            wvGet(url)
            extractPosterDataUris(posterUrls.filter { it.startsWith(mainUrl) })
        } else emptyMap()

        val finalItems = items.map { resp ->
            val newPoster = posterMap[resp.posterUrl] ?: resp.posterUrl
            when (resp) {
                is TvSeriesSearchResponse -> newTvSeriesSearchResponse(resp.name, resp.url, TvType.Anime) { this.posterUrl = newPoster }
                is MovieSearchResponse -> newMovieSearchResponse(resp.name, resp.url, TvType.AnimeMovie) { this.posterUrl = newPoster }
                else -> resp
            }
        }

        val hasNext = document.selectFirst(".pagination .next, .pagination a.next.page-numbers, a.next.page-numbers") != null
        Log.d("AnimeOnlineNinja", "getMainPage: ${finalItems.size} items, hasNext=$hasNext")
        return newHomePageResponse(
            list = HomePageList(name = request.name, list = finalItems, isHorizontalImages = false),
            hasNext = hasNext
        )
    }

    private fun Element.toSearchResult(posterMap: Map<String, String> = emptyMap()): SearchResponse? {
        val linkEl = this.selectFirst(".data h3 a") ?: return null
        val href = linkEl.attr("abs:href").ifBlank { return null }
        val title = linkEl.text().trim()
        if (title.isBlank()) return null

        val img = this.selectFirst(".poster img")
        val src = img?.attr("data-src")?.ifBlank { img.attr("src") }?.ifBlank { null }
        val poster = posterMap[src] ?: src

        val type = when {
            href.contains("/pelicula/") || this.hasClass("movies") -> TvType.AnimeMovie
            else -> TvType.Anime
        }

        return when (type) {
            TvType.AnimeMovie -> newMovieSearchResponse(title, href, TvType.AnimeMovie) { this.posterUrl = poster }
            else -> newTvSeriesSearchResponse(title, href, TvType.Anime) { this.posterUrl = poster }
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        val nonce = getSearchNonce()
        if (nonce != null) {
            val searchUrl = "$mainUrl/wp-json/dooplay/search/?keyword=${query.replace(" ", "%20")}&nonce=$nonce"
            Log.d("AnimeOnlineNinja", "search: AJAX query='$query' url=$searchUrl")
            val jsonStr = safeGet(searchUrl, timeoutMs = 60000L, retries = 2)
            if (jsonStr != null) {
                try {
                    val json = JSONObject(jsonStr)
                    val results = mutableListOf<SearchResponse>()
                    for (key in json.keys()) {
                        val item = json.getJSONObject(key)
                        val title = item.optString("title", "").trim()
                        val url = item.optString("url", "").trim()
                        val img = item.optString("img", "").trim()
                        if (title.isNotBlank() && url.isNotBlank()) {
                            val type = if (url.contains("/pelicula/")) TvType.AnimeMovie else TvType.Anime
                            val poster = img.ifBlank { null }
                            results.add(
                                when (type) {
                                    TvType.AnimeMovie -> newMovieSearchResponse(title, url, TvType.AnimeMovie) { this.posterUrl = poster }
                                    else -> newTvSeriesSearchResponse(title, url, TvType.Anime) { this.posterUrl = poster }
                                }
                            )
                        }
                    }
                    Log.d("AnimeOnlineNinja", "search: AJAX returned ${results.size} results for '$query'")
                    return results
                } catch (e: Exception) {
                    Log.e("AnimeOnlineNinja", "search: AJAX JSON parse error: ${e.message}")
                }
            }
        }

        val fallbackUrl = "$mainUrl/?s=${query.replace(" ", "+")}"
        Log.d("AnimeOnlineNinja", "search: falling back to HTML $fallbackUrl")
        val html = getHtml(fallbackUrl) ?: return emptyList()
        val document = Jsoup.parse(html)
        var results = document.select("article.item").mapNotNull { it.toSearchResult() }
        if (results.isEmpty()) {
            Log.d("AnimeOnlineNinja", "search: 0 via article.item, logging candidates...")
            document.select("article, div[class], li[class]").take(8).forEach { el ->
                Log.d("AnimeOnlineNinja", "  candidate: <${el.tagName()} class='${el.className()}'> text='${el.text().take(60)}'")
            }
            results = document.select("article, div.post, div.entry, li").mapNotNull { alt ->
                val a = alt.selectFirst("a[href*='/online/'], a[href*='/pelicula/'], a[href*='/temporada/']") ?: return@mapNotNull null
                val t = a.text().trim().ifBlank { return@mapNotNull null }
                val img = alt.selectFirst("img")
                val src = img?.attr("data-src")?.ifBlank { img.attr("src") }?.ifBlank { null }
                val href = a.attr("abs:href")
                val type = if (href.contains("/pelicula/")) TvType.AnimeMovie else TvType.Anime
                when (type) {
                    TvType.AnimeMovie -> newMovieSearchResponse(t, href, TvType.AnimeMovie) { this.posterUrl = src }
                    else -> newTvSeriesSearchResponse(t, href, TvType.Anime) { this.posterUrl = src }
                }
            }
        }
        Log.d("AnimeOnlineNinja", "search: ${results.size} HTML results for '$query'")
        return results
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("AnimeOnlineNinja", "load: url=$url")
        val html = getHtml(url) ?: throw Exception("Failed to load page")
        val document = Jsoup.parse(html)

        val title = document.selectFirst(".sheader .data h1")?.text()?.trim()
            ?: document.selectFirst("h1")?.text()?.trim()
            ?: "Unknown"
        Log.d("AnimeOnlineNinja", "load: title='$title'")

        val poster = extractPoster(document)
        Log.d("AnimeOnlineNinja", "load: poster=$poster")

        val description = document.selectFirst("#info .wp-content p")?.text() ?: ""
        Log.d("AnimeOnlineNinja", "load: description=${description.take(100)}")

        val tags = document.select(".sgeneros a").map { it.text() }
        Log.d("AnimeOnlineNinja", "load: tags=$tags")

        val year = document.selectFirst("meta[property='article:published_time']")?.attr("content")?.let {
            Regex("(\\d{4})").find(it)?.groupValues?.get(1)?.toIntOrNull()
        }
        Log.d("AnimeOnlineNinja", "load: year=$year")

        val episodes = extractEpisodes(document, poster)
        if (episodes.isNotEmpty() && wv != null) {
            wvGet(url)
            val epPosterUrls = episodes.mapNotNull { it.posterUrl }.filter { it.startsWith(mainUrl) }.distinct()
            if (epPosterUrls.isNotEmpty()) {
                val posterMap = extractPosterDataUris(epPosterUrls)
                if (posterMap.isNotEmpty()) {
                    val indexed = episodes.map { ep ->
                        val newPoster = posterMap[ep.posterUrl] ?: ep.posterUrl
                        newEpisode(ep.data) {
                            this.name = ep.name; this.episode = ep.episode; this.season = ep.season
                            this.posterUrl = newPoster; this.date = ep.date
                        }
                    }
                    return newAnimeLoadResponse(title, url, TvType.Anime) {
                        addEpisodes(DubStatus.Subbed, indexed)
                        this.posterUrl = poster; this.plot = description; this.year = year; this.tags = tags
                    }
                }
            }
        }
        Log.d("AnimeOnlineNinja", "load: ${episodes.size} episodes extracted")

        return if (episodes.isNotEmpty()) {
            Log.d("AnimeOnlineNinja", "load: returning Anime with ${episodes.size} episodes")
            newAnimeLoadResponse(title, url, TvType.Anime) {
                addEpisodes(DubStatus.Subbed, episodes)
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.tags = tags
            }
        } else {
            Log.d("AnimeOnlineNinja", "load: no episodes, returning AnimeMovie")
            newMovieLoadResponse(title, url, TvType.AnimeMovie, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
                this.year = year
            }
        }
    }

    private fun extractEpisodes(document: Document, posterUrl: String): List<Episode> {
        val episodes = mutableListOf<Episode>()
        val seasonContainers = document.select("#seasons .se-c")
        Log.d("AnimeOnlineNinja", "extractEpisodes: ${seasonContainers.size} season containers found")

        seasonContainers.forEach { season ->
            val seasonNum = season.selectFirst(".se-q .se-t")?.text()?.toIntOrNull() ?: 1
            val epItems = season.select(".se-a ul.episodios li")
            Log.d("AnimeOnlineNinja", "extractEpisodes: season=$seasonNum, ${epItems.size} episode items")

            epItems.forEach { item ->
                val link = item.selectFirst(".episodiotitle a") ?: run {
                    Log.w("AnimeOnlineNinja", "extractEpisodes: no .episodiotitle a in item")
                    return@forEach
                }
                val href = link.attr("abs:href").ifBlank { return@forEach }
                val name = link.text().trim()

                val img = item.selectFirst(".imagen img")
                val epPoster = img?.attr("data-src")?.ifBlank { img.attr("src") }?.ifBlank { null }
                    ?: posterUrl.ifBlank { null }

                val numText = item.selectFirst(".numerando")?.text() ?: ""
                val parts = numText.split("-").map { it.trim() }
                val episodeNum = parts.getOrNull(1)?.toIntOrNull()

                if (episodeNum != null) {
                    episodes.add(newEpisode(href) {
                        this.name = name
                        this.episode = episodeNum
                        this.season = seasonNum
                        this.posterUrl = epPoster
                    })
                } else {
                    Log.w("AnimeOnlineNinja", "extractEpisodes: invalid numerando='$numText' for '$name'")
                }
            }
        }

        val sorted = episodes.sortedBy { it.episode }
        Log.d("AnimeOnlineNinja", "extractEpisodes: ${sorted.size} total episodes")
        return sorted
    }

    private fun extractPoster(document: Document): String {
        val ogImage = document.selectFirst("meta[property='og:image']")?.attr("content")
        if (!ogImage.isNullOrBlank() && !ogImage.contains("svg") && ogImage.startsWith("http")) return ogImage
        val posterImg = document.selectFirst(".sheader .poster img")
        if (posterImg != null) {
            val src = posterImg.attr("data-src").ifBlank { posterImg.attr("src") }
            if (src.isNotBlank() && !src.contains("svg")) return src
        }
        return ""
    }

    @Suppress("DEPRECATION", "DEPRECATION_ERROR")
    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("AnimeOnlineNinja", "loadLinks: data=$data")
        val html = getHtml(data) ?: return false
        val document = Jsoup.parse(html)

        val apiBase = "$mainUrl/wp-json/dooplayer/v1/post/"
        var found = false

        val playerOptions = document.select("#playeroptionsul li.dooplay_player_option")
        Log.d("AnimeOnlineNinja", "loadLinks: ${playerOptions.size} player options found")

        playerOptions.forEach { option ->
            val postId = option.attr("data-post")
            val nume = option.attr("data-nume")
            val type = option.attr("data-type")
            val serverName = option.selectFirst(".server")?.text() ?: "Server"
            val titleName = option.selectFirst(".title")?.text() ?: serverName
            Log.d("AnimeOnlineNinja", "loadLinks: option type=$type post=$postId nume=$nume server=$serverName")

            if (postId.isNotBlank() && nume.isNotBlank()) {
                try {
                    val apiUrl = "${apiBase}${postId}?type=${type}&source=${nume}"
                    Log.d("AnimeOnlineNinja", "loadLinks: calling API $apiUrl")
                    val apiHeaders = ajaxHeaders + ("Referer" to data)
                    val response = app.get(apiUrl, headers = apiHeaders, timeout = 60000L,
                        interceptor = cloudflareKiller, cookies = buildCookies())
                    val responseText = response.text
                    Log.d("AnimeOnlineNinja", "loadLinks: API response ${responseText.length} chars")
                    found = true

                    try {
                        val json = JSONObject(responseText)
                        val embedUrl = json.optString("embed_url", "")
                        Log.d("AnimeOnlineNinja", "loadLinks: JSON parsed - embed_url='${embedUrl.take(100)}'")
                        if (embedUrl.isNotBlank()) {
                            Log.d("AnimeOnlineNinja", "loadLinks: loading extractor from embed_url=$embedUrl")
                            loadExtractor(embedUrl, mainUrl, subtitleCallback, callback)
                            try {
                                val embedHtml = app.get(embedUrl, headers = chromeHeaders, timeout = 15000L,
                                    cookies = buildCookies()).text
                                val embedDoc = Jsoup.parse(embedHtml)
                                val sources = embedDoc.select("video source[src], source[src], iframe[src]")
                                Log.d("AnimeOnlineNinja", "loadLinks: embed page has ${sources.size} sources")
                                sources.forEach { srcEl ->
                                    val src = srcEl.attr("abs:src").ifBlank { return@forEach }
                                    if (srcEl.tagName() == "iframe") {
                                        loadExtractor(src, mainUrl, subtitleCallback, callback)
                                    } else {
                                        callback(newExtractorLink(serverName, titleName, src) {
                                            this.referer = embedUrl; this.quality = Qualities.Unknown.value
                                            this.type = if (src.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                                        })
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("AnimeOnlineNinja", "loadLinks: embed fetch error: ${e.message}")
                            }
                        } else {
                            Log.w("AnimeOnlineNinja", "loadLinks: JSON has no embed_url")
                        }
                    } catch (e: Exception) {
                        Log.d("AnimeOnlineNinja", "loadLinks: response is not JSON, parsing as HTML: ${e.message}")
                        val respDoc = Jsoup.parse(responseText)
                        val iframes = respDoc.select("iframe")
                        val sources = respDoc.select("video source[src]")
                        Log.d("AnimeOnlineNinja", "loadLinks: HTML fallback - ${iframes.size} iframes, ${sources.size} video sources")
                        iframes.forEach { iframe ->
                            val src = iframe.attr("src")
                            if (src.isNotBlank()) {
                                Log.d("AnimeOnlineNinja", "loadLinks: iframe src=$src")
                                loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
                            }
                        }
                        sources.forEach { source ->
                            val src = source.attr("src")
                            if (src.isNotBlank()) {
                                Log.d("AnimeOnlineNinja", "loadLinks: video source src=$src")
                                callback(newExtractorLink(titleName, titleName, src) {
                                    this.referer = mainUrl
                                    this.quality = Qualities.Unknown.value
                                    this.type = ExtractorLinkType.M3U8
                                })
                            }
                        }
                        if (iframes.isEmpty() && sources.isEmpty()) {
                            Log.w("AnimeOnlineNinja", "loadLinks: HTML fallback found no iframes or video sources, raw response: ${responseText.take(500)}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AnimeOnlineNinja", "API error for $postId/$nume: ${e.message}")
                }
            }
        }

        if (!found) {
            Log.d("AnimeOnlineNinja", "loadLinks: no player options, trying direct iframes")
            document.select("iframe").forEach { iframe ->
                val src = iframe.attr("src")
                if (src.isNotBlank() && !src.contains("google") && !src.contains("facebook")) {
                    Log.d("AnimeOnlineNinja", "loadLinks: direct iframe src=$src")
                    loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
                }
            }
        }

        return true
    }
}
