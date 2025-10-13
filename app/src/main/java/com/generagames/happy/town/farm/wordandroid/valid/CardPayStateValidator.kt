package com.generagames.happy.town.farm.wordandroid.valid

import com.generagames.happy.town.farm.wordandroid.domain.models.states.pay.CardPayState
import java.time.Year

object CardPayStateValidator {
    private val cardNumberValidators = listOf(
        BlankValidator,
        EmptyStringValidator,
        LengthRangeStringValidator(length = 16),
        CreditCardNumberValidator
    )

    private val cardNameValidators = listOf(
        BlankValidator,
        EmptyStringValidator,
        LengthRangeStringValidator(1..60)
    )



    private val cvv2Validator = RegexStringValidator("[0-9]{3}".toRegex())


    fun valid(state: CardPayState): String? {
        cardNumberValidators.firstNotNullOfOrNull {
            it.valid(
                state.cardNumber.replace(
                    "\\s+".toRegex(),
                    ""
                )
            )
        }?.let {
            return@valid "Card number: $it"
        }

        cardNameValidators.firstNotNullOfOrNull { it.valid(state.cardName) }?.let {
            return@valid "Card name: $it"
        }

        if (state.expiryYear == Year.now().value && state.expiryMonth <= java.time.MonthDay.now().monthValue) {
            return "Expiry date: Expiry date must be in the future"
        }


        cvv2Validator.valid(state.cvv2)?.let {
            return@valid "CVV2: $it"
        }

        PhoneNumberValidator.valid(state.phoneNumber)?.let {
            return@valid "Phone number: $it"
        }

        EmailValidator.valid(state.email)?.let {
            return@valid "Email: $it"
        }

        return null
    }
}