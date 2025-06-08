package com.generagames.happy.town.farm.wordandroid.actions

sealed interface EditPlayListAction {
    data class Init(val playListId: String, val name: String) : EditPlayListAction
    data class NameChanged(val name: String) : EditPlayListAction
    data object Submit : EditPlayListAction
}