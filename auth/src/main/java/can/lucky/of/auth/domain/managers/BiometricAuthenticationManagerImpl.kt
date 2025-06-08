package can.lucky.of.auth.domain.managers

import android.annotation.SuppressLint
import android.content.Context
import androidx.biometric.BiometricManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import can.lucky.of.auth.domain.utils.getDeviceName
import can.lucky.of.auth.net.clients.AlternativeClient
import can.lucky.of.auth.net.models.requests.AlternativeLogInRequest
import com.google.gson.Gson
import android.provider.Settings
import can.lucky.of.auth.domain.mappers.toUser
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.data.Token
import retrofit2.HttpException


internal class BiometricAuthenticationManagerImpl(
    private val context: Context,
    private val client: AlternativeClient,
    private val authHistoryManager: AuthHistoryManager,
    private val userCacheManager: UserCacheManager
) : BiometricAuthenticationManager {

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val gson = Gson()

    override val isBiometricAuthAvailable: Boolean
        get() = isBiometricAvailable()

    override fun onFailedBiometricAuth() {
        sharedPreferences.getString(TEMP_TOKEN, null)?.let {
            val tokenHolder = gson.fromJson(it, TokenHolder::class.java)
            if (tokenHolder.tryCount < MAX_TRY_COUNT) {
                sharedPreferences.edit()
                    .putString(
                        TEMP_TOKEN,
                        gson.toJson(tokenHolder.copy(tryCount = tokenHolder.tryCount + 1))
                    ).apply()
            } else {
                sharedPreferences.edit().remove(TEMP_TOKEN).apply()
            }
        }
    }

    override suspend fun onSuccessfulBiometricAuth(): Boolean {
        if (sharedPreferences.contains(TEMP_TOKEN).not()) return false

        try {
            val response = client.logIn(getRequest())
            response.user?.toUser()?.let {
                userCacheManager.saveUser(it)
            }

            response.token?.let {
                userCacheManager.saveToken(Token(it.value, it.expirationTime))
            }

            return true
        }catch (e: HttpException){
            sharedPreferences.edit().remove(TEMP_TOKEN).apply()
            throw e
        }
    }



    private fun getRequest(): AlternativeLogInRequest {
        return AlternativeLogInRequest(
            tempToken = token.orEmpty(),
            phoneNumber = authHistoryManager.lastPhoneNumber.orEmpty(),
            deviceName = getDeviceName(),
            installId = getAndroidID()
        )
    }


    override fun setTempToken(token: String) {
        sharedPreferences.edit()
            .putString(
                TEMP_TOKEN,
                gson.toJson(TokenHolder(token = token, tryCount = 0))
            ).apply()
    }

    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        val isAvailable =
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
        val hasPhoneNumber = authHistoryManager.lastPhoneNumber.isNullOrBlank().not()
        return isAvailable and sharedPreferences.contains(TEMP_TOKEN) and hasPhoneNumber
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidID(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private val token: String?
        get() = sharedPreferences.getString(TEMP_TOKEN, null)?.let {
            gson.fromJson(it, TokenHolder::class.java).token
        }

    private data class TokenHolder(val token: String, val tryCount: Int)

    companion object {
        private const val FILE_NAME = "biometric_auth"
        private const val TEMP_TOKEN = "temp_token"
        private const val MAX_TRY_COUNT = 6
    }
}