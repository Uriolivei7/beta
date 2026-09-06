package com.example

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

private const val MONOS_TAG = "MonosChinos"
private const val MONOS_SOURCE = "MonosChinos"

private const val FM_READY_JS = "h.length > 3000 && (h.includes('.m3u8') || h.includes('.mp4') || h.includes('video'))"
private const val FM_DUMP_JS = "(function(){try{NativeBridge.onHtml(document.documentElement.outerHTML);}catch(e){NativeBridge.onHtml('ERR:'+e);}})()"

private suspend fun renderViaWebView(pageUrl: String, referer: String?, waitMs: Long = 12000L): String? {
    return withContext(Dispatchers.Main) {
        val appCtx = MonoschinosProvider.pluginContext?.applicationContext ?: run {
            Log.w(MONOS_TAG, "[FM-WebView] sin context")
            return@withContext null
        }
        var webView: WebView? = null
        val mainHandler = Handler(Looper.getMainLooper())
        try {
            webView = WebView(appCtx)
            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                cacheMode = WebSettings.LOAD_NO_CACHE
                userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36"
                allowFileAccess = false
            }
            val deferred = CompletableDeferred<String?>()
            val polls = java.util.concurrent.atomic.AtomicInteger(0)
            val maxPolls = (waitMs / 2000L).toInt().coerceAtLeast(1)
            fun dump() {
                if (deferred.isCompleted) return
                try {
                    webView?.evaluateJavascript(FM_DUMP_JS, null)
                } catch (_: Exception) {
                    if (!deferred.isCompleted) deferred.complete(null)
                }
            }
            fun pollOnce() {
                if (deferred.isCompleted) return
                try {
                    webView?.evaluateJavascript(
                        "(function(){try{var h=document.documentElement.outerHTML;NativeBridge.onPoll(($FM_READY_JS));}catch(e){NativeBridge.onPoll(false);}})()",
                        null
                    )
                } catch (_: Exception) {
                    if (!deferred.isCompleted) deferred.complete(null)
                }
            }
            webView.addJavascriptInterface(object {
                @JavascriptInterface
                fun onHtml(html: String) {
                    if (!deferred.isCompleted) deferred.complete(html)
                }

                @JavascriptInterface
                fun onPoll(ready: Boolean) {
                    mainHandler.post {
                        if (deferred.isCompleted) return@post
                        if (ready) {
                            Log.d(MONOS_TAG, "[FM-WebView] listo antes de tiempo, dumpeando")
                            dump()
                            return@post
                        }
                        if (polls.incrementAndGet() >= maxPolls) {
                            dump()
                        } else {
                            mainHandler.postDelayed({ pollOnce() }, 2000L)
                        }
                    }
                }
            }, "NativeBridge")
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    polls.set(0)
                    mainHandler.postDelayed({ pollOnce() }, 2000L)
                }

                override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                    if (!deferred.isCompleted) deferred.complete(null)
                }
            }
            if (!referer.isNullOrBlank()) webView.loadUrl(pageUrl, mapOf("Referer" to referer))
            else webView.loadUrl(pageUrl)
            Log.d(MONOS_TAG, "[FM-WebView] renderizando ${pageUrl.take(100)}")
            withTimeoutOrNull(waitMs + 15000L) { deferred.await() }
        } catch (e: Exception) {
            Log.w(MONOS_TAG, "[FM-WebView] error: ${e.message}")
            null
        } finally {
            try { mainHandler.removeCallbacksAndMessages(null) } catch (_: Exception) {}
            try { webView?.destroy() } catch (_: Exception) {}
        }
    }
}

class MonosLuluvdo : ExtractorApi() {
    override val name = "MonosLuluvdo"
    override val mainUrl = "https://luluvdo.com"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        Log.d(MONOS_TAG, "[Lulu] URL: $url")
        try {
            val resp = app.get(url, headers = mapOf(
                "User-Agent" to USER_AGENT,
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Referer" to (referer ?: url),
            ), timeout = 20000L)
            Log.d(MONOS_TAG, "[Lulu] HTTP ${resp.code} len=${resp.text.length}")
            parseHtml(resp.text, MONOS_SOURCE, callback)
        } catch (e: Exception) {
            Log.e(MONOS_TAG, "[Lulu] Error: ${e.message}", e)
        }
    }

    suspend fun parseHtml(
        html: String,
        sourceName: String,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val packed = Regex("""\}\('(.*?)',(\d+),(\d+),'(.*?)'\.split\('\|'\)""", RegexOption.DOT_MATCHES_ALL)
            .find(html)
        if (packed == null) {
            Log.w(MONOS_TAG, "[Lulu] no packed JS found")
            return false
        }
        val decoded = unpackDeanEdwards(
            packed.groupValues[1],
            packed.groupValues[2].toIntOrNull() ?: 36,
            packed.groupValues[3].toIntOrNull() ?: 0,
            packed.groupValues[4]
        ) ?: return false
        Log.d(MONOS_TAG, "[Lulu] decoded len=${decoded.length}")
        val m3u8Regex = Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""")
        var found = false
        for (m in m3u8Regex.findAll(decoded)) {
            Log.d(MONOS_TAG, "[Lulu] M3U8: ${m.value.take(120)}")
            callback.invoke(
                newExtractorLink(sourceName, "$sourceName - LuluVdo", m.value, ExtractorLinkType.M3U8) {
                    this.referer = mainUrl
                    this.headers = mapOf("Origin" to mainUrl)
                }
            )
            found = true
        }
        if (!found) {
            val fileRegex = Regex("""(?:file|src)\s*:\s*["']((?:https?:)?//[^"']+)["']""")
            for (m in fileRegex.findAll(decoded)) {
                val f = m.groupValues[1].replace("\\/", "/").trim()
                val full = if (f.startsWith("//")) "https:$f" else f
                if (full.contains(".m3u8")) {
                    Log.d(MONOS_TAG, "[Lulu] file: ${full.take(120)}")
                    callback.invoke(newExtractorLink(sourceName, "$sourceName - LuluVdo", full, ExtractorLinkType.M3U8) {
                        this.referer = mainUrl
                        this.headers = mapOf("Origin" to mainUrl)
                    })
                    found = true
                }
            }
        }
        if (!found) Log.w(MONOS_TAG, "[Lulu] no m3u8 after unpack, snippet=${decoded.take(300)}")
        return found
    }
}

class MonosMixdrop : ExtractorApi() {
    override val name = "MonosMixdrop"
    override val mainUrl = "https://mixdrop.top"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        Log.d(MONOS_TAG, "[Mix] URL: $url")
        try {
            val resp = app.get(url, headers = mapOf(
                "User-Agent" to USER_AGENT,
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Referer" to (referer ?: url),
            ), timeout = 20000L)
            Log.d(MONOS_TAG, "[Mix] HTTP ${resp.code} len=${resp.text.length}")
            parseHtml(resp.text, MONOS_SOURCE, callback)
        } catch (e: Exception) {
            Log.e(MONOS_TAG, "[Mix] Error: ${e.message}", e)
        }
    }

    suspend fun parseHtml(
        html: String,
        sourceName: String,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val packerRegex = Regex("""\}\('(.*?)',(\d+),(\d+),'(.*?)'\.split\('\|'\)""", RegexOption.DOT_MATCHES_ALL)
        var found = false
        for (pm in packerRegex.findAll(html)) {
            try {
                val decoded = unpackDeanEdwards(
                    pm.groupValues[1],
                    pm.groupValues[2].toIntOrNull() ?: 36,
                    pm.groupValues[3].toIntOrNull() ?: 0,
                    pm.groupValues[4]
                ) ?: continue
                Log.d(MONOS_TAG, "[Mix] decoded len=${decoded.length}")
                val wurl = Regex("""MDCore\.wurl\s*=\s*"([^"]*)""").find(decoded)?.groupValues?.get(1)
                if (!wurl.isNullOrBlank()) {
                    var f = wurl.trim()
                    if (f.startsWith("//")) f = "https:$f"
                    else if (!f.startsWith("http")) f = "https:$f"
                    Log.d(MONOS_TAG, "[Mix] wurl: ${f.take(120)}")
                    callback.invoke(
                        newExtractorLink(sourceName, "$sourceName - Mixdrop", f, INFER_TYPE) {
                            this.referer = mainUrl
                            this.headers = mapOf("Origin" to mainUrl)
                        }
                    )
                    found = true
                }
            } catch (_: Exception) {}
            if (found) break
        }
        if (!found) Log.w(MONOS_TAG, "[Mix] no wurl found, hasPacker=${html.contains("function(p,a,c,k")}")
        return found
    }
}

class MonosVoe : ExtractorApi() {
    override val name = "MonosVoe"
    override val mainUrl = "https://voe.sx"
    override val requiresReferer = true

    companion object {
        private val voeMirrors = listOf(
            "https://eugenemakedraw.com",
            "https://yip.su",
            "https://tubelessceliolymph.com",
            "https://donaldlineelse.com",
            "https://charlestoughrace.com",
            "https://simpulumlamerop.com",
            "https://urochsunloath.com",
        )
    }

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        Log.d(MONOS_TAG, "[Voe] URL: $url")
        val voeHeaders = mapOf(
            "User-Agent" to USER_AGENT,
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "Accept-Language" to "en-US,en;q=0.9",
        )
        val id = Regex("""/e/([A-Za-z0-9_-]+)""").find(url)?.groupValues?.get(1)
        if (id.isNullOrBlank()) {
            Log.w(MONOS_TAG, "[Voe] no id in url: $url")
            return
        }

        val redirectRegexes = listOf(
            Regex("""(?:window\.)?location(?:\.href)?\s*=\s*'([^']+)'"""),
            Regex("""(?:window\.)?location\.replace\s*\(\s*'([^']+)'"""),
            Regex("""(?:window\.)?location\.assign\s*\(\s*'([^']+)'"""),
        )

        var currentUrl = url
        var currentReferer = referer ?: url
        var res = app.get(currentUrl, headers = voeHeaders, referer = currentReferer)
        Log.d(MONOS_TAG, "[Voe] HP. ${res.code} len=${res.text.length} url=$currentUrl")

        var redirectUrl: String? = null
        for (r in redirectRegexes) {
            r.find(res.text)?.let { redirectUrl = it.groupValues[1] }
            if (!redirectUrl.isNullOrBlank()) break
        }
        var maxRedirects = 5
        while (!redirectUrl.isNullOrBlank() && maxRedirects > 0) {
            Log.d(MONOS_TAG, "[Voe] Redirect to: $redirectUrl")
            currentReferer = currentUrl
            currentUrl = redirectUrl!!
            res = app.get(currentUrl, headers = voeHeaders, referer = currentReferer)
            maxRedirects--
            redirectUrl = null
            for (r in redirectRegexes) {
                r.find(res.text)?.let { redirectUrl = it.groupValues[1] }
                if (!redirectUrl.isNullOrBlank()) break
            }
        }
        if (maxRedirects == 0 && !redirectUrl.isNullOrBlank()) {
            Log.e(MONOS_TAG, "[Voe] Too many redirects, giving up")
        }
        Log.d(MONOS_TAG, "[Voe] final len=${res.text.length} url=$currentUrl hasAppJson=${res.text.contains("application/json")} hasBlob=${res.text.contains("DROH") || res.text.contains("@$")}")
        if (!parseHtml(res.text, currentUrl, MONOS_SOURCE, subtitleCallback, callback)) {
            Log.d(MONOS_TAG, "[Voe] direct parse failed, probando mirrors (id=$id)")
            for (mirror in voeMirrors) {
                if (mirror in currentUrl) continue
                val mUrl = "$mirror/e/$id"
                try {
                    val resp = app.get(mUrl, headers = voeHeaders, referer = currentUrl, timeout = 15000L)
                    Log.d(MONOS_TAG, "[Voe] mirror $mirror -> ${resp.code} len=${resp.text.length}")
                    if (parseHtml(resp.text, mUrl, MONOS_SOURCE, subtitleCallback, callback)) break
                } catch (_: Exception) {}
            }
        }
    }

    suspend fun parseHtml(
        html: String,
        pageUrl: String,
        sourceName: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        if (html.contains("altcha-widget") || html.contains("Confirm you&#039;re human")) {
            Log.w(MONOS_TAG, "[Voe] CAPTCHA page, no parse: ${pageUrl.take(100)}")
            return false
        }
        val pageOrigin = try {
            val u = java.net.URL(pageUrl)
            "${u.protocol}://${u.host}"
        } catch (_: Exception) { pageUrl }

        var encodedString: String? = Regex(
            """<script[^>]*type=["']application/json["'][^>]*>(.*?)</script>""",
            RegexOption.DOT_MATCHES_ALL
        ).find(html)?.groupValues?.get(1)?.trim()
            ?.takeIf { it.contains("[\"") }
            ?.substringAfter("[\"")
            ?.substringBeforeLast("\"]")

        if (encodedString == null) {
            val scripts = Regex("""<script[^>]*>(.*?)</script>""", RegexOption.DOT_MATCHES_ALL)
                .findAll(html).map { it.groupValues[1] }
            outer@ for (body in scripts) {
                for (m in Regex("""["']([A-Za-z0-9+/=@\^~\*\?&#!%$]{100,})["']""").findAll(body)) {
                    val cand = m.groupValues[1]
                    val test = decryptVoeF7(cand, quiet = true)
                    if (test?.source != null || test?.directAccessUrl != null) {
                        encodedString = cand
                        break@outer
                    }
                }
            }
        }

        if (encodedString == null) {
            Log.w(MONOS_TAG, "[Voe] encoded string not found: ${pageUrl.take(100)}")
            return false
        }

        val decryptedJson = decryptVoeF7(encodedString)
        val m3u8 = decryptedJson?.source
        val mp4 = decryptedJson?.directAccessUrl
        var emitted = false
        if (m3u8 != null) {
            Log.d(MONOS_TAG, "[Voe] Found M3U8: ${m3u8.take(100)}")
            M3u8Helper.generateM3u8(
                sourceName,
                m3u8,
                "$pageOrigin/",
                headers = mapOf("Origin" to "$pageOrigin/"),
            ).forEach(callback)
            emitted = true
        }
        if (mp4 != null) {
            Log.d(MONOS_TAG, "[Voe] Found MP4: ${mp4.take(100)}")
            callback.invoke(
                newExtractorLink("$sourceName MP4", "$sourceName MP4", mp4, INFER_TYPE) {
                    this.referer = pageUrl
                    this.quality = Qualities.Unknown.value
                }
            )
            emitted = true
        }
        if (!emitted) Log.w(MONOS_TAG, "[Voe] No source found after decryption")
        return emitted
    }

    private fun decryptVoeF7(p8: String, quiet: Boolean = false): MonosVoeDecrypted? {
        return try {
            val vF = rot13(p8)
            val vF2 = replacePatterns(vF)
            val vF3 = removeUnderscores(vF2)
            val vF4 = base64Decode(vF3)
            val vF5 = charShift(vF4, 3)
            val vF6 = vF5.reversed()
            val vAtob = base64Decode(vF6)
            parseJson<MonosVoeDecrypted>(vAtob)
        } catch (e: Exception) {
            if (!quiet) Log.e(MONOS_TAG, "[Voe] decrypt error: ${e.message}")
            null
        }
    }

    private fun rot13(input: String): String {
        return input.map { c ->
            when (c) {
                in 'A'..'Z' -> ((c - 'A' + 13) % 26 + 'A'.code).toChar()
                in 'a'..'z' -> ((c - 'a' + 13) % 26 + 'a'.code).toChar()
                else -> c
            }
        }.joinToString("")
    }

    private fun replacePatterns(input: String): String {
        val patterns = listOf("@\$", "^^", "~@", "%?", "*~", "!!", "#&")
        return patterns.fold(input) { result, pattern ->
            result.replace(Regex(Regex.escape(pattern)), "_")
        }
    }

    private fun removeUnderscores(input: String): String = input.replace("_", "")

    private fun charShift(input: String, shift: Int): String {
        return input.map { (it.code - shift).toChar() }.joinToString("")
    }
}

class MonosFilemoon : ExtractorApi() {
    override val name = "MonosFilemoon"
    override val mainUrl = "https://filemoon.sx"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        Log.d(MONOS_TAG, "[FM] URL: $url")
        var renderedFetched = false
        try {
            val resp = app.get(url, headers = mapOf(
                "User-Agent" to USER_AGENT,
                "Referer" to (referer ?: url),
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            ), timeout = 20000L)
            Log.d(MONOS_TAG, "[FM] HTTP ${resp.code} len=${resp.text.length}")
            if (parseHtml(resp.text, MONOS_SOURCE, callback)) return
            Log.w(MONOS_TAG, "[FM] sin m3u8/mp4 vía HTTP (len=${resp.text.length}), probable SPA Byse -> WebView")
        } catch (e: Exception) {
            Log.e(MONOS_TAG, "[FM] HTTP Error: ${e.message}")
        }

        val rendered = renderViaWebView(url, referer ?: url, waitMs = 15000L)
        renderedFetched = !rendered.isNullOrBlank()
        if (rendered.isNullOrBlank()) {
            Log.w(MONOS_TAG, "[FM] WebView devolvió vacío")
            return
        }
        Log.d(MONOS_TAG, "[FM] WebView HTML len=${rendered.length}")
        val found = parseHtml(rendered, MONOS_SOURCE, callback, isWebView = true)
        if (!found) {
            Log.w(MONOS_TAG, "[FM] WebView sin m3u8/mp4, snippet=${rendered.take(300).replace("\n", " ")}")
        }
        Log.d(MONOS_TAG, "[FM] done renderedFetched=$renderedFetched found=$found")
    }

    suspend fun parseHtml(
        html: String,
        sourceName: String,
        callback: (ExtractorLink) -> Unit,
        isWebView: Boolean = false,
    ): Boolean {
        val m3u8Regex = Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""")
        val mp4Regex = Regex("""(https?://[^"'\s<>]+\.(?:mp4|ts)[^"'\s<>]*)""")
        var found = false
        for (m in m3u8Regex.findAll(html)) {
            Log.d(MONOS_TAG, "[FM] M3U8: ${m.value.take(120)}")
            callback.invoke(newExtractorLink(MONOS_SOURCE, "$MONOS_SOURCE - Filemoon", m.value, ExtractorLinkType.M3U8) {
                this.referer = mainUrl
                this.headers = mapOf("Origin" to mainUrl)
            })
            found = true
        }
        if (!found) {
            for (m in mp4Regex.findAll(html)) {
                Log.d(MONOS_TAG, "[FM] MP4: ${m.value.take(120)}")
                callback.invoke(newExtractorLink(MONOS_SOURCE, "$MONOS_SOURCE - Filemoon", m.value, ExtractorLinkType.M3U8) {
                    this.referer = mainUrl
                    this.headers = mapOf("Origin" to mainUrl)
                })
                found = true
            }
        }
        if (!found) {
            val fileRegex = Regex("""(?:file|src)\s*:\s*["']((?:https?:)?//[^"']+)["']""")
            for (m in fileRegex.findAll(html)) {
                var f = m.groupValues[1].replace("\\/", "/").trim()
                if (f.startsWith("//")) f = "https:$f"
                if (f.contains(".m3u8") || f.contains(".mp4")) {
                    Log.d(MONOS_TAG, "[FM] file: ${f.take(120)}")
                    callback.invoke(newExtractorLink(MONOS_SOURCE, "$MONOS_SOURCE - Filemoon", f, ExtractorLinkType.M3U8) {
                        this.referer = mainUrl
                        this.headers = mapOf("Origin" to mainUrl)
                    })
                    found = true
                }
            }
        }
        if (!found && !isWebView) Log.w(MONOS_TAG, "[FM] no m3u8/mp4 found, snippet=${html.take(300).replace("\n", " ")}")
        return found
    }
}

data class MonosVoeDecrypted(
    @JsonProperty("source") val source: String? = null,
    @JsonProperty("direct_access_url") val directAccessUrl: String? = null,
)

fun unpackDeanEdwards(packed: String, base: Int, count: Int, dictRaw: String): String? {
    return try {
        val k = dictRaw.split("|").toTypedArray()
        val result = StringBuilder(packed)
        for (idx in count - 1 downTo 0) {
            val key = idx.toString(base)
            val value = k.getOrElse(idx) { "" }
            if (key.isNotEmpty() && value.isNotEmpty()) {
                val replaced = Regex("\\b${Regex.escape(key)}\\b").replace(result, Regex.escapeReplacement(value))
                result.clear()
                result.append(replaced)
            }
        }
        result.toString().replace("\\'", "'")
    } catch (_: Exception) { null }
}