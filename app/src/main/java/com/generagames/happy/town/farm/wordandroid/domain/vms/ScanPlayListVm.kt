package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.ScanPlayListAction
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.SharePlayListManager
import com.generagames.happy.town.farm.wordandroid.domain.models.states.ScanPlayListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanPlayListVm(
    private val sharePlayListManager: SharePlayListManager
) : AbstractMviViewModel<ScanPlayListState,ScanPlayListAction>() {

    private val mutableState = MutableStateFlow(ScanPlayListState())
    override val state: StateFlow<ScanPlayListState> = mutableState
    override fun sent(action: ScanPlayListAction) {
        when (action) {
            is ScanPlayListAction.SetId -> handleSetId(action)
            is ScanPlayListAction.Accept -> handleAccept()
            is ScanPlayListAction.SetError -> handleSetError(action)
        }
    }

    private fun handleSetError(action: ScanPlayListAction.SetError) {
        mutableState.value = mutableState.value.copy(errorMessage = ErrorMessage(action.error))
    }

    private fun handleAccept() {
        if (mutableState.value.run { playList == null || shareId.isBlank() }) return

        viewModelScope.launch(Dispatchers.IO){
            try {
                sharePlayListManager.acceptSharedPlayList(mutableState.value.shareId)
                mutableState.value = mutableState.value.copy(isEnd = true)
            }catch (e: Exception){
                mutableState.value = mutableState.value.copy(errorMessage = ErrorMessage(e.message.orEmpty()))
            }
        }
    }

    private fun handleSetId(action: ScanPlayListAction.SetId) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = sharePlayListManager.getSharedPlayList(action.id)
                mutableState.value = mutableState.value.copy(
                    playList = result,
                    shareId = action.id
                )
            }catch (e: Exception){
                mutableState.value = mutableState.value.copy(errorMessage = ErrorMessage(e.message.orEmpty()))
            }
        }
    }


}