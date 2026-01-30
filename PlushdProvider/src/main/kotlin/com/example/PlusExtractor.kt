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

// ============ PELISPLUS BASE ============
open class PelisPlusBase : VidStack() {
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val fixedReferer = "https://pelisplus.upns.pro/"
        super.getUrl(url, fixedReferer, subtitleCallback, callback)
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

// ============ EMTURBOVID ============
class EmturbovidCom : Filesim() {
    override var mainUrl = "https://emturbovid.com"
    override var name = "Plus"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("EmturbovidCom", "URL recibida: $url")
        try {
            // Intentar con URL original primero
            super.getUrl(url, referer ?: mainUrl, subtitleCallback, callback)
        } catch (e: Exception) {
            Log.e("EmturbovidCom", "Error con URL original, intentando alternativa: ${e.message}")
            val fixedUrl = url.replace("emturbovid.com", "turbovid.eu").replace("/e/", "/v/")
            super.getUrl(fixedUrl, "https://turbovid.eu/", subtitleCallback, callback)
        }
    }
}

// ============ VIDHIDE ============
class Vidhide : VidHidePro() {
    override var mainUrl = "https://vidhidepro.com"
    override var name = "Vidhide Pro"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("Vidhide", "Extrayendo: $url")
        super.getUrl(url, "https://vidhidepro.com/", subtitleCallback, callback)
    }
}

// ============ LISTEAMED ============
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
        Log.d("Listeamed", "Extrayendo: $url")

        try {
            val response = app.get(url, referer = referer)
            val html: String = response.text

            // Buscar file:"..." o file:'...' en el HTML
            val sourceRegex = Regex("""file:\s*["']([^"']+)["']""")
            val match = sourceRegex.find(html)
            val videoUrl: String? = match?.groupValues?.get(1)

            if (!videoUrl.isNullOrBlank()) {
                Log.d("Listeamed", "✅ Video encontrado: $videoUrl")

                val linkType = if (videoUrl.contains(".m3u8")) {
                    ExtractorLinkType.M3U8
                } else {
                    ExtractorLinkType.VIDEO
                }

                callback.invoke(
                    newExtractorLink(
                        source = name,
                        name = name,
                        url = videoUrl,
                        type = linkType
                    ) {
                        this.referer = url
                        this.quality = Qualities.Unknown.value
                    }
                )
            } else {
                Log.w("Listeamed", "⚠️ No se encontró URL de video")
            }
        } catch (e: Exception) {
            Log.e("Listeamed", "❌ Error: ${e.message}")
        }
    }
}