package com.generagames.happy.town.farm.wordandroid.net.models.requests.pay

import can.lucky.of.core.domain.models.enums.Currency
import com.generagames.happy.town.farm.wordandroid.domain.models.keys.Platforms

data class GooglePayRequest(
    val token: String,
    val currency: Currency,

    val cost: Float,
    val usdCost: Float,

    val platform: String = Platforms.PLATFORM_ANDROID
)
