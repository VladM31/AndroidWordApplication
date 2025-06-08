package com.generagames.happy.town.farm.wordandroid.actions

import can.lucky.of.core.domain.models.filters.UserWordFilter


sealed interface UserWordsAction{
    data class SelectWord(val wordId: String,val position: Int) : UserWordsAction
    data class UnSelectWord(val wordId: String) : UserWordsAction
    data class ChangeFilter(val filter: UserWordFilter) : UserWordsAction
    data object Clear: UserWordsAction
    data class PinWords(val playListId: String) : UserWordsAction
    data object ReFetch: UserWordsAction
}