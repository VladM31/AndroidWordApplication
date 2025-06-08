package com.generagames.happy.town.farm.wordandroid.domain.models.states

import androidx.paging.Pager
import can.lucky.of.core.domain.models.data.playlists.PlayListCount
import can.lucky.of.core.domain.models.filters.PlayListCountFilter

data class PlayListState(
    val filter: PlayListCountFilter = PlayListCountFilter(),
    val pager: Pager<Long, PlayListCount>,
)
