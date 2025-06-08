package can.lucky.of.profile.net.responds

internal data class EditUserRespond(
    val user : User,
    val token : Token
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
}