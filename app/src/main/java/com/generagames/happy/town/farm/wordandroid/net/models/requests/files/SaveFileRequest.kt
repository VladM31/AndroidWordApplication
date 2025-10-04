package com.generagames.happy.town.farm.wordandroid.net.models.requests.files

data class SaveFileRequest(
    val content: ByteArray,
    val fileName: String,
)
