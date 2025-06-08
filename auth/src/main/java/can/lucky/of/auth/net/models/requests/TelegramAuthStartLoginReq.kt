package can.lucky.of.auth.net.models.requests

internal data class TelegramAuthStartLoginReq(
    val phoneNumber: String,
    val deviceName: String?
)