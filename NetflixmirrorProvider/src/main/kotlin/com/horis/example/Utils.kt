package com.horis.example

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lagradost.cloudstream3.USER_AGENT
import com.lagradost.nicehttp.Requests
import com.lagradost.nicehttp.ResponseParser
import kotlin.reflect.KClass
import com.lagradost.api.Log
import com.lagradost.cloudstream3.APIHolder
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import android.content.Context
import android.content.SharedPreferences
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import okhttp3.FormBody
import okhttp3.Request
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// ---------------------------------------------------------------------------
// JSON / HTTP
// ---------------------------------------------------------------------------

val JSONParser = object : ResponseParser {
    val mapper: ObjectMapper = jacksonObjectMapper().configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
    ).configure(
        JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true
    )

    override fun <T : Any> parse(text: String, kClass: KClass<T>): T =
        mapper.readValue(text, kClass.java)

    override fun <T : Any> parseSafe(text: String, kClass: KClass<T>): T? =
        try { mapper.readValue(text, kClass.java) } catch (_: Exception) { null }

    override fun writeValueAsString(obj: Any): String =
        mapper.writeValueAsString(obj)
}

val app = Requests(responseParser = JSONParser).apply {
    defaultHeaders = mapOf("User-Agent" to USER_AGENT)
}

/** Plugin context for WebView operations – set by NetflixmirrorPlugin.load() */
var pluginContext: android.content.Context? = null

inline fun <reified T : Any> parseJson(text: String): T = JSONParser.parse(text, T::class)

inline fun <reified T : Any> tryParseJson(text: String): T? =
    try { JSONParser.parseSafe(text, T::class) } catch (_: Exception) { null }

inline fun <reified T : Any> tryParseJsonList(text: String): List<T>? {
    val mapper = jacksonObjectMapper().configure(
        com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
    ).configure(
        com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true
    )
    return try { mapper.readValue(text, object : TypeReference<List<T>>() {}) } catch (_: Exception) { null }
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

fun convertRuntimeToMinutes(runtime: String): Int {
    var totalMinutes = 0
    for (part in runtime.split(" ")) {
        when {
            part.endsWith("h") -> totalMinutes += (part.removeSuffix("h").trim().toIntOrNull() ?: 0) * 60
            part.endsWith("m") -> totalMinutes += part.removeSuffix("m").trim().toIntOrNull() ?: 0
        }
    }
    return totalMinutes
}

// ---------------------------------------------------------------------------
// NetflixMirrorStorage – persistent cookie storage
// ---------------------------------------------------------------------------

object NetflixMirrorStorage {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences("NetflixMirrorPrefs", Context.MODE_PRIVATE)
    }

    fun saveCookie(cookie: String) {
        prefs?.edit()?.apply {
            putString("nf_cookie", cookie)
            putLong("nf_cookie_timestamp", System.currentTimeMillis())
            apply()
        }
    }

    fun getCookie(): Pair<String?, Long> {
        return Pair(
            prefs?.getString("nf_cookie", null),
            prefs?.getLong("nf_cookie_timestamp", 0L) ?: 0L
        )
    }

    fun clearCookie() {
        prefs?.edit()?.apply {
            remove("nf_cookie")
            remove("nf_cookie_timestamp")
            apply()
        }
    }
}

// ---------------------------------------------------------------------------
// Cloudflare bypass – gets t_hash_t cookie from net52.cc
// ---------------------------------------------------------------------------

suspend fun bypass(mainUrl: String): String {
    val (savedCookie, savedTimestamp) = NetflixMirrorStorage.getCookie()
    if (!savedCookie.isNullOrEmpty() && System.currentTimeMillis() - savedTimestamp < 54_000_000) {
        Log.d("bypass", "Using cached cookie (${savedCookie.take(100)}...) ts=${savedTimestamp}")
        return savedCookie
    }

    NetflixMirrorStorage.clearCookie()

    val allCookies = mutableMapOf<String, String>()
    fun captureCookies(resp: okhttp3.Response) {
        resp.headers("Set-Cookie").forEach { raw ->
            val name = raw.substringBefore("=")
            val value = raw.substringAfter("=").substringBefore(";")
            if (name.isNotBlank()) allCookies[name] = value
        }
    }

    val browserHeaders = mapOf(
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        "Accept-Language" to "en-US,en;q=0.9",
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36",
        "Connection" to "keep-alive"
    )

    val client = app.baseClient.newBuilder()
        .followRedirects(false)
        .followSslRedirects(false)
        .build()

    // Step 1: visit $mainUrl/home → redirects to net22.cc/verify2 (or similar)
    var addhash = ""
    try {
        var currentUrl = "$mainUrl/home"
        for (hop in 0..5) {
            val req = Request.Builder()
                .url(currentUrl)
                .apply { browserHeaders.forEach { (k, v) -> addHeader(k, v) } }
                .apply {
                    val cookieStr = allCookies.map { "${it.key}=${it.value}" }.joinToString("; ")
                    if (cookieStr.isNotBlank()) addHeader("Cookie", cookieStr)
                }
                .build()
            client.newCall(req).execute().use { resp ->
                captureCookies(resp)
                val location = resp.header("Location")
                if (location != null) {
                    currentUrl = if (location.startsWith("http")) location else "$mainUrl$location"
                } else {
                    val body = resp.body?.string().orEmpty()
                    Log.d("bypass", "final url=$currentUrl body.len=${body.length}")
                    addhash = Regex("""[?&]addhash=([^&]+)""").find(currentUrl)?.groupValues?.getOrNull(1).orEmpty()
                    if (addhash.isBlank()) {
                        val doc = org.jsoup.Jsoup.parse(body)
                        addhash = doc.selectFirst("input[name=addhash]")?.attr("value").orEmpty()
                    }
                    if (addhash.isBlank()) {
                        addhash = Regex("""["']addhash["']\s*[:=]\s*["']([^"']+)""").find(body)?.groupValues?.getOrNull(1).orEmpty()
                    }
                    Log.d("bypass", "body first 500: ${body.take(500)}")
                    break
                }
            }
        }
    } catch (e: Exception) {
        Log.d("bypass", "redirect failed: ${e.message}")
    }
    Log.d("bypass", "addhash=${addhash.take(20)} cookies so far=$allCookies")

    // Step 2: POST to verify.php with fake recaptcha
    val verifyHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36",
        "Accept" to "*/*",
        "Origin" to "https://net22.cc",
        "Referer" to "https://net22.cc/verify2",
        "Content-Type" to "application/x-www-form-urlencoded"
    )

    for (count in 0..3) {
        try {
            val formBody = FormBody.Builder()
                .add("g-recaptcha-response", UUID.randomUUID().toString())
                .apply { if (addhash.isNotBlank()) add("addhash", addhash) }
                .build()
            val request = Request.Builder()
                .url("$mainUrl/verify.php")
                .post(formBody)
                .apply { verifyHeaders.forEach { (k, v) -> addHeader(k, v) } }
                .apply {
                    val cookieStr = allCookies.map { "${it.key}=${it.value}" }.joinToString("; ")
                    if (cookieStr.isNotBlank()) addHeader("Cookie", cookieStr)
                }
                .build()
            client.newCall(request).execute().use { resp ->
                captureCookies(resp)
                val respBody = resp.body?.string().orEmpty()
                Log.d("bypass", "verify.php status=${resp.code} cookies=$allCookies body=${respBody.take(200)}")
                if (allCookies.containsKey("t_hash_t")) {
                    val cookieStr = allCookies.map { "${it.key}=${it.value}" }.joinToString("; ")
                    NetflixMirrorStorage.saveCookie(cookieStr)
                    Log.d("bypass", "Got all cookies: $cookieStr")
                    return cookieStr
                }
                Log.d("bypass", "Attempt $count: no t_hash_t, resp=$respBody")
            }
            kotlinx.coroutines.delay(2000)
        } catch (e: Exception) {
            Log.w("bypass", "Attempt $count failed: ${e.message}")
            if (count < 3) kotlinx.coroutines.delay(2000) else throw e
        }
    }
    throw Exception("Failed to get t_hash_t after 4 attempts")
}

// ---------------------------------------------------------------------------
// Retry helper for transient MySQL "Too many connections" errors
// ---------------------------------------------------------------------------

suspend fun <T> retryOnDbError(maxRetries: Int = 3, block: suspend () -> T): T {
    var lastException: Exception? = null
    repeat(maxRetries) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            lastException = e
            val msg = e.message.orEmpty() + e.cause?.message.orEmpty()
            if (msg.contains("Too many connections") || msg.contains("1040") || msg.contains("08004") || msg.contains("HY000")) {
                Log.d("Retry", "DB connection pool full (attempt ${attempt + 1}/$maxRetries), retrying in ${(attempt + 1) * 2}s")
                kotlinx.coroutines.delay(((attempt + 1) * 2000L))
            } else {
                throw e
            }
        }
    }
    throw lastException ?: Exception("Retry failed after $maxRetries attempts")
}

/** Check raw API text for MySQL error HTML and throw for retryOnDbError to catch */
fun checkDbError(text: String) {
    if (text.startsWith("<") && (text.contains("mysqli_connect") || text.contains("Too many connections") || text.contains("1040"))) {
        throw Exception("mysqli_connect(): Too many connections (detected in response body)")
    }
}

// ---------------------------------------------------------------------------
// NewTV shared infrastructure
// ---------------------------------------------------------------------------

val newTvBaseHeaders = mapOf(
    "Cache-Control" to "no-cache, no-store, must-revalidate",
    "Pragma"        to "no-cache",
    "Expires"       to "0",
    "X-Requested-With" to "NetmirrorNewTV v1.0",
    "User-Agent"    to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:136.0) Gecko/20100101 Firefox/136.0 /OS.GatuNewTV v1.0",
    "Accept"        to "application/json, text/plain, */*"
)

val newTvDomains = listOf(
    "aHR0cHM6Ly9tb2JpbGVkZXRlY3RzLmNvbQ==",
    "aHR0cHM6Ly9tb2JpbGVkZXRlY3QuYXBw",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LmFydA==",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LmNj",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LmNsaWNr",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0Lmluaw==",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LmxpdmU=",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LnBybw==",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LnNob3A=",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LnNpdGU=",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LnNwYWNl",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LnN0b3Jl",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0LnZpcA==",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0Lndpa2k=",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0Lnh5eg==",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0cy5hcnQ=",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0cy5jYw==",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0cy5pbmZv",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0cy5pbms=",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0cy5saXZl",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0cy5wcm8=",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0cy5zdG9yZQ==",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0cy50b3A=",
    "aHR0cHM6Ly9tb2JpZGV0ZWN0cy54eXo="
)

fun decodeBase64(value: String): String = String(Base64.decode(value, Base64.DEFAULT))

private var resolvedApiUrl: String = ""

suspend fun resolveApiUrl(): String {
    if (resolvedApiUrl.isNotBlank()) return resolvedApiUrl
    return withContext(Dispatchers.IO) {
        coroutineScope {
            val deferreds = newTvDomains.map { encoded ->
                async {
                    val base = decodeBase64(encoded).trimEnd('/')
                    try {
                        val response = app.get("$base/checknewtv.php", headers = newTvBaseHeaders)
                            .parsed<NewTvTokenResponse>()
                        val tokenHash = response.token_hash
                        if (!tokenHash.isNullOrBlank()) {
                            decodeBase64(tokenHash).trimEnd('/')
                        } else null
                    } catch (e: Exception) {
                        Log.d("NewTV", "Failed $base: ${e.message}")
                        null
                    }
                }
            }
            for (deferred in deferreds) {
                val result = deferred.await()
                if (result != null) {
                    resolvedApiUrl = result
                    Log.d("NewTV", "Resolved API URL: $resolvedApiUrl")
                    return@coroutineScope resolvedApiUrl
                }
            }
            throw Exception("Failed to resolve NewTV API base URL")
        }
    }
}

fun buildVerticalPosterUrl(id: String, ott: String = "nf"): String {
    return when (ott) {
        "pv" -> "https://imgcdn.kim/pv/v/$id.jpg"
        "hs" -> "https://imgcdn.kim/hs/v/$id.jpg"
        else -> "https://imgcdn.kim/poster/v/$id.jpg"
    }
}

fun buildBackgroundPosterUrl(id: String, ott: String = "nf"): String {
    return when (ott) {
        "pv" -> "https://imgcdn.kim/pv/h/$id.jpg"
        "hs" -> "https://imgcdn.kim/hs/h/$id.jpg"
        else -> "https://imgcdn.kim/poster/h/$id.jpg"
    }
}

fun buildVerticalPosterUrlWithProxy(id: String, ott: String = "nf"): String {
    val raw = buildVerticalPosterUrl(id, ott)
    return "https://wsrv.nl/?url=$raw&w=500"
}

fun buildNewTvHeaders(ott: String, extra: Map<String, String> = emptyMap()): Map<String, String> {
    val headers = newTvBaseHeaders.toMutableMap()
    headers["Ott"] = ott
    extra.forEach { (k, v) -> headers[k] = v }
    return headers
}

fun buildPosterUrl(template: String?, id: String): String? {
    if (template.isNullOrBlank()) return null
    return if (template.contains("------------------"))
        template.replace("------------------", id)
    else
        template.trimEnd('/') + "/" + id + ".jpg"
}

// ---------------------------------------------------------------------------
// Shared NewTV data classes
// ---------------------------------------------------------------------------

data class NewTvId(val id: String)

data class NewTvLoadData(val title: String, val id: String)

data class NewTvTokenResponse(val token_hash: String? = null)

data class NewTvMainResponse(
    val status: String? = null,
    val post: List<NewTvPostCategory>? = null,
    val imgcdn_h: String? = null,
    val imgcdn_v: String? = null,
    val img_referer: String? = null
)

data class NewTvPostCategory(
    val ids: String? = null,
    val cate: String? = null,
    val row: String? = null
)

data class NewTvSearchResponse(
    val searchResult: List<NewTvSearchResult>? = null,
    val detailsimgcdn: String? = null,
    val imgcdn: String? = null,
    val img_referer: String? = null
)

data class NewTvSearchResult(
    val id: String,
    val t: String
)

data class NewTvLang(
    val l: String? = null,
    val s: String? = null
)

data class NewTvPostResponse(
    val status: String? = null,
    val title: String? = null,
    val main_id: String? = null,
    val desc: String? = null,
    val year: String? = null,
    val genre: String? = null,
    val cast: String? = null,
    val match: String? = null,
    val runtime: String? = null,
    val episodes: List<NewTvApiEpisode?>? = null,
    val season: List<NewTvSeason>? = null,
    val nextPageSeason: String? = null,
    val nextPageShow: Int? = null,
    val type: String? = null,
    val main_poster: String? = null,
    val morelike_poster: String? = null,
    val ep_poster: String? = null,
    val suggest: List<NewTvSuggest>? = null,
    val age: String? = null,
    val certification: String? = null,
    val ua: String? = null,
    val moredetails: List<NewTvMoredetail>? = null,
    val lang: List<NewTvLang>? = null
)

data class NewTvApiEpisode(
    val id: String? = null,
    val t: String? = null,
    val ep: String? = null,
    val epNum: String? = null,
    val s: String? = null,
    val sNum: String? = null,
    val time: String? = null,
    val timeVal: String? = null,
    val ep_desc: String? = null,
    val info: List<String>? = null
)

data class NewTvSeason(
    val id: String? = null,
    val selected: Boolean? = null,
    val s: String? = null,
    val t: String? = null
)

data class NewTvSuggest(
    val id: String
)

data class NewTvMoredetail(
    val k: String? = null,
    val v: String? = null
)

data class NewTvEpisodesResponse(
    val episodes: List<NewTvApiEpisode>? = null,
    val nextPageShow: Int? = null
)

data class NewTvPlayerResponse(
    val status: String? = null,
    val video_link: String? = null,
    val referer: String? = null
)

// New playlist.php flow (play.php → playlist.php)

data class PlayHashResponse(
    val h: String? = null
)

data class PlaylistSource(
    val file: String? = null,
    val label: String? = null,
    val type: String? = null,
    val default: String? = null
)

data class PlaylistTrack(
    val kind: String? = null,
    val file: String? = null,
    val label: String? = null,
    val language: String? = null
)

data class PlaylistResponse(
    val title: String? = null,
    val image2: String? = null,
    val sources: List<PlaylistSource>? = null,
    val tracks: List<PlaylistTrack>? = null
)

val playPhpDomains = listOf("https://net11.cc", "https://net22.cc", "https://net52.cc")

// ---------------------------------------------------------------------------
// WebView-based postMessage cookie capture (user_token, t_hash_p, ott, hd)
// ---------------------------------------------------------------------------

private var pmWv: WebView? = null

private fun destroyPmWv() {
    try { pmWv?.destroy() } catch (_: Exception) {}
    pmWv = null
}

@SuppressLint("SetJavaScriptEnabled")
suspend fun capturePmCookiesViaWebView(
    loadUrl: String,
    existingCookie: String = ""
): Map<String, String> {
    val ctx = pluginContext ?: run {
        Log.e("PlayPhp", "capturePmCookiesViaWebView: pluginContext null")
        return emptyMap()
    }
    return withContext(Dispatchers.Main) {
        suspendCoroutine { cont ->
            var resumed = false
            val handler = Handler(Looper.getMainLooper())
            val timeoutRunnable = Runnable {
                if (!resumed) {
                    resumed = true
                    Log.d("PlayPhp", "PM WebView timeout")
                    cont.resume(emptyMap())
                }
            }
            handler.postDelayed(timeoutRunnable, 30000)
            try {
                destroyPmWv()
                CookieManager.getInstance().setAcceptCookie(true)
                if (existingCookie.isNotBlank()) {
                    existingCookie.split(";").forEach { part ->
                        val kv = part.trim().split("=", limit = 2)
                        if (kv.size == 2) CookieManager.getInstance().setCookie("https://net52.cc", "${kv[0]}=${kv[1]}")
                    }
                }
                val view = WebView(ctx.applicationContext).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    settings.cacheMode = WebSettings.LOAD_DEFAULT
                }
                pmWv = view
                val captured = mutableMapOf<String, String>()

                class PmJsBridge {
                    @android.webkit.JavascriptInterface
                    fun onPostMessage(json: String) {
                        if (resumed) return
                        try {
                            val obj = org.json.JSONObject(json)
                            for (key in listOf("user_token", "t_hash_p", "ott", "hd")) {
                                if (obj.has(key)) captured[key] = obj.getString(key)
                            }
                            if (captured.containsKey("user_token") && captured.containsKey("t_hash_p")) {
                                resumed = true
                                handler.removeCallbacksAndMessages(null)
                                destroyPmWv()
                                cont.resume(captured.toMap())
                            }
                        } catch (_: Exception) {}
                    }
                }

                view.addJavascriptInterface(PmJsBridge(), "PmBridge")

                view.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(v: WebView, u: String) {
                        // Check for err page and bail immediately
                        v.evaluateJavascript("document.body ? document.body.innerText.substring(0,200) : ''") { text ->
                            if (!resumed && text != null && (text.contains("err:") || text.contains("Page Not Found"))) {
                                Log.d("PlayPhp", "PM WebView err page detected, bailing")
                                resumed = true
                                handler.removeCallbacksAndMessages(null)
                                destroyPmWv()
                                cont.resume(emptyMap())
                                return@evaluateJavascript
                            }
                        }
                        if (resumed) return
                        v.evaluateJavascript("""
                            (function() {
                                var origPM = window.postMessage;
                                window.postMessage = function(msg, to, tr) {
                                    if (typeof msg === 'string' && msg.indexOf('=') > 0) {
                                        var eq = msg.indexOf('=');
                                        var key = msg.substring(0, eq);
                                        var val = msg.substring(eq + 1);
                                        if (key === 'user_token' || key === 't_hash_p' || key === 'ott' || key === 'hd') {
                                            var o = {};
                                            o[key] = val;
                                            if (window.PmBridge) PmBridge.onPostMessage(JSON.stringify(o));
                                        }
                                    }
                                    return origPM.call(window, msg, to, tr);
                                };
                                window.addEventListener('message', function(e) {
                                    if (typeof e.data === 'string' && e.data.indexOf('=') > 0) {
                                        var eq = e.data.indexOf('=');
                                        var key = e.data.substring(0, eq);
                                        var val = e.data.substring(eq + 1);
                                        if (key === 'user_token' || key === 't_hash_p' || key === 'ott' || key === 'hd') {
                                            var o = {};
                                            o[key] = val;
                                            if (window.PmBridge) PmBridge.onPostMessage(JSON.stringify(o));
                                        }
                                    }
                                });
                            })();
                        """.trimIndent(), null)

                        handler.post(object : Runnable {
                            var attempts = 0
                            override fun run() {
                                if (resumed) return
                                if (captured.containsKey("user_token") && captured.containsKey("t_hash_p")) {
                                    resumed = true
                                    handler.removeCallbacksAndMessages(null)
                                    destroyPmWv()
                                    cont.resume(captured.toMap())
                                    return
                                }
                                if (attempts++ > 20) {
                                    if (!resumed) {
                                        resumed = true
                                        handler.removeCallbacksAndMessages(null)
                                        destroyPmWv()
                                        Log.d("PlayPhp", "PM WebView poll exhausted. captured=$captured")
                                        cont.resume(captured.toMap())
                                    }
                                    return
                                }
                                handler.postDelayed(this, 500)
                            }
                        })
                    }
                }
                view.loadUrl(loadUrl)
            } catch (e: Exception) {
                Log.e("PlayPhp", "PM WebView error: ${e.message}")
                if (!resumed) { resumed = true; destroyPmWv(); cont.resume(emptyMap()) }
            }
        }
    }
}

suspend fun getPlaylistUrl(
    mainUrl: String,
    ott: String,
    id: String,
    title: String,
    cookie: String = "",
    apiBase: String? = null
): Pair<String, List<PlaylistTrack>>? {
    val domains = (listOf(mainUrl.trimEnd('/')) + playPhpDomains).distinct()

    var playHash: String? = null
    var timestamp: String = "0"
    var playDomain: String = ""

    for (domain in domains) {
        try {
            // net52.cc blocks same-origin — send net22.cc Referer/Origin (cross-origin)
            val bypassOrigin = if (domain.contains("net52")) "https://net22.cc" else domain
            val bypassReferer = if (domain.contains("net52")) "https://net22.cc/verify2" else "$domain/"
            val browserHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
                "Accept" to "*/*",
                "Referer" to bypassReferer,
                "Origin" to bypassOrigin,
                "Cookie" to cookie,
                "Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8"
            )
            val resp = app.post(
                "$domain/play.php",
                requestBody = FormBody.Builder()
                    .add("id", id)
                    .build(),
                headers = browserHeaders
            )
            val text = resp.text.trim()
            Log.d("PlayPhp", "Domain=$domain response=$text")
            if (text.startsWith("{")) {
                val parsed = tryParseJson<PlayHashResponse>(text)
                val h = parsed?.h
                if (!h.isNullOrBlank()) {
                    playHash = h.removePrefix("in=")
                    timestamp = playHash.split("::").getOrNull(2) ?: "0"
                    playDomain = domain
                    Log.d("PlayPhp", "Got hash=$playHash ts=$timestamp from $domain")
                    break
                }
            }
        } catch (e: Exception) {
            Log.w("PlayPhp", "Domain=$domain failed: ${e.message}")
        }
    }

    // Also try to extract a play hash from $mainUrl/home page (may contain net52.cc-style hash)
    try {
        val homeUrl = "${domains.first()}/home"
        Log.d("PlayPhp", "Trying to extract play hash from $homeUrl")
        val homeResp = app.get(homeUrl, headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
            "Accept" to "text/html,*/*",
            "Cookie" to cookie
        ))
        val body = homeResp.text
        Log.d("PlayPhp", "home page len=${body.length} first 500=${body.take(500)}")
        var h = Regex("""["']h["']\s*[:=]\s*["'](in=[^"']+)""").find(body)?.groupValues?.getOrNull(1)
        if (h.isNullOrBlank()) {
            h = Regex("""\bh=in=[a-f0-9:]{10,}""").find(body)?.value?.removePrefix("h=")
        }
        if (!h.isNullOrBlank()) {
            val homeHash = h.removePrefix("in=")
            // Prefer net52.cc-style hash (with ::ep::p:: format) over net11.cc hash (::ep::i::)
            if (homeHash.contains("::ep::p::") || playHash.isNullOrBlank()) {
                playHash = homeHash
                timestamp = playHash.split("::").getOrNull(2) ?: "0"
                playDomain = domains.first()
                Log.d("PlayPhp", "Got hash from home page=$playHash ts=$timestamp")
            } else {
                Log.d("PlayPhp", "Home page hash is ::ep::i:: format, keeping existing")
            }
        } else {
            Log.w("PlayPhp", "No play hash found in home page")
        }
    } catch (e: Exception) {
        Log.w("PlayPhp", "Home page extraction failed: ${e.message}")
    }

    if (playHash.isNullOrBlank()) {
        Log.w("PlayPhp", "All domains failed to get play hash")
        return null
    }

    try {
        // Send only the first 3 parts (token::hash::timestamp) to playlist.php
        // The extra ::ep::i:: from play.php is for internal use and shouldn't be sent
        val cleanHash = playHash.split("::").take(3).joinToString("::")
        val rawHash = "in=$playHash"

        // Base64 variants for the h= parameter (net52.cc iframe format)
        val b64raw = Base64.encodeToString(rawHash.toByteArray(), Base64.NO_WRAP)
        val b64rawUrl = Base64.encodeToString(rawHash.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
        val b64play = Base64.encodeToString(playHash.toByteArray(), Base64.NO_WRAP)
        val b64clean = Base64.encodeToString(cleanHash.toByteArray(), Base64.NO_WRAP)
        // Try ::ep::p:: format (what playlist.php returns with proper auth)
        val parts = playHash.split("::")
        val first3 = parts.take(3)
        val playHashP = first3.joinToString("::") + "::ep::p::" + parts.getOrElse(1) { "" }  // TOKEN2 as placeholder TOKEN3
        val playHashPEnd = first3.joinToString("::") + "::ep::p::"  // no TOKEN3
        val b64playP = Base64.encodeToString(playHashP.toByteArray(), Base64.NO_WRAP)
        val b64playPEnd = Base64.encodeToString(playHashPEnd.toByteArray(), Base64.NO_WRAP)

        // Extract postMessage cookies from play.php HTML (user_token, t_hash_p, ott)
        // These are set via JavaScript postMessage, not HTTP headers.
        // Strategy: fetch the play.php HTML page with id&in params → find iframe src
        // → fetch iframe (net52.cc/play.php?h=...) → extract postMessage data
        val pmCookies = mutableMapOf<String, String>()
        val pmTryDomains = (listOf(mainUrl.trimEnd('/')) + playPhpDomains).distinct()
        data class PmVariant(val label: String, val param: String, val value: String)
        val pmVariants = listOf(
            PmVariant("in=3part", "in", cleanHash),
            PmVariant("in=5part", "in", playHash),
            PmVariant("h=5part", "h", playHash),
            PmVariant("h=b64raw", "h", b64raw),
            PmVariant("h=b64raw_urlsafe", "h", b64rawUrl),
            PmVariant("h=b64play", "h", b64play),
            PmVariant("h=b64clean", "h", b64clean),
            PmVariant("in=p_part", "in", playHashP),
            PmVariant("h=b64playP", "h", b64playP),
            PmVariant("in=p_end", "in", playHashPEnd),
            PmVariant("h=b64playPEnd", "h", b64playPEnd)
        )
        for (tryDomain in pmTryDomains) {
            // net52.cc blocks requests from same origin — it expects Referer/Origin from net22.cc
            val pmOrigin = if (tryDomain.contains("net52")) "https://net22.cc" else tryDomain
            val pmReferer = if (tryDomain.contains("net52")) "https://net22.cc/play.php?id=$id" else "$tryDomain/"
            // Try with just id (no hash) – page may self-generate
            try {
                val noHashUrl = "$tryDomain/play.php?id=$id"
                Log.d("PlayPhp", "PM trying no-hash on $tryDomain: $noHashUrl")
                val noHashResp = app.get(noHashUrl, headers = mapOf(
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36",
                    "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                    "Origin" to pmOrigin,
                    "Referer" to pmReferer,
                    "Cookie" to cookie
                ))
                val nb = noHashResp.text
                Log.d("PlayPhp", "PM no-hash body len=${nb.length} start=${nb.take(200)}")
                val iframeSrc = Regex("""<iframe[^>]+src="([^"]+)"[^>]*>""").find(nb)?.groupValues?.getOrNull(1)
                if (!iframeSrc.isNullOrBlank()) {
                    Log.d("PlayPhp", "PM no-hash iframe: $iframeSrc")
                    val iframeUrl = if (iframeSrc.startsWith("http")) iframeSrc else "$tryDomain$iframeSrc"
                    val iframeResp = app.get(iframeUrl, headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36",
                        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                        "Referer" to noHashUrl,
                        "Cookie" to cookie
                    ))
                    Regex("""parent\.postMessage\(\s*"([^"=]+)=([^"]*)"\s*,\s*"\*"\s*\)""").findAll(iframeResp.text).forEach { match ->
                        val k = match.groupValues[1]; val v = match.groupValues[2]
                        if (k in setOf("user_token", "t_hash_p", "ott", "hd")) { pmCookies[k] = v; Log.d("PlayPhp", "PM cookie from no-hash iframe: $k=$v") }
                    }
                }
                Regex("""parent\.postMessage\(\s*"([^"=]+)=([^"]*)"\s*,\s*"\*"\s*\)""").findAll(nb).forEach { match ->
                    val k = match.groupValues[1]; val v = match.groupValues[2]
                    if (k in setOf("user_token", "t_hash_p", "ott", "hd")) { if (!pmCookies.containsKey(k)) pmCookies[k] = v; Log.d("PlayPhp", "PM cookie from no-hash inline: $k=$v") }
                }
            } catch (e: Exception) {
                Log.w("PlayPhp", "PM no-hash failed ($tryDomain): ${e.message}")
            }
            if (pmCookies.isNotEmpty()) break

            // Also try with hash params
            for (pmV in pmVariants) {
                try {
                    val pmUrl = "$tryDomain/play.php?id=$id&${pmV.param}=${pmV.value}"
                    Log.d("PlayPhp", "PM cookies trying $pmV.label on $tryDomain: $pmUrl")
                    val pmResp = app.get(pmUrl, headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36",
                        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                        "Origin" to pmOrigin,
                        "Referer" to pmReferer,
                        "Cookie" to cookie
                    ))
                    val body = pmResp.text
                    Log.d("PlayPhp", "PM $pmV.label body len=${body.length} start=${body.take(200)}")

                    // Find iframe src (points to net52.cc/play.php?h=...)
                    val iframeSrc = Regex("""<iframe[^>]+src="([^"]+)"[^>]*>""").find(body)?.groupValues?.getOrNull(1)
                    if (!iframeSrc.isNullOrBlank()) {
                        val iframeUrl = if (iframeSrc.startsWith("http")) iframeSrc else "$tryDomain$iframeSrc"
                        Log.d("PlayPhp", "PM iframe found: $iframeUrl")
                        val iframeResp = app.get(iframeUrl, headers = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36",
                            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                            "Referer" to pmUrl,
                            "Cookie" to cookie
                        ))
                        Regex("""parent\.postMessage\(\s*"([^"=]+)=([^"]*)"\s*,\s*"\*"\s*\)""").findAll(iframeResp.text).forEach { match ->
                            val k = match.groupValues[1]
                            val v = match.groupValues[2]
                            if (k in setOf("user_token", "t_hash_p", "ott", "hd")) {
                                pmCookies[k] = v
                                Log.d("PlayPhp", "PM cookie from iframe: $k=$v")
                            }
                        }
                    }

                    // Also check the main page for inline postMessage
                    Regex("""parent\.postMessage\(\s*"([^"=]+)=([^"]*)"\s*,\s*"\*"\s*\)""").findAll(body).forEach { match ->
                        val k = match.groupValues[1]
                        val v = match.groupValues[2]
                        if (k in setOf("user_token", "t_hash_p", "ott", "hd")) {
                            if (!pmCookies.containsKey(k)) pmCookies[k] = v
                            Log.d("PlayPhp", "PM cookie inline: $k=$v")
                        }
                    }

                    if (pmCookies.isNotEmpty()) break
                } catch (e: Exception) {
                    Log.w("PlayPhp", "PM cookies failed ($pmV.label on $tryDomain): ${e.message}")
                }
            }
            if (pmCookies.isNotEmpty()) break
        }

        // WebView fallback: when HTTP PM extraction failed, try loading play.php in a real WebView
        // The parent page (net22.cc/play.php?id=X) renders an iframe → net52.cc/play.php?h=BASE64(hash)
        // Loading the parent page lets the page build the iframe URL naturally, with correct Referer.
        if (pmCookies.isEmpty() && pluginContext != null) {
            val encodedRawHash = java.net.URLEncoder.encode(rawHash, "UTF-8")
            val wvUrls = listOf(
                "https://net22.cc/play.php?id=$id",
                "https://net11.cc/play.php?id=$id",
                "https://net52.cc/play.php?id=$id",
                "https://net52.cc/play.php?h=$b64rawUrl",
                "https://net52.cc/play.php?h=$encodedRawHash",
                "https://net52.cc/play.php?h=$b64playP",           // ::ep::p::TOKEN2 format
                "https://net52.cc/play.php?h=$b64playPEnd"      // ::ep::p:: format (empty TOKEN3)
            )
            for (wvUrl in wvUrls) {
                Log.d("PlayPhp", "PM WebView trying: $wvUrl")
                val wvResult = capturePmCookiesViaWebView(wvUrl, cookie)
                if (wvResult.isNotEmpty()) {
                    pmCookies.putAll(wvResult)
                    Log.d("PlayPhp", "PM WebView got cookies: $wvResult")
                    break
                }
            }
        }

        // Merge postMessage cookies into existing cookie string
        // Also inject default values when extraction failed (server may check existence)
        val tHashT = cookie.split(";").map { it.trim() }.firstOrNull { it.startsWith("t_hash_t=") }?.substringAfter("=") ?: ""
        val defaultPmCookies = mapOf(
            "ott" to ott,
            "user_token" to id,
            "t_hash_p" to tHashT
        )
        val mergedPmCookies = if (pmCookies.isNotEmpty()) pmCookies else defaultPmCookies
        val cMap = mutableMapOf<String, String>()
        cookie.split(";").forEach { part ->
            val kv = part.trim().split("=", limit = 2)
            if (kv.size == 2 && kv[0].isNotBlank()) cMap[kv[0]] = kv[1]
        }
        cMap.putAll(mergedPmCookies)
        val mergedCookie = cMap.map { "${it.key}=${it.value}" }.joinToString("; ")

        // Try multiple playlist.php request formats
        val playlistDomains = listOf("https://net52.cc", playDomain, mainUrl.trimEnd('/')).distinct()
        data class PlVariant(val param: String, val value: String)
        val playlistVariants = listOf(
            PlVariant("h", cleanHash),     // h=3-part (current approach)
            PlVariant("h", playHash),      // h=5-part with ::ep::i::
            PlVariant("h", rawHash),       // h=in=5-part (raw from API)
            PlVariant("in", cleanHash),    // in=3-part
            PlVariant("in", playHash),     // in=5-part with ::ep::i::
            PlVariant("h", playHashP),     // h=::ep::p::TOKEN2 (placeholder)
            PlVariant("in", playHashP),    // in=::ep::p::TOKEN2 (placeholder)
            PlVariant("h", playHashPEnd),  // h=::ep::p:: (empty TOKEN3)
            PlVariant("in", playHashPEnd)  // in=::ep::p:: (empty TOKEN3)
        )
        var foundSource: Pair<String, List<PlaylistTrack>>? = null
        var weakFallback: Pair<String, List<PlaylistTrack>>? = null   // ::ep::99 format
        outer@ for (plDomain in playlistDomains) {
            for (variant in playlistVariants) {
                try {
                    val plUrl = "$plDomain/playlist.php?id=$id&t=$title&tm=$timestamp&${variant.param}=${variant.value}"
                    Log.d("PlayPhp", "Trying playlist: $plUrl")
                    val plResp = app.get(plUrl, headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36",
                        "Accept" to "*/*",
                        "X-Requested-With" to "NetmirrorNewTV v1.0",
                        "Origin" to plDomain,
                        "Referer" to "$plDomain/play.php?id=$id&${variant.param}=${variant.value}",
                        "Cookie" to mergedCookie
                    ))
                    val body = plResp.text
                    Log.d("PlayPhp", "playlist response len=${body.length} body=${body.take(300)}")
                    val parsed = tryParseJsonList<PlaylistResponse>(body)
                    val first = parsed?.firstOrNull()
                    var sourceFile = first?.sources?.firstOrNull()?.file
                    val tracks = first?.tracks.orEmpty()
                    if (!sourceFile.isNullOrBlank() && !sourceFile.contains("unknown")) {
                        var m3u8Url = if (sourceFile.startsWith("http")) sourceFile
                                      else "${plDomain.trimEnd('/')}/${sourceFile.removePrefix("/")}"
                        Log.d("PlayPhp", "M3U8 url=$m3u8Url tracks=${tracks.size} (domain=$plDomain ${variant.param}=${variant.value})")
                        Log.e("PLAYURL", m3u8Url)
                        if (m3u8Url.contains("::ep::99")) {
                            // ::ep::99 is degraded — store as weak fallback, keep checking
                            weakFallback = Pair(m3u8Url, tracks)
                            Log.d("PlayPhp", "::ep::99 saved as weak fallback, trying API player.php...")
                        } else {
                            foundSource = Pair(m3u8Url, tracks)
                            break@outer
                        }
                    }
                } catch (e: Exception) {
                    Log.w("PlayPhp", "playlist attempt failed ($plDomain ${variant.param}=${variant.value}): ${e.message}")
                }
            }
        }
        // Try direct M3U8 URL (curl example pattern) as fallback — bypass playlist.php
        for (plDomain in playlistDomains) {
            for (variant in listOf(PlVariant("in", cleanHash), PlVariant("in", playHash), PlVariant("in", playHashP))) {
                try {
                    val m3u8Url = "$plDomain/hls/$id.m3u8?${variant.param}=${variant.value}"
                    Log.d("PlayPhp", "Trying direct M3U8: $m3u8Url")
                    val m3u8Resp = app.get(m3u8Url, headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
                        "Accept" to "*/*",
                        "Referer" to "$plDomain/play.php?id=$id",
                        "Cookie" to mergedCookie
                    ))
                    val body = m3u8Resp.text
                    Log.d("PlayPhp", "Direct M3U8 len=${body.length} start=${body.take(200)}")
                    if (body.startsWith("#EXTM3U") && !body.contains("unknown")) {
                        Log.d("PlayPhp", "Direct M3U8 SUCCESS: $m3u8Url")
                        Log.e("PLAYURL", m3u8Url)
                        foundSource = Pair(m3u8Url, emptyList())
                        break
                    }
                } catch (e: Exception) {
                    Log.w("PlayPhp", "Direct M3U8 failed ($plDomain): ${e.message}")
                }
            }
            if (foundSource != null) break
        }
        // Try API player.php with the hash - only if we have NO valid source yet (don't overwrite direct M3U8)
        if (apiBase != null && foundSource == null) {
            // Try multiple hash formats: clean 3-part, full 5-part, ::ep::p:: format
            val apiHashVariants = listOf(
                "clean" to cleanHash,
                "full" to playHash,
                "p_part" to playHashP
            )
            val playerHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
                "Accept" to "application/json, text/plain, */*",
                "Ott" to ott,
                "Usertoken" to "",
                "Referer" to "https://net52.cc",
                "Cookie" to cookie
            )
            for ((hLabel, hVal) in apiHashVariants) {
                for (plParam in listOf("h", "in")) {
                    try {
                        val playerUrl = "$apiBase/newtv/player.php?id=$id&$plParam=${java.net.URLEncoder.encode(hVal, "UTF-8")}"
                        Log.d("PlayPhp", "Trying API player.php ($hLabel $plParam): $playerUrl")
                        val playerResp = app.get(playerUrl, headers = playerHeaders)
                        val parsed = tryParseJson<NewTvPlayerResponse>(playerResp.text)
                        if (parsed != null && (parsed.status == "ok" || parsed.status == "otp") && !parsed.video_link.isNullOrBlank()) {
                            Log.d("PlayPhp", "API player.php SUCCESS: ${parsed.video_link}")
                            Log.e("PLAYURL", parsed.video_link)
                            foundSource = Pair(parsed.video_link, emptyList())
                            break
                        } else {
                            Log.d("PlayPhp", "API player.php no valid source ($hLabel $plParam): status=${parsed?.status} link=${parsed?.video_link}")
                        }
                    } catch (e: Exception) {
                        Log.w("PlayPhp", "API player.php failed ($hLabel $plParam): ${e.message}")
                    }
                }
                if (foundSource != null) break
            }
        }
        // Use API result if found, otherwise fall back to ::ep::99
        if (foundSource != null) return foundSource
        if (weakFallback != null) {
            Log.d("PlayPhp", "Using ::ep::99 fallback: ${weakFallback.first}")
            Log.e("PLAYURL", weakFallback.first)
            foundSource = weakFallback
        }
        if (foundSource != null) return foundSource
    } catch (e: Exception) {
        Log.w("PlayPhp", "playlist.php failed: ${e.message}")
    }

    return null
}