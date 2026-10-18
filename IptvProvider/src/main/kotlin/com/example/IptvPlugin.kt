package com.example

import android.content.Context
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
            savedLinks.forEach { link ->
                registerMainAPI(IptvProvider(normalizeIptvLink(link.link), link.name))
            }

            MainActivity.afterPluginsLoadedEvent.invoke(true)
        } catch (e: Exception) {
        }
    }

    private fun unregisterExisting() {
        try {
            APIHolder.allProviders
                .filter { it is IptvProvider }
                .forEach { existing ->
                    APIHolder.allProviders.remove(existing)
                    APIHolder.removePluginMapping(existing)
                }
        } catch (e: Exception) {
        }
    }
}
