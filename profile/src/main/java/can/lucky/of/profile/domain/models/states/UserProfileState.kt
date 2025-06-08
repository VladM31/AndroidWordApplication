package can.lucky.of.profile.domain.models.states


internal data class UserProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String? = null,
    val phone: String = "",
    val currency: String = ""
)
