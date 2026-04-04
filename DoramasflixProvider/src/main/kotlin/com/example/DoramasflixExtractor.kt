package com.example

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink

/*
    CEO: Ranita
    Empresa: RANITA TECH
    Versión software: 1.0     fecha: 09/08/2025
*/

class OkRuExtractor : ExtractorApi() {
    override val name = "Ok.ru"
    override val mainUrl = "https://ok.ru"
    override val requiresReferer = true

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("OkRu", "Iniciando extracción de: $url")
        try {
            val response = app.get(url, referer = "$mainUrl/")
            val doc = response.document

            val scriptData = doc.select("script").find { it.data().contains("m3u8") || it.data().contains("flashvars") }
                ?.data()
            
            Log.d("OkRu", "Script data: ${scriptData?.take(300)}")

            val m3u8 = scriptData?.let {
                Regex(""""md_preload":"([^"]+)"""").find(it)?.groupValues?.get(1)
                    ?: Regex(""""hlsMasterUrl":"([^"]+)"""").find(it)?.groupValues?.get(1)
                    ?: Regex("""file:\s*"(https?://[^"]+\.m3u8[^"]*)"""").find(it)?.groupValues?.get(1)
            }

            if (m3u8 != null) {
                Log.d("OkRu", "M3U8 encontrado: ${m3u8.take(100)}")
                callback.invoke(
                    newExtractorLink(this.name, this.name, m3u8, ExtractorLinkType.M3U8) {
                        this.referer = "$mainUrl/"
                        this.quality = Qualities.Unknown.value
                    }
                )
            } else {
                Log.e("OkRu", "No se encontró M3U8 en la página")
            }
        } catch (e: Exception) {
            Log.e("OkRu", "Error: ${e.message}")
        }
    }
}

class Do7GoExtractor : ExtractorApi() {
    override val name = "Do7Go"
    override val mainUrl = "https://do7go.com"
    override val requiresReferer = true

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("Do7Go", "Iniciando extracción de: $url")
        try {
            val response = app.get(url, referer = "$mainUrl/")
            val doc = response.document

            val scriptData = doc.select("script").find { it.data().contains("sources") }
                ?.data()
            
            Log.d("Do7Go", "Script data: ${scriptData?.take(300)}")

            val videoUrl = scriptData?.let {
                Regex("""sources\s*:\s*\[\{file:\s*"([^"]+)"""").find(it)?.groupValues?.get(1)
                    ?: Regex("""file:\s*"(https?://[^"]+\.m3u8[^"]*)"""").find(it)?.groupValues?.get(1)
            }

            if (videoUrl != null) {
                Log.d("Do7Go", "Video URL encontrado: ${videoUrl.take(100)}")
                callback.invoke(
                    newExtractorLink(this.name, this.name, videoUrl, ExtractorLinkType.M3U8) {
                        this.referer = "$mainUrl/"
                        this.quality = Qualities.Unknown.value
                    }
                )
            } else {
                Log.e("Do7Go", "No se encontró video URL")
            }
        } catch (e: Exception) {
            Log.e("Do7Go", "Error: ${e.message}")
        }
    }
}