package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.navigation.NavController
import can.lucky.of.history.ui.navigators.ListLearningHistoryNavigator
import com.generagames.happy.town.farm.wordandroid.R

class ListLearningHistoryNavigatorImpl : ListLearningHistoryNavigator {
    override fun navigateToList(nav: NavController) {
        nav.navigate(R.id.action_statisticLearningHistoryFragment_to_listLearningHistoryFragment)
    }
}