package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.PlayListFilterBundle

interface PlayListFilterNavigator {

    fun navigateToFilter(navController: NavController, bundle: PlayListFilterBundle)

    fun popBack( fragment: Fragment, bundle: PlayListFilterBundle)

    fun listenBack(fragment: Fragment, callback: (PlayListFilterBundle) -> Unit)
}