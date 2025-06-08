package can.lucky.of.profile.di

import can.lucky.of.profile.domain.vms.EditUserProfileVm
import can.lucky.of.profile.domain.vms.UserProfileVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val profileViewModelDi = module {
    viewModel {
        UserProfileVm(get())
    }
    viewModel {
        EditUserProfileVm(
            editUserManager = get(),
            userCacheManager = get()
        )
    }
}