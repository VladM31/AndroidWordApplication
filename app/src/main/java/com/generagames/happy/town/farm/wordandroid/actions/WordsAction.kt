package com.generagames.happy.town.farm.wordandroid.actions

import can.lucky.of.core.domain.models.filters.WordFilter

sealed interface WordsAction{
    data class AddWord(val wordId: String, val position: Int) : WordsAction
    data class RemoveWord(val wordId: String) : WordsAction
    data object Clear : WordsAction
    data class UpdateFilter(
        val filter: WordFilter,
        val selectedWords: Map<String,Int>
    ) : WordsAction
}