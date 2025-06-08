package can.lucky.of.auth.net.models.responses

internal data class AuthResponse(
    val user : User? = null,
    val token : Token? = null,
    val error: Error? = null,
    val tempToken: String? = null
){

    data class Token(
        val value : String,
        val expirationTime : Long
    )

    data class User(
        val id: String,
        val phoneNumber: String,
        val firstName: String,
        val lastName: String,
        val currency: String,
        val email: String?,
        val role: String
    )

    data class Error(
        val message: String
    )

    fun hasError() = error != null
}