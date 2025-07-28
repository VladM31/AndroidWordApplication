package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.word.WordManager
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.filters.PageFilter
import can.lucky.of.core.domain.models.filters.WordFilter
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.WordsAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.WordsState
import com.generagames.happy.town.farm.wordandroid.ui.sources.WordsPageLoader
import com.generagames.happy.town.farm.wordandroid.ui.sources.WordsPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class WordsViewModel(
    private val wordManager: WordManager,
    private val userCacheManager: UserCacheManager
) : ViewModel(), MviViewModel<WordsState, WordsAction> {
    private val mutableFilter: MutableStateFlow<WordFilter> = MutableStateFlow(WordFilter(
        userId = WordFilter.UserId(id = userCacheManager.user.id, isIn = false)
    ))

    private val mutableState : MutableStateFlow<WordsState>

    init {
        val wordsFlow = mutableFilter
            .flatMapLatest { filter -> getPageWords(filter) }
            .cachedIn(viewModelScope)


        mutableState = MutableStateFlow(
            WordsState(
                filter = mutableFilter.value,
                words = wordsFlow,
            )
        )
    }

    override val state : StateFlow<WordsState> = mutableState

    override fun sent(action: WordsAction) {
        if (action is WordsAction.UpdateFilter) {
            mutableFilter.value = action.filter.copy(
                userId = WordFilter.UserId(id = userCacheManager.user.id, isIn = false),
            )
            mutableState.apply {
                value = value.copy(
                    filter = action.filter.copy(
                        userId = WordFilter.UserId(id = userCacheManager.user.id, isIn = false)
                    ),
                    selectedWords = action.selectedWords
                )
            }
            return
        }

        val set = mutableState.value.selectedWords.toMutableMap()

        when(action){
            is WordsAction.AddWord -> set[action.wordId] = action.position
            is WordsAction.RemoveWord -> set.remove(action.wordId)
            is WordsAction.Clear -> set.clear()
            else -> return
        }
        if (set.size != mutableState.value.selectedWords.size) {
            mutableState.apply {
                value = value.copy(
                    selectedWords = set
                )
            }
        }
    }

    private fun getPageWords(filter: WordFilter) : Flow<PagingData<Word>>{
        val loafer: WordsPageLoader = { page, pageSize ->
            wordManager.findBy(filter.copy(pagination = filter.pagination?.copy(
                    page.toLong(), pageSize.toLong()
            ) ?: PageFilter(page.toLong(), pageSize.toLong())
            ))
        }

        return Pager(
            config = PagingConfig(
                pageSize = WORDS_SIZE,
                initialLoadSize = WORDS_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { WordsPagingSource( loafer) }
        ).flow
    }

    private fun deleteWordFromList(){

    }

    companion object{
        private const val WORDS_SIZE = 15
    }
}