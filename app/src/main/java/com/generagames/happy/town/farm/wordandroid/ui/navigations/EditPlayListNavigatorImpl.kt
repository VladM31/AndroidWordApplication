package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.EditPlayListBundle
import com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists.PlayListDetailsFragmentDirections

class EditPlayListNavigatorImpl : EditPlayListNavigator {
    override fun navigateToEdit(controller: NavController, bundle: EditPlayListBundle) {
        controller.navigate(
            PlayListDetailsFragmentDirections.actionPlayListDetailsFragmentToEditPlayListFragment(bundle)
        )
    }

    override fun Fragment.listenBack(action: (EditPlayListBundle) -> Unit) {
        parentFragmentManager.setFragmentResultListener(REQUEST_CODE, viewLifecycleOwner){
                _, b ->
            b.getParcelable<EditPlayListBundle>(BUNDLE_KEY)?.let {
                action(it)
            }
        }
    }

    override fun popBack(controller: NavController, manager: FragmentManager,bundle: EditPlayListBundle) {
        manager.setFragmentResult(REQUEST_CODE, bundleOf(BUNDLE_KEY to bundle))
        controller.popBackStack()
    }

    companion object {
        private const val REQUEST_CODE = "EditPlayListNavigatorImpl.REQUEST_CODE"
        private const val BUNDLE_KEY = "EditPlayListNavigatorImpl.BUNDLE_KEY"
    }
}