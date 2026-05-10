// ! Bu araç @byayzen tarafından | @CS-Karma için yazılmıştır.
package com.example

import com.lagradost.cloudstream3.extractors.OkRuSSL
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class DoramaslatinoxPlugin: Plugin() {
    override fun load() {
        registerMainAPI(DoramaslatinoxProvider())
        registerExtractorAPI(DoramasLatinoXExtractor())
        registerExtractorAPI(OkRuSSL())
    }
}