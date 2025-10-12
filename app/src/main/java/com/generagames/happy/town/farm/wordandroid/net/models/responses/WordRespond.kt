package com.generagames.happy.town.farm.wordandroid.net.models.responses

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.WordType

data class WordRespond(
    val id: String,
    val original: String,
    val translate: String,
    val lang: Language,
    val translateLang: Language,
    val cefr: CEFR,
    val type: WordType,
    val description: String? = null,
    val category: String? = null,
    val soundLink: String? = null,
    val imageLink: String? = null
)
