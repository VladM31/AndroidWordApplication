package can.lucky.of.addword.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.addword.domain.actions.ChooseAddWordAction
import can.lucky.of.addword.domain.models.states.ChooseAddWordState
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.vms.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ChooseAddWordVm(
    private val subscribeCacheManager: SubscribeCacheManager
) : ViewModel(), MviViewModel<ChooseAddWordState, ChooseAddWordAction> {


    private val mutableState = MutableStateFlow(ChooseAddWordState())
    override val state: StateFlow<ChooseAddWordState>
        get() = mutableState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mutableState.value =
                mutableState.value.copy(isSubscribed = subscribeCacheManager.isActiveSubscribe())
        }
    }


    override fun sent(action: ChooseAddWordAction) {

    }


}