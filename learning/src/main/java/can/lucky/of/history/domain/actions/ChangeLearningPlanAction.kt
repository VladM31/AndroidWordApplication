package can.lucky.of.history.domain.actions

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.history.domain.models.bundles.LearningPlanBundle

internal interface ChangeLearningPlanAction {
    data class Init(val bundle: LearningPlanBundle) : ChangeLearningPlanAction
    data class ChangeWordsPerDay(val wordsPerDay: Int) : ChangeLearningPlanAction
    data object PlusOneWordsPerDay : ChangeLearningPlanAction
    data object MinusOneWordsPerDay : ChangeLearningPlanAction
    data class ChangeNativeLang(val lang: String) : ChangeLearningPlanAction
    data class ChangeLearningLang(val lang: String) : ChangeLearningPlanAction
    data class ChangeCefr(val cefr: CEFR) : ChangeLearningPlanAction
    data object Submit : ChangeLearningPlanAction
}