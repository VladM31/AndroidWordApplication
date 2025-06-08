package com.generagames.happy.town.farm.wordandroid.utils.ctyprs

class CaesarCipher(
    private val key: IntArray
) : TokenCipher {

    override fun encrypt(text: String): String{
        return crypt(text, true)
    }

    override fun decrypt(text: String): String{
        return crypt(text, false)
    }

    private fun crypt(text: String, encrypt: Boolean): String{
        val valueCrypt = if (encrypt) 1 else -1
        val textArray = text.toCharArray()
        val result = StringBuilder()
        for (i in textArray.indices){
            val char = textArray[i]
            val key = key[i % key.size]
            val encryptedChar = char + (key * valueCrypt)
            result.append(encryptedChar)
        }
        return result.toString()
    }
}