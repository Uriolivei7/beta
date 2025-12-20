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
            list.sortedBy { it.second }.forEach {
                it.first?.let { section -> sections.add(section) }
            }
        }

        if (sections.isEmpty()) {
            sections.add(HomePageList("No hay secciones activas", emptyList()))
        }
        return newHomePageResponse(sections, true)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val allResults = ytParser.parseSearch(query)
        return allResults?.filterIsInstance<MovieSearchResponse>() ?: emptyList()
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
        val debugTag = "YoutubeProviderDebug"

        return try {
            val service = org.schabi.newpipe.extractor.ServiceList.YouTube

            val extractor = service.getStreamExtractor(data)

            extractor.fetchPage()

            val refererUrl = extractor.url ?: data

            val dashUrl = try { extractor.dashMpdUrl } catch (e: Exception) { null }

            if (dashUrl != null && dashUrl.length > 0) {
                Log.d(debugTag, "DASH encontrado: Forzando 1080p")
                callback.invoke(
                    newExtractorLink(
                        this.name,
                        "YouTube Alta Calidad (DASH)",
                        dashUrl,
                        com.lagradost.cloudstream3.utils.ExtractorLinkType.DASH
                    ) {
                        this.referer = refererUrl
                        this.quality = Qualities.P1080.value
                    }
                )
            }

            val addedRes = mutableSetOf<Int>()
            extractor.videoStreams?.sortedByDescending { it.resolution?.removeSuffix("p")?.toIntOrNull() ?: 0 }
                ?.forEach { stream ->
                    val res = stream.resolution ?: return@forEach
                    val qualInt = res.removeSuffix("p").toIntOrNull() ?: 360

                    if (!addedRes.contains(qualInt)) {
                        val streamUrl = stream.url ?: return@forEach
                        callback.invoke(
                            newExtractorLink(
                                this.name,
                                "YouTube $res (Directo)",
                                streamUrl,
                                com.lagradost.cloudstream3.utils.ExtractorLinkType.VIDEO
                            ) {
                                this.referer = refererUrl
                                this.quality = qualInt
                            }
                        )
                        addedRes.add(qualInt)
                    }
                }

            true
        } catch (e: Exception) {
            Log.e(debugTag, "Error: ${e.message}")
            false
        }
    }
}