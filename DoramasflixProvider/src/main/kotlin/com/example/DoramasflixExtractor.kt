package com.example

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.loadExtractor
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
                Log.e("Do7Go", "No se encontró video URL - intentando con PlayMogo")
                loadExtractor("https://playmogo.com${url.substringAfter("/e")}", url, subtitleCallback, callback)
            }
        } catch (e: Exception) {
            Log.e("Do7Go", "Error: ${e.message}")
        }
    }
}

class PlayMogoExtractor : ExtractorApi() {
    override val name = "PlayMogo"
    override val mainUrl = "https://playmogo.com"
    override val requiresReferer = true

    private val extractorHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
        "Accept" to "*/*",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
        "sec-ch-ua" to "\"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Brave\";v=\"146\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "empty",
        "sec-fetch-mode" to "cors",
        "sec-fetch-site" to "same-origin",
        "sec-gpc" to "1"
    )

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("PlayMogo", "Iniciando extracción de: $url")
        try {
            val pageRes = app.get(url, headers = extractorHeaders, referer = "$mainUrl/")
            val pageText = pageRes.text
            
            val hashMatch = Regex("""/pass_md5/([^"']+)""").find(pageText)
            val hash = hashMatch?.groupValues?.getOrNull(1)
            
            if (hash != null) {
                Log.d("PlayMogo", "Hash encontrado: ${hash.take(50)}...")
                
                val passMd5Url = "$mainUrl/pass_md5/$hash"
                val passRes = app.post(passMd5Url, headers = extractorHeaders, referer = url)
                
                val m3u8Url = passRes.text.trim()
                Log.d("PlayMogo", "M3U8 URL: ${m3u8Url.take(100)}")
                
                if (m3u8Url.startsWith("http") && (m3u8Url.contains(".m3u8") || m3u8Url.contains("mp4"))) {
                    callback.invoke(
                        newExtractorLink(this.name, this.name, m3u8Url, ExtractorLinkType.M3U8) {
                            this.referer = "$mainUrl/"
                            this.quality = Qualities.Unknown.value
                            this.headers = extractorHeaders
                        }
                    )
                } else {
                    Log.e("PlayMogo", "URL no válida: $m3u8Url")
                }
            } else {
                Log.e("PlayMogo", "No se encontró hash en la página")
            }
        } catch (e: Exception) {
            Log.e("PlayMogo", "Error: ${e.message}")
        }
    }
}