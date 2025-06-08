package can.lucky.of.history.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState
import can.lucky.of.history.domain.models.enums.PlanFragmentType
import can.lucky.of.validation.models.DifferentLanguageable

internal data class ChangeLearningPlanState(
    override val isEnd: Boolean = false,
    override val errorMessage: ErrorMessage? = null,
    val isInit: Boolean = false,
    val type: PlanFragmentType = PlanFragmentType.UNDEFINED,
    val wordsPerDay: Int = 1,
    val nativeLang: Language = Language.UNDEFINED,
    val learningLang: Language = Language.UNDEFINED,
    val cefr: CEFR = CEFR.A1
) : EndetableState, ErrorableState, DifferentLanguageable {
    override fun getLanguage(): String = nativeLang.name
    override fun getSecondLanguage(): String = learningLang.name
}
