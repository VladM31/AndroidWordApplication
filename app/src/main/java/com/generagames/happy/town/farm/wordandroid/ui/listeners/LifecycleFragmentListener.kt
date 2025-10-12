package com.generagames.happy.town.farm.wordandroid.ui.listeners

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import can.lucky.of.core.ui.listeners.spotlights.SpotlightsFragmentListener

class LifecycleFragmentListener(
    application: Application,
    private val listeners: List<SpotlightsFragmentListener>
) : Application.ActivityLifecycleCallbacks {

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is androidx.fragment.app.FragmentActivity) return

        val callback = object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {
                Log.i(
                    "LifecycleFragmentListener",
                    "onFragmentViewCreated: ${f.javaClass.simpleName}"
                )
                listeners.firstOrNull { it.onFragmentViewCreated(fm, f, v, savedInstanceState) }
            }

            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                listeners.firstOrNull { it.onFragmentResumed(fm, f) }
            }

            override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                listeners.firstOrNull { it.onFragmentViewDestroyed(fm, f) }
            }
        }

        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(callback, true)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}