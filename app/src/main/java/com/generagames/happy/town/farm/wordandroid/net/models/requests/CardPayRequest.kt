package com.generagames.happy.town.farm.wordandroid.net.models.requests

data class CardPayRequest(
    val phoneNumber : String,
    val email : String,

    val cardNumber : String,
    val expiryDate: String,
    val cardName: String,
    val cvv2: String,
    val cost: Float,
    val platform: String = PLATFORM_ANDROID,

    ){

    companion object{
        const val PLATFORM_ANDROID = "ANDROID"
    }
}
