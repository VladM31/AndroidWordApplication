package com.generagames.happy.town.farm.wordandroid.actions.pay

interface ChoosePayAction {

    data class GooglePayReady(val isReady: Boolean) : ChoosePayAction

    data class Error(val message: String) : ChoosePayAction

    data class FetchedGPayToken(val token: String) : ChoosePayAction
}