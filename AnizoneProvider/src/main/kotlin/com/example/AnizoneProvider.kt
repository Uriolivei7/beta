package com.example

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.ViewGroup
import android.widget.LinearLayout
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
    private var wireData = mutableMapOf("wireSnapshot" to "", "token" to "")

    companion object {
        var pluginContext: Context? = null
        var persistentWebView: WebView? = null
        var cfResolved = false
        var resolving = false
    }

    @SuppressLint("SetJavaScriptEnabled")
    private suspend fun resolveCloudflare(): Boolean {
        if (cfResolved && persistentWebView != null) return true
        if (resolving) {
            // Ya hay una resolución en curso, esperar
            for (i in 1..30) {
                if (cfResolved) return true
                delay(1000)
            }
            return cfResolved
        }
        resolving = true

        val ctx = pluginContext ?: return false
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                try {
                    val webView = WebView(ctx)
                    webView.settings.javaScriptEnabled = true
                    webView.settings.domStorageEnabled = true
                    webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.6367.83 Mobile Safari/537.36"
                    webView.settings.builtInZoomControls = true
                    webView.settings.displayZoomControls = false

                    val layout = LinearLayout(ctx)
                    layout.orientation = LinearLayout.VERTICAL
                    layout.addView(webView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

                    val dialog = AlertDialog.Builder(ctx)
                        .setTitle("AniZone - Cloudflare")
                        .setView(layout)
                        .setNegativeButton("Cancelar") { d, _ -> d.dismiss(); cont.resume(false) }
                        .setCancelable(false)
                        .create()

                    var resolved = false
                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            if (resolved) return
                            if (!url.contains("/cdn-cgi/") && !url.contains("challenges.cloudflare.com")) {
                                view.evaluateJavascript("document.querySelector('main div[wire\\\\:snapshot]') !== null") { result ->
                                    if (result == "true" && !resolved) {
                                        resolved = true
                                        Log.d("AniZone", "Cloudflare resuelto")
                                        persistentWebView = webView.also { (it.parent as? ViewGroup)?.removeView(it) }
                                        cfResolved = true
                                        dialog.dismiss()
                                        cont.resume(true)
                                    }
                                }
                            }
                        }
                    }
                    webView.loadUrl("$mainUrl/anime")
                    dialog.show()
                } catch (e: Exception) {
                    Log.e("AniZone", "Error: ${e.message}")
                    resolving = false
                    cont.resume(false)
                }
            }
        }
    }

    private fun decodeHtmlFromJs(jsResult: String?): String? {
        if (jsResult == null) return null
        return try {
            JSONObject("{\"h\":$jsResult}").getString("h")
        } catch (e: Exception) {
            Log.e("AniZone", "Error decodificando HTML: ${e.message}")
            null
        }
    }

    private suspend fun wvGet(url: String): Document? {
        val wv = persistentWebView ?: return null
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                wv.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        view.evaluateJavascript("document.documentElement.outerHTML") { html ->
                            val raw = decodeHtmlFromJs(html)
                            cont.resume(if (raw != null) Jsoup.parse(raw) else null)
                        }
                    }
                }
                wv.loadUrl(url)
            }
        }
    }

    private suspend fun wvPost(url: String, jsonBody: String, csrfToken: String): Document? {
        val wv = persistentWebView ?: return null
        val encodedBody = java.net.URLEncoder.encode(jsonBody, "UTF-8")
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                val jsCode = """
                    fetch('$url', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json', 'X-CSRF-TOKEN': '$csrfToken', 'Accept': 'application/json'},
                        body: decodeURIComponent('$encodedBody')
                    }).then(r => r.text()).then(t => Android.onResult(t));
                """.trimIndent()

                wv.addJavascriptInterface(object {
                    @JavascriptInterface
                    fun onResult(text: String) {
                        cont.resume(Jsoup.parse(text))
                    }
                }, "Android")

                wv.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        view.evaluateJavascript(jsCode, null)
                    }
                }
                wv.loadDataWithBaseURL(url, "<html><body></body></html>", "text/html", "UTF-8", null)
            }
        }
    }

    private suspend fun fetchPage(url: String): Document? {
        if (cfResolved && persistentWebView != null) return wvGet(url)
        return app.get(url, headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/126.0.0.0 Safari/537.36",
            "Accept" to "text/html,*/*"
        )).document
    }

    private suspend fun ensureInitialized(): Boolean {
        if (!wireData["wireSnapshot"].isNullOrBlank()) return true
        val ok = resolveCloudflare()
        if (!ok) return false

        for (attempt in 1..5) {
            try {
                val doc = fetchPage("$mainUrl/anime") ?: continue
                val csrfToken = doc.select("script[data-csrf]").attr("data-csrf")
                val snapshot = doc.select("main div[wire:snapshot]").attr("wire:snapshot").replace("&quot;", "\"")
                if (csrfToken.isNotBlank() && snapshot.isNotBlank()) {
                    wireData["token"] = csrfToken
                    wireData["wireSnapshot"] = snapshot
                    sortAnimeLatest()
                    Log.d("AniZone", "Initialized OK")
                    return true
                }
                Log.w("AniZone", "Intento $attempt: snapshot='${snapshot.take(40)}' token='${csrfToken.take(20)}'")
                delay(2000)
            } catch (e: Exception) {
                Log.e("AniZone", "Intento $attempt: ${e.message}")
                delay(2000)
            }
        }
        return false
    }

    private suspend fun sortAnimeLatest() {
        try { postToLiveWire(mapOf("sort" to "release-desc"), emptyList(), true) }
        catch (e: Exception) { Log.e("AniZone", "sortAnimeLatest: ${e.message}") }
    }

    private suspend fun postToLiveWire(updates: Map<String, String>, calls: List<Map<String, Any>>, remember: Boolean): JSONObject {
        val payload = mapOf("_token" to wireData["token"], "components" to listOf(
            mapOf("snapshot" to wireData["wireSnapshot"], "updates" to updates, "calls" to calls)
        ))
        val jsonStr = payload.toJson()
        val token = wireData["token"]!!

        if (cfResolved && persistentWebView != null) {
            val doc = wvPost("$mainUrl/livewire/update", jsonStr, token)
            if (doc != null) {
                val json = JSONObject(doc.text())
                if (remember) wireData["wireSnapshot"] = json.getJSONArray("components").getJSONObject(0).getString("snapshot")
                return json
            }
            throw Exception("wvPost falló")
        }

        val req = app.post("$mainUrl/livewire/update",
            requestBody = jsonStr.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
            headers = mapOf("X-CSRF-TOKEN" to token),
            cookies = mapOf(), referer = "$mainUrl/anime"
        )
        val body = req.text
        if (body.isBlank() || body.trim().startsWith("<!DOCTYPE") || body.trim().startsWith("<html"))
            throw Exception("Livewire no devolvió JSON")
        val json = JSONObject(body)
        if (remember) wireData["wireSnapshot"] = json.getJSONArray("components").getJSONObject(0).getString("snapshot")
        return json
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        if (!ensureInitialized()) return newHomePageResponse(HomePageList(request.name, emptyList()), false)
        return try {
            var doc = getHtmlFromWire(postToLiveWire(mapOf("type" to request.data), emptyList(), true))
            for (i in 1 until page) {
                if (doc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") == null) break
                doc = getHtmlFromWire(postToLiveWire(emptyMap(), listOf(mapOf("path" to "", "method" to "loadMore", "params" to emptyList<String>())), true))
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
        if (!ensureInitialized()) return emptyList()
        return getHtmlFromWire(postToLiveWire(mapOf("search" to query), emptyList(), false))
            .select("div[wire:key]").mapNotNull { toResult(it) }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = fetchPage(url) ?: throw NotImplementedError("No se pudo cargar la página")
        val wd = mutableMapOf(
            "wireSnapshot" to doc.select("main div[wire:snapshot]").attr("wire:snapshot").replace("&quot;", "\""),
            "token" to doc.select("script[data-csrf]").attr("data-csrf")
        )
        val title = doc.selectFirst("h1")?.text() ?: throw NotImplementedError("No title")
        val imdbId = doc.selectFirst("a[href*='imdb.com']")?.attr("href")?.substringAfter("title/")?.trimEnd('/', ' ', '?')
            ?.let { if (it.startsWith("tt") && it.length > 2) it else "tt0000000" } ?: "tt0000000"

        var currentDoc = doc
        var attempt = 0
        while (currentDoc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null && attempt < 100) {
            attempt++
            try { currentDoc = getHtmlFromWire(postToLiveWire(emptyMap(), listOf(mapOf("path" to "", "method" to "loadMore", "params" to emptyList<String>())), true)) }
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
        val doc = fetchPage(episodeUrl) ?: return false
        val sourceName = doc.selectFirst("span.truncate")?.text() ?: ""
        val mediaPlayer = doc.selectFirst("media-player")
        val m3U8 = mediaPlayer?.attr("src") ?: ""
        mediaPlayer?.select("track")?.forEach { subtitleCallback(newSubtitleFile(it.attr("label"), it.attr("src"))) }
        callback(newExtractorLink(sourceName, name, m3U8, type = ExtractorLinkType.M3U8) {
            this.referer = episodeUrl; this.quality = 0
        })
        return true
    }

    private fun getHtmlFromWire(json: JSONObject) =
        Jsoup.parse(json.getJSONArray("components").getJSONObject(0).getJSONObject("effects").getString("html"))
}
