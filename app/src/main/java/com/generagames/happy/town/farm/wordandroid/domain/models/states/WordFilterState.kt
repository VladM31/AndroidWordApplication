package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.WordSortBy

data class WordFilterState(
    val originalLang: Language? = null,
    val translateLang: Language? = null,
    val original: String? = null,
    val translate: String? = null,
    val categories: List<String>? = null,
    val asc: Boolean = false,
    val sortBy: WordSortBy? = null,
    val isInit: Boolean = false,
    val cefrs: Collection<CEFR>? = null,
)