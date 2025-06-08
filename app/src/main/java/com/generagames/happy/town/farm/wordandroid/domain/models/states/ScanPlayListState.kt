package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SharePlayList

data class ScanPlayListState(
    val shareId: String = "",
    val playList: SharePlayList? = null,
    override val errorMessage: ErrorMessage? = null,
    override val isEnd: Boolean = false
) : EndetableState, ErrorableState
