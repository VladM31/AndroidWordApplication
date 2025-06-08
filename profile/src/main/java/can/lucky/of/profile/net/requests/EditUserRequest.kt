package can.lucky.of.profile.net.requests

import can.lucky.of.core.domain.models.enums.Currency

internal data class EditUserRequest(
    val firstName: String,
    val lastName: String,
    val currency: Currency,
)