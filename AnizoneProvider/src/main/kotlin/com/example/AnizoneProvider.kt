package com.example

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
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
import com.lagradost.nicehttp.NiceResponse
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
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
    )

    override var lang = "en"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val hasDownloadSupport = true
    override val usesWebView = true
    override val mainPage = mainPageOf(
        "2" to "Animes",
        "4" to "Películas",
        "6" to "Más Contenido"
    )
    private var cookies = mutableMapOf<String, String>()
    private var wireData = mutableMapOf(
        "wireSnapshot" to "",
        "token" to ""
    )

    private suspend fun fetchWithCF(url: String): NiceResponse {
        val headers = mutableMapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
            "Referer" to mainUrl,
            "Origin" to mainUrl
        )
        val cookiesToUse = cachedCookies
        if (cookiesToUse != null && cookiesToUse.isNotEmpty()) {
            headers["Cookie"] = cookiesToUse.map { "${it.key}=${it.value}" }.joinToString("; ")
        }
        val response = app.get(url, headers = headers)
        if (response.cookies.isNotEmpty()) {
            cachedCookies = response.cookies
        }
        Log.d("AniZoneCF", "fetchWithCF: HTTP ${response.code}, cookies_enviadas=${cookiesToUse?.size ?: 0}")
        return response
    }

    companion object {
        var pluginContext: Context? = null
        var cachedCookies: Map<String, String>? = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    private suspend fun showCloudflareDialog(url: String) {
        val ctx = pluginContext ?: run {
            Log.e("AniZone", "No hay Context para mostrar WebView")
            return
        }
        withContext(Dispatchers.Main) {
            suspendCoroutine { cont ->
                try {
                    val webView = WebView(ctx)
                    webView.settings.javaScriptEnabled = true
                    webView.settings.domStorageEnabled = true
                    webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.6367.83 Mobile Safari/537.36"

                    webView.loadUrl(url)

                    val dialog = AlertDialog.Builder(ctx)
                        .setTitle("Cloudflare - Resuelve el desafío")
                        .setMessage("Resuelve el captcha en el navegador y presiona Listo")
                        .setView(webView)
                        .setPositiveButton("Listo") { _, _ ->
                            captureCookiesFromWebView(url)
                            cont.resume(Unit)
                        }
                        .setNegativeButton("Cancelar") { _, _ ->
                            cont.resume(Unit)
                        }
                        .setOnCancelListener { cont.resume(Unit) }
                        .create()
                    dialog.show()
                } catch (e: Exception) {
                    Log.e("AniZone", "Error WebView: ${e.message}")
                    cont.resume(Unit)
                }
            }
        }
    }

    private fun captureCookiesFromWebView(url: String) {
        try {
            val cookieManager = CookieManager.getInstance()
            val cookiesStr = cookieManager.getCookie(url)
            if (cookiesStr != null && cookiesStr.isNotBlank()) {
                cachedCookies = cookiesStr.split(";").associate { part ->
                    val trimmed = part.trim()
                    val idx = trimmed.indexOf('=')
                    if (idx == -1) trimmed to "" else trimmed.substring(0, idx) to trimmed.substring(idx + 1)
                }
                Log.d("AniZoneCF", "Cookies capturadas del WebView: ${cachedCookies?.keys}")
            } else {
                Log.w("AniZoneCF", "No se encontraron cookies en el WebView")
            }
        } catch (e: Exception) {
            Log.e("AniZoneCF", "Error capturando cookies: ${e.message}")
        }
    }

    private fun isCloudflareChallenge(text: String): Boolean =
        text.contains("Just a moment", ignoreCase = true) ||
        text.contains("/cdn-cgi/challenge-platform", ignoreCase = true)

    private suspend fun initializeLiveWire(): Boolean {
        if (!wireData["wireSnapshot"].isNullOrBlank()) return true

        for (attempt in 1..10) {
            try {
                val initReq = fetchWithCF("$mainUrl/anime")
                val doc = initReq.document
                val csrfToken = doc.select("script[data-csrf]").attr("data-csrf")
                val snapshot = getSnapshot(doc)

                if (csrfToken.isNotBlank() && snapshot.isNotBlank()) {
                    this.cookies = initReq.cookies.toMutableMap()
                    wireData["token"] = csrfToken
                    wireData["wireSnapshot"] = snapshot
                    sortAnimeLatest()
                    Log.d("AniZone Init", "LiveWire inicializado OK en intento $attempt")
                    return true
                }

                val htmlPreview = initReq.text.take(500)
                if (isCloudflareChallenge(htmlPreview)) {
                    if (attempt == 1) {
                        Log.w("AniZone Init", "Cloudflare detectado. Abriendo WebView para resolver...")
                        showCloudflareDialog("$mainUrl/anime")
                    } else {
                        Log.d("AniZone Init", "Reintento $attempt/10 con cookies del WebView...")
                        delay(2000)
                    }
                } else {
                    Log.e("AniZone Init", "HTML inesperado: $htmlPreview")
                    return false
                }
            } catch (e: Exception) {
                Log.e("AniZone Init", "Intento $attempt Error: ${e.message}")
                delay(2000)
            }
        }
        Log.e("AniZone Init", "No se pudo inicializar LiveWire.")
        return false
    }

    private suspend fun sortAnimeLatest() {
        try {
            liveWireBuilder(mapOf("sort" to "release-desc"), mutableListOf(), this.cookies, this.wireData, true)
        } catch (e: Exception) {
            Log.e("AniZone Init", "sortAnimeLatest: ${e.message}")
        }
    }

    private fun getSnapshot(doc: Document): String =
        doc.select("main div[wire:snapshot]").attr("wire:snapshot").replace("&quot;", "\"")

    private fun getSnapshot(json: JSONObject): String =
        json.getJSONArray("components").getJSONObject(0).getString("snapshot")

    private fun getHtmlFromWire(json: JSONObject): Document =
        Jsoup.parse(json.getJSONArray("components").getJSONObject(0).getJSONObject("effects").getString("html"))

    private suspend fun liveWireBuilder(
        updates: Map<String, String>, calls: List<Map<String, Any>>,
        biscuit: MutableMap<String, String>,
        wireCreds: MutableMap<String, String>,
        remember: Boolean
    ): JSONObject {
        val payload = mapOf(
            "_token" to wireCreds["token"], "components" to listOf(
                mapOf("snapshot" to wireCreds["wireSnapshot"], "updates" to updates, "calls" to calls)
            )
        )
        val requestBody = payload.toJson().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val req = app.post(
            url = "$mainUrl/livewire/update",
            requestBody = requestBody,
            headers = mapOf("X-CSRF-TOKEN" to wireCreds["token"]!!),
            cookies = biscuit,
            referer = "$mainUrl/anime"
        )
        val bodyString = req.text
        if (bodyString.isBlank()) throw Exception("Respuesta Livewire vacía (HTTP ${req.code}).")
        if (bodyString.trim().startsWith("<!DOCTYPE", ignoreCase = true) || bodyString.trim().startsWith("<html", ignoreCase = true))
            throw Exception("Livewire no devolvió JSON. HTTP ${req.code}")
        val responseJson = JSONObject(bodyString)
        if (remember) {
            wireCreds["wireSnapshot"] = getSnapshot(responseJson)
            biscuit.putAll(req.cookies)
        }
        return responseJson
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val initialized = initializeLiveWire()
        if (!initialized) {
            Log.w("AniZone", "LiveWire no inicializado.")
            return newHomePageResponse(HomePageList(request.name, emptyList(), isHorizontalImages = false), hasNext = false)
        }
        return try {
            var responseJson = liveWireBuilder(mapOf("type" to request.data), mutableListOf(), this.cookies, this.wireData, true)
            var doc = getHtmlFromWire(responseJson)
            for (i in 1 until page) {
                if (doc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") == null) break
                responseJson = liveWireBuilder(mutableMapOf(), listOf(mapOf("path" to "", "method" to "loadMore", "params" to listOf<String>())), this.cookies, this.wireData, true)
                doc = getHtmlFromWire(responseJson)
            }
            newHomePageResponse(HomePageList(request.name, doc.select("div[wire:key]").map { toResult(it) }, isHorizontalImages = false),
                hasNext = doc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null)
        } catch (e: Exception) {
            Log.e("AniZone", "getMainPage: ${e.message}")
            newHomePageResponse(HomePageList(request.name, emptyList(), isHorizontalImages = false), hasNext = false)
        }
    }

    private fun toResult(post: Element): SearchResponse {
        return newMovieSearchResponse(
            post.selectFirst("img")?.attr("alt") ?: "",
            post.selectFirst("a")?.attr("href") ?: "",
            TvType.Movie
        ) { this.posterUrl = post.selectFirst("img")?.attr("src") }
    }

    override suspend fun quickSearch(query: String) = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        initializeLiveWire()
        return getHtmlFromWire(liveWireBuilder(mapOf("search" to query), mutableListOf(), this.cookies, this.wireData, false))
            .select("div[wire:key]").mapNotNull { toResult(it) }
    }

    override suspend fun load(url: String): LoadResponse {
        val r = fetchWithCF(url)
        val doc = r.document
        val cookie = r.cookies.toMutableMap()
        val wd = mutableMapOf("wireSnapshot" to getSnapshot(doc), "token" to doc.select("script[data-csrf]").attr("data-csrf"))
        val title = doc.selectFirst("h1")?.text() ?: throw NotImplementedError("No title")
        val imdbId = doc.selectFirst("a[href*='imdb.com']")?.attr("href")?.substringAfter("title/")?.trimEnd('/', ' ', '?')?.let { if (it.startsWith("tt") && it.length > 2) it else "tt0000000" } ?: "tt0000000"

        var currentDoc = doc
        var attempts = 0
        while (currentDoc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null && attempts < 100) {
            attempts++
            try {
                currentDoc = getHtmlFromWire(liveWireBuilder(mutableMapOf(), listOf(mapOf("path" to "", "method" to "loadMore", "params" to listOf<String>())), cookie, wd, true))
            } catch (e: Exception) { break }
        }

        val episodes = currentDoc.select("li[x-data]").map { elt ->
            newEpisode(data = elt.selectFirst("a")?.attr("href") ?: "") {
                this.name = elt.selectFirst("h3")?.text()?.substringAfter(":")?.trim()
                this.season = 0
                this.posterUrl = elt.selectFirst("img")?.attr("src")
                this.data = "${elt.selectFirst("a")?.attr("href")}|||$imdbId"
                this.date = elt.selectFirst("span[title]")?.selectFirst("span.line-clamp-1")?.text()?.trim()?.replace(Regex("\\s+"), "")?.ifEmpty { null }?.let {
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
                "Completed" -> ShowStatus.Completed
                "Ongoing" -> ShowStatus.Ongoing
                else -> null
            }
            addEpisodes(DubStatus.None, episodes)
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val episodeUrl = data.split("|||")[0]
        val webReq = fetchWithCF(episodeUrl)
        val web = webReq.document
        val cookie = webReq.cookies
        val sourceName = web.selectFirst("span.truncate")?.text() ?: ""
        val mediaPlayer = web.selectFirst("media-player")
        val m3U8 = mediaPlayer?.attr("src") ?: ""

        mediaPlayer?.select("track")?.forEach {
            subtitleCallback(newSubtitleFile(it.attr("label"), it.attr("src")))
        }
        callback(newExtractorLink(sourceName, name, m3U8, type = ExtractorLinkType.M3U8) {
            this.referer = episodeUrl
            this.quality = 0
            this.headers = mapOf(
                "Origin" to mainUrl, "Accept" to "*/*",
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "Cookie" to cookie.map { "${it.key}=${it.value}" }.joinToString("; ")
            )
        })
        return true
    }
}
