package com.generagames.happy.town.farm.wordandroid.net.clients.userword

import com.generagames.happy.town.farm.wordandroid.net.models.requests.DeleteUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.GradeUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.UserWordCountRespond
import com.generagames.happy.town.farm.wordandroid.net.models.responses.UserWordRespond
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UserWordClient {

    @GET("storage/user-words/count")
    suspend fun countBy(@Header("Authorization") token: String, @QueryMap filter: Map<String,String>,
                        @Query(value = "wordIds") wordIds: List<String>?,
                        @Query(value = "ids") ids: List<String>?): UserWordCountRespond

    @GET("storage/user-words")
    suspend fun findBy(@Header("Authorization") token: String, @QueryMap filter: Map<String,String>,
                       @Query(value = "wordIds") wordIds: List<String>?,
                       @Query(value = "ids") ids: List<String>?): List<UserWordRespond>

    @PUT("storage/user-words/grades")
    suspend fun putGrades(@Header("Authorization") token: String, @Body requests: List<GradeUserWordRequest>): IntArray

    @POST("storage/user-words/delete")
    suspend fun delete(@Header("Authorization") token: String,@Body requests: List<DeleteUserWordRequest>): IntArray
}