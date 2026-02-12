package it.dogior.example

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException
import org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper
import java.util.concurrent.TimeUnit

// Copied from DownloaderTestImpl. I only changed the cookies as the playlists wouldn't show up in search results.
// Now why is it called downloader if downloading it's not its only job I don't know
class NewPipeDownloader(builder: OkHttpClient.Builder): Downloader() {
    private val client: OkHttpClient = builder.readTimeout(30, TimeUnit.SECONDS).build()

    // Permitimos que el User Agent sea dinámico
    var customUserAgent: String = DEFAULT_USER_AGENT
    var cookies: String? = null

    override fun execute(request: Request): Response {
        val httpMethod: String = request.httpMethod()
        val url: String = request.url()

        val dataToSend: ByteArray? = request.dataToSend()
        var requestBody: RequestBody? = null
        if (dataToSend != null) {
            requestBody = dataToSend.toRequestBody(null, 0, dataToSend.size)
        }

        val requestBuilder: okhttp3.Request.Builder = okhttp3.Request.Builder()
            .method(httpMethod, requestBody).url(url)
            .addHeader("User-Agent", customUserAgent) // Usamos el dinámico

        // Si tenemos cookies guardadas, las añadimos manualmente
        cookies?.let {
            requestBuilder.addHeader("Cookie", it)
        }

        val response = client.newCall(requestBuilder.build()).execute()

        // Manejo de error 429 (Demasiadas peticiones / Bot check)
        if (response.code == 429) {
            response.close()
            throw ReCaptchaException("reCaptcha Challenge requested", url)
        }

        val body = response.body
        val responseBodyToReturn: String = body?.string() ?: ""
        val latestUrl = response.request.url.toString()

        return Response(
            response.code, response.message, response.headers.toMultimap(),
            responseBodyToReturn, latestUrl
        )
    }

    companion object {
        // User Agent de Android TV por defecto (más estable)
        private const val DEFAULT_USER_AGENT = "com.google.android.youtube.tv/4.10.001 (Android 11; Television; Sony; BRAVIA 4K VH2; America/New_York; en_US)"
        private var instance: NewPipeDownloader? = null

        fun init(builder: OkHttpClient.Builder?): NewPipeDownloader {
            val newInstance = NewPipeDownloader(builder ?: OkHttpClient.Builder())
            instance = newInstance
            return newInstance
        }

        fun getInstance(): NewPipeDownloader {
            return instance ?: init(null)
        }
    }
}