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
import kotlinx.coroutines.coroutineScope
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

        when (action) {
            is CardPayAction.ConfirmPay -> handlePay(action)
            is CardPayAction.SetExpiryMonth -> {
                mutableState.value = state.value.copy(
                    expiryMonth = action.month
                )
            }

            is CardPayAction.SetExpiryYear -> {
                mutableState.value = state.value.copy(
                    expiryYear = action.year
                )
            }
        }
    }

    private fun handlePay(action: CardPayAction.ConfirmPay) {
        mutableState.value = action.toState().let {
            CardPayStateValidator.valid(it)?.let { error ->
                it.copy(errorMessage = ErrorMessage(error))
            } ?: it
        }

        if (state.value.errorMessage != null) {
            return
        }
        mutableState.value = state.value.copy(
            submitEnabled = false,
        )

        viewModelScope.launch {
            val result = payManager.pay(state.value.toCardPay())

            if (result.error != null) {
                mutableState.value = state.value.copy(
                    errorMessage = ErrorMessage(result.error),
                    submitEnabled = true
                )
                return@launch
            }

            mutableState.value = state.value.copy(
                dateCacheId = result.dateCacheId.orEmpty(),
                submitEnabled = false
            )

            repeat(30) {
                waitResult()
            }

            if (!state.value.isEnd) {
                mutableState.value = state.value.copy(
                    errorMessage = ErrorMessage("Time out"),
                    submitEnabled = true
                )
            }
        }
    }

    private suspend fun waitResult() {
        coroutineScope {
            delay(6000)
            val waitResult = payManager.waitExpirationDate(state.value.dateCacheId)
            if (waitResult.errorMessage != null) {
                mutableState.value = state.value.copy(
                    errorMessage = ErrorMessage(waitResult.errorMessage),
                    submitEnabled = true
                )
                return@coroutineScope
            }

            val expirationDate = waitResult.expirationDate.orEmpty()
            if (expirationDate.isBlank()) return@coroutineScope

            if ("Canceled" == expirationDate) {
                mutableState.value = state.value.copy(
                    errorMessage = ErrorMessage("Canceled")
                )
                delay(2000)

                mutableState.value = state.value.copy(isEnd = true)
                cancel()
                return@coroutineScope
            }
            subscribeCacheManager.update(expirationDate.toLocalDateTime())
            mutableState.value = state.value.copy(
                isEnd = true
            )
            delay(5000)
            cancel()
        }
    }

    private fun CardPayAction.ConfirmPay.toState(): CardPayState {
        return state.value.copy(
            cardNumber = cardNumber,
            cardName = cardName,
            cvv2 = cvv2,
            phoneNumber = phoneNumber,
            email = email,
            errorMessage = null
        )
    }

    private fun CardPayState.toExpiryDate() = "%02d.%02d".format(expiryMonth, expiryYear % 100)

    private fun CardPayState.toCardPay(): CardPay {
        return CardPay(
            cardNumber = cardNumber,
            cardName = cardName,
            expiryDate = toExpiryDate(),
            cvv2 = cvv2,
            phoneNumber = phoneNumber,
            email = email,
            cost = state.value.cost
        )
    }
}