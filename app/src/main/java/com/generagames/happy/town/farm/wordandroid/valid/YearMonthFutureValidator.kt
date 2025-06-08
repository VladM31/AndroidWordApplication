package com.generagames.happy.town.farm.wordandroid.valid

import java.time.YearMonth

import java.time.format.DateTimeFormatter




data object YearMonthFutureValidator : StringValidator {
    private const val ERROR_MESSAGE = "Date is not valid"
    private val formatter = DateTimeFormatter.ofPattern("MM.yy")


    override fun valid(string: String): String? {
        if (string.length != 5) {
            return ERROR_MESSAGE
        }
        return string.runCatching {
            val yearMonth = YearMonth.parse(string, formatter)
            return if (YearMonth.now().isBefore(yearMonth)) {
                null
            } else {
                ERROR_MESSAGE
            }
        }.getOrDefault(ERROR_MESSAGE)
    }
}