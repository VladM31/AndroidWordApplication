package com.generagames.happy.town.farm.wordandroid.valid

data object PhoneNumberValidator : StringValidator {
    override fun valid(phoneNumber: String) : String?{
        return if (phoneNumber.matches(Regex("\\d{10,15}")).not()) {
            "Phone number length must be between 10 and 15 and be only digits"
        }else{
            null
        }
    }
}