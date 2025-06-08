package can.lucky.of.auth.domain.managers

import can.lucky.of.auth.domain.models.data.AuthResult
import can.lucky.of.auth.domain.models.data.SignUpModel


internal interface AuthManager {
    suspend fun logIn(phoneNumber: String, password: String) : AuthResult

    suspend fun signUp(req: SignUpModel) : AuthResult

    suspend fun isRegistered(phoneNumber:  String): Boolean

    suspend fun parseToken(): Boolean
}