package can.lucky.of.auth.net.models.requests

data class AlternativeLogInRequest(
    val phoneNumber: String,
    val deviceName: String,
    val installId: String,
    val tempToken: String
)