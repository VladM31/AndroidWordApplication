package com.generagames.happy.town.farm.wordandroid.ui.controllers

import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.generagames.happy.town.farm.wordandroid.databinding.NavigationBarBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.data.NavigateBarButton

class NavigateBarController(
    binding: NavigationBarBinding,
    private val navController : NavController,
    private val button: NavigateBarButton
) {
    private val map = mapOf(
        NavigateBarButton.MENU to binding.homeNavButton,
        NavigateBarButton.SETTING to binding.settingButton,
        NavigateBarButton.PLAY_LIST to binding.playListNavButton
    )

    fun start(){
        for (btn in NavigateBarButton.entries) {
            if (btn == button) {
                map[btn]?.setImageResource(btn.activeDrawId)
                continue
            }

            map[btn]?.setOnClickListener {
                navController.navigate(btn.graphId,null, navOptions {
                    popUpTo(button.graphId) {
                        inclusive = true
                    }
                })
            }
        }

    }


}