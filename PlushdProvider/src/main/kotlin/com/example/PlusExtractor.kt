package com.example

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.extractors.Filesim
import com.lagradost.cloudstream3.extractors.VidStack
import com.lagradost.cloudstream3.extractors.VidHidePro
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink

open class PelisPlusBase : VidStack() {
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("PelisPlusBase", "🔍 Extrayendo: $url (clase: ${this.name})")
        var found = false
        val fixedReferer = "https://pelisplus.upns.pro/"
        try {
            super.getUrl(url, fixedReferer, subtitleCallback) { link ->
                found = true
                Log.d("PelisPlusBase", "✅ ENLACE REAL: ${link.name} -> ${link.url}")
                callback(link)
            }
        } catch (e: Exception) {
            Log.e("PelisPlusBase", "❌ Error: ${e.message}")
            e.printStackTrace()
        }
        if (!found) {
            Log.w("PelisPlusBase", "⚠️ No se encontró ningún enlace para ${this.name}")
        }
    }
}

class PelisplusUpnsPro : PelisPlusBase() {
    override var mainUrl = "https://pelisplus.upns.pro"
    override var name = "UPFAST"
}

class PelisplusUpnsPro2 : PelisPlusBase() {
    override var mainUrl = "https://pelisplus.strp2p.com"
    override var name = "P2P"
}

class PelisplusUpnsPro3 : PelisPlusBase() {
    override var mainUrl = "https://pelisplusto.4meplayer.pro"
    override var name = "4mePlayer"
}

class RPMStream : PelisPlusBase() {
    override var mainUrl = "https://pelisplus.rpmstream.live"
    override var name = "RPM"
}

class EmturbovidCom : Filesim() {
    override var mainUrl = "https://emturbovid.com"
    override var name = "Plus"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("EmturbovidCom", "🔍 URL recibida: $url")
        var found = false

        try {
            // Intentar con URL original
            super.getUrl(url, referer ?: mainUrl, subtitleCallback) { link ->
                found = true
                Log.d("EmturbovidCom", "✅ ENLACE REAL: ${link.name} -> ${link.url}")
                callback(link)
            }
        } catch (e: Exception) {
            Log.e("EmturbovidCom", "❌ Error con URL original: ${e.message}")
        }

        if (!found) {
            Log.w("EmturbovidCom", "⚠️ Intentando URL alternativa...")
            try {
                val fixedUrl = url.replace("emturbovid.com", "turbovid.eu").replace("/e/", "/v/")
                Log.d("EmturbovidCom", "🔄 URL alternativa: $fixedUrl")
                super.getUrl(fixedUrl, "https://turbovid.eu/", subtitleCallback) { link ->
                    found = true
                    Log.d("EmturbovidCom", "✅ ENLACE REAL (alt): ${link.name} -> ${link.url}")
                    callback(link)
                }
            } catch (e2: Exception) {
                Log.e("EmturbovidCom", "❌ Error con URL alternativa: ${e2.message}")
            }
        }

        if (!found) {
            Log.w("EmturbovidCom", "⚠️ No se encontró ningún enlace")
        }
    }
}

class Vidhide : VidHidePro() {
    override var mainUrl = "https://vidhidepro.com"
    override var name = "Vidhide Pro"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("Vidhide", "🔍 Extrayendo: $url")
        var found = false
        try {
            super.getUrl(url, "https://vidhidepro.com/", subtitleCallback) { link ->
                found = true
                Log.d("Vidhide", "✅ ENLACE REAL: ${link.name} -> ${link.url}")
                callback(link)
            }
        } catch (e: Exception) {
            Log.e("Vidhide", "❌ Error: ${e.message}")
            e.printStackTrace()
        }
        if (!found) {
            Log.w("Vidhide", "⚠️ No se encontró ningún enlace")
        }
    }
}

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
            val response = app.get(url, referer = referer)
            val html: String = response.text

            Log.d("Listeamed", "📄 HTML recibido: ${html.length} caracteres")

            val patterns = listOf(
                Regex("""file:\s*["']([^"']+)["']"""),
                Regex("""source:\s*["']([^"']+)["']"""),
                Regex("""src:\s*["']([^"']+\.m3u8[^"']*)["']"""),
                Regex(""""file":\s*"([^"]+)""""),
                Regex("""sources:\s*\[\{[^}]*file:\s*["']([^"']+)["']"""),
                Regex("""https?://[^"'\s]+\.m3u8[^"'\s]*""")
            )

            for ((index, pattern) in patterns.withIndex()) {
                val match = pattern.find(html)
                val videoUrl: String? = match?.groupValues?.getOrNull(1) ?: match?.value

                Log.d("Listeamed", "🔎 Patrón ${index + 1}: ${if (match != null) "encontrado" else "no encontrado"}")

                if (!videoUrl.isNullOrBlank() && (videoUrl.contains(".m3u8") || videoUrl.contains(".mp4"))) {
                    Log.d("Listeamed", "✅ VIDEO ENCONTRADO: $videoUrl")

                    callback.invoke(
                        newExtractorLink(
                            source = name,
                            name = name,
                            url = videoUrl,
                            type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                        ) {
                            this.referer = url
                            this.quality = Qualities.Unknown.value
                        }
                    )

                    Log.d("Listeamed", "📤 Callback invocado!")
                    return
                }
            }

            Log.w("Listeamed", "⚠️ No se encontró URL de video")

        } catch (e: Exception) {
            Log.e("Listeamed", "❌ Error: ${e.message}")
            e.printStackTrace()
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// CALLISTANISE (El dominio real de Vidhide)
// ═══════════════════════════════════════════════════════════════
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
        Log.d("Callistanise", "🔍 Extrayendo: $url")

        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Referer" to (referer ?: mainUrl)
            )

            val response = app.get(url, headers = headers).text
            Log.d("Callistanise", "📄 HTML: ${response.length} caracteres")

            val patterns = listOf(
                Regex("""file:\s*["']([^"']+\.m3u8[^"']*)["']"""),
                Regex("""source:\s*["']([^"']+)["']"""),
                Regex(""""file":\s*"([^"]+)""""),
                Regex("""sources:\s*\[\{[^}]*file:\s*["']([^"']+)["']"""),
                Regex("""https?://[^\s"'<>]+\.m3u8[^\s"'<>]*""")
            )

            for (pattern in patterns) {
                val match = pattern.find(response)
                if (match != null) {
                    val videoUrl = match.groupValues.getOrElse(1) { match.value }
                    if (videoUrl.startsWith("http") && (videoUrl.contains(".m3u8") || videoUrl.contains(".mp4"))) {
                        Log.d("Callistanise", "✅ VIDEO: $videoUrl")

                        callback.invoke(
                            newExtractorLink(
                                source = name,
                                name = name,
                                url = videoUrl,
                                type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            ) {
                                this.referer = url
                                this.quality = Qualities.Unknown.value
                            }
                        )
                        return
                    }
                }
            }

            Log.w("Callistanise", "⚠️ No se encontró video")
            Log.d("Callistanise", "📄 Response preview: ${response.take(1500)}")

        } catch (e: Exception) {
            Log.e("Callistanise", "❌ Error: ${e.message}")
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// TURBOVIDHLS (El dominio real de EmTurbovid)
// ═══════════════════════════════════════════════════════════════
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
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Referer" to (referer ?: mainUrl)
            )

            val response = app.get(url, headers = headers).text
            Log.d("TurbovidHLS", "📄 HTML: ${response.length} caracteres")

            val patterns = listOf(
                Regex("""file:\s*["']([^"']+)["']"""),
                Regex("""source:\s*["']([^"']+)["']"""),
                Regex(""""file":\s*"([^"]+)""""),
                Regex(""""sources":\s*\[.*?"file":\s*"([^"]+)""""),
                Regex("""var\s+source\s*=\s*["']([^"']+)["']"""),
                Regex("""https?://[^\s"'<>]+\.m3u8[^\s"'<>]*""")
            )

            for (pattern in patterns) {
                val match = pattern.find(response)
                if (match != null) {
                    val videoUrl = match.groupValues.getOrElse(1) { match.value }
                    if (videoUrl.startsWith("http")) {
                        Log.d("TurbovidHLS", "✅ VIDEO: $videoUrl")

                        callback.invoke(
                            newExtractorLink(
                                source = name,
                                name = name,
                                url = videoUrl,
                                type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                            ) {
                                this.referer = url
                                this.quality = Qualities.Unknown.value
                            }
                        )
                        return
                    }
                }
            }

            Log.w("TurbovidHLS", "⚠️ No se encontró video")
            Log.d("TurbovidHLS", "📄 Response preview: ${response.take(1500)}")

        } catch (e: Exception) {
            Log.e("TurbovidHLS", "❌ Error: ${e.message}")
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// WAAW.TO (Netu/HQQ)
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
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Referer" to (referer ?: mainUrl),
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
            )

            val response = app.get(url, headers = headers).text
            Log.d("WaawTo", "📄 HTML: ${response.length} caracteres")

            // Waaw.to a veces usa base64 o tiene el video en un script
            val patterns = listOf(
                Regex("""file:\s*["']([^"']+)["']"""),
                Regex("""source:\s*["']([^"']+)["']"""),
                Regex(""""file":\s*"([^"]+)""""),
                Regex("""sources\s*=\s*\[["']([^"']+)["']\]"""),
                Regex("""https?://[^\s"'<>]+\.m3u8[^\s"'<>]*"""),
                Regex("""https?://[^\s"'<>]+\.mp4[^\s"'<>]*""")
            )

            for (pattern in patterns) {
                val matches = pattern.findAll(response)
                for (match in matches) {
                    val videoUrl = match.groupValues.getOrElse(1) { match.value }
                    if (videoUrl.startsWith("http") && (videoUrl.contains(".m3u8") || videoUrl.contains(".mp4"))) {
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
                            }
                        )
                        return
                    }
                } catch (e: Exception) {
                    Log.e("WaawTo", "Error decodificando base64: ${e.message}")
                }
            }

            Log.w("WaawTo", "⚠️ No se encontró video")
            Log.d("WaawTo", "📄 Response preview: ${response.take(1500)}")

        } catch (e: Exception) {
            Log.e("WaawTo", "❌ Error: ${e.message}")
        }
    }
}