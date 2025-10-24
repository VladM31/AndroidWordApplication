package can.lucky.of.profile.net.clients

import can.lucky.of.profile.net.requests.EditUserRequest
import can.lucky.of.profile.net.responds.EditUserRespond
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

internal interface EditUserClient {

    @PUT("user/")
    suspend fun editUser(@Header("Authorization") token: String, @Body request: EditUserRequest): EditUserRespond
}