package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.ShareUserWordAction
import can.lucky.of.addword.domain.managers.userwords.ShareUserWordManager
import can.lucky.of.addword.domain.models.ShareWordOptionals
import com.generagames.happy.town.farm.wordandroid.domain.models.states.ShareUserWordState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShareUserWordVm(
    private val shareUserWordManager: ShareUserWordManager
) : AbstractMviViewModel<ShareUserWordState, ShareUserWordAction>() {

    private val mutableState = MutableStateFlow(ShareUserWordState())
    override val state: StateFlow<ShareUserWordState> = mutableState

    override fun sent(action: ShareUserWordAction) {
        when (action) {
            is ShareUserWordAction.Init -> handleInit(action)
        }
    }

    private fun handleInit(action: ShareUserWordAction.Init){
        viewModelScope.launch(Dispatchers.IO){
            val  optionals = ShareWordOptionals(
                userWordId = action.userWordId,
                width = action.width,
                height = action.height
            )


            try {
                val result = shareUserWordManager.share(optionals)

                mutableState.value = mutableState.value.copy(
                    qrCode = result.qrCode,
                    isInit = true,
                    userWordId = action.userWordId,
                    time = result.liveTimeInSeconds
                )
            }catch (e: Exception){
                println(e)
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

        viewModelScope.launch(Dispatchers.IO){
            mutableState.value = mutableState.value.copy(
                time = newTime,
                isEnd = newTime == 0
            )
        }
    }
}