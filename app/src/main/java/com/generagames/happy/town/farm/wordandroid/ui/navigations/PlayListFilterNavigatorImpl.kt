package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.PlayListFilterBundle
import com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists.PlayListFragmentDirections

class PlayListFilterNavigatorImpl : PlayListFilterNavigator {
    override fun navigateToFilter(navController: NavController, bundle: PlayListFilterBundle) {
        navController.navigate(
            PlayListFragmentDirections.actionPlayListFragmentToPlayListFilterFragment(bundle)
        )
    }

    override fun popBack(fragment: Fragment, bundle: PlayListFilterBundle) {
        fragment.parentFragmentManager.setFragmentResult(REQUEST_CODE, bundleOf(BUNDLE_KEY to bundle))
        fragment.requireActivity().onBackPressed()
    }

    override fun listenBack(fragment: Fragment, callback: (PlayListFilterBundle) -> Unit) {
        fragment.parentFragmentManager.setFragmentResultListener(
            REQUEST_CODE,
            fragment.viewLifecycleOwner
        ) { _, b ->
            b.getParcelable<PlayListFilterBundle>(BUNDLE_KEY)?.let {
                callback(it)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = "PlayListFilterNavigatorImpl.REQUEST_CODE"
        private const val BUNDLE_KEY = "PlayListFilterNavigatorImpl.BUNDLE_KEY"
    }
}