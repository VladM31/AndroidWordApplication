package com.generagames.happy.town.farm.wordandroid.actions

interface SharePlayListAction {
    data class Init(val playListId: String, val width: Int, val height: Int): SharePlayListAction
}