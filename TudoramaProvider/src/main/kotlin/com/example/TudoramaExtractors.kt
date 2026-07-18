package com.example

import com.lagradost.cloudstream3.extractors.VidStack

class TudoramaBysesukior : VidStack() {
    override var name = "TudoramaBysesukior"
    override var mainUrl = "https://bysesukior.com"
    override var requiresReferer = true
}

class TudoramaMinochinos : VidStack() {
    override var name = "TudoramaMinochinos"
    override var mainUrl = "https://minochinos.com"
    override var requiresReferer = true
}

class TudoramaHgcloud : VidStack() {
    override var name = "TudoramaHgcloud"
    override var mainUrl = "https://hgcloud.to"
    override var requiresReferer = true
}

class Tudorama4meplayer : VidStack() {
    override var name = "Tudorama4meplayer"
    override var mainUrl = "https://tudorama.4meplayer.pro"
    override var requiresReferer = true
}
