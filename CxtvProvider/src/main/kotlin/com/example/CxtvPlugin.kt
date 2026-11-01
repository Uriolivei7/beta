package com.example

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class CxtvPlugin : Plugin() {
    override fun load(context: Context) {
        CxtvProvider.pluginContext = context.applicationContext ?: context
        registerMainAPI(CxtvProvider())
    }
}
