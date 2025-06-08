package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.UserWordFilterBundle
import com.generagames.happy.town.farm.wordandroid.ui.fragments.words.UserWordFilterFragmentArgs
import com.generagames.happy.town.farm.wordandroid.ui.fragments.words.UserWordsFragmentDirections

class UserWordFilterNavigator {
    private val key = "UserWordFilterBundleKey"
    private val reqKey = "UserWordFilterReqKey"

    fun navigateToFilter(nav: NavController, bundle: UserWordFilterBundle) {
        val direction =
            UserWordsFragmentDirections.actionUserWordsFragmentToUserWordFilterFragment(bundle)
        nav.navigate(direction)
    }

    fun getBundle(fragment: Fragment): UserWordFilterBundle {
        return UserWordFilterFragmentArgs.fromBundle(fragment.requireArguments()).filter
    }

    fun listenBundle(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        onBundle: (UserWordFilterBundle) -> Unit
    ) {
        fragmentManager.setFragmentResultListener(reqKey, fragment) { _, bundle ->
            onBundle(bundle.getParcelable(key)!!)
        }
    }

    fun popBack(nav: NavController,fragmentManager: FragmentManager, bundle: UserWordFilterBundle) {
        fragmentManager.setFragmentResult(reqKey, bundleOf(key to bundle))
        nav.popBackStack()
    }
}