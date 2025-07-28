package can.lucky.of.exercise.di

import can.lucky.of.exercise.domain.managers.ExerciseDetailsManager
import can.lucky.of.exercise.domain.managers.ExerciseStatisticalManager
import can.lucky.of.exercise.domain.managers.impls.ExerciseDetailsManagerImpl
import can.lucky.of.exercise.domain.managers.ExerciseTransactionManager
import can.lucky.of.exercise.domain.managers.impls.ExerciseTransactionManagerImpl
import can.lucky.of.exercise.domain.managers.ExerciseWordManager
import can.lucky.of.exercise.domain.managers.impls.ExerciseStatisticalManagerImpl
import can.lucky.of.exercise.domain.managers.impls.ExerciseWordManagerImpl
import org.koin.dsl.module

internal val managerModule = module {

    single<ExerciseDetailsManager> {
        ExerciseDetailsManagerImpl(exerciseDao = get())
    }

    single<ExerciseTransactionManager> {
        ExerciseTransactionManagerImpl(exerciseTransactionDao = get(), exerciseManager = get())
    }

    single<ExerciseWordManager> {
        ExerciseWordManagerImpl(
            exerciseWordDao = get(),
            userWordManager = get()
        )
    }

    single<ExerciseStatisticalManager> {
        ExerciseStatisticalManagerImpl(
            client = get(),
            userManager = get(),
            learningPlanClient = get(),
            learningHistoryClient = get()
        )
    }
}