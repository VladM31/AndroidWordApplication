package can.lucky.of.addword.di

import can.lucky.of.addword.net.clients.RecognizeWordClient
import can.lucky.of.addword.net.clients.RecognizeWordClientImpl
import can.lucky.of.addword.net.clients.ShareUserWordClient
import can.lucky.of.core.domain.keepers.MainOkClientKeeper
import can.lucky.of.core.domain.keepers.MainRetrofitKeeper
import can.lucky.of.core.domain.storages.BaseUrlStore
import okhttp3.Headers
import org.koin.dsl.module


internal val clientModule = module {
    single<RecognizeWordClient> {
        RecognizeWordClientImpl(
            okHttpClient = get<MainOkClientKeeper>().okHttpClient,
            imagePath = get<BaseUrlStore>().getBaseUrl() + "/file/recognize/word/image",
            textPath = get<BaseUrlStore>().getBaseUrl() + "/file/recognize/word/text",
            headerFactory = get()
        )
    }

    single {
        get<MainRetrofitKeeper>().retrofit.create(ShareUserWordClient::class.java)
    }
}