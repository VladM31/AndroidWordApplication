package can.lucky.of.profile.domain.models.data

import can.lucky.of.core.domain.models.enums.Currency

internal data class EditUser(
    val firstName: String,
    val lastName: String,
    val currency: Currency,
)
