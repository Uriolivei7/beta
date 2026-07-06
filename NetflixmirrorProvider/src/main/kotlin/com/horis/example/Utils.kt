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
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import android.content.Context
import android.content.SharedPreferences
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.util.UUID

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
// Auth bypass – gets token_hash from verify.php on resolved API domain
// ---------------------------------------------------------------------------

suspend fun bypass(mainUrl: String): String {
    val (savedCookie, savedTimestamp) = NetflixMirrorStorage.getCookie()
    if (!savedCookie.isNullOrEmpty() && System.currentTimeMillis() - savedTimestamp < 54_000_000) {
        Log.d("bypass", "Using cached cookie ts=${savedTimestamp}")
        return savedCookie
    }
    NetflixMirrorStorage.clearCookie()

    val apiBase = resolveApiUrl()
    val androidUA = newTvBaseHeaders["User-Agent"].orEmpty()

    // NewTv verify on resolved API (mobiledetects domain or net52.cc fallback)
    try {
        val formBody = FormBody.Builder()
            .add("g-recaptcha-response", UUID.randomUUID().toString())
            .build()
        val resp = app.post(
            "$apiBase/verify.php",
            requestBody = formBody,
            headers = newTvBaseHeaders + mapOf(
                "Content-Type" to "application/x-www-form-urlencoded",
                "Origin" to mainUrl,
                "Referer" to "$mainUrl/verify"
            )
        )
        val text = resp.text
        Log.d("bypass", "Verify status=${resp.code} body=${text.take(200)}")
        val parsed = tryParseJson<NewTvTokenResponse>(text)
        if (parsed?.token_hash != null && parsed.token_hash.length > 10) {
            NetflixMirrorStorage.saveCookie(parsed.token_hash)
            Log.d("bypass", "Got token_hash: ${parsed.token_hash.take(60)}")
            return parsed.token_hash
        }
        val tHashCandidates = resp.headers.values("Set-Cookie")
        val tHash = tHashCandidates.firstOrNull { it.startsWith("t_hash_t=") }
            ?.substringAfter("=")?.substringBefore(";")
        if (tHash != null) {
            val cookieStr = "t_hash_t=$tHash; hd=on"
            NetflixMirrorStorage.saveCookie(cookieStr)
            Log.d("bypass", "Got t_hash_t: $cookieStr")
            return cookieStr
        }
    } catch (e: Exception) {
        Log.w("bypass", "Verify on $apiBase failed: ${e.message}")
    }

    // Fallback: direct verify on mainUrl (net52.cc etc)
    try {
        val formBody = FormBody.Builder()
            .add("g-recaptcha-response", UUID.randomUUID().toString())
            .build()
        val resp = app.post(
            "$mainUrl/verify.php",
            requestBody = formBody,
            headers = mapOf(
                "User-Agent" to androidUA,
                "Accept" to "*/*",
                "Origin" to "https://net22.cc",
                "Referer" to "https://net22.cc/verify2",
                "Content-Type" to "application/x-www-form-urlencoded",
                "X-Requested-With" to "app.netmirror.netmirrornew"
            )
        )
        val tHashCandidates = resp.headers.values("Set-Cookie")
        val tHash = tHashCandidates.firstOrNull { it.startsWith("t_hash_t=") }
            ?.substringAfter("=")?.substringBefore(";")
        if (tHash != null) {
            val cookieStr = "t_hash_t=$tHash; hd=on"
            NetflixMirrorStorage.saveCookie(cookieStr)
            Log.d("bypass", "Fallback t_hash_t: $cookieStr")
            return cookieStr
        }
    } catch (e: Exception) {
        Log.w("bypass", "Fallback verify failed: ${e.message}")
    }

    throw Exception("Failed to get auth token")
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
                        Log.d("NewTV", "checknewtv.php failed $base: ${e.message}")
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
            // Fallback: try pinging domain roots directly
            val pingDeferreds = newTvDomains.map { encoded ->
                async {
                    val base = decodeBase64(encoded).trimEnd('/')
                    try {
                        app.get(base, headers = newTvBaseHeaders)
                        base
                    } catch (_: Exception) { null }
                }
            }
            for (d in pingDeferreds) {
                val r = d.await()
                if (r != null) {
                    resolvedApiUrl = r
                    Log.d("NewTV", "Ping-resolved API URL: $resolvedApiUrl")
                    return@coroutineScope resolvedApiUrl
                }
            }
            resolvedApiUrl = "https://net52.cc"
            Log.d("NewTV", "All domains failed, falling back to $resolvedApiUrl")
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
        Log.d("CdnFix", "Interceptor firing for: $url")
        val resp: Response
        try {
            resp = chain.proceed(req)
        } catch (e: Exception) {
            val cdnHost = Regex("https://([^/]+)/").find(url)?.groupValues?.get(1).orEmpty()
            if (cdnHost.contains("nm-cdn") || cdnHost.contains("imgcdn") || cdnHost.contains("freecdn")) {
                Log.d("CdnFix", "CDN unreachable: $url - ${e.message}")
            }
            throw e
        }
        val ct = (resp.body?.contentType()?.toString() ?: "")
        if (url.contains(".m3u8") || ct.contains("mpegurl") || ct.contains("vnd.apple.mpegurl")) {
            val body = resp.body?.string() ?: return@Interceptor resp
            if (!body.startsWith("#EXT")) {
                Log.d("CdnFix", "M3U8 NOT valid: $url status=${resp.code} len=${body.length}")
                return@Interceptor resp
            }
            val inParam = Regex("[?&]in=([^&]+)").find(url)?.groupValues?.get(1)
            Log.d("CdnFix", "M3U8 OK: $url len=${body.length} hasBrokenCdn=${body.contains("https:///files/")} in=${inParam?.take(50)}")
            var fixed = body
            // Fix broken https:///files/ → net11.cc/hls/ (CDN path /files/ → origin path /hls/)
            if (fixed.contains("https:///files/")) {
                fixed = fixed.replace("https:///files/", "https://net11.cc/hls/")
                Log.d("netmirror", "CDN fix broken →net11.cc/hls/: $url")
                Log.d("CdnFix", "Fixed broken CDN URLs (→net11.cc/hls/): $url")
            }
            // no CDN unify — s21.freecdn*.top works; nm-cdn*.top are private/inaccessible
            // Fix relative segment URLs missing the in= auth param
            if (inParam != null) {
                // Use the original in hash as-is (including ::ep suffix) for segments
                val relSegmentRegex = Regex("^(?!#)([^\n\r]+)$", RegexOption.MULTILINE)
                var segmentFixed = 0
                fixed = relSegmentRegex.replace(fixed) { match ->
                    val line = match.value.trim()
                    if (line.isBlank() || line.startsWith("http") || line.contains("in=")) line
                    else {
                        segmentFixed++
                        if (line.contains("?")) "$line&in=$inParam" else "$line?in=$inParam"
                    }
                }
                if (segmentFixed > 0) {
                    Log.d("CdnFix", "Fixed $segmentFixed relative segment URLs (using original in= param)")
                }
            }
            if (fixed != body) {
                val fixedBody = ResponseBody.create(resp.body?.contentType(), fixed)
                return@Interceptor resp.newBuilder().body(fixedBody).build()
            }
        }
        resp
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
    val hlsDomains = listOf("https://net11.cc", "https://net52.cc").distinct()
    var tracks: List<PlaylistTrack> = emptyList()
    var sourceUrl: String? = null
    // Upgrade cookie suffix if degraded
    val plCookie = cookie.replace("::ep::99", "::ep::m").replace("%3A%3Aep%3A%3A99", "%3A%3Aep%3A%3Am")
    // Try playlist.php with cookie + cross-origin headers for hash exchange
    val plHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36",
        "Accept" to "*/*",
        "X-Requested-With" to "NetmirrorNewTV v1.0",
        "Referer" to "https://net11.cc/play.php?id=$id",
        "Origin" to "https://net11.cc"
    ) + if (plCookie.isNotBlank()) mapOf("Cookie" to plCookie) else emptyMap()
    val m3u8Referer = "https://net11.cc/playlist.php?id=$id&t=$title&tm=$timestamp&h=$cleanHash"
    val m3u8Headers = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36",
        "Accept" to "*/*",
        "Referer" to m3u8Referer
    ) + if (plCookie.isNotBlank()) mapOf("Cookie" to plCookie) else emptyMap()
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
                Log.d("netmirror", "playlist source OK len=${body.length} hasBroken=${body.contains("https:///files/")}")
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
                Log.d("netmirror", "direct M3U8 OK len=${body.length} host=$hlsDomain")
                Log.e("PLAYURL", m3u8Url)
                return Pair(m3u8Url, tracks)
            }
        } catch (e: Exception) {
            Log.w("PlayPhp", "Direct M3U8 failed ($hlsDomain): ${e.message}")
        }
    }
    return null
}