package com.generagames.happy.town.farm.wordandroid.ui.utils

import android.text.Editable
import android.text.TextWatcher

class CardNumberFormatTextWatcher(
    private val cardNumberInput: android.widget.EditText
) : TextWatcher {
    private var previousText: String = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        if (previousText == s.toString()) {
            return
        }
        if (s.toString().length > 19) {
            cardNumberInput.setText(previousText)
            cardNumberInput.setSelection(previousText.length)
            return
        }

        val digits = s.toString().replace("[^0-9]".toRegex(), "")

        val formatted = formatCardNumber(digits)

        previousText = formatted
        cardNumberInput.setText(formatted)
        cardNumberInput.setSelection(formatted.length)
    }

    // Функция для форматирования номера карты
    private fun formatCardNumber(digits: String): String {
        val formatted = StringBuilder()
        for (i in digits.indices) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ")
            }
            formatted.append(digits[i])
        }
        return formatted.toString()
    }
}