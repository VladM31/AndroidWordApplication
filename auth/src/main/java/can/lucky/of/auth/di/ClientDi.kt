package can.lucky.of.auth.di

import can.lucky.of.auth.net.clients.AlternativeClient
import can.lucky.of.auth.net.clients.AuthClient
import can.lucky.of.auth.net.clients.OkHttpAuthClient
import can.lucky.of.auth.net.clients.TelegramAuthClient
import can.lucky.of.core.domain.keepers.MainOkClientKeeper
import can.lucky.of.core.domain.keepers.MainRetrofitKeeper
import org.koin.dsl.module

internal val clientDi = module{
    single<AuthClient> {
        OkHttpAuthClient(
            client = get<MainOkClientKeeper>().okHttpClient
        )
    }

    single {
        get<MainRetrofitKeeper>().retrofit.create(TelegramAuthClient::class.java)
    }

    single {
        get<MainRetrofitKeeper>().retrofit.create(AlternativeClient::class.java)
    }
}