package can.lucky.of.history.ui.navigators

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import can.lucky.of.history.domain.models.bundles.LearningPlanBundle

interface LearningPlanNavigator {

    fun navigateToChangePlan(
        nav: NavController,
        bundle: LearningPlanBundle = LearningPlanBundle()
    )

    fun getBundle(fragment: Fragment): LearningPlanBundle

    fun setListener(fragment: Fragment,fragmentManager: FragmentManager, listener : () -> Unit)

    fun popBackStack(nav: NavController,fragment: Fragment,fragmentManager: FragmentManager)
}