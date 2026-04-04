package com.example

/*
    CEO: Ranita
    Empresa: RANITA TECH
    Versión software: 1.0     fecha: 09/08/2025
*/
import com.lagradost.cloudstream3.extractors.DoodStreamExtractor
import com.lagradost.cloudstream3.extractors.FileMoon
import com.lagradost.cloudstream3.extractors.Filesim
import com.lagradost.cloudstream3.extractors.MixDrop
import com.lagradost.cloudstream3.extractors.StreamWishExtractor
import com.lagradost.cloudstream3.extractors.VidStack
import com.lagradost.cloudstream3.extractors.Voe

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
class EmturbovidCom : Filesim() {
    override var mainUrl = "https://emturbovid.com"
    override var name = "Emturbovid"
}
class StreamWishTo : StreamWishExtractor() {
    override var mainUrl = "https://streamwish.to"
    override var name = "StreamWish"
}
class VidHidePro : StreamWishExtractor() {
    override var mainUrl = "https://vidhidepro.com"
    override var name = "VidHidePro"
}
class VoeExtractor : Voe() {
    override var mainUrl = "https://voe.sx"
    override var name = "Voe"
}
class DoodStream : DoodStreamExtractor() {
    override var mainUrl = "https://dood.la"
    override var name = "DoodStream"
}
class FilemoonHD : FileMoon() {
    override var mainUrl = "https://filemoon.sx"
    override var name = "FileMoon"
}
class MixDropExtractor : MixDrop() {
    override var mainUrl = "https://mixdrop.co"
    override var name = "MixDrop"
}