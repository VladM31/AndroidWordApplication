package com.generagames.happy.town.farm.wordandroid.valid

data object BlankValidator : StringValidator {
    override fun valid(string: String): String? {
        return if (string.isBlank()) {
            "Field must not be blank"
        } else {
            null
        }
    }
}