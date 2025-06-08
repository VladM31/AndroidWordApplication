package com.generagames.happy.town.farm.wordandroid.net.models.responses

import com.google.gson.annotations.SerializedName

data class UserWordRespond(
    val id: String,
    val learningGrade: Long,
    val dateOfAdded: String,
    val lastReadDate: String,
    @SerializedName("wordDto")
    val word: WordRespond,
)
