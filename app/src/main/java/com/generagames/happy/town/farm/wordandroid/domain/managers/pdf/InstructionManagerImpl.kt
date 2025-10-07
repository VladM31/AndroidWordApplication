package com.generagames.happy.town.farm.wordandroid.domain.managers.pdf

import android.content.Context
import com.generagames.happy.town.farm.wordandroid.net.clients.files.DownloadClient
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import java.io.File

class InstructionManagerImpl(
    private val downloadClient: DownloadClient,
    context: Context,
) : PdfManager(
    key = "last_update_day_key",
    fileName = "instruction.pdf",
    context = context,
    sharedPrefName = "instruction_pref",
), InstructionManager {
    override suspend fun getInstructionFile(): File {
        return downloadAndSave()
    }

    override suspend fun fetchContent(): ByteArray {
        val fileUrl = Firebase.remoteConfig.getString("instruction_link")
        return downloadClient.downloadFile(fileUrl).use {
            it.bytes()
        }
    }

    override fun getLastUpdated(): String =
        Firebase.remoteConfig.getString("instruction_last_update")
}
