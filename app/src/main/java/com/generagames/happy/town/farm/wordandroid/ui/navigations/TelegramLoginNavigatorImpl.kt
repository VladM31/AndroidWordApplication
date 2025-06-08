package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import can.lucky.of.auth.ui.fragments.LoginFragmentDirections
import can.lucky.of.auth.ui.fragments.TelegramLoginFragmentDirections
import can.lucky.of.auth.ui.navigations.TelegramLoginNavigator
import com.generagames.happy.town.farm.wordandroid.R

class TelegramLoginNavigatorImpl : TelegramLoginNavigator {
    override fun navigateToTelegramLogin(fragment: Fragment) {
        NavHostFragment.findNavController(fragment)
            .navigate(LoginFragmentDirections.actionLoginFragmentToTelegramLoginFragment())
    }

    override fun navigateFromTelegramLogin(fragment: Fragment) {
        val current = NavHostFragment.findNavController(fragment).currentDestination?.id

        if (current != R.id.telegramLoginFragment) {
            return
        }

        NavHostFragment.findNavController(fragment).navigate(
            TelegramLoginFragmentDirections.actionTelegramLoginFragmentToNavBarGraph(),
            NavOptions.Builder().setPopUpTo(R.id.telegramLoginFragment, true).build()
        )
    }
}