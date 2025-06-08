package can.lucky.of.addword.net.models.responses

internal data class ShareWordResponse(
    val qrCode: String,
    val liveTimeInSeconds: Int
)