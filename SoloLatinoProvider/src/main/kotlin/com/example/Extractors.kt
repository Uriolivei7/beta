package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class FilemoonExtractor : ExtractorApi() {
    override var mainUrl = "https://filemoon.sx"
    override var name = "Filemoon"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("SoloLatino", "[Filemoon] URL recibida: $url")
        Log.d("SoloLatino", "[Filemoon] Referer: $referer")

        val response = app.get(url, referer = referer)
        Log.d("SoloLatino", "[Filemoon] HTML length: ${response.text.length}")

        val script = response.document.selectFirst("script:containsData(eval)")?.data()
            ?: response.document.selectFirst("script:containsData(wurl)")?.data()
            ?: response.document.selectFirst("script:containsData(sources:)")?.data()
            ?: run {
                Log.e("SoloLatino", "[Filemoon] No se encontró script con datos de video")
                return
            }

        Log.d("SoloLatino", "[Filemoon] Script length: ${script.length}")

        val sources = Regex("""sources:\s*\[\{file:\s*"([^"]+)"""")
            .find(script)?.groupValues?.get(1)
            ?: Regex("""file:\s*"([^"]+\.m3u8[^"]*)"""")
            .find(script)?.groupValues?.get(1)
            ?: Regex("""wurl\s*=\s*"([^"]+)"""")
            .find(script)?.groupValues?.get(1)
            ?: run {
                Log.e("SoloLatino", "[Filemoon] No se pudo extraer URL del script")
                return
            }

        Log.d("SoloLatino", "[Filemoon] Stream URL encontrado: $sources")

        callback.invoke(
            ExtractorLink(
                source = name,
                name = name,
                url = sources,
                referer = mainUrl,
                quality = Qualities.Unknown.value,
                type = ExtractorLinkType.M3U8
            )
        )
    }
}

class VidhideExtractor : ExtractorApi() {
    override var mainUrl = "https://vidhidepro.com"
    override var name = "Vidhide"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("SoloLatino", "[Vidhide] URL recibida: $url")
        Log.d("SoloLatino", "[Vidhide] Referer: $referer")

        val response = app.get(url, referer = referer)
        Log.d("SoloLatino", "[Vidhide] HTML length: ${response.text.length}")

        val script = response.document.select("script").find { it.data().contains("sources:") }?.data()
            ?: response.document.select("script").find { it.data().contains("file:") }?.data()
            ?: run {
                Log.e("SoloLatino", "[Vidhide] No se encontró script con datos de video")
                return
            }

        Log.d("SoloLatino", "[Vidhide] Script length: ${script.length}")

        val fileUrl = Regex("""file:\s*"([^"]+)"""")
            .find(script)?.groupValues?.get(1)
            ?: Regex("""sources:\s*\[\{file:\s*"([^"]+)"""")
            .find(script)?.groupValues?.get(1)
            ?: run {
                Log.e("SoloLatino", "[Vidhide] No se pudo extraer URL del script")
                return
            }

        Log.d("SoloLatino", "[Vidhide] Stream URL encontrado: $fileUrl")

        val isM3u8 = fileUrl.contains(".m3u8")
        callback.invoke(
            ExtractorLink(
                source = name,
                name = name,
                url = fileUrl,
                referer = mainUrl,
                quality = Qualities.Unknown.value,
                type = if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
            )
        )
    }
}

class StreamTapeExtractor : ExtractorApi() {
    override var mainUrl = "https://streamtape.cc"
    override var name = "StreamTape"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("SoloLatino", "[StreamTape] URL recibida: $url")
        Log.d("SoloLatino", "[StreamTape] Referer: $referer")

        val response = app.get(url, referer = referer)
        Log.d("SoloLatino", "[StreamTape] HTML length: ${response.text.length}")

        val script = response.document.select("script").find { it.data().contains("getElementById('robotlink')") }?.data()
            ?: run {
                Log.e("SoloLatino", "[StreamTape] No se encontró script con datos de video")
                return
            }

        Log.d("SoloLatino", "[StreamTape] Script length: ${script.length}")

        val videoUrl = Regex("""innerHTML = '([^']+)'\s*\+\s*'\s*'""")
            .find(script)?.groupValues?.get(1)
            ?: Regex("""document\.getElementById\('robotlink'\)\.innerHTML = '([^']+)'""")
            .find(script)?.groupValues?.get(1)
            ?: run {
                Log.e("SoloLatino", "[StreamTape] No se pudo extraer URL del script")
                return
            }

        val finalUrl = "https:$videoUrl"
        Log.d("SoloLatino", "[StreamTape] Stream URL encontrado: $finalUrl")

        callback.invoke(
            ExtractorLink(
                source = name,
                name = name,
                url = finalUrl,
                referer = mainUrl,
                quality = Qualities.Unknown.value,
                type = ExtractorLinkType.VIDEO
            )
        )
    }
}

class DoodStreamExtractor : ExtractorApi() {
    override var mainUrl = "https://dood.la"
    override var name = "DoodStream"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("SoloLatino", "[DoodStream] URL recibida: $url")
        Log.d("SoloLatino", "[DoodStream] Referer: $referer")

        val response = app.get(url, referer = referer)
        Log.d("SoloLatino", "[DoodStream] HTML length: ${response.text.length}")

        val doodHost = Regex("https://(.*?)/").find(url)?.groupValues?.get(1) ?: mainUrl.replace("https://", "")
        val ds = response.document.select("script").find { it.data().contains("passMD5") }?.data()
            ?: run {
                Log.e("SoloLatino", "[DoodStream] No se encontró script passMD5")
                return
            }

        Log.d("SoloLatino", "[DoodStream] Script passMD5 found")

        val videoUrl = Regex("""\[''\]\.slice\(1\)\)\s*\+\s*'([^']+)""")
            .find(ds)?.groupValues?.get(1)
            ?: run {
                Log.e("SoloLatino", "[DoodStream] No se pudo extraer URL del script")
                return
            }

        val pass = Regex("""passMD5='([^']+)'""").find(ds)?.groupValues?.get(1) ?: ""
        val finalUrl = "https://$doodHost$videoUrl$pass"
        Log.d("SoloLatino", "[DoodStream] Stream URL encontrado: $finalUrl")

        callback.invoke(
            ExtractorLink(
                source = name,
                name = name,
                url = finalUrl,
                referer = mainUrl,
                quality = Qualities.Unknown.value,
                type = ExtractorLinkType.VIDEO
            )
        )
    }
}

class UqloadExtractor : ExtractorApi() {
    override var mainUrl = "https://uqload.com"
    override var name = "Uqload"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("SoloLatino", "[Uqload] URL recibida: $url")
        Log.d("SoloLatino", "[Uqload] Referer: $referer")

        val response = app.get(url, referer = referer)
        Log.d("SoloLatino", "[Uqload] HTML length: ${response.text.length}")

        val script = response.document.select("script").find { it.data().contains("sources:") }?.data()
            ?: run {
                Log.e("SoloLatino", "[Uqload] No se encontró script con datos de video")
                return
            }

        Log.d("SoloLatino", "[Uqload] Script length: ${script.length}")

        val fileUrl = Regex("""sources:\s*\["([^"]+)"""")
            .find(script)?.groupValues?.get(1)
            ?: run {
                Log.e("SoloLatino", "[Uqload] No se pudo extraer URL del script")
                return
            }

        Log.d("SoloLatino", "[Uqload] Stream URL encontrado: $fileUrl")

        callback.invoke(
            ExtractorLink(
                source = name,
                name = name,
                url = fileUrl,
                referer = mainUrl,
                quality = Qualities.Unknown.value,
                type = ExtractorLinkType.VIDEO
            )
        )
    }
}

class LulustreamExtractor : ExtractorApi() {
    override var mainUrl = "https://lulustream.com"
    override var name = "Lulustream"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("SoloLatino", "[Lulustream] URL recibida: $url")
        Log.d("SoloLatino", "[Lulustream] Referer: $referer")

        val response = app.get(url, referer = referer)
        Log.d("SoloLatino", "[Lulustream] HTML length: ${response.text.length}")

        val script = response.document.select("script").find { it.data().contains("sources:") || it.data().contains("file:") }?.data()
            ?: run {
                Log.e("SoloLatino", "[Lulustream] No se encontró script con datos de video")
                return
            }

        Log.d("SoloLatino", "[Lulustream] Script length: ${script.length}")

        val fileUrl = Regex("""file:\s*"([^"]+)"""")
            .find(script)?.groupValues?.get(1)
            ?: Regex("""sources:\s*\[\{file:\s*"([^"]+)"""")
            .find(script)?.groupValues?.get(1)
            ?: run {
                Log.e("SoloLatino", "[Lulustream] No se pudo extraer URL del script")
                return
            }

        val isM3u8 = fileUrl.contains(".m3u8")
        Log.d("SoloLatino", "[Lulustream] Stream URL encontrado: $fileUrl (isM3u8=$isM3u8)")

        callback.invoke(
            ExtractorLink(
                source = name,
                name = name,
                url = fileUrl,
                referer = mainUrl,
                quality = Qualities.Unknown.value,
                type = if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
            )
        )
    }
}

class WatchSBExtractor : ExtractorApi() {
    override var mainUrl = "https://watchsb.com"
    override var name = "WatchSB"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("SoloLatino", "[WatchSB] URL recibida: $url")
        Log.d("SoloLatino", "[WatchSB] Referer: $referer")

        val response = app.get(url, referer = referer)
        Log.d("SoloLatino", "[WatchSB] HTML length: ${response.text.length}")

        val script = response.document.select("script").find { it.data().contains("sources:") }?.data()
            ?: run {
                Log.e("SoloLatino", "[WatchSB] No se encontró script con datos de video")
                return
            }

        Log.d("SoloLatino", "[WatchSB] Script length: ${script.length}")

        val fileUrl = Regex("""file:\s*"([^"]+)"""")
            .find(script)?.groupValues?.get(1)
            ?: Regex("""sources:\s*\[\{file:\s*"([^"]+)"""")
            .find(script)?.groupValues?.get(1)
            ?: run {
                Log.e("SoloLatino", "[WatchSB] No se pudo extraer URL del script")
                return
            }

        Log.d("SoloLatino", "[WatchSB] Stream URL encontrado: $fileUrl")

        callback.invoke(
            ExtractorLink(
                source = name,
                name = name,
                url = fileUrl,
                referer = mainUrl,
                quality = Qualities.Unknown.value,
                type = ExtractorLinkType.M3U8
            )
        )
    }
}
