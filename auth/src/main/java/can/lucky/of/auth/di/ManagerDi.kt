package can.lucky.of.auth.di

import can.lucky.of.auth.domain.managers.AuthHistoryManager
import can.lucky.of.auth.domain.managers.AuthHistoryManagerImpl
import can.lucky.of.auth.domain.managers.AuthManager
import can.lucky.of.auth.domain.managers.AuthManagerImpl
import can.lucky.of.auth.domain.managers.BiometricAuthenticationManager
import can.lucky.of.auth.domain.managers.BiometricAuthenticationManagerImpl
import can.lucky.of.auth.domain.managers.TelegramAuthManager
import can.lucky.of.auth.domain.managers.TelegramAuthManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.get
import org.koin.dsl.module

internal val managerDi = module {
    single<AuthManager> {
        AuthManagerImpl(
            authClient = get(),
            userCacheManager = get(),
            context = androidContext(),
            biometricAuthenticationManager = get()
        )
    }

    single<TelegramAuthManager> {
        TelegramAuthManagerImpl(
            telegramAuthClient = get(),
            userCacheManager = get()
        )
    }

    single<AuthHistoryManager> {
        AuthHistoryManagerImpl(
            context = androidContext()
        )
    }

    single<BiometricAuthenticationManager> {
        BiometricAuthenticationManagerImpl(
            context = androidContext(),
            userCacheManager = get(),
            authHistoryManager = get(),
            client = get()
        )
    }
}