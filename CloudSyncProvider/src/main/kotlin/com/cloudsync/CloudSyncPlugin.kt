package com.cloudsync

import android.content.Context
import com.cloudsync.provider.CloudSyncProvider
import com.cloudsync.storage.CloudSyncStorage
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@CloudstreamPlugin
class CloudSyncPlugin : Plugin() {
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun load(context: Context) {
        CloudSyncStorage.init(context)
        registerMainAPI(CloudSyncProvider())
        
        val creds = CloudSyncStorage.getCreds()
        if (creds != null && creds.isLoggedIn()) {
            scope.launch { CloudSyncProvider().startSync(context) }
        }
    }
}