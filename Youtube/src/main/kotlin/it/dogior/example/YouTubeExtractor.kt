package it.dogior.example

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.mvvm.logError
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.schemaStripRegex
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeStreamExtractor
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeStreamLinkHandlerFactory

open class YouTubeExtractor() : ExtractorApi() {
    override val mainUrl = "https://www.youtube.com"
    override val requiresReferer = false
    override val name = "YouTube"

    private fun mapResolutionToQuality(resolution: String): Qualities {
        return when (resolution.lowercase().trim()) {
            "2160p" -> Qualities.P2160
            "1440p" -> Qualities.P1440
            "1080p" -> Qualities.P1080
            "720p" -> Qualities.P720
            "480p" -> Qualities.P480
            "360p" -> Qualities.P360
            "240p" -> Qualities.P240
            "144p" -> Qualities.P144
            else -> Qualities.Unknown
        }
    }

    @Suppress("DEPRECATION")
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        val TAG = "YoutubeDebug"

        val link = YoutubeStreamLinkHandlerFactory.getInstance().fromUrl(url.replace(schemaStripRegex, ""))
        val extractor = object : YoutubeStreamExtractor(org.schabi.newpipe.extractor.ServiceList.YouTube, link) {}
        extractor.fetchPage()

        val allVideos = (extractor.videoStreams ?: emptyList()) + (extractor.videoOnlyStreams ?: emptyList())

        allVideos.filterNotNull().forEach { stream ->
            val quality = mapResolutionToQuality(stream.resolution)
            Log.d(TAG, "Detectado: ${stream.resolution}")

            callback.invoke(
                newExtractorLink(
                    this.name,
                    "${this.name} ${stream.resolution}",
                    stream.url!!,
                    com.lagradost.cloudstream3.utils.ExtractorLinkType.VIDEO
                ) {
                    this.referer = url
                    this.quality = quality.value
                }
            )
        }
    }

}