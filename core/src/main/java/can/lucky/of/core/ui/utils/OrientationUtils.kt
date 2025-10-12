package can.lucky.of.core.ui.utils

import android.content.res.Configuration
import android.widget.GridLayout

fun GridLayout.setColumnCountByOrientation(
    portraitCount: Int,
    landscapeCount: Int
) {
    val orientation = resources.configuration.orientation
    columnCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        landscapeCount
    } else {
        portraitCount
    }
}