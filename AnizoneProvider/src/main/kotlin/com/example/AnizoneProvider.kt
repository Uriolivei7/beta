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
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newAnimeLoadResponse
import com.lagradost.cloudstream3.newEpisode
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newMovieSearchResponse
import com.lagradost.cloudstream3.newSubtitleFile
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URLEncoder
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
        "2" to "Animes", "4" to "Películas", "6" to "Más Contenido"
    )
    private var cookies = mutableMapOf<String, String>()
    private var wireData = mutableMapOf("wireSnapshot" to "", "token" to "")

    companion object {
        var pluginContext: Context? = null
        private var wv: WebView? = null
        private var resolving = false
    }

    private fun getWv() = wv

    @SuppressLint("SetJavaScriptEnabled")
    private suspend fun ensureWebView(): Boolean {
        if (getWv() != null) return true
        val ctx = pluginContext ?: return false
        if (resolving) {
            for (i in 1..60) { if (wv != null) return true; delay(1000) }
            return false
        }
        resolving = true
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                try {
                    val webView = WebView(ctx.applicationContext)
                    webView.settings.javaScriptEnabled = true
                    webView.settings.domStorageEnabled = true
                    webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    webView.settings.cacheMode = WebSettings.LOAD_DEFAULT

                    val prog = ProgressBar(ctx, null, android.R.attr.progressBarStyleHorizontal)
                    prog.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8)

                    val layout = LinearLayout(ctx)
                    layout.orientation = LinearLayout.VERTICAL
                    layout.addView(prog)
                    layout.addView(webView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

                    val dialog = AlertDialog.Builder(ctx)
                        .setTitle("AniZone - Resuelve Cloudflare")
                        .setView(layout)
                        .setNegativeButton("Cancelar") { _, _ -> try { webView.destroy() } catch (_: Exception) {}; cont.resume(false) }
                        .setCancelable(false)
                        .create()

                    webView.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(v: WebView, p: Int) { prog.progress = p }
                    }
                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            if (url.contains("anizone.to") && !url.contains("/cdn-cgi/")) {
                                view.evaluateJavascript("document.querySelector('main div[wire\\\\:snapshot]') !== null") { r ->
                                    if (r == "true") {
                                        wv = view
                                        CookieManager.getInstance().getCookie("$mainUrl/anime")?.let { c ->
                                            cookies.clear()
                                            c.split(";").forEach { part ->
                                                val t = part.trim(); val i = t.indexOf('=')
                                                if (i > 0) cookies[t.substring(0, i)] = t.substring(i + 1)
                                            }
                                        }
                                        dialog.dismiss()
                                        resolving = false
                                        cont.resume(true)
                                    }
                                }
                            }
                        }
                    }
                    webView.loadUrl("$mainUrl/anime")
                    dialog.show()
                } catch (e: Exception) {
                    Log.e("AniZone", "Error WebView: ${e.message}")
                    resolving = false
                    cont.resume(false)
                }
            }
        }
    }

    private suspend fun wvGet(url: String): Document {
        val view = getWv() ?: throw Exception("WebView no disponible")
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                view.stopLoading()
                view.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(v: WebView, u: String) {
                        v.evaluateJavascript("(function(){return document.documentElement.outerHTML;})()") { html ->
                            val decoded = try {
                                JSONObject("{\"h\":$html}").getString("h")
                            } catch (e: Exception) {
                                html?.removeSurrounding("\"")?.replace("\\\"", "\"")?.replace("\\n", "\n") ?: ""
                            }
                            cont.resume(Jsoup.parse(decoded, url))
                        }
                    }
                }
                view.loadUrl(url)
            }
        }
    }

    private suspend fun wvCurrentHtml(): Document {
        val view = getWv() ?: throw Exception("WebView no disponible")
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                view.evaluateJavascript("(function(){return document.documentElement.outerHTML;})()") { html ->
                    val decoded = try {
                        JSONObject("{\"h\":$html}").getString("h")
                    } catch (e: Exception) {
                        html?.removeSurrounding("\"")?.replace("\\\"", "\"")?.replace("\\n", "\n") ?: ""
                    }
                    cont.resume(Jsoup.parse(decoded, view.url ?: mainUrl))
                }
            }
        }
    }

    private suspend fun wvPost(url: String, jsonBody: String): String {
        val view = getWv() ?: throw Exception("WebView no disponible")
        val encoded = URLEncoder.encode(jsonBody, "UTF-8")
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                view.evaluateJavascript(
                    "fetch('$url', {" +
                    "  method: 'POST'," +
                    "  headers: {'Content-Type': 'application/json', 'X-CSRF-TOKEN': '${wireData["token"]}'}," +
                    "  body: decodeURIComponent('$encoded')" +
                    "}).then(r => r.text()).then(t => { window.__wvR = t; window.__wvD = true; })" +
                    ".catch(e => { window.__wvR = 'ERROR:' + e; window.__wvD = true; })"
                ) {
                    fun poll(n: Int) {
                        if (n <= 0) { cont.resume(""); return }
                        view.evaluateJavascript("window.__wvD") { d ->
                            if (d == "true") {
                                view.evaluateJavascript("window.__wvR") { r ->
                                    cont.resume(r?.removeSurrounding("\"")?.replace("\\\"", "\"") ?: "")
                                }
                            } else {
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ poll(n - 1) }, 500)
                            }
                        }
                    }
                    poll(40)
                }
            }
        }
    }

    private suspend fun fetchPage(url: String): Document {
        val view = getWv()
        if (view != null) {
            try { return wvGet(url) } catch (e: Exception) { Log.w("AniZone", "wvGet falló: ${e.message}") }
        }
        val headers = mutableMapOf(
            "User-Agent" to "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.6367.83 Mobile Safari/537.36",
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "Accept-Language" to "es-ES,es;q=0.9"
        )
        if (cookies.isNotEmpty()) headers["Cookie"] = cookies.map { "${it.key}=${it.value}" }.joinToString("; ")
        val resp = app.get(url, headers = headers)
        if (resp.cookies.isNotEmpty()) cookies.putAll(resp.cookies)
        return resp.document
    }

    private suspend fun postToLiveWire(jsonStr: String): JSONObject {
        val view = getWv()
        if (view != null) {
            try {
                val body = wvPost("$mainUrl/livewire/update", jsonStr)
                if (body.isNotBlank() && !body.startsWith("<!DOCTYPE") && !body.startsWith("<html") && !body.startsWith("ERROR:"))
                    return JSONObject(body)
            } catch (e: Exception) { Log.w("AniZone", "wvPost falló: ${e.message}") }
        }
        val headers = mutableMapOf(
            "X-CSRF-TOKEN" to (wireData["token"] ?: ""),
            "Content-Type" to "application/json"
        )
        if (cookies.isNotEmpty()) headers["Cookie"] = cookies.map { "${it.key}=${it.value}" }.joinToString("; ")
        val resp = app.post("$mainUrl/livewire/update",
            requestBody = jsonStr.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
            headers = headers, cookies = cookies, referer = "$mainUrl/anime"
        )
        if (resp.cookies.isNotEmpty()) cookies.putAll(resp.cookies)
        val body = resp.text
        if (body.isBlank() || body.trim().startsWith("<!DOCTYPE") || body.trim().startsWith("<html"))
            throw Exception("Livewire no devolvió JSON. HTTP=${resp.code}")
        return JSONObject(body)
    }

    private suspend fun initializeLiveWire(): Boolean {
        if (!wireData["wireSnapshot"].isNullOrBlank()) return true

        val ok = ensureWebView()
        if (!ok && cookies.isEmpty()) return false

        for (attempt in 1..10) {
            try {
                val doc = if (getWv() != null) wvCurrentHtml() else fetchPage("$mainUrl/anime")
                val csrf = doc.select("script[data-csrf]").attr("data-csrf")
                var snap = doc.select("main div[wire:snapshot]").attr("wire:snapshot")
                if (snap.isBlank()) snap = doc.select("div[wire:snapshot]").attr("wire:snapshot")
                snap = snap.replace("&quot;", "\"")
                if (csrf.isNotBlank() && snap.isNotBlank()) {
                    wireData["token"] = csrf
                    wireData["wireSnapshot"] = snap
                    sortAnimeLatest()
                    return true
                }
                Log.w("AniZone", "Intento $attempt: token='$csrf' snap=${snap.take(50)}")
                delay(2000)
            } catch (e: Exception) {
                Log.e("AniZone", "Intento $attempt: ${e.message}")
                delay(2000)
            }
        }
        return false
    }

    private suspend fun sortAnimeLatest() {
        try { liveWireBuilder(mapOf("sort" to "release-desc"), emptyList(), true) }
        catch (e: Exception) { Log.e("AniZone", "sort: ${e.message}") }
    }

    private suspend fun liveWireBuilder(updates: Map<String, String>, calls: List<Map<String, Any>>, remember: Boolean): JSONObject {
        val payload = mapOf("_token" to (wireData["token"] ?: ""), "components" to listOf(
            mapOf("snapshot" to (wireData["wireSnapshot"] ?: ""), "updates" to updates, "calls" to calls)
        ))
        val json = postToLiveWire(payload.toJson())
        if (remember) {
            try { wireData["wireSnapshot"] = json.getJSONArray("components").getJSONObject(0).getString("snapshot") }
            catch (e: Exception) { Log.w("AniZone", "snapshot no actualizado: ${e.message}") }
        }
        return json
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        if (!initializeLiveWire()) return newHomePageResponse(HomePageList(request.name, emptyList()), false)
        return try {
            var doc = Jsoup.parse(liveWireBuilder(mapOf("type" to request.data), emptyList(), true)
                .getJSONArray("components").getJSONObject(0).getJSONObject("effects").getString("html"))
            for (i in 1 until page) {
                if (doc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") == null) break
                doc = Jsoup.parse(liveWireBuilder(emptyMap(), listOf(mapOf("path" to "", "method" to "loadMore", "params" to emptyList<String>())), true)
                    .getJSONArray("components").getJSONObject(0).getJSONObject("effects").getString("html"))
            }
            newHomePageResponse(HomePageList(request.name, doc.select("div[wire:key]").map { toResult(it) }),
                hasNext = doc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null)
        } catch (e: Exception) {
            Log.e("AniZone", "getMainPage: ${e.message}")
            newHomePageResponse(HomePageList(request.name, emptyList()), false)
        }
    }

    private fun toResult(post: Element) = newMovieSearchResponse(
        post.selectFirst("img")?.attr("alt") ?: "", post.selectFirst("a")?.attr("href") ?: "", TvType.Movie
    ) { this.posterUrl = post.selectFirst("img")?.attr("src") }

    override suspend fun quickSearch(query: String) = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        if (!initializeLiveWire()) return emptyList()
        return try {
            Jsoup.parse(liveWireBuilder(mapOf("search" to query), emptyList(), false)
                .getJSONArray("components").getJSONObject(0).getJSONObject("effects").getString("html"))
                .select("div[wire:key]").mapNotNull { toResult(it) }
        } catch (e: Exception) {
            Log.e("AniZone", "search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = fetchPage(url)
        val title = doc.selectFirst("h1")?.text() ?: throw NotImplementedError("No title")
        val imdbId = doc.selectFirst("a[href*='imdb.com']")?.attr("href")?.substringAfter("title/")?.trimEnd('/', ' ', '?')
            ?.let { if (it.startsWith("tt") && it.length > 2) it else "tt0000000" } ?: "tt0000000"

        var currentDoc = doc
        var attempt = 0
        while (currentDoc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null && attempt < 100) {
            attempt++
            try { currentDoc = Jsoup.parse(liveWireBuilder(emptyMap(), listOf(mapOf("path" to "", "method" to "loadMore", "params" to emptyList<String>())), true)
                .getJSONArray("components").getJSONObject(0).getJSONObject("effects").getString("html")) }
            catch (e: Exception) { break }
        }
        val episodes = currentDoc.select("li[x-data]").map { elt ->
            newEpisode(data = elt.selectFirst("a")?.attr("href") ?: "") {
                this.name = elt.selectFirst("h3")?.text()?.substringAfter(":")?.trim()
                this.season = 0; this.posterUrl = elt.selectFirst("img")?.attr("src")
                this.data = "${elt.selectFirst("a")?.attr("href")}|||$imdbId"
                this.date = elt.selectFirst("span[title]")?.selectFirst("span.line-clamp-1")?.text()?.trim()
                    ?.replace(Regex("\\s+"), "")?.ifEmpty { null }?.let {
                    try { SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(it)?.time } catch (e: Exception) { null }
                } ?: 0L
            }
        }
        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = doc.selectFirst("main img")?.attr("src")
            this.plot = doc.selectFirst(".sr-only + div")?.text() ?: ""
            this.tags = doc.select("a[wire:navigate][wire:key]").map { it.text() }
            this.year = doc.select("span.inline-block").map { it.text() }.getOrNull(3)?.toIntOrNull()
            this.showStatus = when (doc.select("span.inline-block").map { it.text() }.getOrNull(1)) {
                "Completed" -> ShowStatus.Completed; "Ongoing" -> ShowStatus.Ongoing; else -> null
            }
            addEpisodes(DubStatus.None, episodes)
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val episodeUrl = data.split("|||")[0]
        val doc = fetchPage(episodeUrl)
        val sourceName = doc.selectFirst("span.truncate")?.text() ?: ""
        val mediaPlayer = doc.selectFirst("media-player")
        val m3U8 = mediaPlayer?.attr("src") ?: ""
        mediaPlayer?.select("track")?.forEach { subtitleCallback(newSubtitleFile(it.attr("label"), it.attr("src"))) }
        callback(newExtractorLink(sourceName, name, m3U8, type = ExtractorLinkType.M3U8) {
            this.referer = episodeUrl; this.quality = 0
        })
        return true
    }
}
