package com.generagames.happy.town.farm.wordandroid.di.configs

import can.lucky.of.core.domain.factories.GlideHeaderFactory
import can.lucky.of.core.domain.factories.HttpOkHeaderFactory
import org.koin.dsl.module

val factoryDi = module {
    single { HttpOkHeaderFactory(userCacheManager = get()) }

    single { GlideHeaderFactory(userCacheManager = get()) }
}