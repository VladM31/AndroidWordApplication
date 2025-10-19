package com.generagames.happy.town.farm.wordandroid.domain.vms

import can.lucky.of.core.domain.vms.AbstractMviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.PlayListFilterAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PlayListFilterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayListFilterVm : AbstractMviViewModel<PlayListFilterState, PlayListFilterAction>() {

    private val mutableState = MutableStateFlow(PlayListFilterState())
    override val state: StateFlow<PlayListFilterState> = mutableState

    override fun sent(action: PlayListFilterAction) {
        when(action){
            is PlayListFilterAction.Init -> handleInit(action)
            is PlayListFilterAction.ChangeStartCount ->  handleStartCount(action)
            is PlayListFilterAction.ChangeEndCount -> handleEndCount(action)
            is PlayListFilterAction.ChangeName -> handleChangeName(action)
            is PlayListFilterAction.Find -> handleFind()
        }
    }

    private fun handleFind() {
        mutableState.value = mutableState.value.copy(
            isEnd = true
        )
    }

    private fun handleChangeName(action: PlayListFilterAction.ChangeName) {
        mutableState.value = mutableState.value.copy(
            name = action.name
        )
    }

    private fun handleEndCount(action: PlayListFilterAction.ChangeEndCount) {
        mutableState.value = mutableState.value.copy(
            endCount = action.endCount
        )
    }

    private fun handleStartCount(action: PlayListFilterAction.ChangeStartCount) {
        mutableState.value = mutableState.value.copy(
            startCount = action.startCount
        )
    }

    private fun handleInit(action: PlayListFilterAction.Init) {
        if (mutableState.value.isInited) return

        mutableState.value = mutableState.value.copy(
            startCount = action.bundle.startCount?.toString().orEmpty(),
            endCount = action.bundle.endCount?.toString().orEmpty(),
            name = action.bundle.name.orEmpty(),
            isInited = true
        )
    }
}