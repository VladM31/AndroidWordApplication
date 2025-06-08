package can.lucky.of.addword.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState
import java.io.File

internal data class DefaultAddWordState(
    val word: String = "",
    val language: Language = Language.ENGLISH,
    val translationLanguage: Language = Language.ENGLISH,
    val translation: String = "",
    val category: String = "",
    val description: String = "",
    val cefr: CEFR = CEFR.A1,
    var image: File? = null,
    var sound: File? = null,
    val needSound: Boolean = false,
    val isSubscribe: Boolean? = null,
    var isLoading: Boolean = false,
    override val errorMessage: ErrorMessage? = null,
    override val isEnd: Boolean = false,
    val isInited: Boolean = false
) : EndetableState, ErrorableState
