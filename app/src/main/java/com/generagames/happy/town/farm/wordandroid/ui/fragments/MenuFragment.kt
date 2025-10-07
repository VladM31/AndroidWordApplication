package com.generagames.happy.town.farm.wordandroid.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.ui.utils.buildRectangleTarget
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentMenuBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.data.NavigateBarButton
import com.generagames.happy.town.farm.wordandroid.ui.controllers.NavigateBarController
import com.takusemba.spotlight.Spotlight


class MenuFragment : Fragment(R.layout.fragment_menu) {
    private var binding: FragmentMenuBinding? = null
    private var spotlight: Spotlight? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentMenuBinding.bind(view)
        binding = newBinding

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            newBinding.menuGridLayout.columnCount = 2
        } else {
            newBinding.menuGridLayout.columnCount = 1
        }

        NavigateBarController(
            binding = newBinding.navigationBar, navController = findNavController(),
            button = NavigateBarButton.MENU
        ).start()

        newBinding.wordsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_wordsFragment)
        }

        newBinding.myWordsBtn.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToUserWordsFragment())
        }

        newBinding.addWordBtn.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToChooseAddWordGraph())
        }

        newBinding.instructionBtn.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToInstructionFragment())
        }

        val container = newBinding.root as ViewGroup

        newBinding.root.doOnPreDraw {
            spotlight = Spotlight.Builder(requireActivity())
                .setTargets(createTargets(newBinding))
                .setBackgroundColor(
                    resources.getColor(can.lucky.of.core.R.color.spotlight_background, null)
                )
                .setDuration(600L)
                .setAnimation(DecelerateInterpolator(2f))
                .setContainer(container)
                .build()

            spotlight?.start()
        }
    }

    private fun createTargets(newBinding: FragmentMenuBinding): List<com.takusemba.spotlight.Target> {
        val onClick: (() -> Unit) = { spotlight?.next() }

        return listOf(
            buildRectangleTarget(
                newBinding.wordsBtn,
                "Words",
                "Opens the standard word library",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            buildRectangleTarget(
                newBinding.myWordsBtn,
                "My words",
                "Shows the words currently being learned",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            buildRectangleTarget(
                newBinding.addWordBtn,
                "Add word",
                "Allows you to manually add new words",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            buildRectangleTarget(
                newBinding.instructionBtn,
                "Instruction",
                "Opens the instruction page",
                onClick = onClick,
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            buildRectangleTarget(
                newBinding.navigationBar.playListNavButton,
                "Playlists",
                "Create and view personal playlists",
                onClick = onClick
            ),
            buildRectangleTarget(
                newBinding.navigationBar.settingButton,
                "Settings",
                "Adjust profile and app preferences",
                onClick = onClick
            )

        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        spotlight?.finish()
        spotlight = null
    }
}

