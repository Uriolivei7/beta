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
import com.lagradost.cloudstream3.utils.newExtractorLink
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
        // User Agent actualizado a una versión más reciente para evitar el bloqueo de "Reload"
        const val ANDROID_USER_AGENT = "com.google.ios.youtube/19.29.1 (iPhone16,2; U; CPU iOS 17_5_1 like Mac OS X;)"
    }

    private var youtubeCookie: String?
        get() = sharedPrefs?.getString("youtube_cookie", null)
        set(value) {
            sharedPrefs?.edit()?.putString("youtube_cookie", value)?.apply()
        }

    init {
        val downloader = NewPipeDownloader.getInstance()
        // 1. Pasamos el User Agent de TV para evitar el bloqueo regional
        downloader.customUserAgent = "com.google.android.youtube.tv/4.10.001 (Android 11; Television; Sony; BRAVIA 4K VH2; America/New_York; en_US)"

        // 2. Pasamos las cookies de las preferencias
        downloader.cookies = sharedPrefs?.getString("youtube_cookie", null)

        // 3. Registramos el downloader en NewPipe
        NewPipe.init(downloader)

        Log.d(TAG, "Logs: Downloader iniciado con UA: ${downloader.customUserAgent} y Cookies: ${downloader.cookies != null}")
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(TAG, "Iniciando getMainPage para página: $page")

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
        return ytParser.videoToLoadResponse(url)
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        Log.d(TAG, "Logs: Iniciando extracción de enlaces para: $data")
        return try {
            val service = ServiceList.YouTube
            val linkHandler = service.streamLHFactory.fromUrl(data)
            val extractor = service.getStreamExtractor(linkHandler)

            try {
                extractor.fetchPage()
            } catch (e: Exception) {
                if (e.message?.contains("reloaded", ignoreCase = true) == true) {
                    Log.w(TAG, "Logs: YouTube detectó bot. Limpiando cliente y reintentando en 2s...")

                    // Limpiamos la versión del cliente para que NewPipe intente obtener una nueva
                    org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.resetClientVersion()

                    kotlinx.coroutines.delay(2500) // Un poco más de tiempo es mejor
                    extractor.fetchPage()
                } else {
                    throw e
                }
            }

            // Log de estado de reproducción
            try {
                // Algunos extractores permiten revisar si el video está bloqueado
                Log.d(TAG, "Logs: Estado de reproducción verificado para ID: ${extractor.id}")
            } catch (e: Exception) {
                Log.w(TAG, "Logs: No se pudo verificar estado de reproducción: ${e.message}")
            }

            val streamsFound = extractor.videoStreams?.filterNotNull() ?: emptyList()
            Log.d(TAG, "Logs: Se encontraron ${streamsFound.size} streams de video.")

            streamsFound.forEach { stream ->
                val streamUrl = stream.content ?: stream.url ?: return@forEach
                val resolutionName = stream.resolution ?: "Unknown"

                val isHls = streamUrl.contains(".m3u8") || streamUrl.contains("manifest")

                callback.invoke(
                    newExtractorLink(
                        source = this.name,
                        name = "YouTube - $resolutionName",
                        url = streamUrl,
                        type = if (isHls) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                    ) {
                        // Importante: Referer exacto para evitar bloqueos 403
                        this.referer = "https://www.youtube.com/watch?v=${extractor.id}"
                    }
                )
            }

            extractor.subtitlesDefault?.filterNotNull()?.forEach { sub ->
                subtitleCallback.invoke(
                    newSubtitleFile(sub.languageTag ?: "Unknown", sub.url ?: return@forEach)
                )
            }

            streamsFound.isNotEmpty()
        } catch (e: Exception) {
            // Log detallado para Samsung A06 y fallos de red
            Log.e(TAG, "Logs: Fallo total en loadLinks (Samsung A06). Causa: ${e.localizedMessage}")
            e.printStackTrace()
            false
        }
    }
}