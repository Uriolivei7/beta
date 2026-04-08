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
    override val name = "OK.RU"
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
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Referer" to "https://ok.ru/"
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("RetrotveProvider", "OKRu: code=${response.code}, len=${pageHtml.length}")
            
            val patterns = listOf(
                Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*"""),
                Pattern.compile("""https?://[^"'\\]+m3u8[^"'\\]*"""),
                Pattern.compile(""""path"\s*:\s*"([^"]+)""""),
                Pattern.compile("""metadataUrl\s*:\s*"([^"]+)"""),
                Pattern.compile("""movieSWF\s*:\s*"([^"]+)""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    var videoUrl = matcher.group().replace("\\", "").replace(""", "").replace(""", "")
                    Log.d("RetrotveProvider", "OKRu: Trying URL: $videoUrl")
                    
                    if (videoUrl.contains("mp4") || videoUrl.contains("m3u8") || videoUrl.contains("ok.ru") || videoUrl.contains("okcdn.ru")) {
                        if (videoUrl.startsWith("//")) videoUrl = "https:$videoUrl"
                        if (videoUrl.contains("okcdn.ru") || videoUrl.contains("ok.ru")) {
                            Log.d("RetrotveProvider", "OKRu: Found $videoUrl")
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
            }
            
            Log.d("RetrotveProvider", "OKRu: No URL found")
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "OKRu: Error ${e.message}")
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
