package com.generagames.happy.town.farm.wordandroid.net.clients.playlist

import can.lucky.of.addword.net.models.responses.SharePlayListRespond
import com.generagames.happy.town.farm.wordandroid.net.models.responses.ShareRespond
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SharePlayListClient {

    @FormUrlEncoded
    @POST("storage-api/share-play-list")
    suspend fun sharePlayList(
        @Header("Authorization") bearerToken: String,
        @Field(value = "playListId", encoded = true) playListId: String,
        @Field(value = "width", encoded = true) width: Int,
        @Field(value = "height", encoded = true) height: Int
    ): ShareRespond

    @GET("storage-api/share-play-list/{sharedId}")
    suspend fun getSharedPlayList( @Header("Authorization") bearerToken: String,@Path("sharedId") sharedId: String): SharePlayListRespond

    @POST("storage-api/share-play-list/{sharedId}/accept")
    suspend fun acceptShare(@Header("Authorization") bearerToken: String,@Path("sharedId") sharedId: String)
}