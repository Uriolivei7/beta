package com.example

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.newSubtitleFile
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

            val dictRegex = Regex("""'([^']+)'\.split\('\|'\)\)\)""")
            val dictMatch = dictRegex.find(response)

            if (dictMatch == null) {
                Log.e("Callistanise", "No se encontró diccionario")
                return
            }

            val words = dictMatch.groupValues[1].split("|")
            Log.d("Callistanise", "Total palabras: ${words.size}")

            val jsKeywords = setOf(
                "errorcode", "scripttag", "seekdone", "addbutton", "nextindex",
                "parseint", "domparser", "reloadkey", "codematch", "dualsound",
                "addclass", "hasclass", "getconfig", "tostring", "samesite",
                "ajaxsetup", "function", "settings", "jwplayer", "document",
                "position", "expanded", "location", "controls", "captions",
                "undefined", "focusable", "protocol", "mousedown", "download11",
                "surface1", "sdkloader", "complete", "mosevura", "unescape",
                "pop3done", "vastdone2", "vastdone1", "aboutlink", "abouttext",
                "insecure", "vpaidmode", "progress", "duration", "minochinos",
                "background", "googleapis", "callistanise", "application",
                "advertising", "transparent", "thumbnails", "androidhls",
                "timeslider", "controlbar", "stretching", "currentfile",
                "audiotracks", "decodedlink", "settimeout", "shouldswitch",
                "textcontent", "startswith", "localstorage", "codefrommessage",
                "errormessage", "switchedlink", "errorcount", "appendchild",
                "createelement", "getaudiotracks", "removeclass", "currenttime",
                "lasterrortime", "createlayer", "parsefromstring",
                "setcurrentaudiotrack", "playbackrates", "getplaylistitem",
                "currenttracks", "insertafter", "getposition", "currenttrack",
                "audiotrackchanged", "toggleclass", "firstframe", "networkerror",
                "fragloaderror", "removechild", "parentnode", "setattribute",
                "innerwidth", "innerheight", "togmtstring", "createcookiesec",
                "queryselector", "pickdirect", "documentelement",
                "encodeuricomponent", "playbackratecontrols", "qualitylabels",
                "backgroundopacity", "backgroundcolor", "fontfamily",
                "fontopacity", "userfontscale", "fullscreenorientationlock",
                "download", "vidhide", "datalayer", "dramiyos", "master",
                "urlset", "hls3", "hls", "sources", "tracks", "default",
                "label", "kind", "file", "type"
            )

            // TLDs válidos
            val validTlds = listOf("cyou", "space", "cfd", "sbs", "store", "shop", "com", "net", "site", "xyz")
            val tldCandidates = words.filter { it in validTlds }

            // Path numbers - formato 0XXXX (5 dígitos empezando con 0)
            // Estos son los paths reales del servidor
            val pathNumbers = words.filter { it.matches(Regex("0\\d{4}")) }
            Log.d("Callistanise", "Path numbers (0XXXX): $pathNumbers")

            // Dominios LARGOS sin números
            val longDomains = words.filter { word ->
                word.length in 15..30 &&
                        word.matches(Regex("[a-z]+")) &&
                        word.lowercase() !in jsKeywords
            }

            // Dominios/subdominios con números
            val shortWithNumbers = words.filter { word ->
                word.length in 8..15 &&
                        word.matches(Regex("[a-z0-9]+")) &&
                        word.any { it.isDigit() } &&
                        word.any { it.isLetter() } &&
                        word.lowercase() !in jsKeywords &&
                        word != videoId &&
                        !word.startsWith("tt") &&
                        !word.startsWith("0")
            }

            // Mixed case con números
            val mixedWithNumbers = words.filter { word ->
                word.length in 10..20 &&
                        word.matches(Regex("[a-zA-Z0-9]+")) &&
                        word.any { it.isUpperCase() } &&
                        word.any { it.isLowerCase() } &&
                        word.any { it.isDigit() } &&
                        word.lowercase() !in jsKeywords
            }

            // Mixed case sin números pero aleatorio
            val mixedWithoutNumbers = words.filter { word ->
                word.length in 10..18 &&
                        word.matches(Regex("[a-zA-Z]+")) &&
                        word.any { it.isUpperCase() } &&
                        word.any { it.isLowerCase() } &&
                        word.lowercase() !in jsKeywords &&
                        word.count { it.isUpperCase() } >= 2 &&
                        !word.matches(Regex("^[a-z]+[A-Z][a-z]+$"))
            }

            val allMixedCandidates = (mixedWithNumbers + mixedWithoutNumbers).distinct()

            Log.d("Callistanise", "Dominios largos: $longDomains")
            Log.d("Callistanise", "Cortos con números: $shortWithNumbers")
            Log.d("Callistanise", "Mixed: $allMixedCandidates")

            // Determinar token y subdomain
            val token: String
            val subdomain: String
            val domainCandidates: List<String>

            when {
                allMixedCandidates.size >= 2 -> {
                    val sorted = allMixedCandidates.map { Pair(it, words.indexOf(it)) }.sortedBy { it.second }
                    token = sorted[0].first
                    subdomain = sorted[1].first
                    domainCandidates = longDomains + shortWithNumbers
                }
                allMixedCandidates.size == 1 && shortWithNumbers.isNotEmpty() -> {
                    token = allMixedCandidates[0]
                    subdomain = shortWithNumbers.first()
                    domainCandidates = longDomains + shortWithNumbers.drop(1)
                }
                else -> {
                    Log.e("Callistanise", "No hay suficientes candidatos")
                    return
                }
            }

            Log.d("Callistanise", "Token: $token")
            Log.d("Callistanise", "Subdomain: $subdomain")

            if (domainCandidates.isEmpty() && shortWithNumbers.isEmpty()) {
                Log.e("Callistanise", "No hay dominios")
                return
            }

            val formats = listOf("_,l,n,h,", "_,l,n,")
            var hlsUrl: String? = null
            var workingDomain: String? = null
            var workingTld: String? = null
            var workingPath: String? = null

            val domainsToTry = if (domainCandidates.isNotEmpty()) domainCandidates else shortWithNumbers

            // Priorizar paths que empiezan con 0 (formato 0XXXX)
            val pathsToTry = pathNumbers.ifEmpty {
                words.filter { it.matches(Regex("\\d{5}")) }.take(10)
            }

            Log.d("Callistanise", "Probando - Dominios: ${domainsToTry.size}, TLDs: ${tldCandidates.size}, Paths: ${pathsToTry.size}")

            outerLoop@ for (domain in domainsToTry.take(5)) {
                for (tld in tldCandidates) {
                    for (pathNumber in pathsToTry.take(8)) {
                        for (format in formats) {
                            val testUrl = "https://${subdomain.lowercase()}.$domain.$tld/$token/hls3/01/$pathNumber/${videoId}${format}.urlset/master.txt"

                            try {
                                val testResponse = app.get(testUrl, headers = mapOf(
                                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                    "Referer" to "https://callistanise.com/"
                                ), timeout = 3)

                                if (testResponse.code == 200 && testResponse.text.contains("#EXTM3U")) {
                                    hlsUrl = testUrl
                                    workingDomain = domain
                                    workingTld = tld
                                    workingPath = pathNumber
                                    Log.d("Callistanise", "✅ URL válida: $hlsUrl")
                                    break@outerLoop
                                }
                            } catch (e: Exception) {
                                // Continuar
                            }
                        }
                    }
                }
            }

            if (hlsUrl == null) {
                Log.e("Callistanise", "No se encontró URL válida")
                return
            }

            Log.d("Callistanise", "URL FINAL: $hlsUrl")

            // Subtítulos
            val subtitleFile = words.find { it.contains(videoId) && it.contains("_spa") }
            if (subtitleFile != null && workingDomain != null && workingTld != null && workingPath != null) {
                val subUrl = "https://${subdomain.lowercase()}.$workingDomain.$workingTld/$token/hls3/01/$workingPath/${subtitleFile}.vtt"
                try {
                    val subResponse = app.get(subUrl, headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                        "Referer" to "https://callistanise.com/"
                    ), timeout = 3)

                    if (subResponse.code == 200 && subResponse.text.contains("WEBVTT")) {
                        subtitleCallback.invoke(newSubtitleFile(lang = "Español", url = subUrl))
                    }
                } catch (e: Exception) { }
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