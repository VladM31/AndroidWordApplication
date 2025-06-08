package can.lucky.of.auth.domain.mappers

import can.lucky.of.auth.domain.models.data.SignUpModel
import can.lucky.of.auth.net.models.requests.SignUpRequest
import can.lucky.of.auth.net.models.responses.AuthResponse
import can.lucky.of.auth.net.models.responses.TelegramLoginRespond
import can.lucky.of.core.domain.models.data.User
import java.util.Date

internal fun AuthResponse.User.toUser(): User {
    return User(
        id = id, firstName = firstName, lastName = lastName, phoneNumber = phoneNumber,
        email = email, currency = currency, role = role, dateOfLonIn = Date().time
    )
}

internal fun SignUpModel.toRequest(): SignUpRequest {
    return SignUpRequest(
        firstName = firstName, lastName = lastName, phoneNumber = phoneNumber,
        email = email, currency = currency, password = password
    )
}

internal fun TelegramLoginRespond.toUser(): User {
    if (user == null) {
        throw IllegalArgumentException("User is null")
    }

    return User(
        id = user.id, firstName = user.firstName, lastName = user.lastName, phoneNumber = user.phoneNumber,
        email = user.email, currency = user.currency, role = user.role, dateOfLonIn = Date().time
    )
}