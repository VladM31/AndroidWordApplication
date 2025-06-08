package can.lucky.of.auth.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.auth.domain.managers.AuthManager
import can.lucky.of.auth.domain.models.data.AuthResult
import can.lucky.of.auth.domain.models.states.ConfirmSignUpState
import can.lucky.of.auth.domain.actions.ConfirmSignUpAction
import can.lucky.of.core.domain.vms.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ConfirmSignUpVm(
    private val authManager: AuthManager
) : ViewModel(), MviViewModel<ConfirmSignUpState, ConfirmSignUpAction> {

    private val mutableState = MutableStateFlow(ConfirmSignUpState())
    override val state: StateFlow<ConfirmSignUpState> = mutableState


    override fun sent(action: ConfirmSignUpAction) {
        when(action){
            is ConfirmSignUpAction.Init -> handleInit(action)
        }
    }

    private fun handleInit(action: ConfirmSignUpAction.Init){
        if (state.value.isInited()) return

        mutableState.value = state.value.copy(
            phoneNumber = action.phoneNumber,
            password = action.password
        )
        waitConfirmRegistration()

    }

    private fun waitConfirmRegistration() {
        viewModelScope.launch(Dispatchers.IO) {
            while (authManager.isRegistered(state.value.phoneNumber).not()) {
                delay(5000)
            }

            var result : AuthResult? = null

            while (result?.success != true) {
                try {
                    result = authManager.logIn(
                        phoneNumber = state.value.phoneNumber,
                        password = state.value.password
                    )
                }catch (e: Exception){
                    delay(3000)
                }
            }

            mutableState.value = state.value.copy(isLoading = false)

        }
    }
}