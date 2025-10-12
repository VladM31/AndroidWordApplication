package com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.main

import android.view.View
import androidx.fragment.app.Fragment
import can.lucky.of.core.domain.managers.spotlight.SpotlightManager
import can.lucky.of.core.ui.listeners.spotlights.AbstractSpotlightsFragmentListener
import can.lucky.of.core.ui.utils.buildRectangleTarget
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentMenuBinding
import com.generagames.happy.town.farm.wordandroid.ui.fragments.MenuFragment


class MenuSpotlightsFragmentListener(
    spotlightManager: SpotlightManager
) : AbstractSpotlightsFragmentListener<FragmentMenuBinding>(spotlightManager, MenuFragment::class) {

    override fun getViewBinding(v: View): FragmentMenuBinding {
        return FragmentMenuBinding.bind(v)
    }

    override fun createTargets(
        newBinding: FragmentMenuBinding,
        fragment: Fragment
    ): List<com.takusemba.spotlight.Target> {
        val onClick: (() -> Unit) = { spotlight?.next() }

        return listOf(
            fragment.buildRectangleTarget(
                newBinding.wordsBtn,
                "Words",
                "Opens the standard word library",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.myWordsBtn,
                "My words",
                "Shows the words currently being learned",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.addWordBtn,
                "Add word",
                "Allows you to manually add new words",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.instructionBtn,
                "Instruction",
                "Opens the instruction page",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.navigationBar.playListNavButton,
                "Playlists",
                "Create and view personal playlists",
                onClick = onClick
            ),
            fragment.buildRectangleTarget(
                newBinding.navigationBar.settingButton,
                "Settings",
                "Adjust profile and app preferences",
                onClick = {
                    spotlight?.finish()
                }
            )
        )
    }

}