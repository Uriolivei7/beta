package com.example

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import java.net.URI
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DoramasLatinoXExtractor : ExtractorApi() {
    override val name = "DoramasLatinoX"
    override val mainUrl = "https://doramasfoxito.p2pplay.online"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        try {
            val headers = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0",
                "Referer" to (referer ?: mainUrl)
            )
            val hash = extractHash(url)
            if (hash.isBlank()) {
                Log.e("DoramasLX", "No se pudo extraer hash de: $url")
                return
            }
            val baseurl = getBaseUrl(url)

            Log.d("DoramasLX", "hash=$hash baseurl=$baseurl")
            val encoded = app.get("$baseurl/api/v1/video?id=$hash", headers = headers).text.trim()
            if (encoded.isBlank()) {
                Log.e("DoramasLX", "API response vacía")
                return
            }

            val key = "kiemtienmua911ca"
            val ivList = listOf("1234567890oiuytr", "0123456789abcdef")

            val decryptedText = ivList.firstNotNullOfOrNull { iv ->
                try { AesHelper.decryptAES(encoded, key, iv) }
                catch (_: Exception) { null }
            } ?: run {
                Log.e("DoramasLX", "Fallo decrypt con todos los IVs")
                return
            }

            val m3u8 = Regex(""""source"\s*:\s*"(.*?)"""").find(decryptedText)
                ?.groupValues?.get(1)
                ?.replace("\\/", "/")
                ?.trim()

            if (m3u8.isNullOrBlank()) {
                Log.e("DoramasLX", "No se encontró source en texto decryptado")
                return
            }

            Log.d("DoramasLX", "m3u8 encontrado: $m3u8")
            callback.invoke(
                newExtractorLink(
                    this.name,
                    this.name,
                    m3u8,
                    type = ExtractorLinkType.M3U8
                ) {
                    this.referer = url
                    this.quality = getQualityFromName(m3u8)
                }
            )
        } catch (e: Exception) {
            Log.e("DoramasLX", "Error en getUrl: ${e.message}")
        }
    }

    private fun extractHash(url: String): String {
        val afterHash = url.substringAfterLast("#")
        return if (afterHash != url) {
            // URL tenía #: /HASH → HASH
            afterHash.removePrefix("/")
        } else {
            // Sin #: intentar último segmento
            url.substringAfterLast("/").substringBefore("?")
        }
    }

    private fun getBaseUrl(url: String): String {
        return try {
            URI(url).let { "${it.scheme}://${it.host}" }
        } catch (e: Exception) {
            Log.e("DoramasLX", "getBaseUrl fallback: ${e.message}")
            mainUrl
        }
    }
}

object AesHelper {
    private const val TRANSFORMATION = "AES/CBC/PKCS5PADDING"

    fun decryptAES(inputHex: String, key: String, iv: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
        val ivSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        val decryptedBytes = cipher.doFinal(inputHex.hexToByteArray())
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun String.hexToByteArray(): ByteArray {
        check(length % 2 == 0) { "Hex string must have an even length" }
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
}