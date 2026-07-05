package com.horis.example

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
open class NetflixmirrorPlugin: Plugin() {
    override fun load(context: Context) {
        pluginContext = context.applicationContext
        NetflixMirrorStorage.init(context.applicationContext)
        registerMainAPI(NetflixProvider())
        registerMainAPI(PrimevideoProvider())
    }

}
