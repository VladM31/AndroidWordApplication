package can.lucky.of.core.ui.listeners.spotlights

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import can.lucky.of.core.domain.managers.spotlight.SpotlightManager
import can.lucky.of.core.ui.utils.notNeedShow
import can.lucky.of.core.ui.utils.setShowed
import com.takusemba.spotlight.Spotlight
import kotlin.reflect.KClass


abstract class AbstractSpotlightsFragmentListener<T : ViewBinding>(
    private val spotlightManager: SpotlightManager,
    private val fragmentClass: KClass<out Fragment>
) : SpotlightsFragmentListener {
    protected var binding: T? = null
    protected var spotlight: Spotlight? = null
    protected var isFinished = true


    protected open fun notMatchFragment(f: Fragment): Boolean {
        return f::class != fragmentClass || spotlightManager.notNeedShow(fragmentClass)
    }

    protected abstract fun getViewBinding(v: View): T

    protected abstract fun createTargets(
        newBinding: T,
        fragment: Fragment
    ): List<com.takusemba.spotlight.Target>

    protected open fun onFinished() {
        spotlightManager.setShowed(fragmentClass)
    }


    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ): Boolean {
        if (notMatchFragment(f)) return false
        val newBinding = getViewBinding(v)
        binding = newBinding

        val container = newBinding.root as ViewGroup

        newBinding.root.doOnPreDraw {
            spotlight = Spotlight.Builder(f.requireActivity())
                .setTargets(createTargets(newBinding, f))
                .setBackgroundColor(
                    f.resources.getColor(can.lucky.of.core.R.color.spotlight_background, null)
                )
                .setDuration(600L)
                .setAnimation(DecelerateInterpolator(2f))
                .setContainer(container)
                .setOnSpotlightListener(object : com.takusemba.spotlight.OnSpotlightListener {
                    override fun onStarted() {
                        isFinished = false
                    }

                    override fun onEnded() {
                        isFinished = true
                        onFinished()
                    }
                })
                .build()

            spotlight?.start()
        }

        return true
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment): Boolean {
        if (notMatchFragment(f)) return false

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isFinished.not()) {
                    spotlight?.finish()
                    return
                }

                isEnabled = false
                f.requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }


        f.requireActivity().onBackPressedDispatcher.addCallback(f.viewLifecycleOwner, callback)
        return true
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment): Boolean {
        if (notMatchFragment(f)) return false
        spotlight?.finish()
        spotlight = null
        binding = null
        return true
    }
}