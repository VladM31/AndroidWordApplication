package com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.main

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import can.lucky.of.core.domain.managers.spotlight.SpotlightManager
import can.lucky.of.core.ui.utils.buildRectangleTarget
import com.generagames.happy.town.farm.wordandroid.databinding.BoxWordBinding
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentWordsBinding
import com.generagames.happy.town.farm.wordandroid.ui.fragments.words.WordsFragment
import com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.AbstractSpotlightsFragmentListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target


class WordsFragmentSpotlightsFragmentListener(
    spotlightManager: SpotlightManager
) :
    AbstractSpotlightsFragmentListener<FragmentWordsBinding>(
        spotlightManager,
        WordsFragment::class
    ) {
    private var wordBoxBinding: BoxWordBinding? = null
    private var wordBoxSpotlight: Spotlight? = null

    override fun getViewBinding(v: View) = FragmentWordsBinding.bind(v)

    override fun createTargets(newBinding: FragmentWordsBinding, fragment: Fragment): List<Target> {
        val itemListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val holderView =
                    newBinding.wordsRecyclerView.findViewHolderForAdapterPosition(0)?.itemView
                        ?: return
                newBinding.wordsRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                wordBoxBinding = BoxWordBinding.bind(holderView)
            }
        }

        newBinding.wordsRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(itemListener)

        return listOf(
            fragment.buildRectangleTarget(
                newBinding.toolBar.backButton,
                "Back Button",
                "Click this button to return to the previous screen",
                onClick = { spotlight?.next() },
            ),
            fragment.buildRectangleTarget(
                newBinding.toolBar.additionalButton,
                "Search Button",
                "Click this button to search for specific words",
                onClick = { spotlight?.next() },
            ),
            fragment.buildRectangleTarget(
                newBinding.wordsRecyclerView,
                "Word List",
                "Browse through the list of words here. Tap on any word to select it.",
                onClick = {
                    spotlight?.finish()
                    setWordBoxSpotlight(fragment)
                },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            )
        )
    }

    private fun createWordBoxTargets(newBinding: BoxWordBinding, fragment: Fragment): List<Target> {
        return listOf(
            fragment.buildRectangleTarget(
                newBinding.originText,
                "Word",
                "This is the word in the original language",
                onClick = { wordBoxSpotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.translateText,
                "Translation",
                "This is the translation of the word",
                onClick = { wordBoxSpotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.category,
                "Category",
                "This indicates the category of the word",
                onClick = { wordBoxSpotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.openWordBtnBox,
                "Open Word Button",
                "Click this button to view detailed information about the word",
                onClick = { wordBoxSpotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.hasSound,
                "Sound Icon",
                "Indicates that this word has an associated sound",
                onClick = { wordBoxSpotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.root,
                "Add to My words",
                "Tap the card to select the word and add it to studying list",
                onClick = { wordBoxSpotlight?.finish() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            )

        )
    }

    private fun setWordBoxSpotlight(f: Fragment) {
        val binding = wordBoxBinding ?: return
        val container = this.binding?.root as? ViewGroup ?: return

        container.doOnPreDraw {
            wordBoxSpotlight = Spotlight.Builder(f.requireActivity())
                .setTargets(createWordBoxTargets(binding, f))
                .setBackgroundColor(
                    f.resources.getColor(
                        can.lucky.of.core.R.color.spotlight_background,
                        null
                    )
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
                    }
                })
                .build()

            wordBoxSpotlight?.start()
        }

    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment): Boolean {
        if (super.onFragmentViewDestroyed(fm, f).not()) return false

        wordBoxBinding = null
        wordBoxSpotlight?.finish()
        wordBoxSpotlight = null

        return true
    }
}