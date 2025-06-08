package can.lucky.of.profile.di

import can.lucky.of.core.domain.keepers.MainRetrofitKeeper
import can.lucky.of.profile.net.clients.EditUserClient
import org.koin.dsl.module

internal val clientDi = module {
    single {
        get<MainRetrofitKeeper>().retrofit.create(EditUserClient::class.java)
    }
}