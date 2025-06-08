package com.generagames.happy.town.farm.wordandroid.actions

sealed interface PlayListDetailsAction{
    data class Fetch(val id: String): PlayListDetailsAction
    data class SelectWord(val id: String, val position: Int): PlayListDetailsAction
    data class HandleEdit(val name: String): PlayListDetailsAction
    data object UnSelect: PlayListDetailsAction
    data object UnPin: PlayListDetailsAction
    data object StartTransaction: PlayListDetailsAction
    data object Delete: PlayListDetailsAction
    data object ReFetch: PlayListDetailsAction
}