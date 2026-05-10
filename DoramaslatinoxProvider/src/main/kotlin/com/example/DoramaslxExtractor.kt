package com.example

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
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
                    val handler = Handler(Looper.getMainLooper())
                    val view = getOrCreateWebView(ctx)

                    val timeoutRunnable = Runnable {
                        if (!resumed) {
                            resumed = true
                            Log.e("DoramasLX", "WebView timeout: $playerUrl")
                            cont.resume(null)
                        }
                    }
                    handler.postDelayed(timeoutRunnable, 25000)

                    view.stopLoading()
                    view.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(v: WebView, u: String) {
                            if (resumed) return
                            Log.d("DoramasLX", "onPageFinished: ${u.take(60)}")

                            pollForVideo(v, 0, handler) { result ->
                                if (!resumed) {
                                    resumed = true
                                    handler.removeCallbacks(timeoutRunnable)
                                    Log.d("DoramasLX", "videoUrl: ${result?.take(80)}")
                                    cont.resume(result)
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

    private fun pollForVideo(
        view: WebView,
        attempt: Int,
        handler: Handler,
        onResult: (String?) -> Unit
    ) {
        if (attempt > 40) {
            Log.e("DoramasLX", "pollForVideo: max attempts")
            onResult(null)
            return
        }

        view.evaluateJavascript(
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
      var s = iframes[i].src || '';
      if (s.match(/\.(m3u8|mp4)/i)) return s;
    }
    var scripts = document.querySelectorAll('script:not([src])');
    for (var i = 0; i < scripts.length; i++) {
      var text = scripts[i].textContent || '';
      var m = text.match(/["'](?:file|src|url)["']\s*[:=]\s*["']([^"']+\.(?:m3u8|mp4)[^"']*)["']/i);
      if (m) return m[1].replace(/\\\//g, '/');
    }
  } catch(e) {}
  return '';
})()"""
        ) { result ->
            val decoded = result?.removeSurrounding("\"")
                ?.replace("\\\"", "\"")
                ?.replace("\\n", "\n")
                ?.replace("\\/", "/")
                ?.trim() ?: ""

            if (decoded.isNotBlank() && decoded != "null") {
                Log.d("DoramasLX", "poll $attempt OK: ${decoded.take(80)}")
                onResult(decoded)
            } else {
                handler.postDelayed({ pollForVideo(view, attempt + 1, handler, onResult) }, 500)
            }
        }
    }
}
