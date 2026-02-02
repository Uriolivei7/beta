package com.example

import com.lagradost.cloudstream3.plugins.BasePlugin
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class UltracinePlugin : BasePlugin() {
    override fun load() {
        registerMainAPI(UltracineProvider())
        registerExtractorAPI(EmbedPlayUpnsPro())
        registerExtractorAPI(EmbedPlayUpnOne())
    }
}
