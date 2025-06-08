package can.lucky.of.exercise.di

import org.koin.dsl.module


val exerciseDi = module {
    includes(
        dataBaseModule,
        clientDi,
        managerModule,
        viewModelModule
    )
}