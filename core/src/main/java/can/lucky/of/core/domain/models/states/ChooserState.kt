package can.lucky.of.core.domain.models.states

import android.net.Uri

data class ChooserState(
    val file: Uri? = null,
    val isSet: Boolean = false,
    val isSusses: Boolean = false
)
