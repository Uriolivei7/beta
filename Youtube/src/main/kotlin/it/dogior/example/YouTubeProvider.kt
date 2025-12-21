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

        // Usamos el User-Agent que usa YouTube para navegadores móviles,
        // que suele ser el más permisivo con los enlaces combinados.
        val mobileUserAgent = "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.91 Mobile Safari/537.36"
        val cookies = YoutubeParsingHelper.getCookieHeader()["Cookie"]?.joinToString("; ") ?: ""

        // 1. Obtener audio (prioridad absoluta a m4a para evitar desfases)
        val audioStream = info.audioStreams?.find { it.format?.name?.contains("m4a") == true }
            ?: info.audioStreams?.maxByOrNull { it.bitrate }
        val audioUrl = audioStream?.url ?: ""

        // 2. Procesar los streams de video
        val videoStreams = (info.videoOnlyStreams ?: emptyList()) + (info.videoStreams ?: emptyList())

        videoStreams.filterNotNull().distinctBy { it.resolution }.forEach { stream ->
            val resName = stream.resolution ?: "360p"

            callback.invoke(
                newExtractorLink(
                    source = this.name,
                    name = "YT $resName",
                    url = stream.url ?: return@forEach,
                    type = ExtractorLinkType.VIDEO
                ) {
                    this.quality = getQuality(resName)
                    this.headers = mapOf(
                        "User-Agent" to mobileUserAgent,
                        "Cookie" to cookies,
                        "Referer" to "https://m.youtube.com/",
                        "Origin" to "https://m.youtube.com"
                    )

                    // El truco para el audio sin NewPipe:
                    // Pasamos el audioUrl pero le pegamos los mismos parámetros de seguridad
                    if (info.videoOnlyStreams?.any { it.url == stream.url } == true) {
                        this.extractorData = audioUrl
                    }
                }
            )
        }

        // Subtítulos
        info.subtitles?.forEach { sub ->
            subtitleCallback.invoke(newSubtitleFile(sub.languageTag ?: "Auto", sub.url ?: ""))
        }

        return true
    }

    private fun getQuality(res: String): Int {
        return when {
            res.contains("1080") -> Qualities.P1080.value
            res.contains("720") -> Qualities.P720.value
            res.contains("480") -> Qualities.P480.value
            else -> Qualities.P360.value
        }
    }

}