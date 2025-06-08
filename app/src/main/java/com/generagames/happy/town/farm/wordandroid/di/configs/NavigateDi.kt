package com.generagames.happy.town.farm.wordandroid.di.configs

import can.lucky.of.auth.ui.navigations.AuthNavigator
import can.lucky.of.auth.ui.navigations.TelegramLoginNavigator
import can.lucky.of.core.ui.navigators.WordRemoveListener
import can.lucky.of.history.ui.navigators.LearningPlanNavigator
import can.lucky.of.history.ui.navigators.ListLearningHistoryNavigator
import can.lucky.of.profile.ui.navigators.UserProfileNavigator
import com.generagames.happy.town.farm.wordandroid.ui.navigations.AuthNavigatorImpl
import com.generagames.happy.town.farm.wordandroid.ui.navigations.EditPlayListNavigator
import com.generagames.happy.town.farm.wordandroid.ui.navigations.EditPlayListNavigatorImpl
import com.generagames.happy.town.farm.wordandroid.ui.navigations.LearningPlanNavigatorImpl
import com.generagames.happy.town.farm.wordandroid.ui.navigations.ListLearningHistoryNavigatorImpl
import com.generagames.happy.town.farm.wordandroid.ui.navigations.PlayListFilterNavigator
import com.generagames.happy.town.farm.wordandroid.ui.navigations.PlayListFilterNavigatorImpl
import com.generagames.happy.town.farm.wordandroid.ui.navigations.ReFetchPlayListsNavigator
import com.generagames.happy.town.farm.wordandroid.ui.navigations.ReFetchPlayListsNavigatorImpl
import com.generagames.happy.town.farm.wordandroid.ui.navigations.TelegramLoginNavigatorImpl
import com.generagames.happy.town.farm.wordandroid.ui.navigations.UserProfileNavigatorImpl
import com.generagames.happy.town.farm.wordandroid.ui.navigations.UserWordFilterNavigator
import com.generagames.happy.town.farm.wordandroid.ui.navigations.WordRemoveListenerImpl
import org.koin.dsl.module


val navigateDi = module {
    single<AuthNavigator> { AuthNavigatorImpl() }

    single<EditPlayListNavigator> { EditPlayListNavigatorImpl() }

    single<PlayListFilterNavigator> { PlayListFilterNavigatorImpl() }

    single<ListLearningHistoryNavigator> { ListLearningHistoryNavigatorImpl() }

    single { UserWordFilterNavigator() }

    single<LearningPlanNavigator> {
        LearningPlanNavigatorImpl()
    }

    single< WordRemoveListener> {
        WordRemoveListenerImpl()
    }

    single< ReFetchPlayListsNavigator> {
        ReFetchPlayListsNavigatorImpl()
    }

    single<UserProfileNavigator> {
        UserProfileNavigatorImpl()
    }

    single<TelegramLoginNavigator> {
        TelegramLoginNavigatorImpl()
    }
}