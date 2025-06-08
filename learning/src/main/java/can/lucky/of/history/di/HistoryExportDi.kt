package can.lucky.of.history.di

import org.koin.dsl.module


val historyExportDi = module {
    includes(clientModule,managerDi,viewModelDi)
}