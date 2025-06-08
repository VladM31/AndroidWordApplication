package com.generagames.happy.town.farm.wordandroid.domain.models.states

import androidx.paging.Pager
import can.lucky.of.core.domain.models.data.playlists.PlayListCount

data class PlayListChooserState(
    val selectedPlayListId: String? = null,
    val previousPosition: Int? = null,
    val pagerPlayList: Pager<Long, PlayListCount>
)
