package com.example

import android.net.Uri
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.AcraApplication.Companion.context
import com.lagradost.cloudstream3.utils.*
import kotlinx.serialization.*
import java.io.File

class UniqueStreamProvider : MainAPI() {
    override var mainUrl = "https://anime.uniquestream.net"
    override var name = "AnimeStream"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val apiUrl = "https://anime.uniquestream.net/api/v1"
    private val TAG = "UniqueStream"

    private val baseHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Accept" to "application/json"
    )

    private fun SeriesItem.toSearchResponse(): SearchResponse {
        return newAnimeSearchResponse(this.title, this.content_id) {
            this.posterUrl = image
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        return try {
            Log.d(TAG, "Cargando MainPage...")

            val response = app.get(
                "$apiUrl/search?query=&limit=20&order_by=popular",
                headers = baseHeaders,
                timeout = 30L
            ).text

            val data = AppUtils.parseJson<SearchRoot>(response)
            val homeItems = mutableListOf<HomePageList>()

            val seriesList = data.series?.map {
                newAnimeSearchResponse(it.title, it.content_id) {
                    this.posterUrl = it.image
                }
            }

            if (!seriesList.isNullOrEmpty()) {
                homeItems.add(HomePageList("Series Destacadas", seriesList))
            }

            newHomePageResponse(homeItems, homeItems.isNotEmpty())
        } catch (e: Exception) {
            Log.e(TAG, "Error en getMainPage: ${e.message}")
            newHomePageResponse(emptyList(), false)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return try {
            val response = app.get(
                "$apiUrl/search?query=$query",
                headers = baseHeaders,
                timeout = 30L
            ).text

            val data = AppUtils.parseJson<SearchRoot>(response)
            data.series?.map { it.toSearchResponse() } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error en search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val cleanId = url.split("/").lastOrNull { it.isNotBlank() } ?: url
        Log.d(TAG, "Cargando serie con ID: $cleanId")

        val seriesResponse = app.get(
            "$apiUrl/series/$cleanId",
            headers = baseHeaders,
            timeout = 30L
        ).text

        val details = AppUtils.parseJson<DetailsResponse>(seriesResponse)
        val episodesList = mutableListOf<Episode>()
        val processedSeasonIds = mutableSetOf<String>()

        details.seasons?.forEach { season ->
            if (processedSeasonIds.contains(season.content_id)) return@forEach
            processedSeasonIds.add(season.content_id)

            var page = 1
            var keepLoading = true

            while (keepLoading) {
                try {
                    val seasonUrl = "$apiUrl/season/${season.content_id}/episodes?page=$page&limit=20&order_by=asc"
                    val response = app.get(seasonUrl, headers = baseHeaders, timeout = 30L).text

                    if (response.trim().startsWith("[")) {
                        val eps = AppUtils.parseJson<List<EpisodeItem>>(response)

                        if (eps.isEmpty()) {
                            keepLoading = false
                        } else {
                            eps.forEach { ep ->
                                if (ep.is_clip != true) {
                                    episodesList.add(newEpisode(ep.content_id) {
                                        this.name = ep.title
                                        this.episode = ep.episode_number?.toInt()
                                        this.season = season.season_number
                                        this.posterUrl = ep.image
                                    })
                                }
                            }

                            if (eps.size < 20) {
                                keepLoading = false
                            } else {
                                page++
                            }
                        }
                    } else {
                        keepLoading = false
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error en paginación: ${e.message}")
                    keepLoading = false
                }
            }
        }

        Log.d(TAG, "Total episodios cargados: ${episodesList.size}")

        return newAnimeLoadResponse(details.title ?: "Sin Título", url, TvType.Anime) {
            this.posterUrl = details.images?.find { it.type == "poster_tall" }?.url
            this.plot = details.description
            this.tags = (details.audio_locales ?: emptyList()) + (details.subtitle_locales ?: emptyList())
            addEpisodes(DubStatus.Subbed, episodesList)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val episodeId = if (data.contains("/")) {
            data.substringAfterLast("/").trim()
        } else {
            data.trim()
        }

        Log.d(TAG, "========================================")
        Log.d(TAG, "Episode ID: $episodeId")
        Log.d(TAG, "========================================")

        return try {
            val watchUrl = "$mainUrl/watch/$episodeId"

            // Visitar página watch para obtener cookies
            Log.d(TAG, "Visitando página watch...")
            val watchHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
            )

            app.get(watchUrl, headers = watchHeaders, timeout = 30L)

            // Probar diferentes locales
            val locales = listOf("es-419", "en-US", "ja-JP")
            var linksEnviados = 0

            for (locale in locales) {
                try {
                    val mediaUrl = "$apiUrl/episode/$episodeId/media/dash/$locale"

                    val apiHeaders = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                        "Accept" to "*/*",
                        "Referer" to watchUrl,
                        "Origin" to mainUrl
                    )

                    val response = app.get(mediaUrl, headers = apiHeaders, timeout = 30L)

                    if (response.code == 200) {
                        Log.d(TAG, "✓ API 200 para $locale")

                        val videoData = AppUtils.parseJson<VideoResponse>(response.text)

                        // Debug: Ver qué contiene la respuesta
                        Log.d(TAG, "DEBUG - DASH disponible: ${videoData.versions?.dash != null}")
                        Log.d(TAG, "DEBUG - HLS disponible: ${videoData.versions?.hls != null}")
                        Log.d(TAG, "DEBUG - Cantidad DASH: ${videoData.versions?.dash?.size ?: 0}")
                        Log.d(TAG, "DEBUG - Cantidad HLS: ${videoData.versions?.hls?.size ?: 0}")

                        val dashVersions = videoData.versions?.dash ?: emptyList()
                        val hlsVersions = mutableListOf<HlsVersion>()
                        videoData.versions?.hls?.let { hlsVersions.addAll(it) }
                        videoData.hls?.let { hlsVersions.add(it) }

                        // DASH
                        if (dashVersions.isNotEmpty()) {
                            Log.d(TAG, "Procesando ${dashVersions.size} versiones DASH")
                            dashVersions.forEach { dashVersion ->
                                if (dashVersion.playlist.isNotBlank()) {
                                    Log.d(TAG, "✓ DASH ${dashVersion.locale}")
                                    callback(
                                        newExtractorLink(
                                            source = this.name,
                                            name = "${this.name} - ${dashVersion.locale.uppercase()}",
                                            url = dashVersion.playlist,
                                            type = ExtractorLinkType.DASH
                                        ) {
                                            this.quality = Qualities.Unknown.value
                                            this.referer = "$mainUrl/"
                                            this.headers = mapOf(
                                                "Accept" to "*/*",
                                                "Origin" to mainUrl,
                                                "Referer" to "$mainUrl/"
                                            )
                                        }
                                    )
                                    linksEnviados++
                                }
                            }
                        }

                        // HLS — descargar master y enviar variantes directo a ExoPlayer
                        if (hlsVersions.isNotEmpty()) {
                            Log.d(TAG, "Procesando ${hlsVersions.size} versiones HLS")
                            for (hlsVersion in hlsVersions) {
                                if (hlsVersion.playlist.isNotBlank()) {
                                    try {
                                        // Descargar master.m3u8 para obtener las variantes
                                        val masterResp = app.get(hlsVersion.playlist, headers = mapOf(
                                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                            "Accept" to "*/*",
                                            "Origin" to mainUrl,
                                            "Referer" to "$mainUrl/"
                                        ), timeout = 20L)

                                        if (masterResp.code != 200) {
                                            Log.w(TAG, "Master ${hlsVersion.locale} falló: ${masterResp.code}")
                                            continue
                                        }

                                        val masterText = masterResp.text
                                        val masterBase = hlsVersion.playlist.substringBeforeLast("/").substringBefore("?")

                                        // Extraer líneas de variante
                                        val variantLines = masterText.lines().filter { line ->
                                            !line.startsWith("#") && line.isNotBlank() && !line.startsWith("http")
                                        }

                                        if (variantLines.isEmpty()) {
                                            Log.w(TAG, "${hlsVersion.locale}: sin variantes en master")
                                            continue
                                        }

                                        for (variantLine in variantLines) {
                                            val variantUrl = "$masterBase/$variantLine"
                                            val quality = when {
                                                variantLine.contains("1920") -> "1080p"
                                                variantLine.contains("1280") -> "720p"
                                                else -> "Auto"
                                            }
                                            val qualityValue = when (quality) {
                                                "1080p" -> Qualities.P1080.value
                                                "720p" -> Qualities.P720.value
                                                else -> Qualities.Unknown.value
                                            }

                                            Log.d(TAG, "Descargando variant playlist: $variantUrl")
                                            val variantResp = app.get(variantUrl, headers = mapOf(
                                                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                                "Accept" to "*/*",
                                                "Origin" to mainUrl,
                                                "Referer" to "$mainUrl/"
                                            ), timeout = 20L)

                                            if (variantResp.code != 200) {
                                                Log.w(TAG, "Variant ${hlsVersion.locale} $quality falló: ${variantResp.code}")
                                                continue
                                            }

                                            // Reescribir playlist con URLs absolutos
                                            val variantBase = variantUrl.substringBeforeLast("/")
                                            val variantDir = variantBase.substringBeforeLast("/")
                                            val variantText = variantResp.text
                                            var rewrittenPlaylist = variantText.lines().joinToString("\n") { line ->
                                                val trimmed = line.trim()
                                                when {
                                                    trimmed.startsWith("#EXT-X-KEY:") && trimmed.contains("URI=") -> {
                                                        val uriMatch = Regex("""URI=["']([^"']+)["']""").find(trimmed)
                                                        if (uriMatch != null) {
                                                            val keyUri = uriMatch.groupValues[1]
                                                            val absoluteKey = if (keyUri.startsWith("http")) keyUri else {
                                                                if (keyUri.startsWith("../")) {
                                                                    variantDir + keyUri.removePrefix("..")
                                                                } else {
                                                                    "$variantBase/$keyUri"
                                                                }
                                                            }
                                                            trimmed.replace(uriMatch.groupValues[0], "URI=\"$absoluteKey\"")
                                                        } else trimmed
                                                    }
                                                    !trimmed.startsWith("#") && trimmed.isNotBlank() -> {
                                                        if (trimmed.startsWith("http")) trimmed else "$variantBase/$trimmed"
                                                    }
                                                    else -> line
                                                }
                                            }

                                            // Log primeras líneas del playlist
                                            Log.d(TAG, "Playlist reescrito (primeras 15 líneas):")
                                            rewrittenPlaylist.lines().take(15).forEach { Log.d(TAG, "  | $it") }

                                            // Guardar en caché local
                                            val cacheDir = context?.cacheDir ?: continue

                                            // Descargar key, decodificar y servir local
                                            val keyUrlMatch = Regex("""URI="(https?://[^"]+)"""").find(rewrittenPlaylist)
                                            if (keyUrlMatch != null) {
                                                val keyUrl = keyUrlMatch.groupValues[1]
                                                try {
                                                    val keyResp = app.get(keyUrl, headers = mapOf(
                                                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                                        "Accept" to "*/*"
                                                    ), timeout = 15L)
                                                    if (keyResp.code == 200) {
                                                        val keyBytes = keyResp.body?.bytes() ?: keyResp.text.toByteArray()
                                                        Log.d(TAG, "Key descargado: ${keyBytes.size} bytes")

                                                        val decodedKey = when {
                                                            keyBytes.size == 16 || keyBytes.size == 32 -> keyBytes
                                                            else -> {
                                                                val text = keyBytes.decodeToString().trim()
                                                                // Intentar hex
                                                                val clean = text.filter { !it.isWhitespace() }
                                                                if (clean.length % 2 == 0 && clean.all { it.isLetterOrDigit() }) {
                                                                    clean.chunked(2).mapNotNull {
                                                                        it.toIntOrNull(16)?.toByte()
                                                                    }.toByteArray().takeIf { it.size == 16 || it.size == 32 }
                                                                } else {
                                                                    // Intentar base64
                                                                    try {
                                                                        val raw = android.util.Base64.decode(clean, android.util.Base64.DEFAULT)
                                                                        raw.takeIf { it.size == 16 || it.size == 32 }
                                                                    } catch (_: Exception) { null }
                                                                }
                                                            }
                                                        }

                                                        if (decodedKey != null) {
                                                            Log.d(TAG, "Key decodificado: ${decodedKey.size} bytes")
                                                            val hexDump = decodedKey.joinToString("") { "%02x".format(it) }
                                                            Log.d(TAG, "Key hex: $hexDump")
                                                            val keyFile = File(cacheDir, "key_${hlsVersion.locale}_$quality.bin")
                                                            keyFile.writeBytes(decodedKey)
                                                            val keyLocalUri = Uri.fromFile(keyFile).toString()
                                                            rewrittenPlaylist = rewrittenPlaylist.replace(keyUrl, keyLocalUri)
                                                            Log.d(TAG, "✓ Key local: $keyLocalUri")
                                                        } else {
                                                            Log.w(TAG, "No se pudo decodificar key (bytes=${keyBytes.size}, text='${keyBytes.decodeToString().trim().take(40)}')")
                                                        }
                                                    } else {
                                                        Log.w(TAG, "Key HTTP ${keyResp.code}")
                                                    }
                                                } catch (e: Exception) {
                                                    Log.w(TAG, "Error descargando key: ${e.message}")
                                                }
                                            }
                                            val cacheFile = File(cacheDir, "pl_${hlsVersion.locale}_$quality.m3u8")
                                            cacheFile.writeText(rewrittenPlaylist)
                                            val localUri = Uri.fromFile(cacheFile)
                                            val localUrl = localUri.toString()

                                            Log.d(TAG, "✓ Playlist local: $localUrl")
                                            Log.d(TAG, "  Uri authority=${localUri.authority} path=${localUri.path} scheme=${localUri.scheme}")
                                            val link = newExtractorLink(
                                                source = this.name,
                                                name = "${this.name} - ${hlsVersion.locale.uppercase()} - $quality",
                                                url = localUrl,
                                                type = ExtractorLinkType.M3U8
                                            ) {
                                                this.quality = qualityValue
                                                this.referer = "$mainUrl/"
                                            }
                                            Log.d(TAG, "  link class=${link::class.java.name} props: type=${link.type} quality=${link.quality}")
                                            callback(link)
                                            linksEnviados++
                                        }
                                    } catch (e: Exception) {
                                        Log.w(TAG, "Error procesando ${hlsVersion.locale}: ${e.message}")
                                        Log.w(TAG, Log.getStackTraceString(e))
                                    }
                                }
                            }
                        }

                        if (linksEnviados > 0) break
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Error con locale $locale: ${e.message}")
                    continue
                }
            }

            Log.d(TAG, "========================================")
            Log.d(TAG, "TOTAL LINKS: $linksEnviados")
            Log.d(TAG, "========================================")

            linksEnviados > 0

        } catch (e: Exception) {
            Log.e(TAG, "ERROR: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    @Serializable
    data class SeriesItem(
        val content_id: String,
        val title: String,
        val image: String? = null
    )

    @Serializable
    data class SearchRoot(
        val series: List<SeriesItem>? = null,
        val episodes: List<EpisodeItem>? = null
    )

    @Serializable
    data class DetailsResponse(
        val content_id: String? = null,
        val title: String? = null,
        val description: String? = null,
        val images: List<ImageItem>? = null,
        val seasons: List<SeasonItem>? = null,
        val audio_locales: List<String>? = null,
        val subtitle_locales: List<String>? = null
    )

    @Serializable
    data class SeasonItem(
        val content_id: String,
        val season_number: Int,
        val title: String? = null
    )

    @Serializable
    data class EpisodeItem(
        val content_id: String,
        val series_id: String? = null,
        val title: String? = null,
        val episode_number: Double? = null,
        val image: String? = null,
        val is_clip: Boolean? = false
    )

    @Serializable
    data class ImageItem(
        val url: String,
        val type: String
    )

    @Serializable
    data class VideoResponse(
        val versions: Versions? = null,
        val hls: HlsVersion? = null
    )

    @Serializable
    data class Versions(
        val hls: List<HlsVersion>? = null,
        val dash: List<DashVersion>? = null
    )

    @Serializable
    data class HlsVersion(
        val locale: String,
        val playlist: String,
        val subtitles: List<SubtitleItem>? = null,
        val hard_subs: List<HardSubItem>? = null
    )

    @Serializable
    data class DashVersion(
        val locale: String,
        val playlist: String,
        val subtitles: List<SubtitleItem>? = null,
        val hard_subs: List<HardSubItem>? = null
    )

    @Serializable
    data class HardSubItem(
        val locale: String,
        val playlist: String
    )

    @Serializable
    data class SubtitleItem(
        val locale: String,
        val url: String
    )
}