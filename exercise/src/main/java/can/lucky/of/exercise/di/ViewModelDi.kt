package can.lucky.of.exercise.di

import can.lucky.of.exercise.domain.vm.ExerciseSelectionVm
import can.lucky.of.exercise.domain.vm.ExerciseViewModel
import can.lucky.of.exercise.domain.vm.LettersMatchVm
import can.lucky.of.exercise.domain.vm.MatchExerciseVm
import can.lucky.of.exercise.domain.vm.SelectingAnOptionVm
import can.lucky.of.exercise.domain.vm.WriteByImageAndFieldVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModelModule = module {

    viewModel{
        ExerciseSelectionVm(
            exerciseDetailsManager = get()
        )
    }

    viewModel {
        ExerciseViewModel(
            exerciseWordManager = get(),
            exerciseTransactionManager = get(),
            subscribeCacheManager = get(),
            playListManager = get(),
            exerciseStatisticalManager = get()
        )
    }
    
    viewModel {
        MatchExerciseVm(
            exerciseStatisticalManager = get()
        )
    }

    viewModel {
        WriteByImageAndFieldVm(
            exerciseStatisticalManager = get()
        )
    }


    viewModel {
        SelectingAnOptionVm(
            mediaManager = get(),
            exerciseStatisticalManager = get()
        )
    }

    viewModel {
        LettersMatchVm(
            exerciseStatisticalManager = get()
        )
    }
}