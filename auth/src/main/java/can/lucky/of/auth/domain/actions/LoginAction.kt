package can.lucky.of.auth.domain.actions

internal sealed interface LoginAction{
    data object Submit: LoginAction
    data class SetPhoneNumber(val value: String): LoginAction
    data class SetPassword(val value: String): LoginAction
    data object BiometricAuthFailed: LoginAction
    data object BiometricAuthSuccess: LoginAction
}