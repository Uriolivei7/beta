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
import com.lagradost.cloudstream3.utils.newExtractorLink
// IMPORTANTE: Asegúrate de tener estas importaciones para configurar NewPipe
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.stream.StreamInfo

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
        // User Agent de escritorio para evitar el bloqueo de Android 15
        const val PC_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"
    }

    init {
        try {
            // Intentamos configurar el User Agent globalmente a través del Downloader
            val downloader = NewPipe.getDownloader()

            // Si .setUserAgent no existe, intentamos inyectarlo mediante los headers por defecto
            // Esto emula el comportamiento de una laptop (WSA) en tu Samsung

            val fields = downloader.javaClass.declaredFields
            val userAgentField = fields.find { it.name == "userAgent" || it.type == String::class.java }

            if (userAgentField != null) {
                userAgentField.isAccessible = true
                userAgentField.set(downloader, PC_USER_AGENT)
                Log.d(TAG, "User Agent configurado exitosamente mediante reflexión.")
            } else {
                Log.w(TAG, "No se pudo encontrar el campo UserAgent, YouTube podría bloquear la conexión.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error configurando el User Agent: ${e.message}")
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(TAG, "Iniciando getMainPage para página: $page")

        // Ejecutar dentro de un try-catch para que si falla una sección no rompa toda la app
        val isTrendingEnabled = sharedPrefs?.getBoolean("trending", true) ?: true
        val sections = mutableListOf<HomePageList>()

        if (isTrendingEnabled) {
            try {
                val videos = ytParser.getTrendingVideoUrls(page)
                videos?.let { sections.add(it) }
            } catch (e: Exception) {
                Log.e(TAG, "Error en tendencias: ${e.message}")
            }
        }

        val playlistsData = sharedPrefs?.getStringSet("playlists", emptySet()) ?: emptySet()
        if (playlistsData.isNotEmpty()) {
            val triples = playlistsData.map { parseJson<Triple<String, String, Long>>(it) }
            val list = triples.amap { data ->
                val playlistUrl = data.first
                try {
                    val urlPath = playlistUrl.substringAfter("youtu").substringAfter("/")
                    val isPlaylist = urlPath.startsWith("playlist?list=")
                    val isChannel = urlPath.startsWith("@") || urlPath.startsWith("channel")

                    val customSections = if (isPlaylist && !isChannel) {
                        ytParser.playlistToSearchResponseList(playlistUrl, page)
                    } else if (!isPlaylist && isChannel) {
                        ytParser.channelToSearchResponseList(playlistUrl, page)
                    } else null

                    customSections to data.third
                } catch (e: Exception) {
                    Log.e(TAG, "Error cargando sección personalizada: ${e.message}")
                    null to data.third
                }
            }
            list.sortedBy { it.second }.forEach {
                it.first?.let { section -> sections.add(section) }
            }
        }

        if (sections.isEmpty()) {
            sections.add(HomePageList("No content available. Check settings.", emptyList()))
        }

        return newHomePageResponse(sections, true)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return try {
            ytParser.parseSearch(query).filterIsInstance<MovieSearchResponse>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        // Aquí es donde ocurría el error en tu log.
        // El ytParser ahora usará la configuración global del Downloader establecida en el init.
        return ytParser.videoToLoadResponse(url)
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        return try {
            // Obtenemos la info del stream.
            // Si sigue fallando en tu A06, YouTube está pidiendo una Cookie obligatoria.
            val info = StreamInfo.getInfo(data)
            val refererUrl = info.url

            info.videoStreams
                .sortedByDescending { it.resolution?.removeSuffix("p")?.toIntOrNull() ?: 0 }
                .forEach { stream ->
                    val streamUrl = stream.url ?: return@forEach
                    val resolutionName = stream.resolution ?: return@forEach

                    callback.invoke(
                        newExtractorLink(
                            source = this.name,
                            name = "Video - $resolutionName",
                            url = streamUrl
                        ) {
                            this.referer = refererUrl
                        }
                    )
                }

            info.subtitles.forEach { sub ->
                subtitleCallback.invoke(newSubtitleFile(sub.languageTag ?: "Unknown", sub.url ?: return@forEach))
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error en loadLinks (Android 15): ${e.message}")
            false
        }
    }
}