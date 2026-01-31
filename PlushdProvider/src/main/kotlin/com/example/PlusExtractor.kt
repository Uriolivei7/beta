package com.example

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink

class Callistanise : ExtractorApi() {
    override var mainUrl = "https://callistanise.com"
    override var name = "Earnvids"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("Callistanise", "========== INICIO ==========")

        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Referer" to (referer ?: "https://tioplus.app/")
            )

            val response = app.get(url, headers = headers).text
            val videoId = url.substringAfterLast("/").substringBefore("?")
            Log.d("Callistanise", "Video ID: $videoId")

            // ══════════════════════════════════════════════════════════
            // EXTRAER SUBTÍTULOS
            // ══════════════════════════════════════════════════════════

            // Patrón para buscar tracks/subtítulos en el HTML
            // Ejemplo: {file:"/dl?...","label":"Español","kind":"captions"}
            val subtitlePatterns = listOf(
                Regex("""\{[^}]*file\s*:\s*["']([^"']+)["'][^}]*label\s*:\s*["']([^"']+)["'][^}]*kind\s*:\s*["']captions["'][^}]*\}"""),
                Regex("""\{[^}]*kind\s*:\s*["']captions["'][^}]*file\s*:\s*["']([^"']+)["'][^}]*label\s*:\s*["']([^"']+)["'][^}]*\}"""),
                Regex("""file\s*:\s*["']([^"']+\.vtt[^"']*)["']"""),
                Regex("""file\s*:\s*["']([^"']+\.srt[^"']*)["']"""),
                Regex("""["']?file["']?\s*:\s*["']([^"']+(?:caption|subtitle|sub)[^"']*)["']""", RegexOption.IGNORE_CASE)
            )

            // Buscar subtítulos en el HTML
            for (pattern in subtitlePatterns) {
                val matches = pattern.findAll(response)
                for (match in matches) {
                    val subUrl = match.groupValues.getOrElse(1) { "" }
                    val subLabel = match.groupValues.getOrElse(2) { "Español" }

                    if (subUrl.isNotEmpty() && (subUrl.contains("vtt") || subUrl.contains("srt") || subUrl.contains("caption") || subUrl.contains("dl?"))) {
                        val fullSubUrl = if (subUrl.startsWith("http")) {
                            subUrl
                        } else if (subUrl.startsWith("/")) {
                            "$mainUrl$subUrl"
                        } else {
                            "$mainUrl/$subUrl"
                        }

                        Log.d("Callistanise", "📝 Subtítulo encontrado: $subLabel -> $fullSubUrl")

                        subtitleCallback.invoke(
                            SubtitleFile(
                                lang = subLabel,
                                url = fullSubUrl
                            )
                        )
                    }
                }
            }

            // También buscar en el diccionario packed por URLs de subtítulos
            val dictRegex = Regex("""'([^']+)'\.split\('\|'\)\)\)""")
            val dictMatch = dictRegex.find(response)

            if (dictMatch != null) {
                val words = dictMatch.groupValues[1].split("|")

                // Buscar palabras que parezcan rutas de subtítulos
                words.forEach { word ->
                    if (word.contains("_spa") || word.contains("_sub") || word.contains("caption")) {
                        Log.d("Callistanise", "📝 Posible subtítulo en diccionario: $word")
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // EXTRAER VIDEO (código existente)
            // ══════════════════════════════════════════════════════════

            if (dictMatch == null) {
                Log.e("Callistanise", "No se encontró diccionario")
                return
            }

            val words = dictMatch.groupValues[1].split("|")

            val candidates = words.filter {
                it.length >= 10 &&
                        it.matches(Regex("[a-zA-Z0-9]+"))
            }

            val ignoreWords = setOf(
                "currentfile", "audiotracks", "decodedlink", "settimeout", "shouldswitch",
                "textcontent", "startswith", "localstorage", "codefrommessage", "errormessage",
                "switchedlink", "errorcount", "appendchild", "createelement", "getaudiotracks",
                "removeclass", "currenttime", "lasterrortime", "createlayer", "parsefromstring",
                "setcurrentaudiotrack", "playbackrates", "getplaylistitem", "currenttracks",
                "insertafter", "getposition", "currenttrack", "audiotrackchanged", "toggleclass",
                "firstframe", "networkerror", "fragloaderror", "removechild", "parentnode",
                "background", "setattribute", "innerwidth", "innerheight", "googleapis",
                "callistanise", "togmtstring", "createcookiesec", "queryselector", "pickdirect",
                "documentelement", "encodeuricomponent", "application", "playbackratecontrols",
                "qualitylabels", "advertising", "backgroundopacity", "transparent",
                "backgroundcolor", "fontfamily", "fontopacity", "userfontscale", "thumbnails",
                "androidhls", "timeslider", "controlbar", "fullscreenorientationlock",
                "stretching", "riverstonelearninghub", "download11", "minochinos",
                "systemdocumentation"
            )

            val token = candidates.find { word ->
                word.length in 10..16 &&
                        word.any { it.isUpperCase() } &&
                        word.any { it.isLowerCase() } &&
                        word.lowercase() !in ignoreWords &&
                        !word.startsWith("tt") &&
                        !word.contains(videoId, ignoreCase = true)
            }

            val subdomain = candidates.find { word ->
                word.length in 12..20 &&
                        word.any { it.isDigit() } &&
                        word.any { it.isLetter() } &&
                        word.lowercase() !in ignoreWords &&
                        word != token &&
                        !word.startsWith("tt") &&
                        !word.contains(videoId, ignoreCase = true) &&
                        word.count { it.isDigit() } >= 1 &&
                        word.count { it.isDigit() } <= 5
            }

            Log.d("Callistanise", "Token: $token")
            Log.d("Callistanise", "Subdomain: $subdomain")

            if (token == null || subdomain == null) {
                Log.e("Callistanise", "No se encontró token o subdomain")
                return
            }

            // Construir URL del video
            val hlsUrl = "https://${subdomain.lowercase()}.riverstonelearninghub.sbs/$token/hls3/01/02145/${videoId}_,l,n,.urlset/master.txt"

            Log.d("Callistanise", "URL FINAL: $hlsUrl")

            // También intentar obtener subtítulos del mismo servidor
            val subUrl = "https://${subdomain.lowercase()}.riverstonelearninghub.sbs/$token/hls3/01/02145/${videoId}_spa.vtt"
            try {
                val subResponse = app.get(subUrl, headers = headers)
                if (subResponse.code == 200) {
                    Log.d("Callistanise", "📝 Subtítulo del CDN encontrado: $subUrl")
                    subtitleCallback.invoke(
                        SubtitleFile(
                            lang = "Español",
                            url = subUrl
                        )
                    )
                }
            } catch (e: Exception) {
                Log.d("Callistanise", "No hay subtítulo en CDN")
            }

            callback.invoke(
                newExtractorLink(
                    source = name,
                    name = name,
                    url = hlsUrl,
                    type = ExtractorLinkType.M3U8
                ) {
                    this.referer = "https://callistanise.com/"
                    this.quality = Qualities.Unknown.value
                    this.headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                        "Referer" to "https://callistanise.com/",
                        "Origin" to "https://callistanise.com"
                    )
                }
            )

        } catch (e: Exception) {
            Log.e("Callistanise", "ERROR: ${e.message}")
        }
    }
}

class TurbovidHLS : ExtractorApi() {
    override var mainUrl = "https://turbovidhls.com"
    override var name = "Plus"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("TurbovidHLS", "🔍 Extrayendo: $url")

        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Referer" to (referer ?: "https://tioplus.app/")
            )

            val response = app.get(url, headers = headers).text
            Log.d("TurbovidHLS", "📄 HTML: ${response.length} caracteres")

            val patterns = listOf(
                Regex("""file\s*:\s*["']([^"']+)["']"""),
                Regex(""""file"\s*:\s*"([^"]+)""""),
                Regex("""source\s*:\s*["']([^"']+)["']"""),
                Regex("""(https?://[^\s"'<>]+\.m3u8[^\s"'<>]*)""")
            )

            for (pattern in patterns) {
                val match = pattern.find(response)
                if (match != null) {
                    var videoUrl = match.groupValues.getOrElse(1) { match.value }
                    videoUrl = videoUrl.replace("\\", "").trim()

                    if (videoUrl.startsWith("http")) {
                        Log.d("TurbovidHLS", "✅ VIDEO: $videoUrl")

                        callback.invoke(
                            newExtractorLink(
                                source = name,
                                name = name,
                                url = videoUrl,
                                type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            ) {
                                this.referer = "https://turbovidhls.com/"
                                this.quality = Qualities.Unknown.value
                                this.headers = mapOf(
                                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                                    "Referer" to "https://turbovidhls.com/",
                                    "Origin" to "https://turbovidhls.com"
                                )
                            }
                        )
                        return
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("TurbovidHLS", "❌ Error: ${e.message}")
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// WAAW.TO (Netu)
// ═══════════════════════════════════════════════════════════════
class WaawTo : ExtractorApi() {
    override var mainUrl = "https://waaw.to"
    override var name = "Netu"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("WaawTo", "🔍 Extrayendo: $url")

        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Referer" to (referer ?: mainUrl),
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
            )

            val response = app.get(url, headers = headers).text
            Log.d("WaawTo", "📄 HTML: ${response.length} caracteres")

            val patterns = listOf(
                Regex("""file:\s*["']([^"']+)["']"""),
                Regex("""source:\s*["']([^"']+)["']"""),
                Regex(""""file":\s*"([^"]+)""""),
                Regex("""sources\s*=\s*\[["']([^"']+)["']\]"""),
                Regex("""var\s+\w+\s*=\s*["'](https?://[^"']+\.m3u8[^"']*)["']"""),
                Regex("""https?://[^\s"'<>\\]+\.m3u8[^\s"'<>\\]*"""),
                Regex("""https?://[^\s"'<>\\]+\.mp4[^\s"'<>\\]*""")
            )

            for (pattern in patterns) {
                val matches = pattern.findAll(response)
                for (match in matches) {
                    var videoUrl = match.groupValues.getOrElse(1) { match.value }
                    videoUrl = videoUrl.replace("\\", "").trim()

                    if (videoUrl.startsWith("http") &&
                        (videoUrl.contains(".m3u8") || videoUrl.contains(".mp4")) &&
                        !videoUrl.contains("player") &&
                        !videoUrl.contains("embed")) {

                        Log.d("WaawTo", "✅ VIDEO: $videoUrl")

                        callback.invoke(
                            newExtractorLink(
                                source = name,
                                name = name,
                                url = videoUrl,
                                type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            ) {
                                this.referer = url
                                this.quality = Qualities.Unknown.value
                                this.headers = mapOf(
                                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                                    "Referer" to url,
                                    "Origin" to mainUrl
                                )
                            }
                        )
                        return
                    }
                }
            }

            // Buscar atob (base64 encoded)
            val atobPattern = Regex("""atob\(["']([^"']+)["']\)""")
            val atobMatch = atobPattern.find(response)
            if (atobMatch != null) {
                try {
                    val decoded = String(android.util.Base64.decode(atobMatch.groupValues[1], android.util.Base64.DEFAULT))
                    Log.d("WaawTo", "🔓 Base64 decoded: $decoded")
                    if (decoded.startsWith("http")) {
                        callback.invoke(
                            newExtractorLink(
                                source = name,
                                name = name,
                                url = decoded,
                                type = if (decoded.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            ) {
                                this.referer = url
                                this.quality = Qualities.Unknown.value
                                this.headers = mapOf(
                                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                    "Referer" to url,
                                    "Origin" to mainUrl
                                )
                            }
                        )
                        return
                    }
                } catch (e: Exception) {
                    Log.e("WaawTo", "Error decodificando base64: ${e.message}")
                }
            }

            Log.w("WaawTo", "⚠️ No se encontró video")
            Log.d("WaawTo", "📄 Preview: ${response.take(2000)}")

        } catch (e: Exception) {
            Log.e("WaawTo", "❌ Error: ${e.message}")
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// LISTEAMED (VidG)
// ═══════════════════════════════════════════════════════════════
class Listeamed : ExtractorApi() {
    override var mainUrl = "https://listeamed.net"
    override var name = "VidG"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("Listeamed", "🔍 Extrayendo: $url")

        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Referer" to (referer ?: mainUrl)
            )

            val response = app.get(url, headers = headers).text
            Log.d("Listeamed", "📄 HTML: ${response.length} caracteres")

            val patterns = listOf(
                Regex("""file:\s*["']([^"']+)["']"""),
                Regex("""source:\s*["']([^"']+)["']"""),
                Regex("""src:\s*["']([^"']+\.m3u8[^"']*)["']"""),
                Regex(""""file":\s*"([^"]+)""""),
                Regex("""sources:\s*\[\{[^}]*file:\s*["']([^"']+)["']"""),
                Regex("""https?://[^\s"'<>\\]+\.m3u8[^\s"'<>\\]*""")
            )

            for (pattern in patterns) {
                val match = pattern.find(response)
                if (match != null) {
                    var videoUrl = match.groupValues.getOrElse(1) { match.value }
                    videoUrl = videoUrl.replace("\\", "").trim()

                    if (videoUrl.startsWith("http") && (videoUrl.contains(".m3u8") || videoUrl.contains(".mp4"))) {
                        Log.d("Listeamed", "✅ VIDEO: $videoUrl")

                        callback.invoke(
                            newExtractorLink(
                                source = name,
                                name = name,
                                url = videoUrl,
                                type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            ) {
                                this.referer = url
                                this.quality = Qualities.Unknown.value
                                this.headers = mapOf(
                                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                                    "Referer" to url,
                                    "Origin" to mainUrl
                                )
                            }
                        )
                        return
                    }
                }
            }

            Log.w("Listeamed", "⚠️ No se encontró video")
            Log.d("Listeamed", "📄 Preview: ${response.take(2000)}")

        } catch (e: Exception) {
            Log.e("Listeamed", "❌ Error: ${e.message}")
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// UPNS (UPFAST)
// ═══════════════════════════════════════════════════════════════
class PelisplusUpns : ExtractorApi() {
    override var mainUrl = "https://pelisplus.upns.pro"
    override var name = "UPFAST"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("UPNS", "🔍 Extrayendo: $url")

        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Referer" to mainUrl
            )

            val response = app.get(url, headers = headers).text
            Log.d("UPNS", "📄 HTML: ${response.length} caracteres")

            val patterns = listOf(
                Regex("""file:\s*["']([^"']+)["']"""),
                Regex("""source:\s*["']([^"']+)["']"""),
                Regex(""""sources":\s*\[\s*\{\s*"file":\s*"([^"]+)""""),
                Regex("""https?://[^\s"'<>\\]+\.m3u8[^\s"'<>\\]*""")
            )

            for (pattern in patterns) {
                val match = pattern.find(response)
                if (match != null) {
                    var videoUrl = match.groupValues.getOrElse(1) { match.value }
                    videoUrl = videoUrl.replace("\\", "").trim()

                    if (videoUrl.startsWith("http")) {
                        Log.d("UPNS", "✅ VIDEO: $videoUrl")

                        callback.invoke(
                            newExtractorLink(
                                source = name,
                                name = name,
                                url = videoUrl,
                                type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            ) {
                                this.referer = mainUrl
                                this.quality = Qualities.Unknown.value
                                this.headers = mapOf(
                                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                                    "Referer" to mainUrl
                                )
                            }
                        )
                        return
                    }
                }
            }

            Log.w("UPNS", "⚠️ No se encontró video")
            Log.d("UPNS", "📄 Preview: ${response.take(2000)}")

        } catch (e: Exception) {
            Log.e("UPNS", "❌ Error: ${e.message}")
        }
    }
}