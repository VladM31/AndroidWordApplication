package can.lucky.of.auth.domain.actions

import can.lucky.of.core.domain.models.enums.Currency

internal sealed interface SighUpAction {
    data object Submit: SighUpAction
    data class SetPhoneNumber(val value: String): SighUpAction
    data class SetPassword(val value: String): SighUpAction
    data class SetFirstName(val value: String): SighUpAction
    data class SetLastName(val value: String): SighUpAction
    data class SetCurrency(val value: Currency): SighUpAction
    data class SetEmail(val value: String): SighUpAction
}