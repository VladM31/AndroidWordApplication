package com.generagames.happy.town.farm.wordandroid.valid

object CreditCardNumberValidator : StringValidator {
    private const val ERROR_MESSAGE = "Invalid card number"
    override fun valid(string: String): String? {
        val cardNumber = string.replace("[^\\d]", "")

        if (cardNumber.length < 13 || cardNumber.length > 19) {
            return ERROR_MESSAGE
        }

        var sum = 0
        var alternate = false
        for (i in cardNumber.length - 1 downTo 0) {
            var n = cardNumber.substring(i, i + 1).toInt()
            if (alternate) {
                n *= 2
                if (n > 9) {
                    n = n % 10 + 1
                }
            }
            sum += n
            alternate = !alternate
        }
        return if(sum % 10 == 0) null else ERROR_MESSAGE
    }
}