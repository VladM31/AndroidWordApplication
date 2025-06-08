package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import can.lucky.of.auth.domain.models.data.LogInBundle
import can.lucky.of.auth.ui.fragments.ConfirmSignUpFragment
import can.lucky.of.auth.ui.fragments.ConfirmSignUpFragmentDirections
import can.lucky.of.auth.ui.fragments.LoginFragmentDirections
import can.lucky.of.auth.ui.fragments.SignUpFragmentDirections
import can.lucky.of.auth.ui.navigations.AuthNavigator
import com.generagames.happy.town.farm.wordandroid.R

class AuthNavigatorImpl : AuthNavigator {
    override fun navigateToSignUp(navController: NavController) {
        navController.navigate(R.id.signUpFragment)
    }

    override fun navigateFromLogin(navController: NavController) {
        if (navController.currentDestination?.id != R.id.loginFragment){
            return
        }

        navController.navigate(
            LoginFragmentDirections.actionLoginFragmentToNavBarGraph(),
            NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build())
    }

    override fun navigateToConfirmation(navController: NavController, bundle: LogInBundle) {
        navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToConfirmSignUpFragment(bundle),
            NavOptions.Builder().setPopUpTo(R.id.signUpFragment, true).build())
    }

    override fun navigateFromConfirmation(navController: NavController) {
        navController.navigate(
            ConfirmSignUpFragmentDirections.actionConfirmSignUpFragmentToNavBarGraph(),
            NavOptions.Builder().setPopUpTo(R.id.confirmSignUpFragment, true).build())
    }
}