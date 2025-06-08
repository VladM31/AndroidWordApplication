package can.lucky.of.auth.domain.actions

internal interface ConfirmSignUpAction {
    data class Init(val phoneNumber: String, val password: String) : ConfirmSignUpAction
}