package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import can.lucky.of.core.domain.managers.playlist.PlayListManager
import can.lucky.of.core.domain.models.data.playlists.PlayListCount
import can.lucky.of.core.domain.models.filters.PlayListCountFilter
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.PlayListChooserDialogAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PlayListChooserState
import com.generagames.happy.town.farm.wordandroid.ui.sources.PlayListCountLoader
import com.generagames.happy.town.farm.wordandroid.ui.sources.PlayListCountPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayListChooserDialogVm(
    private val playListManager: PlayListManager
) : ViewModel(), MviViewModel<PlayListChooserState, PlayListChooserDialogAction> {


    private val mutableState = MutableStateFlow(
        PlayListChooserState(
            pagerPlayList = getPagePlayList()
        )
    )
    override val state: StateFlow<PlayListChooserState> = mutableState

    override fun sent(action: PlayListChooserDialogAction) {
        when(action) {
            is PlayListChooserDialogAction.SelectPlayList -> {
                mutableState.value = mutableState.value.copy(
                    selectedPlayListId = action.playListId,
                    previousPosition = action.position
                )
            }
        }
    }

    private fun getPagePlayList(): Pager<Long, PlayListCount> {
        val loafer: PlayListCountLoader = { page, pageSize ->
            playListManager.countBy(
                PlayListCountFilter(
                    page = page,
                    size = pageSize
                )
            ).content
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PlayListCountPagingSource(loafer, pageSize = PAGE_SIZE) }
        )
    }

    companion object{
        private const val PAGE_SIZE = 10
    }
}