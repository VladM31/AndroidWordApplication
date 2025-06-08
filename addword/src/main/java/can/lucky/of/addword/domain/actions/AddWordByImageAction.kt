package can.lucky.of.addword.domain.actions

import android.net.Uri
import can.lucky.of.core.domain.models.enums.Language

internal sealed interface AddWordByImageAction {
    data class SetLanguage(val language: Language) : AddWordByImageAction
    data class SetTranslateLanguage(val language: Language) : AddWordByImageAction
    data class SetImage(val image: Uri?) : AddWordByImageAction
    data object Confirm : AddWordByImageAction
}