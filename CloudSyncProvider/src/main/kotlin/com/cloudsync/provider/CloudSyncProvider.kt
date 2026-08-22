package com.cloudsync.provider

import com.cloudsync.backup.CloudSyncBackup
import com.cloudsync.model.*
import com.cloudsync.storage.CloudSyncStorage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lagradost.api.Log
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageData
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.newHomePageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CloudSyncProvider : MainAPI() {

    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime)
    override val hasMainPage = true
    override val hasQuickSearch = false
    
    private val mapper = jacksonObjectMapper()

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        return withContext(Dispatchers.IO) {
            newHomePageResponse(
                HomePageList(request.name, emptyList(), isHorizontalImages = false),
                hasNext = false
            )
        }
    }

    fun startSync(context: android.content.Context) {
        val creds = CloudSyncStorage.getCreds()
        Log.d("CloudSync", "startSync: creds=${if (creds != null) "present (syncKey=${creds.syncKey})" else "null"}, loggedIn=${creds?.isLoggedIn()}")
        if (creds == null || !creds.isLoggedIn()) {
            Log.w("CloudSync", "startSync: No creds or not logged in, aborting sync")
            return
        }
        
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.SupervisorJob() + kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                Log.d("CloudSync", "performFullSync starting for categories: ${SyncCategory.values().filter { creds.isBackupEnabled(it) }.joinToString { it.key }}")
                performFullSync(context, creds)
                Log.d("CloudSync", "performFullSync completed")
            } catch (e: Exception) {
                Log.e("CloudSync", "performFullSync exception: ${e.message}")
            }
        }
    }
    
    private fun performFullSync(context: android.content.Context, creds: CloudSyncCreds) {
        val categories = SyncCategory.values()
        
        for (category in categories) {
            val backupEnabled = creds.isBackupEnabled(category)
            val restoreEnabled = creds.isRestoreEnabled(category)
            Log.d("CloudSync", "Category ${category.key}: backupEnabled=$backupEnabled, restoreEnabled=$restoreEnabled")
            if (!backupEnabled && !restoreEnabled) continue
            
            if (backupEnabled) {
                val backup = CloudSyncBackup.buildBackupForCategory(context, category, creds)
                if (backup != null) {
                    Log.d("CloudSync", "Pushing category ${category.key}")
                    pushCategory(category, backup)
                } else {
                    Log.d("CloudSync", "No backup data for category ${category.key}")
                }
            }
            
            if (restoreEnabled) {
                Log.d("CloudSync", "Pulling category ${category.key}")
                pullCategory(context, category, creds)
            }
        }
    }
    
    private fun pushCategory(category: SyncCategory, backup: BackupFile) {
        val json = mapper.writeValueAsString(backup)
        val hash = CloudSyncBackup.computeHash(json)
        val currentHash = CloudSyncStorage.getCategoryHash(category)
        Log.d("CloudSync", "pushCategory ${category.key}: newHash=$hash, currentHash=$currentHash")
        if (hash != currentHash) {
            Log.d("CloudSync", "Pushing to Firebase for category ${category.key} (TODO: implement)")
            CloudSyncStorage.setCategoryHash(category, hash)
            CloudSyncStorage.setCategoryTimestamp(category, System.currentTimeMillis())
        } else {
            Log.d("CloudSync", "No changes for category ${category.key}, skipping push")
        }
    }
    
    private fun pullCategory(context: android.content.Context, category: SyncCategory, creds: CloudSyncCreds) {
        Log.d("CloudSync", "pullCategory ${category.key} - TODO: implement Firebase pull")
    }
}