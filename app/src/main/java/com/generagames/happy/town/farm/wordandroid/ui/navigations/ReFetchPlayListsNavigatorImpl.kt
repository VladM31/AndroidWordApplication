package com.generagames.happy.town.farm.wordandroid.ui.navigations

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class ReFetchPlayListsNavigatorImpl : ReFetchPlayListsNavigator {
    override fun sendRequest(manager: FragmentManager) {
        manager.setFragmentResult(REQUEST_CODE, Bundle())
    }

    override fun listenRequest(manager: FragmentManager, fragment: Fragment, callback: () -> Unit) {
        manager.setFragmentResultListener(REQUEST_CODE, fragment.viewLifecycleOwner) { _, _ ->
            callback()
        }
    }

    companion object{
        const val REQUEST_CODE = "RE_FETCH_PLAY_LISTS_NAVIGATOR_REQUEST_CODE"
    }
}