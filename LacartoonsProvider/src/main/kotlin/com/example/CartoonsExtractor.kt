package com.example

/*
    CEO: Ranita
    Empresa: RANITA TECH
    Versión software: 1.0     fecha: 09/08/2025
*/

import android.util.Log
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.newSubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.INFER_TYPE
import com.lagradost.cloudstream3.utils.M3u8Helper.Companion.generateM3u8
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.fixUrl
import java.net.URL
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

open class Cubeembed : ExtractorApi() {
    override var name = "Cubeembed"
    override var mainUrl = "https://cubeembed.rpmvid.com"
    override val requiresReferer = true

    companion object {
        var pluginContext: Context? = null
    }

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d("Cubeembed", "resolve start host=${try { URL(url).host } catch (_: Exception) { "unknown" }}")
        val turboResult = resolveTurboEmbed(url)
        if (turboResult != null && turboResult.streamUrl.isNotBlank()) {
            Log.d("Cubeembed", "TurboEmbed JW source found host=${try { URL(turboResult.streamUrl).host } catch (_: Exception) { "unknown" }}")
            callback(newExtractorLink(this.name, "$name [TurboEmbed]", turboResult.streamUrl, ExtractorLinkType.M3U8) {
                this.referer = turboResult.pageUrl
                this.quality = Qualities.Unknown.value
            })
            return
        }
        Log.w("Cubeembed", "TurboEmbed WebView terminó sin fuente JW; intento extractor API legado")

        val hash = url.substringAfterLast("#").substringAfter("/")
        val baseUrl = getBaseUrl(url)
        Log.d("Cubeembed", "hash=$hash baseUrl=$baseUrl")

        val headers = mapOf("User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0")
        val apiResponse = app.get("$baseUrl/api/v1/video?id=$hash", headers = headers)
        val encrypted = apiResponse.text.trim()
        Log.d("Cubeembed", "Legacy API HTTP ${apiResponse.code} contentType=${apiResponse.headers["content-type"]} responseLength=${encrypted.length}")
        if (encrypted.isEmpty()) {
            Log.w("Cubeembed", "API devolvió cuerpo vacío para id=$hash")
            return
        }

        val key = "kiemtienmua911ca"
        val ivList = listOf("1234567890oiuytr", "0123456789abcdef")

        val decryptedText = ivList.firstNotNullOfOrNull { iv ->
            try {
                decryptAES(encrypted, key, iv)
            } catch (e: Exception) {
                Log.w("Cubeembed", "Decrypt failed with IV=$iv: ${e.message}")
                null
            }
        } ?: throw Exception("Failed to decrypt with all IVs")

        Log.d("Cubeembed", "Decrypted payload length=${decryptedText.length}")

        val source = Regex("\"source\":\"(.*?)\"").find(decryptedText)
            ?.groupValues?.get(1)
            ?.replace("\\/", "/") ?: ""
        Log.d("Cubeembed", "parsed source=${source.isNotBlank()} sourceLength=${source.length}")

        val subtitleSection = Regex("\"subtitle\":\\{(.*?)\\}").find(decryptedText)?.groupValues?.get(1)
        subtitleSection?.let { section ->
            Regex("\"([^\"]+)\":\\s*\"([^\"]+)\"").findAll(section).forEach { match ->
                val lang = match.groupValues[1]
                val rawPath = match.groupValues[2].split("#")[0]
                if (rawPath.isNotEmpty()) {
                    val path = rawPath.replace("\\/", "/")
                    val subUrl = "$baseUrl$path"
                    subtitleCallback(newSubtitleFile(lang, fixUrl(subUrl)))
                }
            }
        }

        if (source.isNotEmpty()) {
            val sourceHttp = source.replaceFirst("https://", "http://")
            Log.d("Cubeembed", "Source M3U8 (HTTP): $sourceHttp")
            callback.invoke(
                newExtractorLink(this.name, "$name ", sourceHttp, ExtractorLinkType.M3U8) {
                    this.referer = url
                    this.quality = Qualities.Unknown.value
                }
            )
            Log.d("Cubeembed", "emitted source M3U8")
        }
        val hlsTiktok = Regex("\"hlsVideoTiktok\":\"(.*?)\"").find(decryptedText)
            ?.groupValues?.get(1)
            ?.replace("\\/", "/") ?: ""
        Log.d("Cubeembed", "parsed hlsVideoTiktok=${hlsTiktok.isNotBlank()} pathLength=${hlsTiktok.length}")
        if (hlsTiktok.isNotEmpty()) {
            val tiktokUrl = "$baseUrl$hlsTiktok"
            Log.d("Cubeembed", "TikTok M3U8: $tiktokUrl")
            callback.invoke(
                newExtractorLink(this.name, "$name TK", tiktokUrl, ExtractorLinkType.M3U8) {
                    this.referer = url
                    this.quality = Qualities.Unknown.value
                }
            )
            Log.d("Cubeembed", "emitted TikTok M3U8")
        }
        if (source.isBlank() && hlsTiktok.isBlank()) {
            Log.w("Cubeembed", "payload descifrado sin sources reconocidos; no se emitieron enlaces")
        }
    }

    private suspend fun resolveTurboEmbed(pageUrl: String): TurboEmbedResolved? = withTimeoutOrNull(45_000L) {
        cubeEmbedWebViewMutex.withLock {
            withContext(Dispatchers.Main) {
                val appContext = pluginContext?.applicationContext ?: return@withContext null
                val handler = Handler(Looper.getMainLooper())
                val result = CompletableDeferred<TurboEmbedResolved?>()
                val webView = WebView(appContext)
                var attempts = 0
                var lastPageUrl = pageUrl

                fun poll() {
                    if (result.isCompleted) return
                    attempts++
                    val script = """(function(){try{
                        var u=null,v=document.querySelector('video');
                        if(v&&v.currentSrc&&/^https?:/i.test(v.currentSrc)){u=v.currentSrc;}
                        if(!u&&performance&&performance.getEntriesByType){var es=performance.getEntriesByType('resource');for(var i=0;i<es.length;i++){var n=es[i].name;if(n&&n.indexOf('.m3u8')>=0){u=n;break;}}}
                        if(!u){var p=window.jwplayer&&window.jwplayer('media-player');if(p&&p.getPlaylistItem){var it=p.getPlaylistItem();var f=it&&(it.file||(it.sources&&it.sources[0]&&it.sources[0].file));if(f&&/^https?:/i.test(f)){u=f;}}}
                        if(u){NativeBridge.onStream(location.href,u);return true;}
                        var b=document.querySelector('#player-button')||document.querySelector('.tvp-poster-play')||document.querySelector('.tvp-center-play');
                        if(b){b.click();}else{var v2=document.querySelector('video');if(v2&&v2.paused){try{v2.play();}catch(e){}}}
                        return false;
                    }catch(e){return false;}})()"""
                    try { webView.evaluateJavascript(script, null) } catch (_: Exception) {}
                    if (attempts < 40) handler.postDelayed({ poll() }, 1000L)
                    else if (!result.isCompleted) result.complete(null)
                }

                fun runApi() {
                    if (result.isCompleted) return
                    val apiScript = """(async function(){try{
                        if(!window.simpleCrypto){NativeBridge.onErr('no simpleCrypto');return;}
                        var slug=location.pathname.split('/').filter(Boolean).pop();
                        if(!slug){NativeBridge.onErr('no slug');return;}
                        var pv=new URLSearchParams(location.search).get('pv');
                        var salts=[location.origin,location.hostname,location.host,''];
                        function collect(o,acc,d){if(!o||typeof o!=='object'||d>6)return;
                          if(typeof o.hls==='string'&&o.hls&&!acc.hls)acc.hls=o.hls;
                          if(typeof o.tiktok==='string'&&o.tiktok&&!acc.tiktok)acc.tiktok=o.tiktok;
                          if(typeof o.direct==='string'&&o.direct&&!acc.direct)acc.direct=o.direct;
                          for(var k in o){try{collect(o[k],acc,d+1);}catch(e){}}}
                        function pick(a){return a.hls||a.tiktok||a.direct||null;}
                        async function tryDec(v,salt){if(typeof v!=='string'||v.length<8)return null;
                          try{var t=await window.simpleCrypto.decrypt(v,salt);var p=JSON.parse(t);if(p&&typeof p==='object')return p;}catch(e){}
                          try{var p2=JSON.parse(v);if(p2&&typeof p2==='object')return p2;}catch(e){}
                          return null;}
                        for(var i=0;i<salts.length;i++){
                          var salt=salts[i],s;
                          try{s=await window.simpleCrypto.encrypt(slug,salt);}catch(e){continue;}
                          var url='/api/stream?s='+s+(pv?'&pv='+encodeURIComponent(pv):'');
                          var r;try{r=await fetch(url);}catch(e){continue;}
                          if(!r.ok){NativeBridge.onErr('api '+r.status+' salt='+i);continue;}
                          var j;try{j=await r.json();}catch(e){continue;}
                          var data=(j&&j.data!==undefined)?j.data:j;
                          var acc={};collect(data,acc,0);var src=pick(acc);
                          if(src){NativeBridge.onStream(location.href,src);return;}
                          if(typeof data==='string'){var p=await tryDec(data,salt);collect(p,acc,0);src=pick(acc);if(src){NativeBridge.onStream(location.href,src);return;}}
                          else if(data&&typeof data==='object'){
                            for(var k in data){if(typeof data[k]==='string'){var p2=await tryDec(data[k],salt);if(p2){collect(p2,acc,0);src=pick(acc);if(src){NativeBridge.onStream(location.href,src);return;}}}}
                          }
                        }
                        NativeBridge.onErr('no source in payload');
                    }catch(e){try{NativeBridge.onErr('ex '+e);}catch(_){}}})()"""
                    try { webView.evaluateJavascript(apiScript, null) } catch (_: Exception) {}
                }

                try {
                    webView.settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        cacheMode = WebSettings.LOAD_NO_CACHE
                        userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36"
                    }
                    webView.addJavascriptInterface(object {
                        @JavascriptInterface
                        fun onStream(currentPage: String, stream: String) {
                            handler.post { if (!result.isCompleted) result.complete(TurboEmbedResolved(currentPage, stream)) }
                        }

                        @JavascriptInterface
                        fun onErr(msg: String) {
                            Log.w("Cubeembed", "[TurboJS] $msg")
                        }
                    }, "NativeBridge")
                    webView.webViewClient = object : WebViewClient() {
                        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                            val u = request?.url?.toString()
                            if (u != null && u.contains(".m3u8")) {
                                handler.post { if (!result.isCompleted) result.complete(TurboEmbedResolved(view?.url ?: lastPageUrl, u)) }
                            }
                            return null
                        }
                        override fun onPageFinished(view: WebView?, currentUrl: String?) {
                            lastPageUrl = currentUrl ?: lastPageUrl
                            val safePage = try {
                                URL(currentUrl ?: "").let { it.host + it.path }
                            } catch (_: Exception) {
                                "unknown"
                            }
                            Log.d("Cubeembed", "TurboEmbed page loaded: " + safePage)
                            handler.postDelayed({ runApi() }, 1200L)
                            handler.postDelayed({ runApi() }, 4500L)
                            handler.postDelayed({ poll() }, 700L)
                        }
                    }
                    webView.loadUrl(pageUrl)
                    result.await()
                } catch (e: Exception) {
                    Log.w("Cubeembed", "TurboEmbed WebView failed: " + e::class.simpleName + ": " + e.message)
                    null
                } finally {
                    handler.removeCallbacksAndMessages(null)
                    webView.stopLoading()
                    webView.removeJavascriptInterface("NativeBridge")
                    webView.destroy()
                }
            }
        }
    }

    private fun getBaseUrl(url: String): String {
        return try {
            val parsed = URL(url)
            "${parsed.protocol}://${parsed.host}"
        } catch (e: Exception) {
            Log.e("Cubeembed", "getBaseUrl fallback: ${e.message}")
            mainUrl
        }
    }

    private fun decryptAES(inputHex: String, key: String, iv: String): String {
        val keySpec = SecretKeySpec(key.encodeToByteArray(), "AES")
        val ivSpec = IvParameterSpec(iv.encodeToByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val decrypted = cipher.doFinal(hexToBytes(inputHex))
        return String(decrypted, Charsets.UTF_8)
    }

    private fun hexToBytes(hex: String): ByteArray {
        check(hex.length % 2 == 0) { "Hex string must have an even length" }
        return hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
}

class Turboembed : Cubeembed() {
    override var name = "TurboEmbed"
    override var mainUrl = "https://turboembed.com"
}

private data class TurboEmbedResolved(val pageUrl: String, val streamUrl: String)

private val cubeEmbedWebViewMutex = Mutex()

class Sendvid : ExtractorApi() {
    override var name = "Sendvid"
    override val mainUrl = "https://sendvid.com"
    override val requiresReferer = false
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val doc = app.get(url).document
        val urlString = doc.select("head meta[property=og:video:secure_url]").attr("content")
        if (urlString.contains("m3u8")) {
            generateM3u8(
                name,
                urlString,
                mainUrl,
            ).forEach(callback)
        } else {
            callback.invoke(
                newExtractorLink(
                    source = this.name,
                    name = this.name,
                    url = urlString,
                    type = INFER_TYPE
                ) {
                    this.referer = url
                    this.quality = Qualities.Unknown.value
                }
            )
        }
    }
}
