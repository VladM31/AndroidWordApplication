package com.generagames.happy.town.farm.wordandroid.net.clients.playlist

import com.generagames.happy.town.farm.wordandroid.net.models.requests.PinPlayRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PinPlayListClient {

    @POST("words-api/pin")
    suspend fun pin(@Header("Authorization") token: String, @Body requests: List<PinPlayRequest>)

    @POST("words-api/unpin")
    suspend fun unpin(@Header("Authorization") token: String, @Body requests: List<PinPlayRequest>)
}