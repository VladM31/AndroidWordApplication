package com.generagames.happy.town.farm.wordandroid.domain.vms.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.SubscribeAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.pay.SubscribeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


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
            val expirationDate =
                LocalDateTime.parse(subscribeCacheManager.cache().value?.expirationDate.toString())
                    .plusHours(getCurrentTimeZoneOffset())

            val newState = SubscribeState(
                condition = if (subscribeCacheManager.isActiveSubscribe()) {
                    SubscribeState.SubscribeCondition.ACTIVE
                } else {
                    SubscribeState.SubscribeCondition.INACTIVE
                },
                expirationDate = expirationDate.format(DATE_TIME_PATTERN)
            )
            mutableState.value = newState
        }
    }

    override fun sent(action: SubscribeAction) {
        when (action) {
            is SubscribeAction.UpdateState -> fetchSub()
        }
    }

    companion object {
        private val DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        fun getCurrentTimeZoneOffset(): Long {
            val zoneId = ZoneId.systemDefault()
            val zoneOffset = zoneId.rules.getOffset(Instant.now())
            return (zoneOffset.totalSeconds / 3600).toLong()
        }
    }
}