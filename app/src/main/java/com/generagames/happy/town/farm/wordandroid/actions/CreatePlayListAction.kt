package com.generagames.happy.town.farm.wordandroid.actions

sealed interface CreatePlayListAction{
    data object ClearError : CreatePlayListAction
    data class Create(val name: String) : CreatePlayListAction
}