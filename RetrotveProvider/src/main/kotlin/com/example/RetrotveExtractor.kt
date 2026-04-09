package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import java.util.regex.Pattern

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
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("RetrotveProvider", "Sendvid: code=${response.code}, len=${pageHtml.length}")
            
            val patterns = listOf(
                Pattern.compile("""video_url\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""file\s*:\s*["']([^"']+\.mp4[^"']*)["']"""),
                Pattern.compile("""src\s*=\s*["']([^"']+\.mp4[^"']*)["']"""),
                Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    var videoUrl = matcher.group(1)?.replace("\\", "") ?: matcher.group()
                    if (videoUrl != null && videoUrl.startsWith("http")) {
                        Log.d("RetrotveProvider", "Sendvid: Found $videoUrl")
                        callback.invoke(
                            newExtractorLink(name, name, videoUrl) {
                                this.referer = url
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
    override val mainUrl = "https://filemoon.to"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("RetrotveProvider", "Filemoon-START: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Referer" to (referer ?: mainUrl)
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("RetrotveProvider", "Filemoon: code=${response.code}, len=${pageHtml.length}")
            
            val patterns = listOf(
                Pattern.compile("""sources\s*:\s*\[\s*\{\s*file\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""file\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""xstream\s*[=:]\s*["']([^"']+)["']"""),
                Pattern.compile("""https?://[^"'\\]+m3u8[^"'\\]*""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    var videoUrl = matcher.group(1)?.replace("\\", "") ?: matcher.group()
                    if (videoUrl != null) {
                        if (videoUrl.startsWith("//")) videoUrl = "https:$videoUrl"
                        if (!videoUrl.startsWith("http")) {
                            val base = url.substringBefore("/e/")
                            videoUrl = "$base/$videoUrl"
                        }
                        if (videoUrl.contains("m3u8") || videoUrl.contains("mp4")) {
                            Log.d("RetrotveProvider", "Filemoon: Found $videoUrl")
                            callback.invoke(
                                newExtractorLink(name, name, videoUrl) {
                                    this.referer = url
                                    this.type = ExtractorLinkType.M3U8
                                }
                            )
                            return
                        }
                    }
                }
            }
            Log.d("RetrotveProvider", "Filemoon: No URL found")
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "Filemoon: Error ${e.message}")
        }
    }
}

class VKVideoExtractor : ExtractorApi() {
    override val name = "VKVideo"
    override val mainUrl = "https://vkvideo.ru"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("RetrotveProvider", "VKVideo-START: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Referer" to "https://vk.com/"
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("RetrotveProvider", "VKVideo: code=${response.code}, len=${pageHtml.length}")
            
            val patterns = listOf(
                Pattern.compile("""url720\s*[=:]\s*["']([^"']+)["']"""),
                Pattern.compile("""url480\s*[=:]\s*["']([^"']+)["']"""),
                Pattern.compile("""url360\s*[=:]\s*["']([^"']+)["']"""),
                Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*"""),
                Pattern.compile("""https?://[^"'\\]+m3u8[^"'\\]*""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    val videoUrl = matcher.group().replace("\\", "")
                    if (videoUrl.startsWith("http")) {
                        Log.d("RetrotveProvider", "VKVideo: Found $videoUrl")
                        callback.invoke(
                            newExtractorLink(name, name, videoUrl) {
                                this.referer = url
                                this.type = if (videoUrl.contains("m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            }
                        )
                        return
                    }
                }
            }
            Log.d("RetrotveProvider", "VKVideo: No URL found")
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "VKVideo: Error ${e.message}")
        }
    }
}

class OKRuExtractor : ExtractorApi() {
    override val name = "OKRU"
    override val mainUrl = "https://ok.ru"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("RetrotveProvider", "OKRu-START: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36",
                "Referer" to "https://ok.ru/",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8"
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("RetrotveProvider", "OKRu: code=${response.code}, len=${pageHtml.length}")
            
            val flashvarsMatch = Regex("""flashvars\s*=\s*\{([^}]+)\}""", RegexOption.DOT_MATCHES_ALL).find(pageHtml)
            Log.d("RetrotveProvider", "OKRu: flashvarsMatch=${flashvarsMatch != null}")
            
            if (flashvarsMatch != null) {
                val flashvars = flashvarsMatch.groupValues[1]
                Log.d("RetrotveProvider", "OKRu: flashvars found, len=${flashvars.length}")
                
                val metadataUrl = Regex("""metadataUrl\s*:\s*"([^"]+)"""").find(flashvars)?.groupValues?.get(1)
                Log.d("RetrotveProvider", "OKRu: metadataUrl=${metadataUrl}")
                
                if (metadataUrl != null) {
                    val metadataHeaders = headers.toMutableMap().apply {
                        put("Referer", url)
                    }
                    val metadataResponse = app.get(metadataUrl, headers = metadataHeaders, timeout = 30L)
                    val metadata = metadataResponse.text
                    
                    Log.d("RetrotveProvider", "OKRu: metadata len=${metadata.length}")
                    
                    val videoUrl = Regex("""["']hlsPlaylist["']\s*:\s*["']([^"']+)["']""")
                        .find(metadata)?.groupValues?.get(1)
                        ?: Regex("""url720\s*:\s*"([^"]+)"""").find(flashvars)?.groupValues?.get(1)
                        ?: Regex("""url480\s*:\s*"([^"]+)"""").find(flashvars)?.groupValues?.get(1)
                        ?: Regex("""url360\s*:\s*"([^"]+)"""").find(flashvars)?.groupValues?.get(1)
                    
                    if (videoUrl != null) {
                        val finalUrl = videoUrl.replace("\\/", "/")
                        Log.d("RetrotveProvider", "OKRu: Found URL: $finalUrl")
                        callback.invoke(
                            newExtractorLink(name, name, finalUrl) {
                                this.referer = url
                                this.type = if (finalUrl.contains("m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            }
                        )
                        return
                    }
                }
            }
            
            val mp4Patterns = listOf(
                """https://[^"'\\]+okcdn\.ru[^"'\\]*\.mp4[^"'\\]*""",
                """["'](https?://[^"'\\]+\.mp4[^"'\\]*)["']"""
            )
            
            for (pattern in mp4Patterns) {
                val match = Regex(pattern).find(pageHtml)
                if (match != null) {
                    var videoUrl = match.groupValues.getOrNull(1) ?: match.value
                    videoUrl = videoUrl.replace("\\/", "/")
                    if (videoUrl.startsWith("http")) {
                        Log.d("RetrotveProvider", "OKRu: Found MP4: $videoUrl")
                        callback.invoke(
                            newExtractorLink(name, name, videoUrl) {
                                this.referer = url
                                this.type = ExtractorLinkType.VIDEO
                            }
                        )
                        return
                    }
                }
            }
            
            Log.d("RetrotveProvider", "OKRu: No URL found")
            
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "OKRu: Error ${e.message}")
            e.printStackTrace()
        }
    }
}

class YourUploadExtractor : ExtractorApi() {
    override val name = "YourUpload"
    override val mainUrl = "https://yourupload.com"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("RetrotveProvider", "YourUpload-START: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Referer" to (referer ?: mainUrl)
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("RetrotveProvider", "YourUpload: code=${response.code}, len=${pageHtml.length}")
            
            val patterns = listOf(
                Pattern.compile("""sources\s*:\s*\[\s*\{\s*file\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""file\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""https?://[^"'\\]+\.(?:mp4|m3u8)[^"'\\]*""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    var videoUrl = matcher.group(1)?.replace("\\", "") ?: matcher.group()
                    if (videoUrl != null && videoUrl.startsWith("http")) {
                        Log.d("RetrotveProvider", "YourUpload: Found $videoUrl")
                        callback.invoke(
                            newExtractorLink(name, name, videoUrl) {
                                this.referer = url
                                this.type = if (videoUrl.contains("m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            }
                        )
                        return
                    }
                }
            }
            Log.d("RetrotveProvider", "YourUpload: No URL found")
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "YourUpload: Error ${e.message}")
        }
    }
}
