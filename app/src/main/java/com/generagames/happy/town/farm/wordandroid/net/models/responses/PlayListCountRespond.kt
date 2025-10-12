package com.generagames.happy.town.farm.wordandroid.net.models.responses

import java.time.OffsetDateTime

data class PlayListCountRespond(
    val id: String,
    val userId: String,
    val name: String,
    val createdAt: OffsetDateTime,
    val count: Long
)
