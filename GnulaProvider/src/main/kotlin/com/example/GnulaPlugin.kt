package com.example

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class GnulaPlugin : Plugin() {
    override fun load(context: Context) {
        GnulaProvider.pluginContext = context
        registerMainAPI(GnulaProvider())
    }
}
