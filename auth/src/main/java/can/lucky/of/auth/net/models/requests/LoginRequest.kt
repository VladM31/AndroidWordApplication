package can.lucky.of.auth.net.models.requests

internal data class LoginRequest(
    val phoneNumber: String,
    val password: String,
    val temporaryToken: TemporaryTokenRequest? = null
){

    data class TemporaryTokenRequest(
        val deviceName: String,
        val installId: String,
    )
}
