package com.example

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.webkit.CookieManager
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
    private var cookies = mutableMapOf<String, String>()
    private var wireData = mutableMapOf("wireSnapshot" to "", "token" to "")

    companion object {
        var pluginContext: Context? = null
        var persistentWebView: WebView? = null
        var cfResolved = false
    }

    @SuppressLint("SetJavaScriptEnabled")
    private suspend fun resolveCloudflare(): Boolean {
        if (cfResolved && persistentWebView != null) return true

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
                    layout.addView(webView, ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    ))

                    val dialog = AlertDialog.Builder(ctx)
                        .setTitle("AniZone - Cloudflare")
                        .setMessage("Resolvé el desafío. Se cerrará solo.")
                        .setView(layout)
                        .setNegativeButton("Cancelar") { d, _ -> d.dismiss(); cont.resume(false) }
                        .setCancelable(false)
                        .create()

                    var resolved = false
                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            if (resolved) return
                            if (!url.contains("/cdn-cgi/") && !url.contains("challenges.cloudflare.com")) {
                                view.evaluateJavascript(
                                    "document.querySelector('main div[wire\\\\:snapshot]') !== null"
                                ) { result ->
                                    if (result == "true" && !resolved) {
                                        resolved = true
                                        Log.d("AniZone", "Cloudflare resuelto!")
                                        persistentWebView = webView.also { wv ->
                                            val parent = wv.parent as? ViewGroup
                                            parent?.removeView(wv)
                                        }
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
                    Log.e("AniZone", "Error resolveCloudflare: ${e.message}")
                    cont.resume(false)
                }
            }
        }
    }

    private suspend fun wvGet(url: String): Document? {
        val wv = persistentWebView ?: return null
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                wv.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        view.evaluateJavascript("document.documentElement.outerHTML") { html ->
                            val doc = if (html != null) {
                                // evaluateJavascript returns a JSON string, so we need to unescape it
                                val raw = html.drop(1).dropLast(1)  // remove quotes
                                    .replace("\\\"", "\"")
                                    .replace("\\n", "\n")
                                    .replace("\\t", "\t")
                                    .replace("\\\\", "\\")
                                Jsoup.parse(raw)
                            } else null
                            cont.resume(doc)
                        }
                    }
                }
                wv.loadUrl(url)
            }
        }
    }

    private suspend fun wvPost(url: String, jsonBody: String, csrfToken: String): Document? {
        val wv = persistentWebView ?: return null
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                val jsCode = """
                    fetch('$url', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-TOKEN': '$csrfToken',
                            'Accept': 'application/json'
                        },
                        body: '${jsonBody.replace("'", "\\'").replace("\n", "\\n")}'
                    }).then(r => r.text()).then(t => {
                        document.body.innerHTML = '<pre>' + t.replace(/</g, '&lt;') + '</pre>';
                    });
                """.trimIndent()

                wv.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        // Poll for fetch result (pre element in body)
                        view.evaluateJavascript(
                            "document.body.querySelector('pre') !== null ? document.body.querySelector('pre').textContent : null"
                        ) { result ->
                            if (result != null && result != "null" && !result.startsWith("null")) {
                                cont.resume(Jsoup.parse(result))
                            } else {
                                cont.resume(null)
                            }
                        }
                    }
                }
                wv.loadDataWithBaseURL(url,
                    "<html><body><script>$jsCode</script></body></html>",
                    "text/html", "UTF-8", null
                )
            }
        }
    }

    private suspend fun fetchWithCF(url: String): Document? {
        if (cfResolved && persistentWebView != null) {
            return wvGet(url)
        }
        // Fallback: intentar con app.get (puede fallar por Cloudflare)
        val headers = mutableMapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
            "Accept" to "text/html,*/*",
            "Accept-Language" to "es-ES,es;q=0.9"
        )
        val cached = cookies
        if (cached.isNotEmpty()) {
            headers["Cookie"] = cached.map { "${it.key}=${it.value}" }.joinToString("; ")
        }
        val resp = app.get(url, headers = headers)
        if (resp.cookies.isNotEmpty()) {
            cookies.putAll(resp.cookies)
        }
        return resp.document
    }

    private suspend fun ensureInitialized(): Boolean {
        if (!wireData["wireSnapshot"].isNullOrBlank()) return true
        if (!cfResolved) {
            val ok = resolveCloudflare()
            if (!ok) return false
        }

        for (attempt in 1..5) {
            try {
                val doc = fetchWithCF("$mainUrl/anime") ?: return false
                val csrfToken = doc.select("script[data-csrf]").attr("data-csrf")
                val snapshot = doc.select("main div[wire:snapshot]").attr("wire:snapshot").replace("&quot;", "\"")
                if (csrfToken.isNotBlank() && snapshot.isNotBlank()) {
                    wireData["token"] = csrfToken
                    wireData["wireSnapshot"] = snapshot
                    sortAnimeLatest()
                    return true
                }
                Log.w("AniZone", "Intento $attempt: token/snapshot vacíos")
                delay(2000)
            } catch (e: Exception) {
                Log.e("AniZone", "Intento $attempt: ${e.message}")
                delay(2000)
            }
        }
        return false
    }

    private suspend fun sortAnimeLatest() {
        try {
            postToLiveWire(mapOf("sort" to "release-desc"), mutableListOf(), true)
        } catch (e: Exception) {
            Log.e("AniZone", "sortAnimeLatest: ${e.message}")
        }
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
                if (remember) {
                    wireData["wireSnapshot"] = json.getJSONArray("components").getJSONObject(0).getString("snapshot")
                }
                return json
            }
            throw Exception("wvPost falló")
        }

        val req = app.post(
            url = "$mainUrl/livewire/update",
            requestBody = jsonStr.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
            headers = mapOf("X-CSRF-TOKEN" to token),
            cookies = cookies,
            referer = "$mainUrl/anime"
        )
        val body = req.text
        if (body.isBlank() || body.trim().startsWith("<!DOCTYPE") || body.trim().startsWith("<html"))
            throw Exception("Livewire no devolvió JSON")
        val json = JSONObject(body)
        if (remember) {
            wireData["wireSnapshot"] = json.getJSONArray("components").getJSONObject(0).getString("snapshot")
            cookies.putAll(req.cookies)
        }
        return json
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        if (!ensureInitialized()) return newHomePageResponse(HomePageList(request.name, emptyList()), false)
        return try {
            var doc = getHtmlFromWire(postToLiveWire(mapOf("type" to request.data), mutableListOf(), true))
            for (i in 1 until page) {
                if (doc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") == null) break
                doc = getHtmlFromWire(postToLiveWire(mutableMapOf(), listOf(mapOf("path" to "", "method" to "loadMore", "params" to listOf<String>())), true))
            }
            newHomePageResponse(HomePageList(request.name, doc.select("div[wire:key]").map { toResult(it) }),
                hasNext = doc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null)
        } catch (e: Exception) {
            Log.e("AniZone", "getMainPage: ${e.message}")
            newHomePageResponse(HomePageList(request.name, emptyList()), false)
        }
    }

    private fun toResult(post: Element) = newMovieSearchResponse(
        post.selectFirst("img")?.attr("alt") ?: "",
        post.selectFirst("a")?.attr("href") ?: "", TvType.Movie
    ) { this.posterUrl = post.selectFirst("img")?.attr("src") }

    override suspend fun quickSearch(query: String) = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        if (!ensureInitialized()) return emptyList()
        return getHtmlFromWire(postToLiveWire(mapOf("search" to query), mutableListOf(), false))
            .select("div[wire:key]").mapNotNull { toResult(it) }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = fetchWithCF(url) ?: throw NotImplementedError("No se pudo cargar la página")
        val cookie = mutableMapOf<String, String>()
        val wd = mutableMapOf("wireSnapshot" to doc.select("main div[wire:snapshot]").attr("wire:snapshot").replace("&quot;", "\""),
            "token" to doc.select("script[data-csrf]").attr("data-csrf"))
        val title = doc.selectFirst("h1")?.text() ?: throw NotImplementedError("No title")
        val imdbId = doc.selectFirst("a[href*='imdb.com']")?.attr("href")?.substringAfter("title/")?.trimEnd('/', ' ', '?')
            ?.let { if (it.startsWith("tt") && it.length > 2) it else "tt0000000" } ?: "tt0000000"

        var currentDoc = doc
        var attempts = 0
        while (currentDoc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null && attempts < 100) {
            attempts++
            try {
                currentDoc = getHtmlFromWire(postToLiveWire(mutableMapOf(), listOf(mapOf("path" to "", "method" to "loadMore", "params" to listOf<String>())), true))
            } catch (e: Exception) { break }
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
        val doc = fetchWithCF(episodeUrl) ?: return false
        val sourceName = doc.selectFirst("span.truncate")?.text() ?: ""
        val mediaPlayer = doc.selectFirst("media-player")
        val m3U8 = mediaPlayer?.attr("src") ?: ""
        mediaPlayer?.select("track")?.forEach {
            subtitleCallback(newSubtitleFile(it.attr("label"), it.attr("src")))
        }
        callback(newExtractorLink(sourceName, name, m3U8, type = ExtractorLinkType.M3U8) {
            this.referer = episodeUrl; this.quality = 0
            this.headers = mapOf("Origin" to mainUrl, "Accept" to "*/*",
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/131.0.0.0 Safari/537.36")
        })
        return true
    }

    private fun getHtmlFromWire(json: JSONObject) =
        Jsoup.parse(json.getJSONArray("components").getJSONObject(0).getJSONObject("effects").getString("html"))
}
