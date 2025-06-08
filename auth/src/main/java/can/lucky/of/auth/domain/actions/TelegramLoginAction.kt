package can.lucky.of.auth.domain.actions

internal sealed interface TelegramLoginAction {
    data class SetPhoneNumber(val value: String): TelegramLoginAction
    data object Submit: TelegramLoginAction
    data object CheckLogin: TelegramLoginAction
}