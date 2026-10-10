package com.example

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class OkruPlugin: Plugin() {
    override fun load(context: Context) {
        // Provider completo (catálogo ok.ru) + extractores globales para
        // el loadExtractor de todos los providers instalados.
        registerMainAPI(OkruProvider())
        registerExtractorAPI(OkRuExtractor())
        registerExtractorAPI(Odnoklassniki())
    }
}
