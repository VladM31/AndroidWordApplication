package can.lucky.of.auth.net.models.requests

data class TelegramAuthLoginReq(
    val phoneNumber: String,
    val code: String
)

