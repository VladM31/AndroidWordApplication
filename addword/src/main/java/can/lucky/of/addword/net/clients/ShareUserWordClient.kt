package can.lucky.of.addword.net.clients

import can.lucky.of.addword.net.models.responses.ShareUserWordResponse
import can.lucky.of.addword.net.models.responses.ShareWordResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ShareUserWordClient {

    @FormUrlEncoded
    @POST("storage-api/share")
    suspend fun share(
        @Header("Authorization") token: String,
        @Field(value = "userWordId", encoded = true) userWordId: String,
        @Field(value = "width", encoded = true) width: Int,
        @Field(value = "height", encoded = true) height: Int
    ): ShareWordResponse

    @GET("storage-api/share")
    suspend fun getShareWord(
        @Header("Authorization") token: String,
        @Query("shareId") shareId: String
    ): ShareUserWordResponse

    @POST("storage-api/share/accept/{shareId}")
    suspend fun acceptShare(
        @Header("Authorization") token: String,
        @Path("shareId") shareId: String
    )
}