package com.example

import com.lagradost.cloudstream3.extractors.VidStack
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.loadExtractor

// --- CLONES DE PELISPLUS (MANTIENEN VIDSTACK) ---

class PelisplusUpnsPro : VidStack() {
    override var mainUrl = "https://pelisplus.upns.pro"
    override var name = "PelisplusUpns"
}

class PelisplusUpnsPro2 : VidStack() {
    override var mainUrl = "https://pelisplus.strp2p.com"
    override var name = "PelisplusStrp2p"
}

class PelisplusUpnsPro3 : VidStack() {
    override var mainUrl = "https://pelisplusto.4meplayer.pro"
    override var name = "Pelisplus4meplayer"
}

class PelisplusRpmStream : VidStack() {
    override var mainUrl = "https://pelisplus.rpmstream.live"
    override var name = "PelisplusRPM"
}

// --- SERVIDORES EXTERNOS (USAN EXTRACTOR_API) ---

class EmturbovidCom : ExtractorApi() {
    override var mainUrl = "https://emturbovid.com"
    override var name = "Emturbovid"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        loadExtractor(url, referer, subtitleCallback, callback)
    }
}

class VidhideCustom : ExtractorApi() {
    override var mainUrl = "https://vidhide.com"
    override var name = "Vidhide"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        loadExtractor(url, referer, subtitleCallback, callback)
    }
}

class LuluvdoCustom : ExtractorApi() {
    override var mainUrl = "https://luluvdo.com"
    override var name = "LuluStream"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        loadExtractor(url, referer, subtitleCallback, callback)
    }
}

class VudeoCustom : ExtractorApi() {
    override var mainUrl = "https://vudeo.net"
    override var name = "Vudeo"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        loadExtractor(url, referer, subtitleCallback, callback)
    }
}