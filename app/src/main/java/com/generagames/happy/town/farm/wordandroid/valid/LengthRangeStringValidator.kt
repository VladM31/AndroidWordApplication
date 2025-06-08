package com.generagames.happy.town.farm.wordandroid.valid

data class LengthRangeStringValidator(
    val range: IntRange
) : StringValidator {

    constructor(length: Int) : this(length..length)

    override fun valid(string: String): String? {
        return if (string.length in range) {
            null
        } else {
            "Field must be between ${range.first} and ${range.last} characters"
        }
    }
}
