package com.generagames.happy.town.farm.wordandroid.net.clients.payment

import com.generagames.happy.town.farm.wordandroid.net.models.requests.pay.CardPayRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.pay.GooglePayRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.pay.PayRespond
import com.generagames.happy.town.farm.wordandroid.net.models.responses.pay.SubCostRespond
import com.generagames.happy.town.farm.wordandroid.net.models.responses.pay.WaitCardPayRespond
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


interface PayClient {
    @POST("pay/payment/card")
    suspend fun pay(@Header("Authorization") token: String, @Body request: CardPayRequest): WaitCardPayRespond

    @POST("pay/payment/google-pay")
    suspend fun pay(@Header("Authorization") token: String, @Body request: GooglePayRequest): PayRespond

    @GET("pay/subscribe/costs")
    suspend fun getCosts(@Header("Authorization") bearerToken: String, @Query("platform") platform: String): List<SubCostRespond>

    @GET("pay/payment/card/date")
    suspend fun waitExpirationDate(@Header("Authorization") token: String, @Query("dateCacheId") dateCacheId: String): PayRespond
}