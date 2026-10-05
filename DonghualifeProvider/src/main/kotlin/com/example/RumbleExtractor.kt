package com.example

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink

private const val TAG = "RumbleExt"

class RumbleExtractor : ExtractorApi() {
    override val name = "Rumble"
    override val mainUrl = "https://rumble.com"
    override val requiresReferer = true

    private val ua =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        // Intento 1: headers de navegador. Intento 2: sin headers extra.
        // rumble.com usa Cloudflare con fingerprint de TLS: `requests` de PC da 403
        // pero curl/OkHttp pasan (200), asi que app.get (OkHttp) debe pasar en el dispositivo.
        val html = try {
            app.get(
                url,
                headers = mapOf(
                    "User-Agent" to ua,
                    "Referer" to "https://rumble.com/",
                    "Accept-Language" to "es-ES,es;q=0.9",
                ),
                timeout = 30L,
            ).text
        } catch (e: Exception) {
            Log.w(TAG, "embed falló (${e.message}), reintento sin headers: ${url.take(90)}")
            try {
                app.get(url, timeout = 30L).text
            } catch (e2: Exception) {
                Log.w(TAG, "embed falló de nuevo: ${e2.message}")
                null
            }
        }?.replace("\\/", "/") ?: return

        // 1) HLS master directo del embed: https://rumble.com/hls-vod/{id}/playlist.m3u8
        // (master adaptativo verificado 200, variantes hasta 1440p; segmentos sin headers, TS válido)
        val hls = Regex("""https://rumble\.com/hls-vod/[^"'\s\\]+?playlist\.m3u8""")
            .findAll(html).map { it.value }.distinct().toList()
        for (u in hls) {
            callback(
                newExtractorLink(name, "$name HLS", u, ExtractorLinkType.M3U8) {
                    this.referer = "https://rumble.com/"
                    this.headers = mapOf("User-Agent" to ua, "Referer" to "https://rumble.com/")
                    this.quality = Qualities.Unknown.value
                }
            )
        }
        if (hls.isNotEmpty()) {
            Log.d(TAG, "HLS OK: ${hls.size} master(s)")
            return
        }

        // 2) mp4 progresivos directos de *.rumble.cloud como respaldo
        var n = 0
        Regex("""https://[^"'\s\\]+\.rumble\.cloud[^"'\s\\]*?\.mp4[^"'\s\\]*""")
            .findAll(html).map { it.value }.distinct().forEach { u ->
                callback(
                    newExtractorLink(name, "$name MP4", u, ExtractorLinkType.VIDEO) {
                        this.referer = "https://rumble.com/"
                        this.headers = mapOf("User-Agent" to ua, "Referer" to "https://rumble.com/")
                        this.quality = Qualities.Unknown.value
                    }
                )
                n++
            }
        Log.d(TAG, if (n > 0) "MP4 OK: $n" else "0 URLs (len=${html.length})")
    }
}
