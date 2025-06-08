package com.generagames.happy.town.farm.wordandroid.actions

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.WordSortBy
import can.lucky.of.core.domain.models.filters.WordFilter

sealed interface WordFilterAction{
    data class SetOriginalLang(val value: Language?) : WordFilterAction
    data class SetTranslateLang(val value: Language?) : WordFilterAction
    data class SetOriginal(val value: String?) : WordFilterAction
    data class SetTranslate(val value: String?) : WordFilterAction
    data class SetCategories(val value: List<String>?) : WordFilterAction
    data class SetAsc(val value: Boolean) : WordFilterAction
    data class SetSortBy(val value: WordSortBy?) : WordFilterAction
    data class Init(val value: WordFilter) : WordFilterAction
    data class SetCefr(val value: CEFR?) : WordFilterAction
}