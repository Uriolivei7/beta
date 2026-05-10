package com.example

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DoramasLatinoXExtractor : ExtractorApi() {
    override val name = "DoramasLatinoX"
    override val mainUrl = "https://short.icu"
    override val requiresReferer = true

    companion object {
        var pluginContext: Context? = null
        private var wv: WebView? = null
        private val wvMutex = Mutex()
    }

    private fun destroyWv() {
        try { wv?.destroy() } catch (_: Exception) {}
        wv = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun getOrCreateWebView(ctx: Context): WebView {
        wv?.let { return it }
        val view = WebView(ctx.applicationContext).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.mediaPlaybackRequiresUserGesture = false
        }
        wv = view
        return view
    }

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val ctx = pluginContext ?: run {
            Log.e("DoramasLX", "pluginContext null")
            return
        }

        val playerUrl = url.ifBlank { return }
        Log.d("DoramasLX", "getUrl: $playerUrl referer=$referer")

        val videoUrl = wvMutex.withLock {
            withContext(Dispatchers.Main) {
                suspendCoroutine<String?> { cont ->
                    var resumed = false
                    var interceptedUrl: String? = null
                    val handler = Handler(Looper.getMainLooper())
                    val view = getOrCreateWebView(ctx)

                    val timeoutRunnable = Runnable {
                        if (!resumed) {
                            resumed = true
                            Log.d("DoramasLX", "timeout, intercepted=$interceptedUrl")
                            cont.resume(interceptedUrl)
                        }
                    }
                    handler.postDelayed(timeoutRunnable, 30000)

                    view.webViewClient = object : WebViewClient() {
                        override fun shouldInterceptRequest(
                            view: WebView,
                            request: WebResourceRequest
                        ): WebResourceResponse? {
                            val reqUrl = request.url.toString()
                            if (request.isForMainFrame) {
                                Log.d("DoramasLX", "main frame: ${reqUrl.take(100)}")
                            }
                            if (interceptedUrl == null) {
                                if (reqUrl.contains(".m3u8", true) || reqUrl.contains(".mp4", true)) {
                                    interceptedUrl = reqUrl
                                    Log.d("DoramasLX", ">>> VIDEO: ${reqUrl.take(80)}")
                                } else if (reqUrl.contains("short.icu") || reqUrl.contains("abyss.to")) {
                                    Log.d("DoramasLX", "relevant: ${reqUrl.take(100)}")
                                }
                            }
                            return null
                        }

                        override fun onPageFinished(v: WebView, u: String) {
                            if (resumed) return
                            Log.d("DoramasLX", "onPageFinished: ${u.take(60)}")
                            pollLoop(v)
                        }

                        fun pollLoop(wv: WebView) {
                            if (resumed) return
                            interceptedUrl?.let {
                                resumed = true
                                handler.removeCallbacks(timeoutRunnable)
                                cont.resume(it)
                                return
                            }

                            wv.evaluateJavascript(
                                """(function() {
  try {
    if (typeof jwplayer !== 'undefined') {
      var jw = jwplayer();
      if (jw && jw.getPlaylist && typeof jw.getPlaylist === 'function') {
        var pl = jw.getPlaylist();
        if (pl && pl.length > 0) {
          for (var i = 0; i < pl.length; i++) {
            var item = pl[i];
            if (item.file) return item.file;
            if (item.sources && item.sources.length > 0) {
              for (var s = 0; s < item.sources.length; s++) {
                if (item.sources[s].file) return item.sources[s].file;
                if (item.sources[s].src) return item.sources[s].src;
              }
            }
          }
        }
      }
    }
    var videos = document.querySelectorAll('video');
    for (var i = 0; i < videos.length; i++) {
      if (videos[i].src) return videos[i].src;
      var sources = videos[i].querySelectorAll('source');
      for (var j = 0; j < sources.length; j++) {
        if (sources[j].src) return sources[j].src;
      }
    }
    var iframes = document.querySelectorAll('iframe');
    for (var i = 0; i < iframes.length; i++) {
      try {
        var doc = iframes[i].contentDocument || iframes[i].contentWindow.document;
        if (doc) {
          var vs = doc.querySelectorAll('video');
          for (var j = 0; j < vs.length; j++) {
            if (vs[j].src) return vs[j].src;
          }
        }
      } catch(e) {}
    }
  } catch(e) {}
  return '';
})()"""
                            ) { result: String? ->
                                if (resumed) return@evaluateJavascript
                                val decoded = result?.removeSurrounding("\"")
                                    ?.replace("\\\"", "\"")
                                    ?.replace("\\n", "\n")
                                    ?.replace("\\/", "/")
                                    ?.trim() ?: ""
                                if (decoded.isNotBlank() && decoded != "null") {
                                    resumed = true
                                    handler.removeCallbacks(timeoutRunnable)
                                    Log.d("DoramasLX", "JS OK: ${decoded.take(80)}")
                                    cont.resume(decoded)
                                } else {
                                    handler.postDelayed({ pollLoop(wv) }, 500)
                                }
                            }
                        }
                    }
                    view.loadUrl(playerUrl)
                }
            }
        } ?: run {
            Log.e("DoramasLX", "No se obtuvo video URL")
            return
        }

        Log.d("DoramasLX", "Video URL final: $videoUrl")
        callback.invoke(
            newExtractorLink(
                this.name,
                this.name,
                videoUrl,
                type = if (videoUrl.contains(".m3u8", true)) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
            ) {
                this.referer = playerUrl
                this.quality = getQualityFromName(videoUrl)
            }
        )
    }
}
