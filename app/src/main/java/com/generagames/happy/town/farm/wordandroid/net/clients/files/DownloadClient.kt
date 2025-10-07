package com.generagames.happy.town.farm.wordandroid.net.clients.files

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface DownloadClient {

    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody
}