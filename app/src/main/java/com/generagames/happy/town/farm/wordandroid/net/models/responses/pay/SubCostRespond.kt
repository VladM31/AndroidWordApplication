package com.generagames.happy.town.farm.wordandroid.net.models.responses.pay

data class SubCostRespond(
    val cost: Float,
    val days: Int,
    val currency: String,
    val originalCost: Float
)