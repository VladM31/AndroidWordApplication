package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.playlist.PlayListManager
import can.lucky.of.core.domain.models.data.playlists.SavePlayList
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.CreatePlayListAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.CreatePlayListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreatePlayListViewModel(
    private val playListManager: PlayListManager
) : ViewModel(), MviViewModel<CreatePlayListState, CreatePlayListAction> {


    private val mutableState = MutableStateFlow(CreatePlayListState())
    override val state: StateFlow<CreatePlayListState>
        get() = mutableState

    override fun sent(action: CreatePlayListAction) {
        when(action){
            is CreatePlayListAction.ClearError -> {
                clearError()
            }
            is CreatePlayListAction.Create -> {
                create(action.name)
            }
        }
    }

    private fun create(name: String) {
        findError(name)?.apply {
            mutableState.value = CreatePlayListState(
                messageError = this
            )
            return@create
        }

        viewModelScope.launch {
            val result = playListManager.save(listOf(
                SavePlayList(
                    name
                )
            )
            ).isSuccess

            mutableState.value = CreatePlayListState(
                saveResult = result,
                dismiss = true,
                messageError = null
            )
        }
    }

    private fun findError(name:String) : String? {
        if (name.isEmpty()) return "Name is empty"
        if (name.isBlank()) return "Name is blank"
        if (name.length > 255) return "Name is too long"

        return null
    }

    private fun clearError() {
        mutableState.value = CreatePlayListState()
    }

}