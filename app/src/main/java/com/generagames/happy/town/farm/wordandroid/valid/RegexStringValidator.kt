package com.generagames.happy.town.farm.wordandroid.valid

data class RegexStringValidator(
    val regex: Regex,
) : StringValidator {
    override fun valid(string: String): String? {
        return if (regex.matches(string)) {
            null
        } else {
            "Field must match regex ${regex.pattern}"
        }
    }
}