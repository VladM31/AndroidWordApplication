package can.lucky.of.profile.di

import org.koin.dsl.module

val profileExport = module {
    includes(clientDi,managerDi,profileViewModelDi)
}