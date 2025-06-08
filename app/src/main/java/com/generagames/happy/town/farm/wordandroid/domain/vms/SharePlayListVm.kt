package com.generagames.happy.town.farm.wordandroid.domain.vms

import android.util.Log
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.SharePlayListAction
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.SharePlayListManager
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SharePlayListOption
import com.generagames.happy.town.farm.wordandroid.domain.models.states.SharePlayListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SharePlayListVm(
    private val sharePlayListManager: SharePlayListManager
) : AbstractMviViewModel<SharePlayListState, SharePlayListAction>() {

    private val mutableState = MutableStateFlow(SharePlayListState())
    override val state: MutableStateFlow<SharePlayListState> = mutableState

    override fun sent(action: SharePlayListAction) {
        when (action) {
            is SharePlayListAction.Init -> handleInit(action)
        }
    }

    private fun handleInit(action: SharePlayListAction.Init){
        viewModelScope.launch(Dispatchers.IO){
            val option = SharePlayListOption(
                playListId = action.playListId,
                width = action.width,
                height = action.height
            )

            try {
                val result = sharePlayListManager.sharePlayList(option)
                mutableState.value = mutableState.value.copy(
                    qrCode = result.qrCode,
                    isInit = true,
                    playListId = action.playListId,
                    time = result.lifeTimeInSeconds - 20
                )
            }catch (e: Exception){
                Log.e("SharePlayListVm", e.toString())
                mutableState.value = state.value.copy(isEnd = true)
            }

            repeat(state.value.time){
                delay(1000)
                handleMinusSecond()
            }
        }
    }

    private fun handleMinusSecond(){
        val newTime = mutableState.value.time - 1
        mutableState.value = mutableState.value.copy(time = newTime)
        if (newTime == 0){
            mutableState.value = mutableState.value.copy(isEnd = true)
        }
    }
}