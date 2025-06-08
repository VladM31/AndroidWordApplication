package com.generagames.happy.town.farm.wordandroid.domain.models.data

import can.lucky.of.core.domain.models.enums.Currency

data class SubCost(
    val cost: Float,
    val days: Int,
    val currency: Currency,
    val originalCost: Float
)
