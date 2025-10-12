package can.lucky.of.addword.ui.listeners.spotlights

import android.view.View
import androidx.fragment.app.Fragment
import can.lucky.of.addword.databinding.FragmentAnalyzeWordTasksBinding
import can.lucky.of.addword.ui.fragments.RecognizeWordTasksFragment
import can.lucky.of.core.domain.managers.spotlight.SpotlightManager
import can.lucky.of.core.ui.listeners.spotlights.AbstractSpotlightsFragmentListener
import can.lucky.of.core.ui.utils.buildRectangleTarget
import com.takusemba.spotlight.Target

class RecognizeWordTasksFragmentListener(
    spotlightManager: SpotlightManager
) : AbstractSpotlightsFragmentListener<FragmentAnalyzeWordTasksBinding>(
    spotlightManager,
    RecognizeWordTasksFragment::class
) {
    override fun getViewBinding(v: View) = FragmentAnalyzeWordTasksBinding.bind(v)

    override fun createTargets(
        newBinding: FragmentAnalyzeWordTasksBinding,
        fragment: Fragment
    ): List<Target> {
        return listOf(
            fragment.buildRectangleTarget(
                newBinding.root,
                "Recognition Tasks Screen",
                "This screen displays a list of words with statuses: \"In queue,\" \"In progress,\" and \"Recognized\". Completed words can be viewed by pressing the \"Open\" button, then edited and saved to \"My words.\"",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.swipeRefreshLayout,
                "Task list",
                "All words sent for recognition are displayed here, indicating their current status.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.swipeRefreshLayout,
                "Refresh list",
                "Pull the screen down to update the statuses of all recognition tasks.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.toolBar.additionalButton,
                "Add Task Button",
                "Pressing this button opens the task creation screen, where you can add a new word for recognition.",
                onClick = { spotlight?.finish() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            )
        )
    }
}