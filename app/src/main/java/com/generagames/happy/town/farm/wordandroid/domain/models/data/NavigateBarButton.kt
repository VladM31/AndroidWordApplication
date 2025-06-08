package com.generagames.happy.town.farm.wordandroid.domain.models.data

import com.generagames.happy.town.farm.wordandroid.R as NavR
import can.lucky.of.core.R as CoreR

enum class NavigateBarButton(
    val activeDrawId: Int,
    val graphId: Int
) {
    MENU(CoreR.drawable.home, NavR.id.menu_nav_graph),
    SETTING(CoreR.drawable.setting,NavR.id.setting_app_nav_graph),
    PLAY_LIST(CoreR.drawable.play_list_nav, NavR.id.play_list_nav_graph)
}