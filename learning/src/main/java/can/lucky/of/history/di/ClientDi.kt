package can.lucky.of.history.di

import can.lucky.of.core.domain.keepers.MainRetrofitKeeper
import can.lucky.of.core.net.clients.LearningHistoryClient
import can.lucky.of.core.net.clients.LearningPlanClient
import org.koin.dsl.module

internal val clientModule = module {
    single {  get<MainRetrofitKeeper>().retrofit.create(LearningHistoryClient::class.java) }

    single { get<MainRetrofitKeeper>().retrofit.create(LearningPlanClient::class.java) }
}