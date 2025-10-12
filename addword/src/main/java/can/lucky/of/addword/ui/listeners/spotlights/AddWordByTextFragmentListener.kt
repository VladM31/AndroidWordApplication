package can.lucky.of.addword.ui.listeners.spotlights

import android.view.View
import androidx.fragment.app.Fragment
import can.lucky.of.addword.databinding.FragmentAddWordByTextBinding
import can.lucky.of.addword.ui.fragments.AddWordByTextFragment
import can.lucky.of.core.domain.managers.spotlight.SpotlightManager
import can.lucky.of.core.ui.listeners.spotlights.AbstractSpotlightsFragmentListener
import can.lucky.of.core.ui.utils.buildRectangleTarget

class AddWordByTextFragmentListener(
    spotlightManager: SpotlightManager
) : AbstractSpotlightsFragmentListener<FragmentAddWordByTextBinding>(
    spotlightManager,
    AddWordByTextFragment::class
) {

    override fun getViewBinding(v: View) = FragmentAddWordByTextBinding.bind(v)

    override fun createTargets(
        newBinding: FragmentAddWordByTextBinding,
        fragment: Fragment
    ): List<com.takusemba.spotlight.Target> {


        return listOf(
            fragment.buildRectangleTarget(
                newBinding.textLayoutWord,
                "Enter a word",
                "Start here. Type the word you want to add. This field is optional if you want to find the original word knowing only its translation—just fill in the field below.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.spinnerWordLang,
                "Original language",
                "Specify the language of the word you entered.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.textLayoutTranslate,
                "Enter the translation",
                "Provide the translation. Don't worry if you don't know it—leave this field blank, and the app will find the correct translation for the word you entered above.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.spinnerTranLang,
                "Translation language",
                "Choose the language to translate the word into.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.menuGridLayout,
                "How it works",
                "You can fill in just one field (either the word or the translation), and the app will find the missing part. Or fill in both to check.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.addWordBtn,
                "Recognize word",
                "Tap here for the AI to process the word, find its translation and definition",
                onClick = { spotlight?.finish() }
            ),
        )
    }
}