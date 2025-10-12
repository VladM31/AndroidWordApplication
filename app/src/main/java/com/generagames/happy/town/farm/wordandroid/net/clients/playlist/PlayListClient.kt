package com.generagames.happy.town.farm.wordandroid.net.clients.playlist

import can.lucky.of.core.net.responses.PagedRespond
import com.generagames.happy.town.farm.wordandroid.net.models.requests.PlayListGradeRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.SavePlayListRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.UpdatePlayListRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.PlayListCountRespond
import com.generagames.happy.town.farm.wordandroid.net.models.responses.PlayListRespond
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.QueryMap


interface PlayListClient {

    @GET("words-api/play-list/count")
    suspend fun countBy(
        @Header("Authorization") token: String,
        @QueryMap filter: Map<String, String>
    ): PagedRespond<PlayListCountRespond>

    @GET("words-api/play-list")
    suspend fun findBy(
        @Header("Authorization") token: String,
        @QueryMap filter: Map<String, String>
    ): PagedRespond<PlayListRespond>

    @POST("words-api/play-list")
    suspend fun save(
        @Header("Authorization") token: String,
        @Body playLists: List<SavePlayListRequest>
    )

    @PUT("words-api/play-list")
    suspend fun update(
        @Header("Authorization") token: String,
        @Body playLists: List<UpdatePlayListRequest>
    )

    @PUT("words-api/play-list/grades")
    suspend fun updateGrades(
        @Header("Authorization") token: String,
        @Body grades: List<PlayListGradeRequest>
    )

    @DELETE("words-api/play-list/delete")
    suspend fun delete(
        @Header("Authorization") token: String,
        @QueryMap filter: Map<String, String>
    )
}