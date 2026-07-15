package com.example

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

var retrotveAppContext: Context? = null

@CloudstreamPlugin
class RetrotvePlugin: Plugin() {
    override fun load(context: Context) {
        retrotveAppContext = context
        registerMainAPI(RetrotveProvider())
    }
}