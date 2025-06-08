package can.lucky.of.auth.net.models.responses

internal data class TelegramLoginRespond(
    val success: Boolean,
    val user : AuthResponse.User? = null,
    val token : AuthResponse.Token? = null
) {
}