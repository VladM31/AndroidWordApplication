package com.generagames.happy.town.farm.wordandroid.domain.vms.pay

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.pay.ChoosePayAction
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayPropositionManager
import com.generagames.happy.town.farm.wordandroid.domain.models.data.GooglePay
import com.generagames.happy.town.farm.wordandroid.domain.models.states.pay.ChoosePayState
import com.generagames.happy.town.farm.wordandroid.utils.toLocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChoosePayViewModel(
    private val payManager: PayManager,
    private val payPropositionManager: PayPropositionManager,
    private val subscribeCacheManager: SubscribeCacheManager
) : AbstractMviViewModel<ChoosePayState, ChoosePayAction>(){

    private val mutableState = MutableStateFlow(ChoosePayState())
    override val state: StateFlow<ChoosePayState> = mutableState;

    init {
        payPropositionManager.getProposition()?.let {subCost ->
            mutableState.update {
                it.copy(costProposition = subCost)
            }
        }

    }

    override fun sent(action: ChoosePayAction) {
        when(action) {
            is ChoosePayAction.GooglePayReady -> handleGooglePayReady(action)
            is ChoosePayAction.Error -> handleError(action)
            is ChoosePayAction.FetchedGPayToken -> handleFetchedGPayToken(action)
        }
    }

    private fun handleGooglePayReady(action: ChoosePayAction.GooglePayReady){
        val isValidCost = payPropositionManager.getProposition()?.cost?.let { cost ->
            cost > 0
        } ?: false

        mutableState.update {
            it.copy(
                googlePay = it.googlePay.copy(
                    isReady = action.isReady,
                    isInited = true,
                    isSubmitEnabled = action.isReady && isValidCost
                )
            )
        }
    }

    private fun handleError(action: ChoosePayAction.Error){
        mutableState.update {
            it.copy(
                errorMessage = ErrorMessage(message = action.message)
            )
        }
    }

    private fun handleFetchedGPayToken(action: ChoosePayAction.FetchedGPayToken) {
        val payData = GooglePay(
            token = action.token,
            cost = state.value.costProposition.cost,
            currency = state.value.costProposition.currency,
            usdCost = state.value.costProposition.originalCost
        )

        viewModelScope.launch {
            val result = payManager.pay(payData)
            if (result.errorMessage?.isNotBlank() == true){
                mutableState.update {
                    it.copy(
                        errorMessage = ErrorMessage(message = result.errorMessage)
                    )
                }
                return@launch
            }

            result.expirationDate?.let {
                subscribeCacheManager.update(it.toLocalDateTime())
            }

            mutableState.update { it.copy(isEnd = true) }

        }
    }
}