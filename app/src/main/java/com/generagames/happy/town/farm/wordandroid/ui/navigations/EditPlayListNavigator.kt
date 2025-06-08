package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.EditPlayListBundle

interface EditPlayListNavigator {

    fun navigateToEdit(controller: NavController, bundle: EditPlayListBundle)

    fun Fragment.listenBack(action: (EditPlayListBundle) -> Unit)

    fun popBack(controller: NavController, manager: FragmentManager, bundle: EditPlayListBundle)
}