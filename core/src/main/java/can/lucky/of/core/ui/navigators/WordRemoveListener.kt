package can.lucky.of.core.ui.navigators

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface WordRemoveListener {

    fun invokeWordRemoved(manager: FragmentManager)

    fun onWordRemove(fragment: Fragment, manager: FragmentManager, callback: () -> Unit)
}