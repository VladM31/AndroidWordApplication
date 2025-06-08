package com.generagames.happy.town.farm.wordandroid.valid

object EmptyStringValidator : StringValidator {
    override fun valid(string: String): String? {
        return if (string.isEmpty()) {
            "Field must not be empty"
        } else {
            null
        }
    }
}