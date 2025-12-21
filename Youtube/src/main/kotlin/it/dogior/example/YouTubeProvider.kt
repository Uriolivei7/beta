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
import org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper

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
        val info = StreamInfo.getInfo(data)
        val refererUrl = info.url
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36"
        val cookies = YoutubeParsingHelper.getCookieHeader()["Cookie"]?.joinToString("; ") ?: ""

        // 1. Obtener el audio y crear un mapa de headers para él
        val bestAudio = info.audioStreams?.filter { it.format?.name?.contains("m4a", ignoreCase = true) == true }
            ?.maxByOrNull { it.bitrate } ?: info.audioStreams?.maxByOrNull { it.bitrate }
        val audioUrl = bestAudio?.url ?: ""

        val allStreams = (info.videoStreams ?: emptyList()) + (info.videoOnlyStreams ?: emptyList())
        val distinctStreams = allStreams.filterNotNull().distinctBy { it.resolution }

        distinctStreams.forEach { stream ->
            val streamUrl = stream.url ?: return@forEach
            val resName = stream.resolution ?: "360p"

            val qualityInt = when {
                resName.contains("2160") -> Qualities.P2160.value
                resName.contains("1440") -> Qualities.P1440.value
                resName.contains("1080") -> Qualities.P1080.value
                resName.contains("720") -> Qualities.P720.value
                resName.contains("480") -> Qualities.P480.value
                resName.contains("360") -> Qualities.P360.value
                else -> Qualities.P144.value
            }

            callback.invoke(
                newExtractorLink(
                    source = this.name,
                    name = "MPEG-4 $resName",
                    url = streamUrl,
                    type = ExtractorLinkType.VIDEO
                ) {
                    this.quality = qualityInt
                    this.referer = refererUrl
                    this.headers = mapOf(
                        "User-Agent" to userAgent,
                        "Cookie" to cookies,
                        "Referer" to "https://www.youtube.com/"
                    )

                    // Verificamos si es un stream DASH (sin audio)
                    // Comparamos con la lista original de videoOnly
                    val isDash = info.videoOnlyStreams?.any { it.url == streamUrl } ?: false

                    if (isDash) {
                        // Pasamos el audioUrl. Cloudstream intentará unirlo.
                        this.extractorData = audioUrl
                    }
                }
            )
        }

        info.subtitles?.forEach { sub ->
            subtitleCallback.invoke(newSubtitleFile(sub.languageTag ?: "Auto", sub.url ?: return@forEach))
        }

        return true
    }

}