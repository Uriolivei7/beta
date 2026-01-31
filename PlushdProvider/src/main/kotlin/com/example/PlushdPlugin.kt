package com.example

import android.content.Context
import android.util.Log
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class PlushdPlugin : Plugin() {
    override fun load(context: Context) {
        Log.d("PlushdPlugin", "🚀 Cargando PlusHD Plugin...")

        registerMainAPI(PlushdProvider())
        Log.d("PlushdPlugin", "✅ Provider registrado")

        // Extractores personalizados
        registerExtractorAPI(Callistanise())
        registerExtractorAPI(TurbovidHLS())
        registerExtractorAPI(WaawTo())
        registerExtractorAPI(Listeamed())
        registerExtractorAPI(PelisplusUpns())

        Log.d("PlushdPlugin", "✅ Todos los extractores registrados")
        Log.d("PlushdPlugin", "🎉 Plugin cargado completamente")
    }
}