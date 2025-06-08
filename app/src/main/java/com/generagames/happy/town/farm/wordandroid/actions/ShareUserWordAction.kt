package com.generagames.happy.town.farm.wordandroid.actions

interface ShareUserWordAction {

    data class Init(val userWordId: String, val width: Int, val height: Int): ShareUserWordAction
}