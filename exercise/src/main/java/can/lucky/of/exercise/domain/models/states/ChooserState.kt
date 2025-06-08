package can.lucky.of.exercise.domain.models.states

import android.net.Uri

internal data class ChooserState(
    val file: Uri? = null,
    val isSet: Boolean = false,
    val isSusses: Boolean = false
)
