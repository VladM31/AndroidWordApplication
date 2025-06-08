package can.lucky.of.auth.net.clients

import can.lucky.of.auth.net.models.requests.AlternativeLogInRequest
import can.lucky.of.auth.net.models.responses.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AlternativeClient {

    @POST("/auth/alternative-login")
    suspend fun logIn(@Body req: AlternativeLogInRequest) : AuthResponse
}