package com.generagames.happy.town.farm.wordandroid.domain.models.states


import androidx.paging.Pager
import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.core.domain.models.filters.UserWordFilter

data class UserWordsState(
    val selectedWords: Map<String,Int> = emptyMap(),
    val filter: UserWordFilter,
    val pager: Pager<Long, UserWord>,
    val count: Long = 0,
    val selectedPlayListId: String? = null,
    val openPlayList: OpenPlayList? = null,
){

    data class OpenPlayList(
        val id: String
    )
}
