package com.generagames.happy.town.farm.wordandroid.actions

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.UserWordFilterBundle

sealed interface UserWordFilterAction {
    data class Init(val bundle: UserWordFilterBundle) : UserWordFilterAction
    data class SetOriginal(val original: String) : UserWordFilterAction
    data class SetTranslate(val translate: String) : UserWordFilterAction
    data class SetLang(val lang: Language) : UserWordFilterAction
    data class SetTranslateLang(val lang: Language) : UserWordFilterAction
    data class SetCategories(val categories: List<String>) : UserWordFilterAction
    data class SetSortBy(val sortBy: String) : UserWordFilterAction
    data class SetAsc(val asc: Boolean) : UserWordFilterAction
    data class SetCefr(val cefr: CEFR?) : UserWordFilterAction
}