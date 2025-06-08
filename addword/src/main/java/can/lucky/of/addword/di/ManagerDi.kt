package can.lucky.of.addword.di

import can.lucky.of.addword.domain.managers.WordRecognizeManager
import can.lucky.of.addword.domain.managers.WordRecognizeManagerImpl
import can.lucky.of.addword.domain.managers.userwords.ShareUserWordManager
import can.lucky.of.addword.domain.managers.userwords.ShareUserWordManagerImpl
import org.koin.dsl.module

internal val managerDi = module {
    single<WordRecognizeManager> {
        WordRecognizeManagerImpl(
            recognizeWordClient = get()
        )
    }


    single<ShareUserWordManager> {
        ShareUserWordManagerImpl(
            shareUserWordClient = get(),
            userCacheManager = get()
        )
    }
}