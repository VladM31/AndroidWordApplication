package com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.auth

import android.view.View
import androidx.fragment.app.Fragment
import can.lucky.of.auth.databinding.FragmentSignUpBinding
import can.lucky.of.auth.ui.fragments.SignUpFragment
import can.lucky.of.core.domain.managers.spotlight.SpotlightManager
import can.lucky.of.core.ui.listeners.spotlights.AbstractSpotlightsFragmentListener
import can.lucky.of.core.ui.utils.buildRectangleTarget
import com.takusemba.spotlight.Target

class SignUpFragmentListener(
    spotlightManager: SpotlightManager
) : AbstractSpotlightsFragmentListener<FragmentSignUpBinding>(
    spotlightManager,
    SignUpFragment::class
) {
    override fun getViewBinding(v: View) = FragmentSignUpBinding.bind(v)

    override fun onFinished() {

    }

    override fun createTargets(
        newBinding: FragmentSignUpBinding,
        fragment: Fragment
    ): List<Target> {
        val onClick: (() -> Unit) = { spotlight?.next() }

        return listOf(
            fragment.buildRectangleTarget(
                newBinding.toolBar.additionalButton,
                "Privacy Policy",
                "Learn how we collect and protect your data.",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.agreeCheckboxMaterial,
                "Confirmation",
                "Tap to accept the terms and create your account.",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.TOP
            )
        )
    }
}