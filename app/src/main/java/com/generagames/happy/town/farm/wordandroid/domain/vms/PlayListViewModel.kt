package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import can.lucky.of.core.domain.models.filters.PageFilter
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.PlayListAction
import can.lucky.of.core.domain.managers.playlist.PlayListManager
import can.lucky.of.core.domain.models.data.playlists.PlayListCount
import can.lucky.of.core.domain.models.filters.PlayListCountFilter
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PlayListState
import com.generagames.happy.town.farm.wordandroid.ui.sources.PlayListCountLoader
import com.generagames.happy.town.farm.wordandroid.ui.sources.PlayListCountPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayListViewModel(
    private val playListManager: PlayListManager
) : ViewModel(), MviViewModel<PlayListState, PlayListAction> {

    private val mutableState = MutableStateFlow(PlayListState(
        pager = getPageWords(PlayListCountFilter())
    ))

    override val state: StateFlow<PlayListState> = mutableState

    override fun sent(action: PlayListAction) {
        when(action){
            is PlayListAction.ReFetch -> handleReFetch()
            is PlayListAction.UpdateFilter -> handleUpdateFilter(action)
        }
    }

    private fun handleReFetch() {
        mutableState.value = mutableState.value.copy(
            pager = getPageWords(mutableState.value.filter)
        )
    }

    private fun handleUpdateFilter(action: PlayListAction.UpdateFilter) {
        mutableState.value = mutableState.value.copy(
            filter = action.filter,
            pager = getPageWords(action.filter)
        )
    }

    private fun getPageWords(filter: PlayListCountFilter): Pager<Long, PlayListCount> {
        val loafer: PlayListCountLoader = { page, pageSize ->
            playListManager.countBy(
                filter.copy(
                    pagination = filter.pagination?.copy(
                        page.toLong(), pageSize.toLong()
                    ) ?: PageFilter(page.toLong(), pageSize.toLong())
                )
            )
        }

        return Pager(
            config = PagingConfig(
                pageSize = SIZE,
                initialLoadSize = SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PlayListCountPagingSource(loafer, SIZE) }
        )
    }

    companion object{
        const val SIZE = 20
    }


}