package com.generagames.happy.town.farm.wordandroid.actions

sealed interface PlayListChooserDialogAction {
    data class SelectPlayList(val playListId: String?,val position: Int) : PlayListChooserDialogAction
}