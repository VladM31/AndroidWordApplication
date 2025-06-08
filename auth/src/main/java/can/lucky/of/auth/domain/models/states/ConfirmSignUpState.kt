package can.lucky.of.auth.domain.models.states

data class ConfirmSignUpState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val phoneNumber: String = "",
    val password: String = "",
){

    fun isInited() = phoneNumber.isNotEmpty()
}
