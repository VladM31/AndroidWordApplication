package can.lucky.of.auth.domain.managers


internal interface TelegramAuthManager {

    suspend fun startLogin(phoneNumber: String): String

    suspend fun login(phoneNumber: String, code: String): Boolean
}