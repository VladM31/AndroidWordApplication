package can.lucky.of.addword.net.clients

import can.lucky.of.addword.net.models.requests.AiRecognizeWordByTextRequest
import can.lucky.of.addword.net.models.requests.RecognizeConfirmationRequest
import can.lucky.of.addword.net.models.responses.AiRecognizeResultRespond
import can.lucky.of.addword.net.models.responses.AiRecognizedWordRespond
import can.lucky.of.core.net.responses.PagedRespond
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface AiRecognizeWordClient {

    @GET("ai-analyze/recognize/results")
    suspend fun getRecognizeResults(
        @Header("Authorization") token: String,
        @Query(value = "page") page: Int,
        @Query(value = "size") size: Int
    ) : PagedRespond<AiRecognizeResultRespond>


    @GET("ai-analyze/recognize/{id}/single")
    suspend fun getWord(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AiRecognizedWordRespond


    @POST("ai-analyze/recognize")
    suspend fun recognizeWord(
        @Header("Authorization") token: String,
        @Body body: AiRecognizeWordByTextRequest
    ) : AiRecognizeResultRespond

    @POST("ai-analyze/recognize/confirm")
    suspend fun confirm(
        @Header("Authorization") token: String,
        @Body body: RecognizeConfirmationRequest
    )
}