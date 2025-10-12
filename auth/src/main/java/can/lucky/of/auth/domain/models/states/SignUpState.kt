package can.lucky.of.auth.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.Currency

internal data class SignUpState(
    val success: Boolean = false,
    val error: ErrorMessage? = null,
    val phoneNumber: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val currency: Currency = Currency.USD,
    val email: String? = null,
    val agreed: Boolean = false
)
