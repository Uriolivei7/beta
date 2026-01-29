package com.example

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.fixUrl
import com.lagradost.cloudstream3.utils.getAndUnpack
import org.jsoup.nodes.Element

class Vimeos : ExtractorApi() {
    override val name = "Vimeos"
    override val mainUrl = "https://vimeos.net"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val embedUrl = getEmbedUrl(url)
        Log.i("LaMovie", "LOG: Cargando Embed de Vimeos -> $embedUrl")

        val res = app.get(embedUrl, referer = referer)
        val doc = res.document

        // Intentamos desempaquetar el JS (p,a,c,k,e,d)
        val unpackedJs = unpackJs(doc)

        if (unpackedJs.isNullOrBlank()) {
            Log.e("LaMovie", "LOG ERROR: No se pudo desempaquetar el JS de Vimeos")
            return
        }

        // 1. Extraer Video M3U8
        val videoUrl = Regex("""file\s*:\s*"([^"]+\.m3u8[^"]*)"""").find(unpackedJs)?.groupValues?.get(1)

        // 2. Extraer Subtítulos (Vimeos suele ponerlos como .vtt)
        val subUrl = Regex("""file\s*:\s*"([^"]+\.vtt[^"]*)"""").find(unpackedJs)?.groupValues?.get(1)

        if (!subUrl.isNullOrBlank()) {
            Log.i("LaMovie", "LOG: Subtítulo encontrado en Vimeos -> $subUrl")
            subtitleCallback.invoke(SubtitleFile("Español", subUrl))
        }

        if (videoUrl != null) {
            Log.i("LaMovie", "LOG: Generando enlaces M3U8 para Vimeos")
            M3u8Helper.generateM3u8(
                this.name,
                fixUrl(videoUrl),
                "$mainUrl/",
            ).forEach(callback)
        } else {
            Log.e("LaMovie", "LOG ERROR: No se encontró enlace de video en el JS desempaquetado")
        }
    }

    private fun unpackJs(doc: org.jsoup.nodes.Document): String? {
        // Buscamos el script que contiene el eval
        val script = doc.select("script").find { it.data().contains("eval(function(p,a,c,k,e,d)") }
        return if (script != null) {
            Log.i("LaMovie", "LOG: Script empaquetado detectado, procediendo a Unpack")
            getAndUnpack(script.data())
        } else {
            // Si no hay eval, devolvemos el contenido de los scripts normales por si acaso
            doc.select("script").joinToString("\n") { it.data() }
        }
    }

    private fun getEmbedUrl(url: String): String {
        return if (!url.contains("/embed-")) {
            val videoId = url.substringAfterLast("/")
            "$mainUrl/embed-$videoId"
        } else {
            url
        }
    }
}

class GoodstreamExtractor : ExtractorApi() {
    override var name = "Goodstream"
    override val mainUrl = "https://goodstream.one"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.i("LaMovie", "LOG: Extrayendo de Goodstream -> $url")
        val doc = app.get(url).document

        doc.select("script").forEach { script ->
            val data = script.data()
            if (data.contains("file:")) {
                val urlRegex = Regex("""file\s*:\s*"([^"]+\.m3u8[^"]*)"""")
                urlRegex.find(data)?.groupValues?.get(1)?.let { link ->
                    Log.i("LaMovie", "LOG: Enlace M3U8 encontrado en Goodstream")
                    M3u8Helper.generateM3u8(
                        this.name,
                        fixUrl(link),
                        "$mainUrl/",
                    ).forEach(callback)
                }
            }
        }
    }
}