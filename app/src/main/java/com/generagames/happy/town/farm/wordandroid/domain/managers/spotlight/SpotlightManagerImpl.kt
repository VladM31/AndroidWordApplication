package com.generagames.happy.town.farm.wordandroid.domain.managers.spotlight

import can.lucky.of.core.domain.managers.spotlight.SpotlightManager

class SpotlightManagerImpl(
    context: android.content.Context
) : SpotlightManager {
    private val prefs =
        context.getSharedPreferences("spotlight_prefs", android.content.Context.MODE_PRIVATE)

    override fun needShow(fragmentTag: String): Boolean {
        return prefs.getBoolean(fragmentTag, false).not()
    }

    override fun setShowed(fragmentTag: String) {
        prefs.edit().putBoolean(fragmentTag, true).apply()
    }
}