package com.example

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.plugins.*
import com.lagradost.cloudstream3.AcraApplication.Companion.getKey

private const val TAG = "XtreamPlugin"

@CloudstreamPlugin
class XtreamPlugin : Plugin() {
    override fun load(context: Context) {
        Log.d(TAG, "XtreamPlugin: LOAD iniciado") // Log de carga
        reload()
    }

    init {
        Log.d(TAG, "XtreamPlugin: INIT iniciado, asignando openSettings") // Log de inicialización
        this.openSettings = { activity ->
            Log.d(TAG, "XtreamPlugin: openSettings llamado por la UI") // Log de clic en Configuración
            try {
                val activity = activity as? AppCompatActivity
                if (activity != null) {
                    val frag = XtreamSettingsFragment(this)
                    frag.show(activity.supportFragmentManager, "XtreamIPTV")
                    Log.d(TAG, "XtreamPlugin: XtreamSettingsFragment mostrado exitosamente")
                } else {
                    Log.e(TAG, "XtreamPlugin: Error - activity is null or not AppCompatActivity")
                }
            } catch (e: Exception) {
                Log.e(TAG, "XtreamPlugin: EXCEPCIÓN al abrir settings: ${e.message}")
            }
        }
    }

    fun reload() {
        Log.d(TAG, "XtreamPlugin: RELOAD iniciado") // Log de recarga
        try {
            val savedLinks = getKey<Array<Link>>("xtream_iptv_links") ?: emptyArray()
            Log.d(TAG, "XtreamPlugin: Links guardados: ${savedLinks.size}")
            savedLinks.forEach { link ->
                val pluginData = PluginManager.getPluginsOnline().find { it.internalName.contains(link.name) }
                if (pluginData != null) {
                    PluginManager.unloadPlugin(pluginData.filePath)
                } else {
                    registerMainAPI(XtreamProvider(link.mainUrl, link.name, link.username, link.password))
                }
            }
            MainActivity.afterPluginsLoadedEvent.invoke(true)
        } catch (e: Exception) {
            Log.e(TAG, "XtreamPlugin: EXCEPCIÓN en reload: ${e.message}")
        }
    }
}