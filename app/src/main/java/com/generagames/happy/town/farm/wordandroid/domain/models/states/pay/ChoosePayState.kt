package com.generagames.happy.town.farm.wordandroid.domain.models.states.pay

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.Currency
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SubCost

data class ChoosePayState(
    val googlePay: GooglePayState = GooglePayState(),
    val costProposition: SubCost = SubCost(
        cost = 0f,
        days = 0,
        currency = Currency.USD,
        originalCost = 0f
    ),
    override val isEnd: Boolean = false,
    override val errorMessage: ErrorMessage? = null,
) : EndetableState, ErrorableState {

    data class GooglePayState(
        val isReady: Boolean = false,
        val isSubmitEnabled: Boolean = false,
        val isInited: Boolean = false,
    )
}
