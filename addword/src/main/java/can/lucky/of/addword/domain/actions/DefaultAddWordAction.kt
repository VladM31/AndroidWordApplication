package can.lucky.of.addword.domain.actions

import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import java.io.File

internal sealed interface DefaultAddWordAction {
    data object Add : DefaultAddWordAction

    data class ChangeLanguage(val language: Language) : DefaultAddWordAction
    data class ChangeTranslationLanguage(val language: Language) : DefaultAddWordAction

    data class SetWord(val word: String) : DefaultAddWordAction
    data class SetTranslation(val translation: String) : DefaultAddWordAction
    data class SetDescription(val description: String) : DefaultAddWordAction
    data class SetCategory(val category: String) : DefaultAddWordAction

    data class SetCefr(val cefr: CEFR) : DefaultAddWordAction

    data class SetNeedSound(val needSound: Boolean) : DefaultAddWordAction

    data class SetImage(val image: File?) : DefaultAddWordAction
    data class SetSound(val sound: File?) : DefaultAddWordAction

    data class Init(val word: Word?) : DefaultAddWordAction
}