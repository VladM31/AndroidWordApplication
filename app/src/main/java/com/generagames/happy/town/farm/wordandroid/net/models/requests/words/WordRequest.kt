package com.generagames.happy.town.farm.wordandroid.net.models.requests.words

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language

data class WordRequest(
    val original: String,
    val lang: Language,

    val translate: String,
    val translateLang: Language,

    val category: String?,

    val description: String?,
    val cefr: CEFR,
)