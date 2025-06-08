package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.ErrorableState

data class CardPayState(
    val cardNumber: String = "",
    val cardName: String = "",
    val expiryDate: String = "",
    val cvv2: String = "",

    val phoneNumber: String,
    val email: String,
    val cost: Float = 0f,

    val isBack: Boolean = false,

    val dateCacheId: String = "",
    override val errorMessage: ErrorMessage? = null

): ErrorableState
