package can.lucky.of.addword.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState

internal data class AddWordByTextState(
    val original: String = "",
    val translate: String = "",
    val language: Language = Language.ENGLISH,
    val translateLang: Language = Language.ENGLISH,
    val isLoading: Boolean = false,
    override val isEnd: Boolean = false,
    override val errorMessage: ErrorMessage? = null
) : ErrorableState, EndetableState