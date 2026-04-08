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
        Log.d("Retrotve-Sendvid", "Extracting: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
                "Accept-Encoding" to "gzip, deflate, br",
                "Connection" to "keep-alive",
                "Upgrade-Insecure-Requests" to "1",
                "Sec-Fetch-Dest" to "document",
                "Sec-Fetch-Mode" to "navigate",
                "Sec-Fetch-Site" to "none",
                "Sec-Fetch-User" to "?1",
                "Cache-Control" to "max-age=0"
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("Retrotve-Sendvid", "Response code: ${response.code}, HTML length: ${pageHtml.length}")
            
            if (response.code != 200) {
                Log.e("Retrotve-Sendvid", "Bad response code: ${response.code}")
                return
            }
            
            val patterns = listOf(
                Pattern.compile("""video_url\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""["']video_url["']\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""sources\s*:\s*\[\s*\{\s*file\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""file\s*:\s*["']([^"']+\.mp4[^"']*)["']"""),
                Pattern.compile("""src\s*[=:]\s*["']([^"']+\.mp4[^"']*)["']"""),
                Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    var videoUrl = matcher.group(1)?.replace("\\", "") ?: matcher.group()
                    if (videoUrl != null && videoUrl.startsWith("http")) {
                        Log.d("Retrotve-Sendvid", "Found URL with pattern ${pattern.pattern()}: $videoUrl")
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
            
            val scriptContent = Regex("""eval\s*\(\s*function\s*\(p,a,c,k,e,d\).*?\)""").find(pageHtml)
            if (scriptContent != null) {
                Log.d("Retrotve-Sendvid", "Found obfuscated JavaScript")
            }
            
            Log.d("Retrotve-Sendvid", "No direct video URL found")
            
        } catch (e: Exception) {
            Log.e("Retrotve-Sendvid", "Error: ${e.message}")
            e.printStackTrace()
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
        Log.d("RetrotveProvider", "Filemoon-START: Extracting: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
                "Referer" to (referer ?: mainUrl)
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("Retrotve-Filemoon", "Response code: ${response.code}, HTML length: ${pageHtml.length}")
            
            val patterns = listOf(
                Pattern.compile("""sources\s*:\s*\[\s*\{\s*file\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""file\s*:\s*["']([^"']+\.m3u8[^"']*)["']"""),
                Pattern.compile("""["']file["']\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""xstream\s*[=:]\s*["']([^"']+)["']"""),
                Pattern.compile("""master\.m3u8[^"'\\]*"""),
                Pattern.compile("""https?://[^"'\\]+m3u8[^"'\\]*""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    var videoUrl = matcher.group(1)?.replace("\\", "") ?: matcher.group()
                    
                    if (videoUrl != null) {
                        if (videoUrl.startsWith("//")) {
                            videoUrl = "https:$videoUrl"
                        }
                        
                        if (!videoUrl.startsWith("http")) {
                            val baseUrl = url.substringBefore("/e/")
                            videoUrl = "$baseUrl/$videoUrl"
                        }
                        
                        if (videoUrl.contains("m3u8") || videoUrl.contains("mp4")) {
                            Log.d("Retrotve-Filemoon", "Found URL: $videoUrl")
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
            
            val doc = Jsoup.parse(pageHtml)
            val scripts = doc.select("script")
            for (script in scripts) {
                val content = script.html()
                if (content.contains("sources") || content.contains("file:")) {
                    val fileMatch = Regex("""file\s*:\s*["']([^"']+)["']""").find(content)
                    if (fileMatch != null) {
                        var videoUrl = fileMatch.groupValues[1].replace("\\", "")
                        Log.d("Retrotve-Filemoon", "Found in script: $videoUrl")
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
            
            Log.d("Retrotve-Filemoon", "No direct video URL found")
            
        } catch (e: Exception) {
            Log.e("Retrotve-Filemoon", "Error: ${e.message}")
            e.printStackTrace()
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
        Log.d("RetrotveProvider", "VKVideo-START: Extracting: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
                "Referer" to "https://vk.com/"
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("Retrotve-VKVideo", "Response code: ${response.code}, HTML length: ${pageHtml.length}")
            
            val patterns = listOf(
                Pattern.compile("""url720\s*[=:]\s*["']([^"']+)["']"""),
                Pattern.compile("""url480\s*[=:]\s*["']([^"']+)["']"""),
                Pattern.compile("""url360\s*[=:]\s*["']([^"']+)["']"""),
                Pattern.compile("""url240\s*[=:]\s*["']([^"']+)["']"""),
                Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*"""),
                Pattern.compile("""https?://[^"'\\]+m3u8[^"'\\]*""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    val videoUrl = matcher.group().replace("\\", "")
                    if (videoUrl.startsWith("http")) {
                        Log.d("Retrotve-VKVideo", "Found: $videoUrl")
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
            
            Log.d("Retrotve-VKVideo", "No direct video URL found")
            
        } catch (e: Exception) {
            Log.e("Retrotve-VKVideo", "Error: ${e.message}")
            e.printStackTrace()
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
        Log.d("RetrotveProvider", "OKRu-START: Extracting: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
                "Referer" to "https://ok.ru/"
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("Retrotve-OKRu", "Response code: ${response.code}, HTML length: ${pageHtml.length}")
            
            val moviePlayerMatch = Regex("""data-options\s*=\s*["']([^"']+)["']""").find(pageHtml)
            if (moviePlayerMatch != null) {
                val dataOptions = moviePlayerMatch.groupValues[1]
                Log.d("Retrotve-OKRu", "Found data-options")
                
                val metadataMatch = Regex("""moviesJsonUrl\s*:\s*"([^"]+)""").find(dataOptions)
                if (metadataMatch != null) {
                    val metadataUrl = metadataMatch.groupValues[1]
                    Log.d("Retrotve-OKRu", "Metadata URL: $metadataUrl")
                    
                    try {
                        val metadataResponse = app.get(metadataUrl, headers = headers, timeout = 30L)
                        val metadataJson = metadataResponse.text
                        
                        val videoUrl = Regex("""hd\d*\s*:\s*"([^"]+)""").find(metadataJson)?.groupValues?.get(1)
                            ?: Regex("""sd\d*\s*:\s*"([^"]+)""").find(metadataJson)?.groupValues?.get(1)
                            ?: Regex("""url\d*\s*:\s*"([^"]+)""").find(metadataJson)?.groupValues?.get(1)
                        
                        if (videoUrl != null) {
                            val cleanUrl = videoUrl.replace("\\", "")
                            Log.d("Retrotve-OKRu", "Found video URL from metadata: $cleanUrl")
                            callback.invoke(
                                newExtractorLink(name, name, cleanUrl) {
                                    this.referer = url
                                    this.type = ExtractorLinkType.VIDEO
                                }
                            )
                            return
                        }
                    } catch (e: Exception) {
                        Log.e("Retrotve-OKRu", "Error fetching metadata: ${e.message}")
                    }
                }
            }
            
            val patterns = listOf(
                Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*"""),
                Pattern.compile("""https?://[^"'\\]+m3u8[^"'\\]*""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    val videoUrl = matcher.group().replace("\\", "")
                    if (videoUrl.startsWith("http")) {
                        Log.d("Retrotve-OKRu", "Found direct: $videoUrl")
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
            
            Log.d("Retrotve-OKRu", "No direct video URL found")
            
        } catch (e: Exception) {
            Log.e("Retrotve-OKRu", "Error: ${e.message}")
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
        Log.d("RetrotveProvider", "YourUpload-START: Extracting: $url")
        
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "Referer" to (referer ?: mainUrl)
            )
            
            val response = app.get(url, headers = headers, timeout = 30L)
            val pageHtml = response.text
            
            Log.d("RetrotveProvider", "YourUpload: Response code: ${response.code}, HTML length: ${pageHtml.length}")
            
            val patterns = listOf(
                Pattern.compile("""sources\s*:\s*\[\s*\{\s*file\s*:\s*["']([^"']+)["']"""),
                Pattern.compile("""file\s*:\s*["']([^"']+\.(?:mp4|m3u8)[^"']*)["']"""),
                Pattern.compile("""https?://[^"'\\]+\.(?:mp4|m3u8)[^"'\\]*""")
            )
            
            for (pattern in patterns) {
                val matcher = pattern.matcher(pageHtml)
                if (matcher.find()) {
                    var videoUrl = matcher.group(1)?.replace("\\", "") ?: matcher.group()
                    
                    if (videoUrl != null) {
                        if (videoUrl.startsWith("//")) {
                            videoUrl = "https:$videoUrl"
                        }
                        
                        if (videoUrl.startsWith("http")) {
                            Log.d("RetrotveProvider", "YourUpload: Found: $videoUrl")
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
            
            Log.d("RetrotveProvider", "YourUpload: No direct video URL found")
            
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "YourUpload: Error: ${e.message}")
            e.printStackTrace()
        }
    }
}
