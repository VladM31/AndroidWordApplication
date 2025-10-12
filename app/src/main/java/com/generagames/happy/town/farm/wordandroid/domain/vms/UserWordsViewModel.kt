package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import can.lucky.of.core.domain.managers.userwords.UserWordManager
import can.lucky.of.core.domain.models.data.playlists.PinPlayList
import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.core.domain.models.filters.UserWordFilter
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.UserWordsAction
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.PinPlayListManager
import com.generagames.happy.town.farm.wordandroid.domain.models.states.UserWordsState
import com.generagames.happy.town.farm.wordandroid.ui.sources.UserWordPagingSource
import com.generagames.happy.town.farm.wordandroid.ui.sources.UserWordsPageLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserWordsViewModel(
    private val userWordManager: UserWordManager,
    private val pinPlayListManager: PinPlayListManager
) : ViewModel(), MviViewModel<UserWordsState, UserWordsAction> {

    private val mutableState: MutableStateFlow<UserWordsState> = MutableStateFlow(
        UserWordsState(
            filter = UserWordFilter(),
            pager = getPageWords(UserWordFilter())
        )
    )

    override val state: StateFlow<UserWordsState> = mutableState

    override fun sent(action: UserWordsAction) {
        when (action) {
            is UserWordsAction.SelectWord -> handleSelectWord(action)
            is UserWordsAction.UnSelectWord -> handleUnSelectWord(action)
            is UserWordsAction.ChangeFilter -> handleChangeFilter(action)
            is UserWordsAction.Clear -> handleClear()
            is UserWordsAction.PinWords -> pinWords(action.playListId)
            is UserWordsAction.ReFetch -> handleReFetch()
        }
    }

    private fun handleClear() {
        mutableState.value = mutableState.value.copy(
            selectedWords = emptyMap()
        )
    }

    private fun handleUnSelectWord(action: UserWordsAction.UnSelectWord) {
        mutableState.value = mutableState.value.copy(
            selectedWords = mutableState.value.selectedWords - action.wordId
        )
    }

    private fun handleSelectWord(action: UserWordsAction.SelectWord) {
        mutableState.value = mutableState.value.copy(
            selectedWords = mutableState.value.selectedWords + (action.wordId to action.position)
        )
    }

    private fun handleChangeFilter(action: UserWordsAction.ChangeFilter) {
        mutableState.value = mutableState.value.copy(
            filter = action.filter,
            pager = getPageWords(action.filter),
            selectedWords = emptyMap()
        )
    }

    private fun pinWords(playListId: String) {
        if (state.value.selectedWords.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val pins = state.value.selectedWords.keys
                .map {
                    PinPlayList(
                        playListId = playListId,
                        wordId = it
                    )
                }

            pinPlayListManager.pin(pins)

            mutableState.value = state.value.copy(
                selectedWords = emptyMap(),
                openPlayList = UserWordsState.OpenPlayList(playListId)
            )

            mutableState.value = state.value.copy(
                openPlayList = null
            )
        }
    }

    private fun getPageWords(filter: UserWordFilter): Pager<Long, UserWord> {
        val loafer: UserWordsPageLoader = { page, pageSize ->
            val search = filter.copy(
                page = page,
                size = pageSize
            )

            try {
                val result = userWordManager.findBy(search)
                mutableState.value = mutableState.value.copy(
                    count = result.page.totalElements
                )
                result.content
            } catch (e: Exception) {
                emptyList()
            }
        }

        return Pager(
            config = PagingConfig(
                pageSize = WORDS_SIZE,
                initialLoadSize = WORDS_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserWordPagingSource(loafer, pageSize = WORDS_SIZE) }
        )
    }

    private fun handleReFetch() {
        mutableState.value = mutableState.value.copy(
            pager = getPageWords(mutableState.value.filter),
            count = state.value.count - 1
        )
    }


    companion object {
        private const val WORDS_SIZE = 20
    }


}