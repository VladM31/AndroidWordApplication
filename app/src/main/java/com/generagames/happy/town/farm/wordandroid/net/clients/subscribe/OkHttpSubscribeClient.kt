package com.generagames.happy.town.farm.wordandroid.net.clients.subscribe

import android.util.Log
import can.lucky.of.core.domain.factories.HttpOkHeaderFactory
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import com.generagames.happy.town.farm.wordandroid.net.models.responses.SubscribeRespond
import com.generagames.happy.town.farm.wordandroid.utils.GsonLocalDateTimeAdapter.addLocalDateTimeAdapter
import com.generagames.happy.town.farm.wordandroid.utils.baseUrl
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request

class OkHttpSubscribeClient(
    private val headerFactory: HttpOkHeaderFactory,
    private val httpClient: OkHttpClient
) : SubscribeClient {
    private val url by lazy { "${baseUrl()}/pay/subscribe" }
    private val gson = GsonBuilder().addLocalDateTimeAdapter().create()

    override suspend fun fetch(): SubscribeRespond? {
        val req = Request.Builder()
            .get()
            .headers(headerFactory.createHeaders())
            .url(url).build()

       return httpClient.runCatching {

            val res = newCall(req).execute()

            if (res.isSuccessful.not()) {
                Log.e("SubscribeClient", "Error: ${res.code}, response.body: ${res.body?.string()}")
                return null
            }

            res.body?.string()?.let {
                gson.fromJson(it, SubscribeRespond::class.java)
            } ?: return null
        }.getOrNull()
    }
}