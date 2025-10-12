package com.generagames.happy.town.farm.wordandroid.di.configs

import can.lucky.of.core.ui.listeners.spotlights.SpotlightsFragmentListener
import com.generagames.happy.town.farm.wordandroid.ui.listeners.LifecycleFragmentListener
import com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.auth.SignUpFragmentListener
import com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.main.MenuSpotlightsFragmentListener
import com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.main.WordsFragmentSpotlightsFragmentListener
import com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.playlists.PlayListSpotlightsFragmentListener
import com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.settings.SettingAppSpotlightsFragmentListener
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

val listenerDiModule = module {

    single<SpotlightsFragmentListener>(named("PlayListSpotlightsFragmentListener")) {
        PlayListSpotlightsFragmentListener(
            spotlightManager = get()
        )
    }

    single<SpotlightsFragmentListener>(named("MenuSpotlightsFragmentListener")) {
        MenuSpotlightsFragmentListener(
            spotlightManager = get()
        )
    }

    single<SpotlightsFragmentListener>(named("WordsFragmentSpotlightsFragmentListener")) {
        WordsFragmentSpotlightsFragmentListener(
            spotlightManager = get()
        )
    }

    single<SpotlightsFragmentListener>(named("SettingAppSpotlightsFragmentListener")) {
        SettingAppSpotlightsFragmentListener(
            spotlightManager = get()
        )
    }

    single<SpotlightsFragmentListener>(named("SignUpFragmentListener")) {
        SignUpFragmentListener(
            spotlightManager = get()
        )
    }

    single<LifecycleFragmentListener> {
        LifecycleFragmentListener(
            application = androidApplication(),
            listeners = getAll()
        )
    }

}