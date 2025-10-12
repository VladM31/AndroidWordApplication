package can.lucky.of.auth.ui.navigations

import androidx.navigation.NavController
import can.lucky.of.auth.domain.models.data.LogInBundle

interface AuthNavigator {

    fun navigateToSignUp(navController: NavController)

    fun navigateFromLogin(navController: NavController)

    fun navigateToConfirmation(navController: NavController, bundle: LogInBundle)

    fun navigateFromConfirmation(navController: NavController)

    fun navigateToPolicy(navController: NavController)
}