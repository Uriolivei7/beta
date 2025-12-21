package it.dogior.example

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
        val link = YoutubeStreamLinkHandlerFactory.getInstance().fromUrl(url.replace(schemaStripRegex, ""))
        val extractor = object : YoutubeStreamExtractor(ServiceList.YouTube, link) {}
        extractor.fetchPage()

        val ref = extractor.url ?: url

        val dashUrl = try { extractor.dashMpdUrl } catch (e: Exception) { null }
        if (!dashUrl.isNullOrEmpty()) {
            callback.invoke(
                newExtractorLink(this.name, "YouTube (Multi-Res HD)", dashUrl!!, ExtractorLinkType.DASH) {
                    this.referer = ref
                    this.quality = Qualities.P1080.value
                }
            )
        }

        val bestAudio = extractor.audioStreams?.maxByOrNull { it.bitrate }?.url

        extractor.videoOnlyStreams?.distinctBy { it.resolution }?.forEach { video ->
            val res = video.resolution ?: return@forEach
            val qualInt = res.removeSuffix("p").toIntOrNull() ?: 0

            if (qualInt >= 720 && bestAudio != null) {
                callback.invoke(
                    newExtractorLink(this.name, "YouTube $res (HD)", video.url!!, ExtractorLinkType.VIDEO) {
                        this.referer = ref
                        this.quality = qualInt
                        this.headers = mapOf("X-Audio-Url" to bestAudio)
                    }
                )
            }
        }

        extractor.videoStreams?.forEach { stream ->
            callback.invoke(
                newExtractorLink(this.name, "YouTube ${stream.resolution} (SD)", stream.url!!, ExtractorLinkType.VIDEO) {
                    this.referer = ref
                    this.quality = 360
                }
            )
        }
    }
}