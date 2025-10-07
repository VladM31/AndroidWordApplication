package com.generagames.happy.town.farm.wordandroid.di.configs

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.media.MediaManager
import can.lucky.of.core.domain.managers.playlist.PlayListManager
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.managers.userwords.UserWordManager
import can.lucky.of.core.domain.managers.word.WordManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.cache.SharedUserCacheManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.media.ExoCacheMediaManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayManagerImpl
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayPropositionManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayPropositionManagerImpl
import com.generagames.happy.town.farm.wordandroid.domain.managers.pdf.InstructionManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.pdf.InstructionManagerImpl
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.PinPlayListManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.SharePlayListManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.impl.PinPlayListManagerImpl
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.impl.PlayListManagerImpl
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.impl.SharePlayListManagerImpl
import com.generagames.happy.town.farm.wordandroid.domain.managers.subscribe.SharedPrefSubscribeCacheManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.userwords.UserWordManagerImpl
import com.generagames.happy.town.farm.wordandroid.domain.managers.word.WordManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val managerModule = module {
    single<UserCacheManager> {
        SharedUserCacheManager(androidContext())
    }

    single<WordManager> {
        WordManagerImpl(
            wordClient = get(),
            userCacheManager = get()
        )
    }

    single<SubscribeCacheManager> {
        SharedPrefSubscribeCacheManager(
            subscribeClient = get(),
            userCacheManager = get(),
            context = androidContext()
        )
    }

    single<UserWordManager> {
        UserWordManagerImpl(
            userWordClient = get(),
            userCacheManager = get(),
            fileClient = get(),
            context = androidContext()
        )
    }

    single<PlayListManager> {
        PlayListManagerImpl(
            playListClient = get(),
            userCacheManager = get()
        )
    }

    single<PinPlayListManager> {
        PinPlayListManagerImpl(
            pinPlayListClient = get(),
            userCacheManager = get()
        )
    }

    single<PayManager> {
        PayManagerImpl(
            userCacheManager = get(),
            payClient = get()
        )
    }

    single<SharePlayListManager> {
        SharePlayListManagerImpl(
            userCacheManager = get(),
            sharePlayListClient = get()
        )
    }

    single<MediaManager>{
        ExoCacheMediaManager(
            context = androidContext(),
            headerFactory = get()
        )
    }

    single< PayPropositionManager> {
        PayPropositionManagerImpl(
            context = androidContext()
        )
    }

    single<InstructionManager> {
        InstructionManagerImpl(
            downloadClient = get(),
            context = androidContext()
        )
    }
}