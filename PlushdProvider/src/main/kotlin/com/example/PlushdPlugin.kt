package com.example

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class PlushdPlugin: Plugin() {
    override fun load(context: Context) {
        registerMainAPI(PlushdProvider())
        registerExtractorAPI(PelisplusUpnsPro())
        registerExtractorAPI(PelisplusUpnsPro2())
        registerExtractorAPI(PelisplusUpnsPro3())
        registerExtractorAPI(PelisplusRpmstreamLive())
        registerExtractorAPI(WaaawTo())
        registerExtractorAPI(FilemoonSx())
        registerExtractorAPI(FilemoonLink())
        registerExtractorAPI(VidhideplusCom())
        registerExtractorAPI(VidhideproCom())
        registerExtractorAPI(StreamwishTo())
        registerExtractorAPI(DoodLa())
        registerExtractorAPI(StreamtapeCc())
        registerExtractorAPI(LulustreamCom())
        registerExtractorAPI(WatchsbCom())
        registerExtractorAPI(UqloadCom())
        registerExtractorAPI(LvturboCom())
    }
}