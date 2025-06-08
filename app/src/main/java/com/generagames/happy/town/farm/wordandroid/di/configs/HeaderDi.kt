package com.generagames.happy.town.farm.wordandroid.di.configs

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.utils.toPair
import com.bumptech.glide.load.model.LazyHeaders
import org.koin.dsl.module


val headerDiModule = module {
    factory<LazyHeaders> {
        val userCacheManager: UserCacheManager = get()

        val pair = userCacheManager.toPair()

        LazyHeaders.Builder()
            .addHeader(pair.first, pair.second)
            .build()
    }

    factory<okhttp3.Headers> {
        val userCacheManager: UserCacheManager = get()

        userCacheManager.toPair().let {
            okhttp3.Headers.Builder()
                .add(it.first, it.second)
                .build()
        }
    }
}