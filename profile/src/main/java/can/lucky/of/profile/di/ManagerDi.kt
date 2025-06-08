package can.lucky.of.profile.di

import can.lucky.of.profile.domain.managers.EditUserManager
import can.lucky.of.profile.domain.managers.EditUserManagerImpl
import org.koin.dsl.module


internal val managerDi = module {
    single<EditUserManager> {
        EditUserManagerImpl(
            userCacheManager = get(),
            editUserClient = get()
        )
    }
}