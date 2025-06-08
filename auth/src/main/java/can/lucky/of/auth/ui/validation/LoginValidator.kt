package can.lucky.of.auth.ui.validation

import can.lucky.of.auth.domain.models.states.LoginState
import kotlinx.coroutines.flow.StateFlow
import can.lucky.of.validation.Validator
import can.lucky.of.validation.schemes.ValidationScheme
import can.lucky.of.validation.schemes.isPhoneNumber
import can.lucky.of.validation.schemes.length
import can.lucky.of.validation.schemes.notBlank

internal val loginValidator: (StateFlow<LoginState>) -> Validator<LoginState> = { state ->
    Validator(state).apply {
        add(
            LoginState::phoneNumber,
            ValidationScheme
                .stringSchema("Phone number")
                .isPhoneNumber()
        )
        add(
            LoginState::password,
            ValidationScheme
                .stringSchema("Password")
                .length(8, 60)
                .notBlank()
        )
    }
}