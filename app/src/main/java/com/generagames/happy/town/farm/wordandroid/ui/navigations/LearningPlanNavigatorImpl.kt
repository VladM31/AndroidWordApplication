package com.generagames.happy.town.farm.wordandroid.ui.navigations

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import can.lucky.of.history.domain.models.bundles.LearningPlanBundle
import can.lucky.of.history.ui.fragments.ChangeLearningPlanFragmentArgs
import can.lucky.of.history.ui.fragments.LearningPlanFragmentDirections
import can.lucky.of.history.ui.navigators.LearningPlanNavigator

class LearningPlanNavigatorImpl : LearningPlanNavigator {
    private val req = "LearningPlanNavigatorImplReq"

    override fun navigateToChangePlan(nav: NavController, bundle: LearningPlanBundle) {
        nav.navigate(LearningPlanFragmentDirections.actionLearningPlanFragment2ToChangeLearningPlanFragment(bundle))
    }

    override fun getBundle(fragment: Fragment): LearningPlanBundle {
        return ChangeLearningPlanFragmentArgs.fromBundle(fragment.requireArguments()).plan
    }

    override fun setListener(
        fragment: Fragment,
        fragmentManager: FragmentManager,
        listener: () -> Unit
    ) {
        fragmentManager.setFragmentResultListener(req, fragment) { _, _ ->
            listener()
        }
    }

    override fun popBackStack(nav: NavController,fragment: Fragment, fragmentManager: FragmentManager) {
        fragmentManager.setFragmentResult(req, bundleOf())
        nav.popBackStack()
    }
}