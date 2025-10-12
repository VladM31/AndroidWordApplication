package can.lucky.of.core.domain.vms.infos

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.actions.infos.InstructionAction
import can.lucky.of.core.domain.managers.infos.InstructionManager
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.infos.InstructionState
import can.lucky.of.core.domain.vms.AbstractMviViewModel
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