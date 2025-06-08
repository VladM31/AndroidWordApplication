package can.lucky.of.auth.domain.managers

internal interface AuthHistoryManager {
    val lastPhoneNumber: String?

    fun updateLastPhoneNumber(phoneNumber: String)

}