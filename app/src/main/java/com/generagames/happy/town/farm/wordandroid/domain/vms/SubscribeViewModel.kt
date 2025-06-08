package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.SubscribeAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.SubscribeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubscribeViewModel(
    private val subscribeCacheManager: SubscribeCacheManager
) : ViewModel(), MviViewModel<SubscribeState, SubscribeAction> {

    private val mutableState = MutableStateFlow(SubscribeState())
    override val state: StateFlow<SubscribeState> = mutableState

    init {
        fetchSub()
    }

    private fun fetchSub() {
        viewModelScope.launch {
            val newState = SubscribeState(
                condition = if (subscribeCacheManager.isActiveSubscribe()) {
                    SubscribeState.SubscribeCondition.ACTIVE
                } else {
                    SubscribeState.SubscribeCondition.INACTIVE
                },
                expirationDate = subscribeCacheManager.cache().value?.expirationDate.toString()
                    .replace("T", " ")
            )
            mutableState.value = newState
        }
    }

    override fun sent(action: SubscribeAction) {
        when (action) {
            is SubscribeAction.UpdateState -> fetchSub()
        }
    }
}