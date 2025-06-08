package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import can.lucky.of.core.ui.navigators.WordRemoveListener
import com.generagames.happy.town.farm.wordandroid.domain.models.keys.WordFilterKeys

class WordRemoveListenerImpl : WordRemoveListener {
    override fun invokeWordRemoved(manager: FragmentManager) {
        manager.setFragmentResult(
            WordFilterKeys.RE_FETCH_CODE,
            bundleOf()
        )
    }

    override fun onWordRemove(fragment: Fragment, manager: FragmentManager, callback: () -> Unit) {
        manager.setFragmentResultListener(WordFilterKeys.RE_FETCH_CODE, fragment.viewLifecycleOwner) { _, _ ->
            callback()
        }
    }
}