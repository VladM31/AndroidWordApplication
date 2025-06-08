package com.generagames.happy.town.farm.wordandroid.net.models.responses

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class SubscribeRespond(
    @SerializedName("_expirationDate")
    val expirationDate : LocalDateTime
)
