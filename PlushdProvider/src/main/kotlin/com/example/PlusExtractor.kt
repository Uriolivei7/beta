package com.example

/*
    CEO: Ranita
    Empresa: RANITA TECH
    Versión software: 1.1     fecha: 17/01/2026
*/

import com.lagradost.cloudstream3.extractors.VidStack

// --- EXTRACTORES PELISPLUS CLONES ---

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

class EmturbovidCom : VidStack() {
    override var mainUrl = "https://emturbovid.com"
    override var name = "Emturbovid"
}

class VidhideCustom : VidStack() {
    override var mainUrl = "https://vidhide.com"
    override var name = "Vidhide"
}

class LuluvdoCustom : VidStack() {
    override var mainUrl = "https://luluvdo.com"
    override var name = "LuluStream"
}

class VudeoCustom : VidStack() {
    override var mainUrl = "https://vudeo.net"
    override var name = "Vudeo"
}