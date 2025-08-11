package com.generagames.happy.town.farm.wordandroid.net.models.requests

import com.generagames.happy.town.farm.wordandroid.domain.models.keys.Platforms

data class CardPayRequest(
    val phoneNumber : String,
    val email : String,

    val cardNumber : String,
    val expiryDate: String,
    val cardName: String,
    val cvv2: String,
    val cost: Float,
    val platform: String = Platforms.PLATFORM_ANDROID,

    )
