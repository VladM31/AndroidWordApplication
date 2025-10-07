package com.generagames.happy.town.farm.wordandroid.net.clients.userword

import can.lucky.of.core.net.responses.PagedRespond
import com.generagames.happy.town.farm.wordandroid.net.models.requests.DeleteUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.words.PinUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.words.UserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.UserWordRespond
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface UserWordClient {

    @GET("words-api/user-words")
    suspend fun findBy(
        @Header("Authorization") token: String,
        @QueryMap filter: Map<String, String>
    ): PagedRespond<UserWordRespond>

    @POST("words-api/user-words")
    suspend fun save(@Header("Authorization") token: String, @Body requests: List<UserWordRequest>)

    @POST("words-api/user-words/pin")
    suspend fun pin(
        @Header("Authorization") token: String,
        @Body requests: List<PinUserWordRequest>
    )

    @POST("words-api/user-words/delete")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Body requests: List<DeleteUserWordRequest>
    )
}