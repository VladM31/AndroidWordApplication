package can.lucky.of.history.di

import can.lucky.of.core.domain.managers.LearningPlanManager
import can.lucky.of.history.domain.managers.LearningHistoryManager
import can.lucky.of.history.domain.managers.LearningHistoryManagerImpl
import can.lucky.of.history.domain.managers.LearningPlanManagerImpl
import org.koin.dsl.module

internal val managerDi = module {
    single<LearningHistoryManager> {
        LearningHistoryManagerImpl(
            userCacheManager = get(),
            client = get(),
        )
    }

    single<LearningPlanManager> {
        LearningPlanManagerImpl(
            httpOkHeaderFactory = get(),
            learningPlanClient = get(),
        )
    }
}