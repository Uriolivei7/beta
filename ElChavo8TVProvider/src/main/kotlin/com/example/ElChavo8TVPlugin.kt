package com.example

import android.content.Context
import com.lagradost.cloudstream3.plugins.Plugin

class ElChavo8TVPlugin : Plugin() {
    override fun load(context: Context) {
        ElChavo8TVProvider.pluginContext = context
    }
}
