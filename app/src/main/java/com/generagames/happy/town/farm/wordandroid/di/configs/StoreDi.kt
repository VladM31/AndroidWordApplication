package com.generagames.happy.town.farm.wordandroid.di.configs

import can.lucky.of.core.domain.storages.BaseUrlStore
import com.generagames.happy.town.farm.wordandroid.domain.stores.BaseUrlStoreImpl
import org.koin.dsl.module


val storeDi = module {
    factory<BaseUrlStore> {
        BaseUrlStoreImpl()
    }
}