package com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface SpotlightsFragmentListener {

    fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ): Boolean

    fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment): Boolean

    fun onFragmentResumed(fm: FragmentManager, f: Fragment): Boolean
}