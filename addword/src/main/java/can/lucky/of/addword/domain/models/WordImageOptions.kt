package can.lucky.of.addword.domain.models

import android.net.Uri
import can.lucky.of.core.domain.models.enums.Language

internal data class WordImageOptions(
    val imageUri: Uri,
    val wordLang: Language,
    val translateLang: Language
)
