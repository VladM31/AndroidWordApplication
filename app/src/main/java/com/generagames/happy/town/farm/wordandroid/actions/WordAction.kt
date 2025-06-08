package com.generagames.happy.town.farm.wordandroid.actions

import can.lucky.of.core.domain.models.data.words.UserWordDetails
import can.lucky.of.core.domain.models.data.words.Word

sealed interface WordAction {
    data class Init(val word: Word, val details: UserWordDetails? = null) : WordAction
    data object Delete : WordAction
}