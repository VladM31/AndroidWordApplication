package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.domain.models.data.words.PinnedWord
import can.lucky.of.core.domain.models.states.EndetableState

data class PlayListDetailsState(
    val name: String,
    val id: String,
    val selectedWords: Map<String, Int> = emptyMap(),
    val words: List<PinnedWord> = emptyList(),
    val transactionId: String? = null,
    override val isEnd: Boolean = false,
) : EndetableState
