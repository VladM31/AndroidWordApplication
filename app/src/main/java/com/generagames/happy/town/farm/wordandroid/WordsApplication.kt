package com.generagames.happy.town.farm.wordandroid

import android.app.Application
import android.util.Log
import can.lucky.of.addword.di.exportAddWordDi
import can.lucky.of.auth.di.exportAuthDi
import can.lucky.of.core.domain.keepers.MainOkClientKeeper
import can.lucky.of.core.domain.managers.media.MediaManager
import can.lucky.of.exercise.di.exerciseDi
import can.lucky.of.history.di.historyExportDi
import can.lucky.of.profile.di.profileExport
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.generagames.happy.town.farm.wordandroid.di.configs.clientModule
import com.generagames.happy.town.farm.wordandroid.di.configs.factoryDi
import com.generagames.happy.town.farm.wordandroid.di.configs.headerDiModule
import com.generagames.happy.town.farm.wordandroid.di.configs.listenerDiModule
import com.generagames.happy.town.farm.wordandroid.di.configs.managerModule
import com.generagames.happy.town.farm.wordandroid.di.configs.navigateDi
import com.generagames.happy.town.farm.wordandroid.di.configs.viewModelModule
import com.generagames.happy.town.farm.wordandroid.ui.listeners.LifecycleFragmentListener
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.InputStream


class WordsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WordsApplication)
            modules(
                navigateDi,
                clientModule,
                managerModule,
                headerDiModule,
                factoryDi,
                viewModelModule,
                exerciseDi,
                exportAddWordDi,
                exportAuthDi,
                historyExportDi,
                profileExport,
                listenerDiModule
            )
        }

        val client = get<MainOkClientKeeper>().okHttpClient

        val factory = OkHttpUrlLoader.Factory(client)
        Glide.get(this).registry.replace(GlideUrl::class.java, InputStream::class.java, factory)

        Log.d("WordsApplication", "onCreate: ${get<LifecycleFragmentListener>()}")
    }

    override fun onTerminate() {
        super.onTerminate()
        get<MediaManager>().runCatching {
            close()
        }
    }
}
