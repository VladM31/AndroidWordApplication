package can.lucky.of.profile.ui.navigators

import androidx.fragment.app.Fragment

interface UserProfileNavigator {

    fun navigateToEdit(fragment: Fragment)

    fun popBack(fragment: Fragment)

    fun listenPopBack(fragment: Fragment, callback: () -> Unit)
}