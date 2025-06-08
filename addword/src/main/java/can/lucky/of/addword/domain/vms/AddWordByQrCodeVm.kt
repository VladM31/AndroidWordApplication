package can.lucky.of.addword.domain.vms

import androidx.lifecycle.viewModelScope
import can.lucky.of.addword.domain.actions.AddWordByQrCodeAction
import can.lucky.of.addword.domain.managers.userwords.ShareUserWordManager
import can.lucky.of.addword.domain.models.states.AddWordByQrCodeState
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddWordByQrCodeVm(
    private val shareUserWordManager: ShareUserWordManager
) : AbstractMviViewModel<AddWordByQrCodeState, AddWordByQrCodeAction>() {

    private val mutableState = MutableStateFlow(AddWordByQrCodeState())
    override val state : StateFlow<AddWordByQrCodeState> = mutableState

    override fun sent(action: AddWordByQrCodeAction) {
        when(action) {
            is AddWordByQrCodeAction.Init -> {
                mutableState.value = AddWordByQrCodeState(isInit = true)
            }
            is AddWordByQrCodeAction.ErrorMessage -> {
                mutableState.value = AddWordByQrCodeState(errorMessage = ErrorMessage(action.message))
            }
            is AddWordByQrCodeAction.Accept -> handleAccept()
            is AddWordByQrCodeAction.FetchWord -> handleFetchWord(action)
        }
    }

    private fun handleFetchWord(action: AddWordByQrCodeAction.FetchWord){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val word = shareUserWordManager.getShareWord(action.shareId)
                mutableState.value = AddWordByQrCodeState(word = word, shareId = action.shareId)
            } catch (e: Exception){
                mutableState.value = AddWordByQrCodeState(errorMessage = ErrorMessage(e.message ?: "Unknown error"))
            }
        }
    }

    private fun handleAccept(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                shareUserWordManager.acceptShare(state.value.shareId.orEmpty())
                mutableState.value = AddWordByQrCodeState(isEnd = true)
            } catch (e: Exception){
                mutableState.value = AddWordByQrCodeState(errorMessage = ErrorMessage(e.message ?: "Unknown error"))
            }
        }
    }
}