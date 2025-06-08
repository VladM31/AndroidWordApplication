package com.generagames.happy.town.farm.wordandroid.di.configs


import can.lucky.of.core.domain.keepers.MainOkClientKeeper
import can.lucky.of.core.domain.keepers.MainRetrofitKeeper
import com.generagames.happy.town.farm.wordandroid.net.clients.payment.PayClient
import com.generagames.happy.town.farm.wordandroid.net.clients.playlist.PinPlayListClient
import com.generagames.happy.town.farm.wordandroid.net.clients.playlist.PlayListClient
import com.generagames.happy.town.farm.wordandroid.net.clients.playlist.SharePlayListClient
import com.generagames.happy.town.farm.wordandroid.net.clients.subscribe.OkHttpSubscribeClient
import com.generagames.happy.town.farm.wordandroid.net.clients.subscribe.SubscribeClient
import com.generagames.happy.town.farm.wordandroid.net.clients.userword.UserWordClient
import com.generagames.happy.town.farm.wordandroid.net.clients.word.OkHttpWordClient
import com.generagames.happy.town.farm.wordandroid.net.clients.word.WordClient
import com.generagames.happy.town.farm.wordandroid.utils.GsonLocalDateTimeAdapter.addLocalDateTimeAdapter
import com.generagames.happy.town.farm.wordandroid.utils.ToStringConverterFactory
import com.generagames.happy.town.farm.wordandroid.utils.baseUrl
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val clientModule = module {
    single {
        MainOkClientKeeper(okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build())
    }

    single{
        MainRetrofitKeeper(retrofit = Retrofit.Builder()
            .baseUrl( baseUrl() )
            .addConverterFactory(ToStringConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().addLocalDateTimeAdapter().create()))
            .client(get<MainOkClientKeeper>().okHttpClient)
            .build())
    }



    single<WordClient> {
        OkHttpWordClient(
            client = get<MainOkClientKeeper>().okHttpClient,
            userCacheManager = get()
        )
    }

    single<SubscribeClient> {
        OkHttpSubscribeClient(
            headerFactory = get(),
            httpClient = get<MainOkClientKeeper>().okHttpClient
        )
    }

    single {
        get<MainRetrofitKeeper>().retrofit.create(UserWordClient::class.java)
    }

    single {
        get<MainRetrofitKeeper>().retrofit.create(PlayListClient::class.java)
    }

    single {
        get<MainRetrofitKeeper>().retrofit.create(PinPlayListClient::class.java)
    }

    single {
        get<MainRetrofitKeeper>().retrofit.create(PayClient::class.java)
    }

    single {
        get<MainRetrofitKeeper>().retrofit.create(SharePlayListClient::class.java)
    }
}