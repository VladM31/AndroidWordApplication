package can.lucky.of.core.domain.models.data

data class User(
    val id: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val currency: String,
    val email: String?,
    val role: String,
    val dateOfLonIn: Long
)
