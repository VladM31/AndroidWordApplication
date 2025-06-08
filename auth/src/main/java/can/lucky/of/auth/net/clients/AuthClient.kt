package can.lucky.of.auth.net.clients

import can.lucky.of.auth.net.models.requests.LoginRequest
import can.lucky.of.auth.net.models.requests.SignUpRequest
import can.lucky.of.auth.net.models.responses.AuthResponse
import can.lucky.of.auth.net.models.responses.SignUpResponse


internal interface AuthClient {
    suspend fun logIn(req: LoginRequest) : AuthResponse

    suspend fun signUp(req: SignUpRequest) : SignUpResponse

    suspend fun isRegistered(phoneNumber:  String): Boolean

    suspend fun parseToken(token: String): AuthResponse.User
}