package com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.settings

import android.view.View
import androidx.fragment.app.Fragment
import can.lucky.of.core.domain.managers.spotlight.SpotlightManager
import can.lucky.of.core.ui.utils.buildRectangleTarget
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentSettingAppBinding
import com.generagames.happy.town.farm.wordandroid.ui.fragments.SettingAppFragment
import com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.AbstractSpotlightsFragmentListener
import com.takusemba.spotlight.Target

class SettingAppSpotlightsFragmentListener(
    spotlightManager: SpotlightManager
) : AbstractSpotlightsFragmentListener<FragmentSettingAppBinding>(
    spotlightManager,
    SettingAppFragment::class
) {

    override fun getViewBinding(v: View) = FragmentSettingAppBinding.bind(v)

    override fun createTargets(
        newBinding: FragmentSettingAppBinding,
        fragment: Fragment
    ): List<Target> {
        return listOf(
            fragment.buildRectangleTarget(
                newBinding.subscribeButton,
                "Subscribe Button",
                "Click here to explore subscription options and unlock premium features.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.learningHistoryButton,
                "Learning History",
                "Review your past learning activities and track your progress over time.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.planButton,
                "Learning Plan",
                "Access and customize your learning plan to achieve your educational goals.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.profileButton,
                "Profile Settings",
                "Manage your personal information and account settings.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.policyBtn,
                "Privacy Policy",
                "Read our privacy policy to understand how we handle your data.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.logOutButton,
                "Log Out",
                "Click here to securely log out of your account.",
                onClick = { spotlight?.finish() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            )
        )
    }
}