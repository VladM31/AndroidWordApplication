package can.lucky.of.auth.domain.managers

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import can.lucky.of.auth.domain.mappers.toRequest
import can.lucky.of.auth.domain.mappers.toUser
import can.lucky.of.auth.domain.models.data.AuthResult
import can.lucky.of.auth.domain.models.data.SignUpModel
import can.lucky.of.auth.domain.utils.getDeviceName
import can.lucky.of.auth.net.clients.AuthClient
import can.lucky.of.auth.net.models.requests.LoginRequest
import can.lucky.of.auth.net.models.requests.SignUpRequest
import can.lucky.of.auth.net.models.responses.AuthResponse
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.data.Token
import can.lucky.of.core.domain.models.data.User

import java.util.Date

internal class AuthManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val authClient: AuthClient,
    private val context: Context,
    private val biometricAuthenticationManager: BiometricAuthenticationManager
) : AuthManager {
    override suspend fun logIn(phoneNumber: String, password: String): AuthResult {
        return try {
            val response = authClient.logIn(
                LoginRequest(
                    phoneNumber,
                    password,
                    temporaryToken = LoginRequest.TemporaryTokenRequest(
                        deviceName = getDeviceName(),
                        installId = getAndroidID()
                    )
                )
            )
            if (response.hasError()) {
               return AuthResult(false, response.error?.message)
            }
            response.user?.let {
                userCacheManager.saveUser(it.toUser())
            }
            response.token?.let {
                userCacheManager.saveToken(
                    Token(
                        it.value,
                        it.expirationTime
                    )
                )
            }
            response.tempToken?.let {
                biometricAuthenticationManager.setTempToken(it)
            }

            AuthResult(true)
        }catch (e: Exception) {
            AuthResult(false, e.message)
        }
    }

    override suspend fun signUp(req: SignUpModel): AuthResult {
        return authClient.runCatching {
            signUp(req.toRequest()).let {
                AuthResult(it.success, it.message)
            }
        }.getOrDefault(AuthResult(false))
    }

    override suspend fun isRegistered(phoneNumber: String): Boolean {
        return authClient.runCatching {
            isRegistered(phoneNumber)
        }.getOrDefault(false)
    }

    override suspend fun parseToken(): Boolean {
        return authClient.runCatching {
            parseToken(userCacheManager.token.value).let {
                userCacheManager.saveUser(it.toUser())
            }
            true
        }.getOrDefault(false)
    }


    @SuppressLint("HardwareIds")
    private fun getAndroidID(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}