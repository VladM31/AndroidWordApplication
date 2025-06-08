package com.generagames.happy.town.farm.wordandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentSettingAppBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.data.NavigateBarButton
import com.generagames.happy.town.farm.wordandroid.ui.controllers.NavigateBarController
import org.koin.android.ext.android.inject


class SettingAppFragment : Fragment(R.layout.fragment_setting_app) {
    private var binding: FragmentSettingAppBinding? = null

    private val userCacheManager: UserCacheManager by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val newBinding = FragmentSettingAppBinding.bind(view)
        binding = newBinding

        binding?.learningHistoryButton?.setOnClickListener {
            findNavController().navigate(SettingAppFragmentDirections.actionSettingAppFragmentToNavLearningHistory())
        }


        NavigateBarController(
            binding = newBinding.navigationBar,
            navController = findNavController(),
            button = NavigateBarButton.SETTING
        ).start()

        newBinding.logOutButton.setOnClickListener {
            userCacheManager.clear()
            findNavController().navigate(R.id.loginFragment, null, navOptions {
                popUpTo(R.id.settingAppFragment) {
                    inclusive = true
                }
            })
        }

        newBinding.subscribeButton.setOnClickListener {
            findNavController().navigate(SettingAppFragmentDirections.actionSettingAppFragmentToSubscribeFragment())
        }

        newBinding.planButton.setOnClickListener {
            findNavController().navigate(SettingAppFragmentDirections.actionSettingAppFragmentToLearningPlanGraph())
        }

        newBinding.profileButton.setOnClickListener {
            findNavController().navigate(SettingAppFragmentDirections.actionSettingAppFragmentToProfileGraph())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}