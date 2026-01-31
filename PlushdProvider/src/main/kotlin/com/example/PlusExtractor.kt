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

            val jsKeywords = setOf(
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
                "stretching", "download", "minochinos", "vidhide", "datalayer"
            )

            // Buscar TODOS los dominios posibles (palabras largas solo minúsculas)
            val domainCandidates = words.filter { word ->
                word.length >= 15 &&
                        word.matches(Regex("[a-z]+")) &&
                        word.lowercase() !in jsKeywords
            }
            Log.d("Callistanise", "Domain candidates: $domainCandidates")

            // Buscar TODOS los TLDs posibles
            val tldCandidates = words.filter { it in listOf("store", "shop", "cfd", "sbs", "com", "net") }
            Log.d("Callistanise", "TLD candidates: $tldCandidates")

            // Candidatos con mayúsculas Y minúsculas
            val mixedCandidates = words.filter { word ->
                word.length in 10..20 &&
                        word.matches(Regex("[a-zA-Z0-9]+")) &&
                        word.any { it.isUpperCase() } &&
                        word.any { it.isLowerCase() } &&
                        word.lowercase() !in jsKeywords &&
                        !word.startsWith("tt") &&
                        !word.contains(videoId, ignoreCase = true)
            }

            Log.d("Callistanise", "Mixed candidates: $mixedCandidates")

            if (mixedCandidates.size < 2) {
                Log.e("Callistanise", "No hay suficientes candidatos")
                return
            }

            // Ordenar por índice
            val indices = mixedCandidates.map { candidate ->
                Pair(candidate, words.indexOf(candidate))
            }.sortedBy { it.second }

            val token = indices[0].first
            val subdomain = indices[1].first

            Log.d("Callistanise", "Token: $token")
            Log.d("Callistanise", "Subdomain: $subdomain")

            // Path number
            val pathNumber = words.find { it.matches(Regex("0\\d{4}")) } ?: "02145"

            // Detectar formato: buscar si hay 'h' cerca de 'l' y 'n' en el diccionario
            // Si el índice de 'h' está entre los índices de 'l' y 'n', usar formato con h
            val indexL = words.indexOf("l")
            val indexN = words.indexOf("n")
            val indexH = words.indexOf("h")
            val useH = indexH > 0 && indexH > indexL && indexH < indexN + 5
            val fileFormat = if (useH) "_,l,n,h," else "_,l,n,"
            Log.d("Callistanise", "File format: $fileFormat (indexL=$indexL, indexN=$indexN, indexH=$indexH)")

            // Intentar construir URLs con diferentes combinaciones de dominio/tld
            var hlsUrl: String? = null
            var workingDomain: String? = null
            var workingTld: String? = null

            // Probar cada combinación
            for (domain in domainCandidates) {
                for (tld in tldCandidates) {
                    val testUrl = "https://${subdomain.lowercase()}.$domain.$tld/$token/hls3/01/$pathNumber/${videoId}${fileFormat}.urlset/master.txt"
                    Log.d("Callistanise", "Probando: $testUrl")

                    try {
                        val testResponse = app.get(testUrl, headers = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                            "Referer" to "https://callistanise.com/"
                        ), timeout = 5)

                        if (testResponse.code == 200) {
                            hlsUrl = testUrl
                            workingDomain = domain
                            workingTld = tld
                            Log.d("Callistanise", "✅ URL válida encontrada!")
                            break
                        }
                    } catch (e: Exception) {
                        // Continuar probando
                    }
                }
                if (hlsUrl != null) break
            }

            // Si no encontró con el formato actual, probar con el otro formato
            if (hlsUrl == null) {
                val altFormat = if (useH) "_,l,n," else "_,l,n,h,"
                Log.d("Callistanise", "Probando formato alternativo: $altFormat")

                for (domain in domainCandidates) {
                    for (tld in tldCandidates) {
                        val testUrl = "https://${subdomain.lowercase()}.$domain.$tld/$token/hls3/01/$pathNumber/${videoId}${altFormat}.urlset/master.txt"

                        try {
                            val testResponse = app.get(testUrl, headers = mapOf(
                                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                "Referer" to "https://callistanise.com/"
                            ), timeout = 5)

                            if (testResponse.code == 200) {
                                hlsUrl = testUrl
                                workingDomain = domain
                                workingTld = tld
                                Log.d("Callistanise", "✅ URL válida con formato alternativo!")
                                break
                            }
                        } catch (e: Exception) {
                            // Continuar
                        }
                    }
                    if (hlsUrl != null) break
                }
            }

            if (hlsUrl == null) {
                Log.e("Callistanise", "No se encontró URL válida")
                return
            }

            Log.d("Callistanise", "URL FINAL: $hlsUrl")

            // Subtítulos - buscar en la página
            val subtitleFile = words.find { it.contains(videoId) && it.contains("_spa") }
            if (subtitleFile != null && workingDomain != null && workingTld != null) {
                val subUrl = "https://${subdomain.lowercase()}.$workingDomain.$workingTld/$token/hls3/01/$pathNumber/${subtitleFile}.vtt"
                Log.d("Callistanise", "📝 Probando subtítulo: $subUrl")

                try {
                    val subResponse = app.get(subUrl, headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                        "Referer" to "https://callistanise.com/"
                    ), timeout = 5)

                    if (subResponse.code == 200 && subResponse.text.contains("WEBVTT")) {
                        Log.d("Callistanise", "✅ Subtítulo válido!")
                        subtitleCallback.invoke(newSubtitleFile(lang = "Español", url = subUrl))
                    } else {
                        Log.d("Callistanise", "⚠️ Subtítulo no válido o vacío")
                    }
                } catch (e: Exception) {
                    Log.d("Callistanise", "⚠️ Error cargando subtítulo: ${e.message}")
                }
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