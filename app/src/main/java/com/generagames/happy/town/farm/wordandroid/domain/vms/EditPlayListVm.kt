package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.validation.Validator
import can.lucky.of.validation.schemes.ValidationScheme
import can.lucky.of.validation.schemes.length
import can.lucky.of.validation.schemes.notBlank
import com.generagames.happy.town.farm.wordandroid.actions.EditPlayListAction
import can.lucky.of.core.domain.managers.playlist.PlayListManager
import can.lucky.of.core.domain.models.data.playlists.UpdatePlayList
import com.generagames.happy.town.farm.wordandroid.domain.models.states.EditPlayListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditPlayListVm(
    private val playListManager: PlayListManager
) : AbstractMviViewModel<EditPlayListState, EditPlayListAction>() {

    private val mutableState = MutableStateFlow(EditPlayListState())
    override val state: StateFlow<EditPlayListState> = mutableState
    private val validator = Validator(state)

    init {
        validator.add(EditPlayListState::name, ValidationScheme.stringSchema("Name")
            .length(2,255)
            .notBlank())
    }

    override fun sent(action: EditPlayListAction) {
        when(action){
            is EditPlayListAction.Init -> handleInit(action)
            is EditPlayListAction.NameChanged -> handleNameChanged(action)
            is EditPlayListAction.Submit -> handleSubmit()
        }
    }

    private fun handleInit(action: EditPlayListAction.Init){
        if (state.value.isInited) return

        mutableState.value = state.value.copy(
            isInited = true,
            isSubmitEnabled = true,
            playListId = action.playListId,
            name = action.name,
        )
    }

    private fun handleNameChanged(action: EditPlayListAction.NameChanged){
        mutableState.value = state.value.copy(
            name = action.name
        )
    }

    private fun handleSubmit(){
        val message = validator.validate()

        if (message.isNotBlank()){
            mutableState.value = state.value.copy(errorMessage = ErrorMessage(message))
            return
        }

        mutableState.value = state.value.copy(
            isSubmitEnabled = false
        )

        viewModelScope.launch {
           try {
               playListManager.update(state.toUpdatePlayList())
                mutableState.value = state.value.copy(
                     isEnd = true
                )
           }catch (e: Exception){
               mutableState.value = state.value.copy(
                   isSubmitEnabled = true,
                   errorMessage = ErrorMessage(e.message ?: "Unknown error")
               )
           }
        }
    }

    private fun StateFlow<EditPlayListState>.toUpdatePlayList() :  List<UpdatePlayList> {
        return listOf(
            UpdatePlayList(
            id = value.playListId,
            name = value.name
        )
        )
    }
}