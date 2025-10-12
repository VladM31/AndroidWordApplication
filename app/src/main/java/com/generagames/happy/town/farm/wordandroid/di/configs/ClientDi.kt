package com.generagames.happy.town.farm.wordandroid.di.configs


import can.lucky.of.core.domain.keepers.MainOkClientKeeper
import can.lucky.of.core.domain.keepers.MainRetrofitKeeper
import com.generagames.happy.town.farm.wordandroid.net.clients.files.DownloadClient
import com.generagames.happy.town.farm.wordandroid.net.clients.files.FileApiClient
import com.generagames.happy.town.farm.wordandroid.net.clients.files.FileApiClientImpl
import com.generagames.happy.town.farm.wordandroid.net.clients.payment.PayClient
import com.generagames.happy.town.farm.wordandroid.net.clients.playlist.PinPlayListClient
import com.generagames.happy.town.farm.wordandroid.net.clients.playlist.PlayListClient
import com.generagames.happy.town.farm.wordandroid.net.clients.playlist.SharePlayListClient
import com.generagames.happy.town.farm.wordandroid.net.clients.subscribe.OkHttpSubscribeClient
import com.generagames.happy.town.farm.wordandroid.net.clients.subscribe.SubscribeClient
import com.generagames.happy.town.farm.wordandroid.net.clients.userword.UserWordClient
import com.generagames.happy.town.farm.wordandroid.net.clients.word.WordClient
import com.generagames.happy.town.farm.wordandroid.utils.ToStringConverterFactory
import com.generagames.happy.town.farm.wordandroid.utils.baseUrl
import com.generagames.happy.town.farm.wordandroid.utils.gson.GsonLocalDateTimeAdapter.addLocalDateTimeAdapter
import com.generagames.happy.town.farm.wordandroid.utils.gson.GsonOffsetDateTimeAdapter.addOffsetDateTimeAdapter
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
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().addOffsetDateTimeAdapter().addLocalDateTimeAdapter().create()
                )
            )
            .client(get<MainOkClientKeeper>().okHttpClient)
            .build())
    }



    single<WordClient> {
        get<MainRetrofitKeeper>().retrofit.create(WordClient::class.java)
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

    single {
        get<MainRetrofitKeeper>().retrofit.create(DownloadClient::class.java)
    }


    single<FileApiClient> {
        FileApiClientImpl(
            userCacheManager = get(),
            client = get<MainOkClientKeeper>().okHttpClient
        )
    }
}