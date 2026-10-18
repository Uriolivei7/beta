package com.example

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lagradost.cloudstream3.APIHolder
import com.lagradost.cloudstream3.CloudStreamApp.Companion.getKey
import com.lagradost.cloudstream3.MainActivity
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class IptvPlugin : Plugin() {
    private var activity: AppCompatActivity? = null

    override fun load(context: Context) {
        activity = context as? AppCompatActivity

        openSettings = {
            val frag = IptvSettingsFragment(this)
            activity?.let { frag.show(it.supportFragmentManager, "IPTV") }
        }

        reload()
    }

    fun reload() {
        try {
            unregisterExisting()

            val savedLinks = getKey<Array<IptvLink>>("iptv_links") ?: emptyArray()
            Log.d(TAG, "reload: guardados=${savedLinks.size}")

            savedLinks.forEach { link ->
                val url = when (link.type) {
                    TYPE_XTREAM -> normalizeXtreamLink(link.link)
                    TYPE_M3U -> normalizeM3uLink(link.link)
                    else -> normalizeIptvLink(link.link)
                }
                Log.d(TAG, "registrando '${link.name}' type=${link.type} url=${url.take(100)}")
                val provider = IptvProvider(url, link.name)
                registerMainAPI(provider)
                refreshApi(provider)
            }

            Log.d(TAG, "reload: apis=${APIHolder.apis.size} providers=${APIHolder.allProviders.size}")
            MainActivity.afterPluginsLoadedEvent.invoke(true)
        } catch (e: Exception) {
            Log.e(TAG, "reload fallo", e)
        }
    }

    private fun refreshApi(provider: IptvProvider) {
        try {
            val current = APIHolder.apis
            if (current.none { it === provider || it.name == provider.name }) {
                APIHolder.apis = current.plus(provider)
                Log.d(TAG, "refreshApi: ${provider.name} agregado a apis")
            } else {
                Log.d(TAG, "refreshApi: ${provider.name} ya estaba en apis")
            }
        } catch (e: Exception) {
            Log.e(TAG, "refreshApi fallo", e)
        }
    }

    private fun unregisterExisting() {
        try {
            val old = APIHolder.allProviders.filter { it is IptvProvider }
            old.forEach { existing ->
                APIHolder.allProviders.remove(existing)
                APIHolder.removePluginMapping(existing)
            }
            Log.d(TAG, "unregisterExisting: quitados=${old.size}")
        } catch (e: Exception) {
            Log.e(TAG, "unregisterExisting fallo", e)
        }
    }

    companion object {
        const val TAG = "IPTV"
    }
}
