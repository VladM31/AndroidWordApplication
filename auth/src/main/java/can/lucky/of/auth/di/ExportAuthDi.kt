package can.lucky.of.auth.di

import org.koin.dsl.module

val exportAuthDi = module {
    includes(clientDi,managerDi,viewModelDi)
}