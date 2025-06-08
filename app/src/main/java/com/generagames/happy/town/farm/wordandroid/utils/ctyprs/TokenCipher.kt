package com.generagames.happy.town.farm.wordandroid.utils.ctyprs

interface TokenCipher{
    fun encrypt(text: String): String
    fun decrypt(text: String): String
}