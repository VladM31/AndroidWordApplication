package com.generagames.happy.town.farm.wordandroid.domain.models.data

import can.lucky.of.core.domain.models.enums.Currency
import com.generagames.happy.town.farm.wordandroid.domain.models.keys.Platforms

data class GooglePay(
    val token: String,
    val currency: Currency,

    val cost: Float,
    val usdCost: Float
)
