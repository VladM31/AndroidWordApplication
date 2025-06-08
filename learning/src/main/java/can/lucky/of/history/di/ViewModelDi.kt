package can.lucky.of.history.di

import can.lucky.of.history.domain.vms.ChangeLearningPlanVm
import can.lucky.of.history.domain.vms.LearningPlanVm
import can.lucky.of.history.domain.vms.ListLearningHistoryVm
import can.lucky.of.history.domain.vms.StatisticLearningHistoryVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModelDi = module {
    viewModel {
        StatisticLearningHistoryVm(
            manager = get()
        )
    }

    viewModel {
        ListLearningHistoryVm(
            learningHistoryManager = get()
        )
    }

    viewModel {
        LearningPlanVm(
            learningPlanManager = get(),
            learningHistoryManager = get()
        )
    }

    viewModel {
        ChangeLearningPlanVm(
            learningPlanManager = get()
        )
    }
}