package can.lucky.of.auth.di

import can.lucky.of.auth.domain.vms.ConfirmSignUpVm
import can.lucky.of.auth.domain.vms.LoginViewModel
import can.lucky.of.auth.domain.vms.SignUpViewModel
import can.lucky.of.auth.domain.vms.TelegramLoginVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModelDi = module {
    viewModel<LoginViewModel> {
        LoginViewModel(
            authManager = get(),
            userCacheManager = get(),
            authHistoryManager = get(),
            biometricAuthManager = get()
        )
    }

    viewModel {
        SignUpViewModel(
            authManager = get()
        )
    }

    viewModel{
        ConfirmSignUpVm(
            authManager = get()
        )
    }

    viewModel {
        TelegramLoginVm(
            telegramAuthManager = get(),
            authHistoryManager = get()
        )
    }
}