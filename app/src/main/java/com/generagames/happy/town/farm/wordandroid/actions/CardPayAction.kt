package com.generagames.happy.town.farm.wordandroid.actions

sealed interface CardPayAction {
    data class ConfirmPay(
        val cardNumber: String,
        val cardName: String,
        val expiryDate: String,
        val cvv2: String,
        val phoneNumber: String,
        val email: String) : CardPayAction

}