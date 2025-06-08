package com.generagames.happy.town.farm.wordandroid.domain.models.data

data class CardPay(
    val phoneNumber : String,
    val email : String,

    val cardNumber : String,
    val expiryDate: String,
    val cardName: String,
    val cvv2: String,
    val cost: Float) {
}