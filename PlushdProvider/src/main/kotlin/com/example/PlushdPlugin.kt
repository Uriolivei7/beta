package com.example

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import com.stormunblessed.PlushdProvider

@CloudstreamPlugin
class PlushdPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(PlushdProvider())
    }
}
