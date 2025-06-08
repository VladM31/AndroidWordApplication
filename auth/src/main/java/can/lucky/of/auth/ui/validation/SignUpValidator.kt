package can.lucky.of.auth.ui.validation

import can.lucky.of.auth.domain.models.states.SignUpState
import can.lucky.of.validation.Validator
import can.lucky.of.validation.schemes.ValidationScheme
import can.lucky.of.validation.schemes.email
import can.lucky.of.validation.schemes.isPhoneNumber
import can.lucky.of.validation.schemes.length
import can.lucky.of.validation.schemes.notBlank
import kotlinx.coroutines.flow.StateFlow

internal val signUpValidator: (StateFlow<SignUpState>) -> Validator<SignUpState> = { state ->
    Validator(state).apply {
        add(
            SignUpState::phoneNumber,
            ValidationScheme.stringSchema("Phone number")
                .isPhoneNumber()
        )
        add(
            SignUpState::password,
            ValidationScheme.stringSchema("Password")
                .length(8, 60)
                .notBlank()
        )

        add(
            SignUpState::firstName,
            ValidationScheme.stringSchema("First name")
                .length(2, 60)
                .notBlank()
        )

        add(
            SignUpState::lastName,
            ValidationScheme.stringSchema("Last name")
                .length(2, 60)
                .notBlank()
        )

        add(
            { it.email.orEmpty() },
            ValidationScheme.stringSchema("Email")
                .email(isRequired = false)
                .notBlank(canBeEmpty = true)
        )


    }
}