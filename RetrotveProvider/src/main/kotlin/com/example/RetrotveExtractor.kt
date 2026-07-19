package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class SendvidExtractor : ExtractorApi() {
    override val name = "Sendvid"
    override val mainUrl = "https://sendvid.com"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("RetrotveProvider", "Sendvid-START: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8"
            )
            
            val embedUrl = if (url.contains("/embed/")) url else url.replace("/?", "/embed/?")
            val response = app.get(embedUrl, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("RetrotveProvider", "Sendvid: code=${response.code}, len=${pageHtml.length}")
            
            val patterns = listOf(
                """video_url\s*:\s*["']([^"']+)["']""".toRegex(),
                """file\s*:\s*["']([^"']+\.mp4[^"']*)["']""".toRegex(),
                """src\s*=\s*["']([^"']+\.mp4[^"']*)["']""".toRegex(),
                """https?://[^"'\s]+\.mp4[^"'\s]*""".toRegex()
            )
            
            for (pattern in patterns) {
                val match = pattern.find(pageHtml)
                if (match != null) {
                    val videoUrl = match.groupValues.getOrNull(1)?.replace("\\/", "/") 
                        ?: match.value.replace("\\/", "/")
                    if (videoUrl.startsWith("http")) {
                        Log.d("RetrotveProvider", "Sendvid: Found $videoUrl")
                        callback.invoke(
                            newExtractorLink(name, name, videoUrl) {
                                this.referer = embedUrl
                                this.type = ExtractorLinkType.VIDEO
                            }
                        )
                        return
                    }
                }
            }
            Log.d("RetrotveProvider", "Sendvid: No URL found")
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "Sendvid: Error ${e.message}")
        }
    }
}

class FilemoonExtractor : ExtractorApi() {
    override val name = "Filemoon"
    override val mainUrl = "https://filemoon.sx"
    override val requiresReferer = true

    private val mapper = ObjectMapper()

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("RetrotveProvider", "FilemoonExtractor: url=$url")

        try {
            val page = app.get(url, referer = referer ?: url).text
            Log.d("RetrotveProvider", "FilemoonExtractor: page len=${page.length}")

            // Try regex in HTML
            val htmlPatterns = listOf(
                Regex("""sources\s*:\s*\[?\s*\{?\s*file\s*:\s*"([^"]+)"\s*\}?\s*]?"""),
                Regex("""https?://[^"'\s<>]+\.(?:mp4|m3u8)[^"'\s<>]*"""),
                Regex("""(?:file|src|url)\s*[:=]\s*["']([^"']+\.(?:m3u8|mp4)[^"']*)["']""")
            )
            for (pattern in htmlPatterns) {
                for (match in pattern.findAll(page)) {
                    val videoUrl = match.groupValues.getOrNull(1)?.replace("\\/", "/") ?: match.value
                    if (videoUrl.startsWith("http")) {
                        Log.d("RetrotveProvider", "FilemoonExtractor: html match $videoUrl")
                        callback(newExtractorLink(name, name, videoUrl) { this.referer = "https://filemoon.sx/" })
                        return
                    }
                }
            }

            val embedId = url.substringAfter("/e/").substringBefore("/")
            if (embedId.length !in 3..20) { Log.d("RetrotveProvider", "FilemoonExtractor: invalid id"); return }
            val apiBase = url.substringBefore("/e/")

            // Try earnvids API patterns
            val apiEndpoints = listOf(
                "/api/source/$embedId",
                "/api/source",
                "/api/get/$embedId",
                "/api/video/$embedId",
                "/api/file/$embedId",
                "/api/play/$embedId",
                "/api/hls/$embedId",
                "/api/media/$embedId",
                "/api/stream/$embedId",
                "/api/manifest/$embedId",
                "/json/$embedId",
                "/get/$embedId",
                "/video/$embedId",
                "/play/$embedId",
                "/source/$embedId",
                "/hls/$embedId",
                "/media/$embedId"
            )
            for (endpoint in apiEndpoints) {
                Log.d("RetrotveProvider", "FilemoonExtractor: trying $apiBase$endpoint")
                try {
                    val resp = app.get("$apiBase$endpoint", referer = url)
                    val text = resp.text
                    if (text.startsWith("{") || text.startsWith("[")) {
                        Log.d("RetrotveProvider", "FilemoonExtractor: JSON from $endpoint: ${text.take(200)}")
                        val parsed = try { mapper.readValue<Map<String, Any>>(text) } catch (e: Exception) { null }
                        if (parsed != null) {
                            val file = parsed["file"]?.toString()
                                ?: (parsed["data"] as? List<*>)?.firstOrNull { it is Map<*, *> }?.let { (it as Map<*, *>)["file"]?.toString() }
                                ?: parsed["url"]?.toString() ?: parsed["src"]?.toString()
                            if (file != null && file.startsWith("http")) {
                                callback(newExtractorLink(name, name, file) { this.referer = apiBase })
                                return
                            }
                        }
                        val videoUrl = Regex("""https?://[^"'\s]+\.(?:m3u8|mp4)[^"'\s]*""").find(text)?.value
                        if (videoUrl != null) {
                            callback(newExtractorLink(name, name, videoUrl) { this.referer = apiBase })
                            return
                        }
                    } else if (text.contains(".m3u8") || text.contains(".mp4")) {
                        val videoUrl = Regex("""https?://[^"'\s]+\.(?:m3u8|mp4)[^"'\s]*""").find(text)?.value
                        if (videoUrl != null) {
                            callback(newExtractorLink(name, name, videoUrl) { this.referer = apiBase })
                            return
                        }
                    }
                } catch (e: Exception) {
                    Log.d("RetrotveProvider", "FilemoonExtractor: endpoint ${endpoint.take(30)} error: ${e.message?.take(100)}")
                }
            }

            // Try JS bundle extraction with broader patterns
            val jsUrl = try {
                val scriptSrc = Regex("""<script\s+[^>]*src\s*=\s*"([^"]*index-[^"]+\.js)""").find(page)?.groupValues?.get(1)
                if (scriptSrc != null) "$apiBase$scriptSrc" else null
            } catch (e: Exception) { null }
            if (jsUrl != null) {
                Log.d("RetrotveProvider", "FilemoonExtractor: fetching JS $jsUrl")
                val js = app.get(jsUrl, referer = url).text
                Log.d("RetrotveProvider", "FilemoonExtractor: JS len=${js.length}")

                // Search for embedId in JS
                val idIndex = js.indexOf(embedId)
                if (idIndex >= 0) {
                    val ctx = js.substring(maxOf(0, idIndex - 300), minOf(js.length, idIndex + 400))
                    Log.d("RetrotveProvider", "FilemoonExtractor: JS context: $ctx")
                }

                // Broader API patterns
                val jsPatterns = listOf(
                    Regex("""["']/(?:api|v1|v2|v3|get|source|video|file|stream|play|hls|media|json)/([^"']+)["']"""),
                    Regex("""["'](?:source|video|file|stream|play|hls|media)/([^"']+)["']"""),
                    Regex("""fetch\s*\(\s*["`]([^"`]+)["`]"""),
                    Regex("""axios[.\[]\w*[.\]]\s*\(\s*["`]([^"`]+)["`]"""),
                    Regex("""\bget\s*\(\s*["`]([^"`]+)["`]"""),
                    Regex("""\bpost\s*\(\s*["`]([^"`]+)["`]"""),
                    Regex("""\bput\s*\(\s*["`]([^"`]+)["`]"""),
                    Regex("""["`](/[a-z]+/[a-z]+(?:\/[a-z]+)?)["`]"""),
                )
                val endpoints = mutableSetOf<String>()
                for (p in jsPatterns) {
                    for (m in p.findAll(js)) {
                        val ep = (m.groupValues.getOrNull(1) ?: m.value).trim()
                        if (ep.length in 5..100) {
                            val clean = if (ep.startsWith("'") || ep.startsWith("\"")) ep.drop(1).dropLast(1) else ep
                            endpoints.add(clean)
                        }
                    }
                }
                Log.d("RetrotveProvider", "FilemoonExtractor: found ${endpoints.size} JS endpoints: $endpoints")
                for (ep in endpoints.take(15)) {
                    val tryUrl = if (ep.startsWith("/")) "$apiBase$ep" else "$apiBase/$ep"
                    try {
                        val resp = app.get(tryUrl, referer = url)
                        val text = resp.text
                        if (text.startsWith("{") || text.contains(".m3u8") || text.contains(".mp4")) {
                            Log.d("RetrotveProvider", "FilemoonExtractor: promising from JS: $tryUrl -> ${text.take(200)}")
                            val file = try { mapper.readValue<Map<String, Any>>(text)["file"]?.toString() } catch (e: Exception) { null }
                            if (file != null) { callback(newExtractorLink(name, name, file) { this.referer = apiBase }); return }
                            val vu = Regex("""https?://[^"'\s]+\.(?:m3u8|mp4)[^"'\s]*""").find(text)?.value
                            if (vu != null) { callback(newExtractorLink(name, name, vu) { this.referer = apiBase }); return }
                        }
                    } catch (e: Exception) { Log.d("RetrotveProvider", "FilemoonExtractor: JS ep error: ${e.message?.take(100)}") }
                }
            }

            Log.d("RetrotveProvider", "FilemoonExtractor: all approaches failed")
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "FilemoonExtractor: error ${e.message}")
        }
    }
}
