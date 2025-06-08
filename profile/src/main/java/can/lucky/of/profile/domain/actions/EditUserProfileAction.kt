package can.lucky.of.profile.domain.actions

import can.lucky.of.core.domain.models.enums.Currency

interface EditUserProfileAction {
    data class FirstNameChanged(val firstName: String): EditUserProfileAction
    data class LastNameChanged(val lastName: String): EditUserProfileAction
    data class CurrencyChanged(val currency: Currency): EditUserProfileAction
    object Submit: EditUserProfileAction
}