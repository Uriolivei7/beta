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
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
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
        // Force re-auth if the cookie has ::ep::99 (degraded)
        if (savedCookie.contains("::ep::99") || savedCookie.contains("%3A%3Aep%3A%3A99")) {
            Log.d("bypass", "Cached cookie has degraded ::ep::99, forcing re-auth")
            NetflixMirrorStorage.clearCookie()
        } else {
            Log.d("bypass", "Using cached cookie (${savedCookie.take(100)}...) ts=${savedTimestamp}")
            return savedCookie
        }
    } else {
        NetflixMirrorStorage.clearCookie()
    }

    val allCookies = mutableMapOf<String, String>()
    fun captureCookies(resp: okhttp3.Response) {
        resp.headers("Set-Cookie").forEach { raw ->
            val name = raw.substringBefore("=")
            val value = raw.substringAfter("=").substringBefore(";")
            if (name.isNotBlank()) allCookies[name] = value
        }
    }

    val androidUA = "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0"
    val browserHeaders = mapOf(
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        "Accept-Language" to "en-IN,en-US;q=0.9,en;q=0.8",
        "User-Agent" to androidUA,
        "Connection" to "keep-alive",
        "X-Requested-With" to "app.netmirror.netmirrornew",
        "sec-ch-ua" to "\"Android WebView\";v=\"149\", \"Chromium\";v=\"149\", \"Not)A;Brand\";v=\"24\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Android\"",
        "Cache-Control" to "max-age=0"
    )

    val client = app.baseClient.newBuilder()
        .followRedirects(false)
        .followSslRedirects(false)
        .build()

    // Step 1: visit $mainUrl/home → redirects to net22.cc/verify2 (or similar)
    // Extract addhash from the redirect Location BEFORE following to dead net22.cc
    var addhash = ""
    var currentUrl = "$mainUrl/home"
    try {
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
                    // Extract addhash from redirect URL before following redirect
                    if (addhash.isBlank()) {
                        addhash = Regex("""[?&]addhash=([^&]+)""").find(location)?.groupValues?.getOrNull(1).orEmpty()
                        if (addhash.isNotBlank()) Log.d("bypass", "Extracted addhash from redirect URL: ${addhash.take(20)}")
                    }
                    // Only follow redirect if target host is resolvable (skip dead net22.cc)
                    val targetHost = Regex("https://([^/]+)").find(location)?.groupValues?.getOrNull(1).orEmpty()
                    if (targetHost.contains("net22.cc")) {
                        Log.d("bypass", "Skipping redirect to dead host $targetHost (addhash already extracted: ${addhash.take(20)})")
                        break
                    }
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
    // If addhash is empty (Cloudflare blocked us), try WebView fallback
    if (addhash.isBlank()) {
        val wvUrl = "$mainUrl/home" // start the same redirect chain in WebView
        Log.d("bypass", "addhash empty, trying WebView fallback on $wvUrl")
        val wvAddhash = captureAddhashViaWebView(wvUrl)
        if (!wvAddhash.isNullOrBlank()) {
            addhash = wvAddhash
            Log.d("bypass", "WebView extracted addhash: ${addhash.take(20)}")
        } else {
            Log.d("bypass", "WebView fallback also failed to get addhash")
        }
    }
    Log.d("bypass", "addhash=${addhash.take(20)} cookies so far=$allCookies")

    // Step 2: POST to verify.php with fake recaptcha
    // Determine correct Referer/Origin from the redirect target (now net11.cc, not net22.cc)
    val verifyHost = Regex("https://([^/]+)/verify2").find(currentUrl)?.groupValues?.getOrNull(1) ?: "net22.cc"
    Log.d("bypass", "verify2 host resolved as: $verifyHost")

    for (count in 0..3) {
        try {
            val verifyHeaders = mapOf(
                "User-Agent" to androidUA,
                "Accept" to "*/*",
                "Accept-Language" to "en-IN,en-US;q=0.9,en;q=0.8",
                "Origin" to "https://$verifyHost",
                "Referer" to "https://$verifyHost/verify2",
                "Content-Type" to "application/x-www-form-urlencoded",
                "X-Requested-With" to "app.netmirror.netmirrornew",
                "sec-ch-ua" to "\"Android WebView\";v=\"149\", \"Chromium\";v=\"149\", \"Not)A;Brand\";v=\"24\"",
                "sec-ch-ua-mobile" to "?0",
                "sec-ch-ua-platform" to "\"Android\"",
                "Cache-Control" to "max-age=0"
            )
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
            // Use a separate client — follow redirects may hit dead domains, capture cookies from first response
            val verifyClient = app.baseClient.newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build()
            verifyClient.newCall(request).execute().use { resp ->
                captureCookies(resp)
                val respBody = resp.body?.string().orEmpty()
                Log.d("bypass", "verify.php status=${resp.code} cookies=$allCookies body=${respBody.take(500)}")
                // If the cookie has ::ep::99, we still accept it but tag for replacement later
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
    "X-Requested-With" to "app.netmirror.netmirrornew",
    "User-Agent"    to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/149.0.7827.91 Safari/537.36 /OS.Gatu v3.0",
    "Accept"        to "application/json, text/plain, */*",
    "Accept-Language" to "en-IN,en-US;q=0.9,en;q=0.8",
    "sec-ch-ua"     to "\"Android WebView\";v=\"149\", \"Chromium\";v=\"149\", \"Not)A;Brand\";v=\"24\"",
    "sec-ch-ua-mobile" to "?0",
    "sec-ch-ua-platform" to "\"Android\""
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
            resolvedApiUrl = "https://net52.cc"
            Log.d("NewTV", "All checknewtv domains failed, falling back to $resolvedApiUrl")
            return@coroutineScope resolvedApiUrl
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

fun m3u8CdnFixInterceptor(): Interceptor {
    return Interceptor { chain ->
        val req = chain.request()
        val url = req.url.toString()
        val resp: Response
        try {
            resp = chain.proceed(req)
        } catch (e: Exception) {
            val cdnHost = Regex("https://([^/]+)/").find(url)?.groupValues?.get(1).orEmpty()
            if (cdnHost.contains("nm-cdn") || cdnHost.contains("imgcdn")) {
                Log.d("CdnFix", "CDN unreachable: $url - ${e.message}")
            }
            throw e
        }
        if (url.contains(".m3u8")) {
            val body = resp.body?.string() ?: return@Interceptor resp
            if (!body.startsWith("#EXT")) {
                Log.d("CdnFix", "M3U8 NOT valid: $url status=${resp.code} len=${body.length}")
                return@Interceptor resp
            }
            val cdnRegex = Regex("https://([^/\"]+)/files/")
            val allCdns = cdnRegex.findAll(body).map { it.groupValues[1] }.toSet()
            val hasBroken = body.contains("https:///files/")
            if (allCdns.isEmpty() && !hasBroken) return@Interceptor resp
            // Replace CDN domains and /files/ → /hls/ (tv.imgcdn.kim uses /hls/, not /files/)
            val fixed = body
                .replace("https:///files/", "https://tv.imgcdn.kim/hls/")
                .let { b ->
                    allCdns.fold(b) { acc, cdn ->
                        acc.replace("https://$cdn/files/", "https://tv.imgcdn.kim/hls/")
                    }
                }
            if (fixed != body) {
                Log.d("CdnFix", "Rerouted CDN->tv.imgcdn.kim/hls in M3U8: $url")
                val fixedBody = ResponseBody.create(resp.body?.contentType(), fixed)
                return@Interceptor resp.newBuilder().body(fixedBody).build()
            }
        }
        resp
    }
}

fun fixM3u8Cdn(body: String): String? {
    val cdnRegex = Regex("https://([^/\"]+)/files/")
    val cdn = cdnRegex.find(body)?.groupValues?.get(1)
        ?: return null
    val fixed = body.replace("https:///files/", "https://$cdn/files/")
    return if (fixed != body) fixed else null
}

// ---------------------------------------------------------------------------
// WebView-based addhash extraction (for Cloudflare-bypassed verify2 page)
// ---------------------------------------------------------------------------

private var addhashWv: WebView? = null

@SuppressLint("SetJavaScriptEnabled")
suspend fun captureAddhashViaWebView(loadUrl: String, timeoutMs: Long = 20000): String? {
    val ctx = pluginContext ?: run {
        Log.e("bypass", "captureAddhashViaWebView: pluginContext null")
        return null
    }
    return withContext(Dispatchers.Main) {
        suspendCoroutine { cont ->
            var resumed = false
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (!resumed) { resumed = true; cont.resume(null) }
            }, timeoutMs)
            try {
                CookieManager.getInstance().setAcceptCookie(true)
                val view = WebView(ctx.applicationContext).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    settings.cacheMode = WebSettings.LOAD_DEFAULT
                }
                addhashWv = view
                view.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(v: WebView, u: String) {
                        if (resumed) return
                        // Poll for addhash — page may update after Cloudflare challenge
                        handler.post(object : Runnable {
                            var attempts = 0
                            override fun run() {
                                if (resumed) return
                                v.evaluateJavascript("""
                                    (function() {
                                        var el = document.querySelector('input[name=addhash]');
                                        if (el) return el.value;
                                        var m = document.body ? document.body.innerText.match(/addhash['"]?\s*[:=]\s*['"]([^'"]+)/i) : null;
                                        if (m) return m[1];
                                        return '';
                                    })()
                                """.trimIndent()) { result ->
                                    if (resumed) return@evaluateJavascript
                                    val r = result?.trim('"')?.trim()
                                    if (r != null && r != "" && r != "null" && r.length > 4) {
                                        resumed = true
                                        handler.removeCallbacksAndMessages(null)
                                        cont.resume(r)
                                        return@evaluateJavascript
                                    }
                                    if (attempts++ > 40) {
                                        if (!resumed) { resumed = true; cont.resume(null) }
                                    } else {
                                        handler.postDelayed(this, 500)
                                    }
                                }
                            }
                        })
                    }
                }
                view.loadUrl(loadUrl)
            } catch (e: Exception) {
                Log.e("bypass", "captureAddhashViaWebView error: ${e.message}")
                if (!resumed) { resumed = true; cont.resume(null) }
            }
        }
    }
}

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
    for (domain in domains) {
        try {
            val resp = app.post(
                "$domain/play.php",
                requestBody = FormBody.Builder().add("id", id).build(),
                headers = mapOf(
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
                    "Accept" to "*/*",
                    "Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8"
                )
            )
            val text = resp.text.trim()
            Log.d("PlayPhp", "Domain=$domain response=$text")
            if (text.startsWith("{")) {
                val parsed = tryParseJson<PlayHashResponse>(text)
                val h = parsed?.h
                if (!h.isNullOrBlank()) {
                    playHash = h.removePrefix("in=")
                    timestamp = playHash.split("::").getOrNull(2) ?: "0"
                    Log.d("PlayPhp", "Got hash=$playHash ts=$timestamp from $domain")
                    break
                }
            }
        } catch (e: Exception) {
            Log.w("PlayPhp", "Domain=$domain failed: ${e.message}")
        }
    }
    if (playHash.isNullOrBlank()) {
        Log.w("PlayPhp", "All domains failed to get play hash")
        return null
    }
    val cleanHash = playHash.split("::").take(3).joinToString("::")
    val hlsDomains = listOf("https://net52.cc", "https://net11.cc").distinct()
    var tracks: List<PlaylistTrack> = emptyList()
    var sourceUrl: String? = null
    // Try playlist.php with cookie + cross-origin headers for hash exchange
    val plHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36",
        "Accept" to "*/*",
        "X-Requested-With" to "NetmirrorNewTV v1.0",
        "Referer" to "https://net22.cc/play.php?id=$id",
        "Origin" to "https://net22.cc"
    ) + if (cookie.isNotBlank()) mapOf("Cookie" to cookie) else emptyMap()
    val m3u8Headers = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
        "Accept" to "*/*"
    ) + if (cookie.isNotBlank()) mapOf("Cookie" to cookie) else emptyMap()
    for (hlsDomain in hlsDomains) {
        try {
            val plResp = app.get(
                "$hlsDomain/playlist.php?id=$id&t=$title&tm=$timestamp&h=$cleanHash",
                headers = plHeaders
            )
            Log.d("PlayPhp", "playlist.php $hlsDomain raw=${plResp.text.take(300)}")
            val parsed = tryParseJsonList<PlaylistResponse>(plResp.text)
            val first = parsed?.firstOrNull()
            if (first != null) {
                tracks = first.tracks.orEmpty()
                val src = first.sources?.firstOrNull()?.file
                if (!src.isNullOrBlank()) {
                    sourceUrl = if (src.startsWith("http")) src else "${hlsDomain}${src}"
                }
            }
            Log.d("PlayPhp", "Playlist $hlsDomain tracks=${tracks.size} sourceUrl=$sourceUrl")
            if (sourceUrl != null) {
                val body = app.get(sourceUrl, headers = m3u8Headers).text
            if (body.startsWith("#EXTM3U") && !body.contains("unknown::ep")) {
                Log.d("PlayPhp", "Source M3U8 OK: $sourceUrl (len=${body.length})")
                Log.d("PlayPhp", "Source M3U8 body: ${body.take(1500)}")
                Log.e("PLAYURL", sourceUrl)
                return Pair(sourceUrl, tracks)
            }
                Log.d("PlayPhp", "Source M3U8 body invalid: ${body.take(200)}")
                sourceUrl = null
            }
        } catch (e: Exception) {
            Log.w("PlayPhp", "playlist.php $hlsDomain failed: ${e.message}")
        }
    }
    // Fallback: try direct M3U8 with 3-part hash on all HLS domains
    for (hlsDomain in hlsDomains) {
        try {
            val m3u8Url = "$hlsDomain/hls/$id.m3u8?in=$cleanHash"
            Log.d("PlayPhp", "Trying direct M3U8: $m3u8Url")
            val body = app.get(m3u8Url, headers = m3u8Headers).text
            Log.d("PlayPhp", "Direct M3U8 len=${body.length} start=${body.take(200)}")
            if (body.startsWith("#EXTM3U") && !body.contains("unknown::ep")) {
                Log.d("PlayPhp", "Direct M3U8 OK: $m3u8Url (len=${body.length})")
                Log.d("PlayPhp", "Direct M3U8 body: ${body.take(1500)}")
                Log.e("PLAYURL", m3u8Url)
                return Pair(m3u8Url, tracks)
            }
        } catch (e: Exception) {
            Log.w("PlayPhp", "Direct M3U8 failed ($hlsDomain): ${e.message}")
        }
    }
    return null
}