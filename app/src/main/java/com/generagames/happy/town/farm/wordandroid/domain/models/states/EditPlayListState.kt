package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState

data class EditPlayListState(
    val name: String = "",
    val playListId: String = "",
    val isSubmitEnabled: Boolean = false,
    val isInited: Boolean = false,
    override val isEnd: Boolean = false,
    override val errorMessage: ErrorMessage? = null
) : EndetableState, ErrorableState
