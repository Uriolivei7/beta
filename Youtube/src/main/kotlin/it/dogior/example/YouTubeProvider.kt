package it.dogior.example

import android.content.SharedPreferences
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.amap
import android.util.Log
import com.lagradost.cloudstream3.MovieSearchResponse
import com.lagradost.cloudstream3.newSubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import org.schabi.newpipe.extractor.stream.StreamInfo
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.Qualities

class YoutubeProvider(
    language: String = "en",
    private val sharedPrefs: SharedPreferences? = null
) : MainAPI() {

    override var mainUrl = MAIN_URL
    override var name = "YouTube"
    override val supportedTypes = setOf(TvType.Others)
    override val hasMainPage = true
    override var lang = language

    private val ytParser = YouTubeParser(this.name)

    companion object {
        const val MAIN_URL = "https://www.youtube.com"
        const val TAG = "Youtube"
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(TAG, "Iniciando getMainPage para página: $page")

        val isTrendingEnabled = sharedPrefs?.getBoolean("trending", true) ?: true
        val sections = mutableListOf<HomePageList>()
        if (isTrendingEnabled) {
            Log.d(TAG, "Obteniendo tendencias...")
            val videos = ytParser.getTrendingVideoUrls(page)
            if (videos == null) {
            Log.d(TAG, "Error: No se obtuvieron videos de tendencias en la página $page.")
        } else {
            Log.d(TAG, "Tendencias obtenidas. Total: ${videos.list.size} videos.")

        }
            videos?.let { sections.add(it) }
        }

        val playlistsData = sharedPrefs?.getStringSet("playlists", emptySet()) ?: emptySet()
        if (playlistsData.isNotEmpty()) {
            Log.d(TAG, "Cargando playlists y canales personalizados. Total de entradas: ${playlistsData.size}")
            val triples = playlistsData.map { parseJson<Triple<String, String, Long>>(it) }
            val list = triples.amap { data ->
            val playlistUrl = data.first
            Log.d(TAG, "Procesando URL: $playlistUrl")
            val urlPath = playlistUrl.substringAfter("youtu").substringAfter("/")
            val isPlaylist = urlPath.startsWith("playlist?list=")
            val isChannel = urlPath.startsWith("@") || urlPath.startsWith("channel")
            val customSections = if (isPlaylist && !isChannel) {
            Log.d(TAG, "Llamando a playlistToSearchResponseList para $playlistUrl")
            ytParser.playlistToSearchResponseList(playlistUrl, page)

        } else if (!isPlaylist && isChannel) {
            Log.d(TAG, "Llamando a channelToSearchResponseList para $playlistUrl")
            ytParser.channelToSearchResponseList(playlistUrl, page)

        } else {
            Log.d(TAG, "URL no reconocida (ni playlist ni canal): $playlistUrl")
            null

        }
            customSections to data.third

        }
            list.sortedBy { it.second }.forEach {
            val homepageSection = it.first
            if (homepageSection != null) {
            sections.add(homepageSection)

        }

        }


        }
        if (sections.isEmpty()) {
            Log.d(TAG, "No hay secciones activas. Se muestra mensaje de error en la UI.")
            sections.add(
            HomePageList(
            "All sections are disabled. Go to the settings to enable them",
            emptyList()
            )
            )

        }
        Log.d(TAG, "Finalizando getMainPage. Secciones totales: ${sections.size}")
        return newHomePageResponse(
        sections, true
        )

    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Iniciando búsqueda para: $query")

        val allResults = ytParser.parseSearch(query)

        Log.d(TAG, "Búsqueda terminada. Resultados encontrados: ${allResults?.size ?: 0}")

        return allResults?.filterIsInstance<MovieSearchResponse>() ?: emptyList()
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d(TAG, "Iniciando carga de LoadResponse para URL: $url")
        val video = ytParser.videoToLoadResponse(url)
        Log.d(TAG, "Carga de LoadResponse completada.")
        return video
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val TAG = "YoutubeProviderDebug"

        return try {
            val info = org.schabi.newpipe.extractor.stream.StreamInfo.getInfo(org.schabi.newpipe.extractor.ServiceList.YouTube, data)
            val refererUrl = info.url ?: data

            val dashMpd: String? = info.dashMpdUrl

            if (dashMpd != null && dashMpd.length > 0) {
                Log.d(TAG, "DASH encontrado satisfactoriamente")
                callback.invoke(
                    newExtractorLink(
                        this.name,
                        "YouTube Pro (DASH)",
                        dashMpd,
                        com.lagradost.cloudstream3.utils.ExtractorLinkType.DASH
                    ) {
                        this.referer = refererUrl
                        this.quality = Qualities.P1080.value
                    }
                )
            }

            // ESTRATEGIA B: HLS
            val hlsM3u8: String? = info.hlsUrl
            if (hlsM3u8 != null && hlsM3u8.length > 0) {
                callback.invoke(
                    newExtractorLink(
                        this.name,
                        "YouTube Multi-Res (HLS)",
                        hlsM3u8,
                        com.lagradost.cloudstream3.utils.ExtractorLinkType.M3U8
                    ) {
                        this.referer = refererUrl
                    }
                )
            }

            val muxed = info.videoStreams ?: emptyList()
            val addedResolutions = mutableSetOf<Int>()

            muxed.sortedByDescending { it.resolution?.removeSuffix("p")?.toIntOrNull() ?: 0 }
                .forEach { stream ->
                    val resStr = stream.resolution ?: return@forEach
                    val qual = resStr.removeSuffix("p").toIntOrNull() ?: 360

                    if (!addedResolutions.contains(qual)) {
                        val streamUrl: String? = stream.url

                        if (streamUrl != null && streamUrl.length > 0) {
                            callback.invoke(
                                newExtractorLink(
                                    this.name,
                                    "YouTube $resStr",
                                    streamUrl,
                                    com.lagradost.cloudstream3.utils.ExtractorLinkType.VIDEO
                                ) {
                                    this.referer = refererUrl
                                    this.quality = qual
                                }
                            )
                            addedResolutions.add(qual)
                        }
                    }
                }

            // Subtítulos
            info.subtitles?.forEach { sub ->
                val sUrl = sub.url
                if (sUrl != null) {
                    subtitleCallback.invoke(newSubtitleFile(sub.languageTag ?: "en", sUrl))
                }
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Error en carga v0.24.8: ${e.message}")
            false
        }
    }

}