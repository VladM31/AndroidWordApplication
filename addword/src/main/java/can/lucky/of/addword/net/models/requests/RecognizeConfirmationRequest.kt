package can.lucky.of.addword.net.models.requests

internal data class RecognizeConfirmationRequest(
    val recognizeId: String,
    val completed: Boolean,
)
