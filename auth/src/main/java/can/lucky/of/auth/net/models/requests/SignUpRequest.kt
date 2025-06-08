package can.lucky.of.auth.net.models.requests

internal data class SignUpRequest(
    val phoneNumber: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val currency: String,
    val email: String?
)
