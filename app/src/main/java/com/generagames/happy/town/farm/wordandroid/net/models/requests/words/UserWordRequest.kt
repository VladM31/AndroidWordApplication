package com.generagames.happy.town.farm.wordandroid.net.models.requests.words

data class UserWordRequest(
    val customSoundFileName: String?,
    val customImageFileName: String?,
    val word: WordRequest,
)
