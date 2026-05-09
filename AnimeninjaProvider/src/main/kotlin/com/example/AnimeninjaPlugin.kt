package com.example

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class AnimeninjaPlugin: Plugin() {
    override fun load(context: Context) {
        // Pon aquí tu cf_clearance del curl:
        // AnimeOnlineNinjaProvider.cfClearance = "MP4dN_e..."
        registerMainAPI(AnimeOnlineNinjaProvider())
    }
}