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
            val doc = app.get(url, referer = referer).document
            val pageHtml = doc.html()
            
            Log.d("Retrotve-Sendvid", "Page HTML length: ${pageHtml.length}")
            
            val videoSrcRegex = Pattern.compile("""src\s*[=:]\s*["']([^"']+\.mp4[^"']*)["']""")
            val matcher = videoSrcRegex.matcher(pageHtml)
            
            if (matcher.find()) {
                val videoUrl = matcher.group(1)?.replace("\\", "")
                Log.d("Retrotve-Sendvid", "Found direct video URL: $videoUrl")
                if (videoUrl != null && videoUrl.startsWith("http")) {
                    callback.invoke(
                        newExtractorLink(name, name, videoUrl) {
                            this.referer = url
                            this.type = ExtractorLinkType.VIDEO
                        }
                    )
                    return
                }
            }
            
            val sourcesRegex = Pattern.compile("""sources\s*:\s*\[\s*\{[^}]*file\s*:\s*["']([^"']+)["']""")
            val sourcesMatcher = sourcesRegex.matcher(pageHtml)
            if (sourcesMatcher.find()) {
                val videoUrl = sourcesMatcher.group(1)?.replace("\\", "")
                Log.d("Retrotve-Sendvid", "Found from sources: $videoUrl")
                if (videoUrl != null && videoUrl.startsWith("http")) {
                    callback.invoke(
                        newExtractorLink(name, name, videoUrl) {
                            this.referer = url
                            this.type = ExtractorLinkType.VIDEO
                        }
                    )
                    return
                }
            }
            
            val videoRegex = Pattern.compile("""["']video_url["']\s*:\s*["']([^"']+)["']""")
            val videoMatcher = videoRegex.matcher(pageHtml)
            if (videoMatcher.find()) {
                val videoUrl = videoMatcher.group(1)?.replace("\\", "")
                Log.d("Retrotve-Sendvid", "Found from video_url: $videoUrl")
                if (videoUrl != null && videoUrl.startsWith("http")) {
                    callback.invoke(
                        newExtractorLink(name, name, videoUrl) {
                            this.referer = url
                            this.type = ExtractorLinkType.VIDEO
                        }
                    )
                    return
                }
            }
            
            val scriptTags = doc.select("script")
            for (script in scriptTags) {
                val scriptContent = script.html()
                if (scriptContent.contains("mp4") || scriptContent.contains("video")) {
                    val mp4Regex = Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*""")
                    val mp4Matcher = mp4Regex.matcher(scriptContent)
                    if (mp4Matcher.find()) {
                        val videoUrl = mp4Matcher.group()
                        Log.d("Retrotve-Sendvid", "Found in script: $videoUrl")
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
        Log.d("Retrotve-Filemoon", "Extracting: $url")
        
        try {
            val doc = app.get(url, referer = referer ?: mainUrl).document
            val pageHtml = doc.html()
            
            Log.d("Retrotve-Filemoon", "Page HTML length: ${pageHtml.length}")
            
            val sourcesRegex = Pattern.compile("""sources\s*:\s*\[\s*\{[^}]*file\s*:\s*["']([^"']+)["']""")
            val matcher = sourcesRegex.matcher(pageHtml)
            
            if (matcher.find()) {
                var videoUrl = matcher.group(1)?.replace("\\", "")
                Log.d("Retrotve-Filemoon", "Found sources: $videoUrl")
                
                if (videoUrl != null && videoUrl.startsWith("http")) {
                    callback.invoke(
                        newExtractorLink(name, name, videoUrl) {
                            this.referer = url
                            this.type = ExtractorLinkType.M3U8
                        }
                    )
                    return
                }
            }
            
            val xstreamRegex = Pattern.compile("""xstream\s*[=:]\s*["']([^"']+)["']""")
            val xstreamMatcher = xstreamRegex.matcher(pageHtml)
            if (xstreamMatcher.find()) {
                var videoUrl = xstreamMatcher.group(1)?.replace("\\", "")
                Log.d("Retrotve-Filemoon", "Found xstream: $videoUrl")
                
                if (videoUrl != null && videoUrl.startsWith("http")) {
                    callback.invoke(
                        newExtractorLink(name, name, videoUrl) {
                            this.referer = url
                            this.type = ExtractorLinkType.M3U8
                        }
                    )
                    return
                }
            }
            
            val masterRegex = Pattern.compile("""master\.m3u8[^"'\\]*""")
            val masterMatcher = masterRegex.matcher(pageHtml)
            if (masterMatcher.find()) {
                val baseUrl = url.substringBefore("/e/")
                val masterPath = masterMatcher.group()
                val fullUrl = if (masterPath.startsWith("http")) masterPath else "$baseUrl/$masterPath"
                Log.d("Retrotve-Filemoon", "Found master: $fullUrl")
                
                callback.invoke(
                    newExtractorLink(name, name, fullUrl) {
                        this.referer = url
                        this.type = ExtractorLinkType.M3U8
                    }
                )
                return
            }
            
            val scriptTags = doc.select("script")
            for (script in scriptTags) {
                val scriptContent = script.html()
                if (scriptContent.contains("sources") || scriptContent.contains("file:")) {
                    val fileRegex = Pattern.compile("""file\s*:\s*["']([^"']+)["']""")
                    val fileMatcher = fileRegex.matcher(scriptContent)
                    if (fileMatcher.find()) {
                        var videoUrl = fileMatcher.group(1)?.replace("\\", "")
                        if (videoUrl != null && videoUrl.startsWith("http")) {
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
        Log.d("Retrotve-VKVideo", "Extracting: $url")
        
        try {
            val vkHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Referer" to "https://vk.com/"
            )
            
            val doc = app.get(url, headers = vkHeaders).document
            val pageHtml = doc.html()
            
            Log.d("Retrotve-VKVideo", "Page HTML length: ${pageHtml.length}")
            
            for (quality in listOf("url720", "url480", "url360")) {
                val pattern = Pattern.compile("""${quality}\s*[=:]\s*["']([^"']+)["']""")
                val patternMatcher = pattern.matcher(pageHtml)
                if (patternMatcher.find()) {
                    val videoUrl = patternMatcher.group(1)?.replace("\\", "")
                    Log.d("Retrotve-VKVideo", "Found $quality: $videoUrl")
                    if (videoUrl != null && videoUrl.startsWith("http")) {
                        val qualityValue = when (quality) {
                            "url720" -> Qualities.P720.value
                            "url480" -> Qualities.P480.value
                            else -> Qualities.P360.value
                        }
                        callback.invoke(
                            newExtractorLink(name, "$name $quality", videoUrl) {
                                this.referer = url
                                this.type = ExtractorLinkType.VIDEO
                                this.quality = qualityValue
                            }
                        )
                        return
                    }
                }
            }
            
            val mp4Direct = Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*""")
            val mp4Matcher = mp4Direct.matcher(pageHtml)
            if (mp4Matcher.find()) {
                val videoUrl = mp4Matcher.group()
                Log.d("Retrotve-VKVideo", "Found direct mp4: $videoUrl")
                callback.invoke(
                    newExtractorLink(name, name, videoUrl) {
                        this.referer = url
                        this.type = ExtractorLinkType.VIDEO
                    }
                )
                return
            }
            
            val m3u8Pattern = Pattern.compile("""https?://[^"'\\]+m3u8[^"'\\]*""")
            val m3u8Matcher = m3u8Pattern.matcher(pageHtml)
            if (m3u8Matcher.find()) {
                val videoUrl = m3u8Matcher.group()
                Log.d("Retrotve-VKVideo", "Found m3u8: $videoUrl")
                callback.invoke(
                    newExtractorLink(name, name, videoUrl) {
                        this.referer = url
                        this.type = ExtractorLinkType.M3U8
                    }
                )
                return
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
        Log.d("Retrotve-OKRu", "Extracting: $url")
        
        try {
            val okHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Referer" to "https://ok.ru/"
            )
            
            val doc = app.get(url, headers = okHeaders).document
            val pageHtml = doc.html()
            
            Log.d("Retrotve-OKRu", "Page HTML length: ${pageHtml.length}")
            
            val moviePlayer = doc.selectFirst("div[data-module='HTML5Player']")?.attr("data-options")
            if (moviePlayer != null) {
                Log.d("Retrotve-OKRu", "Found movie player data")
                val metadataUrl = Regex("""moviesJsonUrl\s*:\s*"([^"]+)""").find(moviePlayer)?.groupValues?.get(1)
                if (metadataUrl != null) {
                    Log.d("Retrotve-OKRu", "Metadata URL: $metadataUrl")
                    val metadataJson = app.get(metadataUrl, headers = okHeaders).text
                    val videoUrl = Regex("""hd\s*\d*\s*:\s*"([^"]+)""").find(metadataJson)?.groupValues?.get(1)
                        ?: Regex("""sd\s*\d*\s*:\s*"([^"]+)""").find(metadataJson)?.groupValues?.get(1)
                    if (videoUrl != null) {
                        val cleanUrl = videoUrl.replace("\\", "")
                        Log.d("Retrotve-OKRu", "Found video URL: $cleanUrl")
                        callback.invoke(
                            newExtractorLink(name, name, cleanUrl) {
                                this.referer = url
                                this.type = ExtractorLinkType.VIDEO
                            }
                        )
                        return
                    }
                }
            }
            
            val mp4Regex = Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*""")
            val matcher = mp4Regex.matcher(pageHtml)
            if (matcher.find()) {
                val videoUrl = matcher.group()
                Log.d("Retrotve-OKRu", "Found direct mp4: $videoUrl")
                callback.invoke(
                    newExtractorLink(name, name, videoUrl) {
                        this.referer = url
                        this.type = ExtractorLinkType.VIDEO
                    }
                )
                return
            }
            
            val m3u8Regex = Pattern.compile("""https?://[^"'\\]+m3u8[^"'\\]*""")
            val m3u8Matcher = m3u8Regex.matcher(pageHtml)
            if (m3u8Matcher.find()) {
                val videoUrl = m3u8Matcher.group()
                Log.d("Retrotve-OKRu", "Found m3u8: $videoUrl")
                callback.invoke(
                    newExtractorLink(name, name, videoUrl) {
                        this.referer = url
                        this.type = ExtractorLinkType.M3U8
                    }
                )
                return
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
        Log.d("Retrotve-YourUpload", "Extracting: $url")
        
        try {
            val doc = app.get(url, referer = referer ?: mainUrl).document
            val pageHtml = doc.html()
            
            Log.d("Retrotve-YourUpload", "Page HTML length: ${pageHtml.length}")
            
            val sourcesRegex = Pattern.compile("""sources\s*:\s*\[\s*\{[^}]*file\s*:\s*["']([^"']+)["']""")
            val matcher = sourcesRegex.matcher(pageHtml)
            
            if (matcher.find()) {
                var videoUrl = matcher.group(1)?.replace("\\", "")
                Log.d("Retrotve-YourUpload", "Found sources: $videoUrl")
                
                if (videoUrl != null && videoUrl.startsWith("http")) {
                    callback.invoke(
                        newExtractorLink(name, name, videoUrl) {
                            this.referer = url
                            this.type = ExtractorLinkType.VIDEO
                        }
                    )
                    return
                }
            }
            
            val mp4Regex = Pattern.compile("""https?://[^"'\\]+\.mp4[^"'\\]*""")
            val mp4Matcher = mp4Regex.matcher(pageHtml)
            if (mp4Matcher.find()) {
                val videoUrl = mp4Matcher.group()
                Log.d("Retrotve-YourUpload", "Found direct mp4: $videoUrl")
                callback.invoke(
                    newExtractorLink(name, name, videoUrl) {
                        this.referer = url
                        this.type = ExtractorLinkType.VIDEO
                    }
                )
                return
            }
            
            Log.d("Retrotve-YourUpload", "No direct video URL found")
            
        } catch (e: Exception) {
            Log.e("Retrotve-YourUpload", "Error: ${e.message}")
            e.printStackTrace()
        }
    }
}
