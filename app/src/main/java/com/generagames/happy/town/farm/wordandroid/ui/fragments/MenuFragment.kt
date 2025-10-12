package com.generagames.happy.town.farm.wordandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.ui.utils.setColumnCountByOrientation
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentMenuBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.data.NavigateBarButton
import com.generagames.happy.town.farm.wordandroid.ui.controllers.NavigateBarController


class MenuFragment : Fragment(R.layout.fragment_menu) {
    private var binding: FragmentMenuBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentMenuBinding.bind(view)
        binding = newBinding

        newBinding.menuGridLayout.setColumnCountByOrientation(1, 2)

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


    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

