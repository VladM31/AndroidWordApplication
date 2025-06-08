package can.lucky.of.auth.ui.navigations

import androidx.fragment.app.Fragment

interface TelegramLoginNavigator {

    fun navigateToTelegramLogin(fragment: Fragment)

    fun navigateFromTelegramLogin(fragment: Fragment)
}