package can.lucky.of.addword.domain.actions

import can.lucky.of.core.domain.models.enums.Language

internal sealed interface AddWordByTextAction {
    data class SetOriginal(val original: String) : AddWordByTextAction
    data class SetTranslate(val translate: String) : AddWordByTextAction
    data class SetLanguage(val language: Language) : AddWordByTextAction
    data class SetTranslateLanguage(val language: Language) : AddWordByTextAction
    data object Recognize : AddWordByTextAction
}