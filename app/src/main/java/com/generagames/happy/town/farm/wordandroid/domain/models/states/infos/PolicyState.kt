package com.generagames.happy.town.farm.wordandroid.domain.models.states.infos

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState
import java.io.File

data class PolicyState(
    val file: File? = null,
    override val isEnd: Boolean = false,
    override val errorMessage: ErrorMessage? = null
) : ErrorableState, EndetableState
