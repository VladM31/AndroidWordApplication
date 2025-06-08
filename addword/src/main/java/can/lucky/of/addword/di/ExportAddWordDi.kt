package can.lucky.of.addword.di

import org.koin.dsl.module


val exportAddWordDi = module {
    includes(clientModule,managerDi,viewModelDi)
}