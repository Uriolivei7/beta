package com.example

import android.util.Log
import com.lagradost.cloudstream3.extractors.Filesim
import com.lagradost.cloudstream3.extractors.VidStack
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.getAndUnpack
import com.lagradost.cloudstream3.extractors.VidHidePro

open class PelisPlusBase : VidStack() {
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val fixedReferer = "https://pelisplus.upns.pro/"
        super.getUrl(url, fixedReferer, subtitleCallback, callback)
    }
}

class PelisplusUpnsPro : PelisPlusBase() {
    override var mainUrl = "https://pelisplus.upns.pro"
    override var name = "UPFAST"
}

class PelisplusUpnsPro2 : PelisPlusBase() {
    override var mainUrl = "https://pelisplus.strp2p.com"
    override var name = "P2P"
}

class PelisplusUpnsPro3 : PelisPlusBase() {
    override var mainUrl = "https://pelisplusto.4meplayer.pro"
    override var name = "4mePlayer"
}

class EmturbovidCom : Filesim() {
    override var mainUrl = "https://emturbovid.com"
    override var name = "Plus"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val fixedUrl = url.replace("emturbovid.com", "turbovid.eu").replace("/e/", "/v/")
        super.getUrl(fixedUrl, "https://turbovid.eu/", subtitleCallback, callback)
    }
}

class Vidhide : VidHidePro() {
    override var mainUrl = "https://vidhidepro.com"
    override var name = "Vidhide Pro"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        super.getUrl(url, "https://vidhidepro.com/", subtitleCallback, callback)
    }
}

class RPMStream : PelisPlusBase() {
    override var mainUrl = "https://pelisplus.rpmstream.live"
    override var name = "RPM"
}