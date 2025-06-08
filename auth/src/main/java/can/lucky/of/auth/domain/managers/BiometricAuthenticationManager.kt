package can.lucky.of.auth.domain.managers

interface BiometricAuthenticationManager {

    val isBiometricAuthAvailable: Boolean

    fun onFailedBiometricAuth()

    suspend fun onSuccessfulBiometricAuth() : Boolean


    fun setTempToken(token: String)
}