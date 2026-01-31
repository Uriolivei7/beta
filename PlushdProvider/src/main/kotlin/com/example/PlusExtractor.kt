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
        Log.d("Callistanise", "========== INICIO EXTRACCION ==========")
        Log.d("Callistanise", "URL entrada: $url")

        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Referer" to (referer ?: "https://tioplus.app/")
            )

            val response = app.get(url, headers = headers).text
            Log.d("Callistanise", "Response length: ${response.length}")

            val videoId = url.substringAfterLast("/").substringBefore("?")
            Log.d("Callistanise", "Video ID: $videoId")

            // MÉTODO 1: Buscar URL directa en el HTML (a veces está sin ofuscar)
            val directUrlPattern = Regex("""(https?://[a-zA-Z0-9]+\.riverstonelearninghub\.[a-z]+/[^"'\s]+master\.[^"'\s]+)""")
            val directMatch = directUrlPattern.find(response)

            if (directMatch != null) {
                val hlsUrl = directMatch.value.replace("\\", "")
                Log.d("Callistanise", "URL DIRECTA ENCONTRADA: $hlsUrl")

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
                return
            }

            // MÉTODO 2: Buscar en el diccionario packed
            val dictRegex = Regex("""'([^']+)'\.split\('\|'\)\)\)""")
            val dictMatch = dictRegex.find(response)

            if (dictMatch == null) {
                Log.e("Callistanise", "NO SE ENCONTRO DICCIONARIO")
                return
            }

            val words = dictMatch.groupValues[1].split("|")
            Log.d("Callistanise", "Palabras totales: ${words.size}")

            // Buscar el subdomain: debe tener formato como "rnzt2t4xvku08"
            // - Solo minúsculas y números
            // - Longitud entre 12-16 caracteres
            // - Contiene tanto letras como números mezclados
            val subdomain = words.find { word ->
                word.length in 12..20 &&
                        word.matches(Regex("[a-z0-9]+")) &&
                        word.count { it.isDigit() } >= 2 &&
                        word.count { it.isLetter() } >= 4 &&
                        !word.contains("file") &&
                        !word.contains("track") &&
                        !word.contains("player") &&
                        !word.contains("source")
            }

            // Buscar el token: formato como "LFDu7HStkKAt"
            // - Mezcla de mayúsculas, minúsculas y números
            // - Longitud entre 10-14 caracteres
            val token = words.find { word ->
                word.length in 10..16 &&
                        word.matches(Regex("[a-zA-Z0-9]+")) &&
                        word.any { it.isUpperCase() } &&
                        word.any { it.isLowerCase() } &&
                        word != subdomain &&
                        !word.contains("Tracks") &&
                        !word.contains("File") &&
                        !word.contains("Player")
            }

            Log.d("Callistanise", "Subdomain encontrado: $subdomain")
            Log.d("Callistanise", "Token encontrado: $token")

            // Si no encontró, buscar alternativas
            if (subdomain == null || token == null) {
                // Buscar cualquier palabra que parezca un hash/token largo
                val possibleTokens = words.filter {
                    it.length >= 10 &&
                            it.matches(Regex("[a-zA-Z0-9]+")) &&
                            !it.matches(Regex("[a-z]+")) && // No solo minúsculas
                            !it.contains("File") &&
                            !it.contains("Track") &&
                            !it.contains("Player") &&
                            !it.contains("source")
                }
                Log.d("Callistanise", "Tokens posibles: ${possibleTokens.take(10)}")

                val possibleSubdomains = words.filter {
                    it.length in 10..20 &&
                            it.matches(Regex("[a-z0-9]+")) &&
                            it.any { c -> c.isDigit() }
                }
                Log.d("Callistanise", "Subdomains posibles: ${possibleSubdomains.take(10)}")
            }

            if (subdomain == null || token == null) {
                Log.e("Callistanise", "No se pudo extraer subdomain o token")
                return
            }

            // Path numbers - buscar específicamente 01 y 02145
            val path1 = words.find { it == "01" } ?: "01"
            val path2 = words.find { it == "02145" } ?: words.find { it.matches(Regex("0\\d{4}")) } ?: "02145"

            Log.d("Callistanise", "Path1: $path1, Path2: $path2")

            val hlsUrl = "https://$subdomain.riverstonelearninghub.sbs/$token/hls3/$path1/$path2/${videoId}_,l,n,.urlset/master.txt"

            Log.d("Callistanise", "========================================")
            Log.d("Callistanise", "URL FINAL: $hlsUrl")
            Log.d("Callistanise", "========================================")

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
            e.printStackTrace()
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