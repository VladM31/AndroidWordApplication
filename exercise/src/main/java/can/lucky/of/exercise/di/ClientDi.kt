package can.lucky.of.exercise.di

import can.lucky.of.core.domain.keepers.MainOkClientKeeper
import can.lucky.of.exercise.net.clients.ExerciseStatisticalClient
import can.lucky.of.exercise.net.clients.impls.OkHttpExerciseStatisticalClient
import org.koin.dsl.module

internal val clientDi = module {
    single<ExerciseStatisticalClient> { OkHttpExerciseStatisticalClient(get<MainOkClientKeeper>().okHttpClient) }
}