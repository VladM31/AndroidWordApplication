package can.lucky.of.auth.net.clients

import can.lucky.of.auth.net.models.requests.TelegramAuthLoginReq
import can.lucky.of.auth.net.models.requests.TelegramAuthStartLoginReq
import can.lucky.of.auth.net.models.responses.TelegramLoginRespond
import retrofit2.http.Body
import retrofit2.http.POST

internal interface TelegramAuthClient {
    @POST("/auth/telegram-start-login")
    suspend fun startLogin(@Body request: TelegramAuthStartLoginReq): String

    @POST("/auth/telegram-login")
    suspend fun login(@Body request: TelegramAuthLoginReq): TelegramLoginRespond

}