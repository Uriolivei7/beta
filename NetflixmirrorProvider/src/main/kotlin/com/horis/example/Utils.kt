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
import okhttp3.FormBody
import okhttp3.Request
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
// Cloudflare bypass – gets t_hash_t cookie from net52.cc
// ---------------------------------------------------------------------------

suspend fun bypass(mainUrl: String): String {
    val (savedCookie, savedTimestamp) = NetflixMirrorStorage.getCookie()
    if (!savedCookie.isNullOrEmpty() && System.currentTimeMillis() - savedTimestamp < 54_000_000) {
        return savedCookie
    }

    NetflixMirrorStorage.clearCookie()

    val bypassHeaders = mapOf(
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        "Accept-Language" to "en-IN,en-US;q=0.9,en;q=0.8",
        "Cache-Control" to "max-age=0",
        "Connection" to "keep-alive",
        "sec-ch-ua" to "\"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"144\", \"Android WebView\";v=\"144\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Android\"",
        "Sec-Fetch-Dest" to "document",
        "Sec-Fetch-Mode" to "navigate",
        "Sec-Fetch-Site" to "same-origin",
        "Sec-Fetch-User" to "?1",
        "Upgrade-Insecure-Requests" to "1",
        "User-Agent" to "Mozilla/5.0 (Linux; Android 13; Pixel 5 Build/TQ3A.230901.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/144.0.7559.132 Safari/537.36 /OS.Gatu v3.0",
        "X-Requested-With" to "XMLHttpRequest"
    )

    // Get addhash from main page following redirects (net52.cc → net22.cc/verify2)
    var addhash = ""
    try {
        val redirectClient = app.baseClient.newBuilder()
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
        val redirectRequest = Request.Builder()
            .url("$mainUrl/home")
            .apply { bypassHeaders.forEach { (k, v) -> addHeader(k, v) } }
            .build()
        redirectClient.newCall(redirectRequest).execute().use { resp ->
            val finalUrl = resp.request.url.toString()
            val body = resp.body?.string().orEmpty()
            Log.d("bypass", "redirect ended at $finalUrl body.len=${body.length}")
            // Try URL parameter first
            addhash = Regex("""[?&]addhash=([^&]+)""").find(finalUrl)?.groupValues?.getOrNull(1).orEmpty()
            if (addhash.isBlank()) {
                val doc = org.jsoup.Jsoup.parse(body)
                addhash = doc.selectFirst("input[name=addhash]")?.attr("value").orEmpty()
            }
            if (addhash.isBlank()) {
                addhash = Regex("""["']addhash["']\s*[:=]\s*["']([^"']+)""").find(body)?.groupValues?.getOrNull(1).orEmpty()
            }
            if (addhash.isBlank()) {
                addhash = Regex("""addhash[^=]*=["']([^"']+)""").find(body)?.groupValues?.getOrNull(1).orEmpty()
            }
        }
    } catch (e: Exception) {
        Log.d("bypass", "addhash extraction failed: ${e.message}")
    }
    Log.d("bypass", "addhash.length=${addhash.length} addhash=${addhash.take(20)}")

    val verifyHeaders = bypassHeaders + mapOf(
        "Origin" to "https://net22.cc",
        "Referer" to "https://net22.cc/verify2",
        "Content-Type" to "application/x-www-form-urlencoded"
    )

    val client = app.baseClient.newBuilder()
        .followRedirects(false)
        .followSslRedirects(false)
        .build()

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
                .build()
            client.newCall(request).execute().use { response ->
                val respBody = response.body?.string().orEmpty()
                val statusCode = response.code
                Log.d("bypass", "verify.php status=$statusCode body=${respBody.take(200)}")
                if (statusCode in 200..399 || respBody.contains("success", ignoreCase = true) || respBody.contains("ok", ignoreCase = true)) {
                    val cookie = response.headers("Set-Cookie")
                        .firstOrNull { it.startsWith("t_hash_t=") }
                        ?.substringAfter("t_hash_t=")
                        ?.substringBefore(";")
                        .orEmpty()
                    if (cookie.isNotEmpty()) {
                        NetflixMirrorStorage.saveCookie(cookie)
                        Log.d("bypass", "Got cookie on attempt $count: ${cookie.take(20)}")
                        return cookie
                    }
                }
                Log.d("bypass", "Attempt $count: no valid cookie, resp=$respBody")
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

suspend fun getPlaylistUrl(
    mainUrl: String,
    ott: String,
    id: String,
    title: String,
    cookie: String = ""
): Pair<String, List<PlaylistTrack>>? {
    val domains = (playPhpDomains + listOf(mainUrl.trimEnd('/'))).distinct()
    val cookieHeader = buildString {
        append("hd=on")
        if (cookie.isNotBlank()) append("; t_hash_t=$cookie")
    }

    var playHash: String? = null
    var timestamp: String = "0"
    var playDomain: String = ""

    for (domain in domains) {
        try {
            val resp = app.post(
                "$domain/play.php",
                requestBody = FormBody.Builder()
                    .add("id", id)
                    .build(),
                headers = buildNewTvHeaders(ott, mapOf(
                    "Referer" to "$domain/home",
                    "Origin" to domain,
                    "Cookie" to cookieHeader,
                    "Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8"
                ))
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

    if (playHash.isNullOrBlank()) {
        Log.w("PlayPhp", "All domains failed to get play hash")
        return null
    }

    try {
        val playlistBody = app.get(
            "$mainUrl/playlist.php?id=$id&t=$title&tm=$timestamp&h=$playHash",
            headers = buildNewTvHeaders(ott, mapOf(
                "Referer" to "$mainUrl",
                "Cookie" to cookieHeader
            ))
        ).text
        Log.d("PlayPhp", "playlist response=$playlistBody")
        val parsed = tryParseJsonList<PlaylistResponse>(playlistBody)
        val first = parsed?.firstOrNull()
        val sourceFile = first?.sources?.firstOrNull()?.file
        val tracks = first?.tracks.orEmpty()
        if (!sourceFile.isNullOrBlank()) {
            val m3u8Url = if (sourceFile.startsWith("http")) sourceFile
                          else "${mainUrl.trimEnd('/')}/${sourceFile.removePrefix("/")}"
            val fixedUrl = m3u8Url.replace("unknown::ep", playHash)
            Log.d("PlayPhp", "M3U8 url=$fixedUrl tracks=${tracks.size}")
            Log.e("PLAYURL", fixedUrl)
            return Pair(fixedUrl, tracks)
        }
    } catch (e: Exception) {
        Log.w("PlayPhp", "playlist.php failed: ${e.message}")
    }

    return null
}