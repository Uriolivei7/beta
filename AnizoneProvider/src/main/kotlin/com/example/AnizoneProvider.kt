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
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
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
        "anime" to "Animes", "peliculas" to "Películas", "mas-contenido" to "Más Contenido"
    )

    companion object {
        var pluginContext: Context? = null
        private var wv: WebView? = null
        private var resolving = false
    }

    @SuppressLint("SetJavaScriptEnabled")
    private suspend fun ensureWebView(): Boolean {
        if (wv != null) return true
        val ctx = pluginContext ?: return false
        if (resolving) {
            for (i in 1..60) { if (wv != null) return true; delay(1000) }
            return false
        }
        resolving = true
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                var resumed = false
                fun done(v: Boolean) { if (!resumed) { resumed = true; resolving = !v; cont.resume(v) } }
                try {
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
                        .setNegativeButton("Cancelar") { _, _ -> try { webView.destroy() } catch (_: Exception) {}; done(false) }
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
                    Log.e("AniZone", "Error WebView: ${e.message}")
                    done(false)
                }
            }
        }
    }

    private suspend fun wvGet(url: String): Document {
        val view = wv ?: throw Exception("WebView no disponible")
        return withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                var resumed = false
                view.stopLoading()
                view.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(v: WebView, u: String) {
                        if (resumed) return
                        v.evaluateJavascript("(function(){return document.documentElement.outerHTML;})()") { html ->
                            if (!resumed) {
                                resumed = true
                                val decoded = try {
                                    JSONObject("{\"h\":$html}").getString("h")
                                } catch (e: Exception) {
                                    html?.removeSurrounding("\"")?.replace("\\\"", "\"")?.replace("\\n", "\n") ?: ""
                                }
                                cont.resume(Jsoup.parse(decoded, url))
                            }
                        }
                    }
                }
                view.loadUrl(url)
            }
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        if (!ensureWebView()) return newHomePageResponse(HomePageList(request.name, emptyList()), false)
        return try {
            val url = "$mainUrl/${request.data}"
            val doc = wvGet(url)
            newHomePageResponse(HomePageList(request.name, doc.select("div[wire:key]").map { toResult(it) }),
                hasNext = false)
        } catch (e: Exception) {
            Log.e("AniZone", "getMainPage: ${e.message}")
            newHomePageResponse(HomePageList(request.name, emptyList()), false)
        }
    }

    private fun imgSrc(img: Element?): String? {
        if (img == null) return null
        return img.attr("src").ifEmpty { img.attr("data-src").ifEmpty { img.attr("data-lazy-src") } }.ifEmpty { null }
    }

    private fun toResult(post: Element) = newMovieSearchResponse(
        post.selectFirst("img")?.attr("alt") ?: "",
        post.selectFirst("a")?.attr("href") ?: "",
        TvType.Movie
    ) { this.posterUrl = imgSrc(post.selectFirst("img")) }

    override suspend fun quickSearch(query: String) = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        if (!ensureWebView()) return emptyList()
        return try {
            val doc = wvGet("$mainUrl/anime?search=$query")
            doc.select("div[wire:key]").mapNotNull { toResult(it) }
        } catch (e: Exception) {
            Log.e("AniZone", "search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = wvGet(url)
        val title = doc.selectFirst("h1")?.text() ?: throw NotImplementedError("No title")
        val imdbId = doc.selectFirst("a[href*='imdb.com']")?.attr("href")?.substringAfter("title/")?.trimEnd('/', ' ', '?')
            ?.let { if (it.startsWith("tt") && it.length > 2) it else "tt0000000" } ?: "tt0000000"

        val episodes = doc.select("li[x-data]").map { elt ->
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
            this.posterUrl = imgSrc(doc.selectFirst("main img, .poster img, img[alt*='$title'], img[src*='/storage/']"))
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
        val doc = wvGet(episodeUrl)
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
