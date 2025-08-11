package com.generagames.happy.town.farm.wordandroid.domain.vms.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.CardPayAction
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayPropositionManager
import com.generagames.happy.town.farm.wordandroid.domain.models.data.CardPay
import com.generagames.happy.town.farm.wordandroid.domain.models.states.pay.CardPayState
import com.generagames.happy.town.farm.wordandroid.utils.toLocalDateTime
import com.generagames.happy.town.farm.wordandroid.valid.CardPayStateValidator
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardPayViewModel(
    userCacheManager: UserCacheManager,
    private val payManager: PayManager,
    private val payPropositionManager: PayPropositionManager,
    private val subscribeCacheManager: SubscribeCacheManager
) : ViewModel(), MviViewModel<CardPayState, CardPayAction> {

    private val mutableState = MutableStateFlow(
        CardPayState(
        phoneNumber = userCacheManager.user.phoneNumber,
        email = userCacheManager.user.email.orEmpty()
    )
    )
    override val state: StateFlow<CardPayState> = mutableState


    init {
        mutableState.value = state.value.copy(
            cost = payPropositionManager.getProposition()?.originalCost ?: 0.0f,
        )
    }

    override fun sent(action: CardPayAction) {

        if (action !is CardPayAction.ConfirmPay) {
            return
        }

        mutableState.value = action.toState().let {
            CardPayStateValidator.valid(it)?.let { error ->
                it.copy(errorMessage = ErrorMessage(error))
            } ?: it
        }

        if (state.value.errorMessage != null) {
            return
        }

        viewModelScope.launch {
            val result = payManager.pay(state.value.toCardPay())

            if (result.error!= null) {
                mutableState.value = state.value.copy(
                    errorMessage = ErrorMessage(result.error)
                )
                return@launch
            }



            mutableState.value = state.value.copy(
                dateCacheId = result.dateCacheId.orEmpty()
            )

            repeat(30){
                delay(5000)
                val waitResult = payManager.waitExpirationDate(state.value.dateCacheId)
                if (waitResult.errorMessage != null) {
                    mutableState.value = state.value.copy(
                        errorMessage = ErrorMessage(waitResult.errorMessage)
                    )
                    return@launch
                }
                waitResult.expirationDate?.let {
                    if (it == "Canceled"){
                        mutableState.value = state.value.copy(
                            errorMessage = ErrorMessage("Canceled")
                        )
                        delay(1000)

                        mutableState.value = state.value.copy(isBack = true)

                    }else{
                        subscribeCacheManager.update(it.toLocalDateTime())
                        mutableState.value = state.value.copy(
                            isBack = true
                        )
                    }
                    this.cancel()
                }
                delay(20000)
            }
        }
    }

    private fun CardPayAction.ConfirmPay.toState(): CardPayState {
        return state.value.copy(
            cardNumber = cardNumber,
            cardName = cardName,
            expiryDate = expiryDate,
            cvv2 = cvv2,
            phoneNumber = phoneNumber,
            email = email,
            errorMessage = null
        )
    }

    private fun CardPayState.toCardPay(): CardPay {
        return CardPay(
            cardNumber = cardNumber,
            cardName = cardName,
            expiryDate = expiryDate,
            cvv2 = cvv2,
            phoneNumber = phoneNumber,
            email = email,
            cost = state.value.cost
        )
    }
}