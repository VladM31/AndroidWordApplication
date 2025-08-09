package can.lucky.of.addword.di

import can.lucky.of.addword.domain.managers.recognizes.AiRecognizeWordManager
import can.lucky.of.addword.domain.managers.recognizes.AiRecognizeWordManagerImpl
import can.lucky.of.addword.domain.managers.userwords.ShareUserWordManager
import can.lucky.of.addword.domain.managers.userwords.ShareUserWordManagerImpl
import org.koin.dsl.module

internal val managerDi = module {



    single<ShareUserWordManager> {
        ShareUserWordManagerImpl(
            shareUserWordClient = get(),
            userCacheManager = get()
        )
    }

    single<AiRecognizeWordManager> {
        AiRecognizeWordManagerImpl(
            aiRecognizeWordClient = get(),
            userCacheManager = get()
        )
    }
}