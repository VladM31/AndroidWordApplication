package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.UserWordSortBy

data class UserWordFilterState(
    val original: String? = null,
    val translate: String? = null,
    val lang: Language? = null,
    val translateLang: Language? = null,
    val categories: List<String>? = null,
    val asc: Boolean = true,
    val sortBy: UserWordSortBy = UserWordSortBy.ORIGIN,
    val isInit: Boolean = false,
    val cefr: CEFR? = null
)
