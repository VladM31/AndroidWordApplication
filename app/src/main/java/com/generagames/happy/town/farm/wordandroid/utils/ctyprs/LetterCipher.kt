package com.generagames.happy.town.farm.wordandroid.utils.ctyprs

class LetterCipher(
    private val key: String
) : TokenCipher {

    override fun encrypt(text: String): String {
        val cipher = StringBuilder()

        text.toCharArray().toList().forEachIndexed { index, c ->
            cipher.append((c.code + key[index % key.length].code).toChar())
        }

        return cipher.reverse().toString()
    }

    override fun decrypt(text: String): String {
        val cipher = StringBuilder()

        text.reversed().toCharArray().toList().forEachIndexed { index, c ->
            cipher.append((c.code - key[index % key.length].code).toChar())
        }

        return cipher.toString()
    }
}