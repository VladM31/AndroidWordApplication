package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.domain.models.data.words.UserWordDetails
import can.lucky.of.core.domain.models.data.words.Word

data class WordState(
    val word: Word? = null,
    val userWordDetails: UserWordDetails? = null,
    val isDeleted: Boolean = false
)
