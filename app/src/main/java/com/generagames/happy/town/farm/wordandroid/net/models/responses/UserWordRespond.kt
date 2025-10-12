package com.generagames.happy.town.farm.wordandroid.net.models.responses

import java.time.OffsetDateTime

data class UserWordRespond(
    val id: String,
    val learningGrade: Long,
    val createdAt: OffsetDateTime,
    val lastReadDate: OffsetDateTime,
    val word: WordRespond,
)
