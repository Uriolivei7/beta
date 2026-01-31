package com.example

import android.content.Context
import android.util.Log
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class PlushdPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(PlushdProvider())
    }
}