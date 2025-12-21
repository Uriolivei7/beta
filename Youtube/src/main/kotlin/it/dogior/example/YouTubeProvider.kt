package it.dogior.example

import android.content.SharedPreferences
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import org.schabi.newpipe.extractor.NewPipe

class YoutubeProvider(
    language: String = "mx",
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

    init {
        try {
            NewPipe.init(NewPipeDownloader.getInstance())
        } catch (e: Exception) {
            Log.e(TAG, "Error inicializando NewPipe: ${e.message}")
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(TAG, "Iniciando getMainPage para página: $page")
        val isTrendingEnabled = sharedPrefs?.getBoolean("trending", true) ?: true
        val sections = mutableListOf<HomePageList>()
        if (isTrendingEnabled) {
            val videos = ytParser.getTrendingVideoUrls(page)
            videos?.let { sections.add(it) }
        }

        val playlistsData = sharedPrefs?.getStringSet("playlists", emptySet()) ?: emptySet()
        if (playlistsData.isNotEmpty()) {
            val triples = playlistsData.map { parseJson<Triple<String, String, Long>>(it) }
            val list = triples.amap { data ->
                val playlistUrl = data.first
                val urlPath = playlistUrl.substringAfter("youtu").substringAfter("/")
                val isPlaylist = urlPath.startsWith("playlist?list=")
                val isChannel = urlPath.startsWith("@") || urlPath.startsWith("channel")
                val customSections = if (isPlaylist && !isChannel) {
                    ytParser.playlistToSearchResponseList(playlistUrl, page)
                } else if (!isPlaylist && isChannel) {
                    ytParser.channelToSearchResponseList(playlistUrl, page)
                } else null
                customSections to data.third
            }
            list.sortedBy { it.second }.forEach { it.first?.let { section -> sections.add(section) } }
        }

        if (sections.isEmpty()) {
            sections.add(HomePageList("All sections are disabled. Go to settings", emptyList()))
        }
        return newHomePageResponse(sections, true)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val allResults = ytParser.parseSearch(query)
        return allResults?.filterIsInstance<SearchResponse>() ?: emptyList()
    }

    override suspend fun load(url: String): LoadResponse {
        return ytParser.videoToLoadResponse(url)
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val DTAG = "YT_LOGICA"
        Log.d(DTAG, "--- Iniciando Carga de Enlaces (Lista Unificada) ---")

        return try {
            val service = org.schabi.newpipe.extractor.ServiceList.YouTube
            val extractor = service.getStreamExtractor(data)
            extractor.fetchPage()

            val referer = extractor.url ?: data

            // 1. Obtener el mejor audio para las pistas mudas (DASH/VideoOnly)
            val audioStream = extractor.audioStreams?.maxByOrNull { it.bitrate }
            val audioUrl = audioStream?.url
            Log.d(DTAG, "Audio base detectado: ${if (audioUrl != null) "SÍ" else "NO"}")

            // 2. Crear una lista maestra de todos los streams de video
            val masterVideoList = mutableListOf<ExtractorLink>()

            // A. Añadir calidades HD (vienen sin audio, inyectamos el audio detectado)
            extractor.videoOnlyStreams?.distinctBy { it.resolution }?.forEach { video ->
                val res = video.resolution ?: "unknown"
                val vUrl = video.url ?: return@forEach

                Log.d(DTAG, "Añadiendo a lista maestra: $res (Inyectando Audio)")
                masterVideoList.add(
                    newExtractorLink(
                        this.name,
                        "YouTube $res (HD + Audio)",
                        vUrl,
                        ExtractorLinkType.VIDEO
                    ) {
                        this.referer = referer
                        this.quality = res.removeSuffix("p").toIntOrNull() ?: 0
                        if (audioUrl != null) {
                            this.headers = mapOf(
                                "extra_audio_url" to audioUrl,
                                "User-Agent" to "com.google.android.youtube.tv/12.05.05 (Linux; U; Android 12; Build/STTE.221215.005)"
                            )
                        }
                    }
                )
            }

            // B. Añadir calidades SD / Muxed (Ya tienen audio, usualmente 360p)
            extractor.videoStreams?.forEach { stream ->
                val res = stream.resolution ?: "360p"
                Log.d(DTAG, "Añadiendo a lista maestra: $res (Audio Integrado)")
                masterVideoList.add(
                    newExtractorLink(
                        this.name,
                        "YouTube $res (SD)",
                        stream.url!!,
                        ExtractorLinkType.VIDEO
                    ) {
                        this.referer = referer
                        this.quality = res.removeSuffix("p").toIntOrNull() ?: 360
                    }
                )
            }

            // 3. Enviar toda la lista al callback de una sola vez
            Log.d(DTAG, "Total de enlaces enviados a la lista: ${masterVideoList.size}")
            masterVideoList.sortByDescending { it.quality } // Ordenar de mayor a menor calidad
            masterVideoList.forEach { callback.invoke(it) }

            true
        } catch (e: Exception) {
            Log.e(DTAG, "Error crítico en lista unificada: ${e.message}")
            false
        }
    }
}