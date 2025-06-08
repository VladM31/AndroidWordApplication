package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.domain.models.states.EndetableState

data class PlayListFilterState(
    val startCount: String = "",
    val endCount: String = "",
    val name: String = "",
    val isInited: Boolean = false,
    override val isEnd: Boolean = false
) : EndetableState
