package com.example

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class DoramasflixPlugin: Plugin() {
    override fun load(context: Context) {
        registerMainAPI(DoramasflixProvider())
        registerExtractorAPI(OkRuExtractor())
        registerExtractorAPI(Do7GoExtractor())
        registerExtractorAPI(PlayMogoExtractor())
        registerExtractorAPI(StreamwishExtractor())
        registerExtractorAPI(FilemoonExtractor())
        registerExtractorAPI(VoeExtractor())
        registerExtractorAPI(UqloadExtractor())
        registerExtractorAPI(FPlayerExtractor())
    }
}