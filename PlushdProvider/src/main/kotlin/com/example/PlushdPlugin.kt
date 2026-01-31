package com.example

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context
import android.util.Log

@CloudstreamPlugin
class PlushdPlugin: Plugin() {
    override fun load(context: Context) {
        Log.d("PlushdPlugin", "🚀 Cargando PlusHD Plugin...")

        // Registrar provider principal
        registerMainAPI(PlushdProvider())
        Log.d("PlushdPlugin", "✅ Provider registrado")

        // Registrar extractores
        registerExtractorAPI(PelisplusUpnsPro())
        registerExtractorAPI(PelisplusUpnsPro2())
        registerExtractorAPI(PelisplusUpnsPro3())
        registerExtractorAPI(EmturbovidCom())
        registerExtractorAPI(Vidhide())
        registerExtractorAPI(RPMStream())
        registerExtractorAPI(Listeamed())

        Log.d("PlushdPlugin", "✅ Todos los extractores registrados")
        Log.d("PlushdPlugin", "🎉 Plugin cargado completamente")
    }
}