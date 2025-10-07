package com.generagames.happy.town.farm.wordandroid.domain.vms.infos

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.infos.InstructionAction
import com.generagames.happy.town.farm.wordandroid.domain.managers.pdf.InstructionManager
import com.generagames.happy.town.farm.wordandroid.domain.models.states.infos.InstructionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class InstructionViewModel(
    private val instructionManager: InstructionManager
) : AbstractMviViewModel<InstructionState, InstructionAction>() {
    private val mutableState = MutableStateFlow(InstructionState())
    override val state = mutableState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = instructionManager.getInstructionFile()
                mutableState.value = state.value.copy(
                    file = file
                )
            } catch (e: Exception) {
                mutableState.value = state.value.copy(
                    errorMessage = ErrorMessage(message = e.message ?: "Unknown Error")
                )
            }
        }
    }

    override fun sent(action: InstructionAction) {
        when (action) {
            is InstructionAction.End -> mutableState.value = state.value.copy(
                isEnd = true
            )
        }

    }
}