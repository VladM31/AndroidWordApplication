package com.generagames.happy.town.farm.wordandroid.utils.ctyprs

class TokenCipherImpl : TokenCipher {
    private val ciphers = listOf(
        CaesarCipher(intArrayOf(1,15,-27,31,-17,7,11)),
        LetterCipher("CJpZFwiOlwiNjZkZmUwNWMtODU2ZC00MzIyLThhMTYtM2FhNDRjNjFkZTMxXCIsXCJwaG9uZU51bWJ")
    )

    override fun encrypt(text: String): String {
        var encrypt = text

        ciphers.forEach { encrypt = it.encrypt(encrypt) }

        return encrypt
    }

    override fun decrypt(text: String): String {
        var decrypt = text

        ciphers.reversed().forEach { decrypt = it.decrypt(decrypt) }

        return decrypt
    }
}