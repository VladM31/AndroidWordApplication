package com.generagames.happy.town.farm.wordandroid.domain.managers.pdf

import android.content.Context
import can.lucky.of.core.domain.managers.infos.PolicyManager
import com.generagames.happy.town.farm.wordandroid.net.clients.files.DownloadClient
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import java.io.File

class PolicyManagerImpl(
    private val downloadClient: DownloadClient,
    context: Context,
) : PdfManager(
    key = "last_update_day_key",
    fileName = "policy.pdf",
    context = context,
    sharedPrefName = "policy_pref",
), PolicyManager {
    override suspend fun getPolicyFile(): File {
        return downloadAndSave()
    }

    override suspend fun fetchContent(): ByteArray {
        val fileUrl = Firebase.remoteConfig.getString("policy_link")
        return downloadClient.downloadFile(fileUrl).use {
            it.bytes()
        }
    }

    override fun getLastUpdated(): String =
        Firebase.remoteConfig.getString("policy_last_update_at")
}