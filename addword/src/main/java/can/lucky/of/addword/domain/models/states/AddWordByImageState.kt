package can.lucky.of.addword.domain.models.states

import android.net.Uri
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.results.ConfirmBox

internal data class AddWordByImageState(
    val language: Language = Language.UKRAINIAN,
    val translateLanguage: Language = Language.ENGLISH,
    val error: ErrorMessage? = null,
    val image: Uri? = null,
    val word: Word? = null,
    val isLoading: Boolean = false,
)
