package com.generagames.happy.town.farm.wordandroid.domain.models.states.pay

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState
import java.time.MonthDay
import java.time.Year

data class CardPayState(
    val cardNumber: String = "",
    val cardName: String = "",

    val cvv2: String = "",
    val expiryMonth: Int = MonthDay.now().monthValue,
    val expiryYear: Int = Year.now().value,

    val phoneNumber: String,
    val email: String,
    val cost: Float = 0f,

    val dateCacheId: String = "",
    val submitEnabled: Boolean = true,

    override val errorMessage: ErrorMessage? = null,
    override val isEnd: Boolean = false
) : ErrorableState, EndetableState
