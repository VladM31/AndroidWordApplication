package com.generagames.happy.town.farm.wordandroid.net.clients.word

import can.lucky.of.core.net.responses.PagedRespond
import com.generagames.happy.town.farm.wordandroid.net.models.responses.WordRespond
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface WordClient {

    @GET("words-api/words")
    suspend fun findBy(
        @Header("Authorization") token: String,
        @QueryMap filter: Map<String, String>
    ): PagedRespond<WordRespond>
}