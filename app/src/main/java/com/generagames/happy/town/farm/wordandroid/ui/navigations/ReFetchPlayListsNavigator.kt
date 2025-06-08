package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface ReFetchPlayListsNavigator {

    fun sendRequest(manager: FragmentManager)

    fun listenRequest(manager: FragmentManager, fragment: Fragment, callback: () -> Unit)
}