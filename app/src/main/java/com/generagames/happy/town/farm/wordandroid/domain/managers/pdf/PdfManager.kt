package com.generagames.happy.town.farm.wordandroid.domain.managers.pdf

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

abstract class PdfManager(
    private val key: String,
    fileName: String,
    context: Context,
    sharedPrefName: String,
) {

    private val contentFile = File(context.cacheDir, fileName)
    private val prefs = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)

    protected suspend fun downloadAndSave(): File {
        val localLastUpdate = prefs.getString(key, null)
        if (localLastUpdate == getLastUpdated() && contentFile.exists()) {
            return contentFile
        }

        return withContext(Dispatchers.IO) {
            val content = fetchContent()
            FileOutputStream(contentFile).use { output ->
                output.write(content)
            }
            prefs.edit().putString(key, getLastUpdated()).apply()
            contentFile
        }
    }

    protected abstract suspend fun fetchContent(): ByteArray

    protected abstract fun getLastUpdated(): String
}