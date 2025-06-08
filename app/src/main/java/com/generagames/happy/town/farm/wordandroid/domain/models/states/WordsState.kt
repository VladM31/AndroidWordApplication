package com.generagames.happy.town.farm.wordandroid.domain.models.states

import androidx.paging.PagingData
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.filters.WordFilter
import kotlinx.coroutines.flow.Flow

data class WordsState(
    val selectedWords: Map<String,Int> = emptyMap(),
    val filter: WordFilter,
    val words: Flow<PagingData<Word>>,
)