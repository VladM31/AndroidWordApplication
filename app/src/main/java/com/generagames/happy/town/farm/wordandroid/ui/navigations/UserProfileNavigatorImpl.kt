package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import can.lucky.of.profile.ui.fragments.UserProfileFragmentDirections
import can.lucky.of.profile.ui.navigators.UserProfileNavigator

class UserProfileNavigatorImpl : UserProfileNavigator {
    private val request: String = "UserProfileNavigatorImpl.REQUEST_CODE"

    override fun navigateToEdit(fragment: Fragment) {
        fragment.findNavController().navigate(
            UserProfileFragmentDirections.actionUserProfileFragmentToEditUserProfileFragment()
        )
    }

    override fun popBack(fragment: Fragment) {
        fragment.parentFragmentManager.setFragmentResult(request, bundleOf())
        fragment.findNavController().popBackStack()
    }

    override fun listenPopBack(fragment: Fragment, callback: () -> Unit) {
        fragment.parentFragmentManager.setFragmentResultListener(
            request,
            fragment.viewLifecycleOwner
        ) { _, _ ->
            callback()
        }
    }


}