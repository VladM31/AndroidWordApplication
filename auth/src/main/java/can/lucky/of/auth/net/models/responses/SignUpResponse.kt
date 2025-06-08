package can.lucky.of.auth.net.models.responses

internal data class SignUpResponse(
    val success: Boolean,
    val message: String? = null
){

    companion object{
        fun success(message: String? = null) =
            SignUpResponse(true, message)
        fun error(message: String? = null) =
            SignUpResponse(false, message)
    }
}