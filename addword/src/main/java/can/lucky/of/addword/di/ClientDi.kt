package can.lucky.of.addword.di

import can.lucky.of.addword.net.clients.AiRecognizeWordClient
import can.lucky.of.addword.net.clients.ShareUserWordClient
import can.lucky.of.core.domain.keepers.MainRetrofitKeeper
import org.koin.dsl.module


internal val clientModule = module {


    single {
        get<MainRetrofitKeeper>().retrofit.create(ShareUserWordClient::class.java)
    }

    single {
        get<MainRetrofitKeeper>().retrofit.create(AiRecognizeWordClient::class.java)
    }
}