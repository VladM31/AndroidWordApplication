package can.lucky.of.auth.domain.vms

import androidx.lifecycle.viewModelScope
import can.lucky.of.auth.domain.actions.TelegramLoginAction
import can.lucky.of.auth.domain.managers.AuthHistoryManager
import can.lucky.of.auth.domain.managers.TelegramAuthManager
import can.lucky.of.auth.domain.models.states.TelegramLoginState
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.core.utils.getRespondMessage
import can.lucky.of.validation.Validator
import can.lucky.of.validation.schemes.ValidationScheme
import can.lucky.of.validation.schemes.isPhoneNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class TelegramLoginVm(
    private val telegramAuthManager: TelegramAuthManager,
    private val authHistoryManager: AuthHistoryManager
) : AbstractMviViewModel<TelegramLoginState, TelegramLoginAction>(){

    private val mutableState = MutableStateFlow(TelegramLoginState(
        phoneNumber = authHistoryManager.lastPhoneNumber.orEmpty()
    ))
    override val state : StateFlow<TelegramLoginState> = mutableState
    private val validator = Validator(state)

    init {
        validator.add({ it.phoneNumber }, ValidationScheme.stringSchema("Phone number").isPhoneNumber())
    }

    override fun sent(action: TelegramLoginAction) {
        when(action){
            is TelegramLoginAction.SetPhoneNumber -> handlePhoneNumber(action)
            is TelegramLoginAction.Submit -> handleSubmit()
            is TelegramLoginAction.CheckLogin -> handleCheckLogin()
        }
    }

    private fun handleCheckLogin() {
        viewModelScope.launch(Dispatchers.IO){
            val success = telegramAuthManager.login(mutableState.value.phoneNumber, mutableState.value.code)
            if (success){
                authHistoryManager.updateLastPhoneNumber(mutableState.value.phoneNumber)
                mutableState.value = mutableState.value.copy(isEnd = true)
            }
        }
    }

    private fun handlePhoneNumber(action: TelegramLoginAction.SetPhoneNumber){
        mutableState.value = mutableState.value.copy(phoneNumber = action.value)
    }

    private fun handleSubmit(){
        val message = validator.validate()
        if (message.isNotBlank()){
            mutableState.value = mutableState.value.copy(errorMessage = ErrorMessage(message))
            return
        }

        viewModelScope.launch(Dispatchers.IO){
            mutableState.value = mutableState.value.copy(isLoading = true)

            val result = telegramAuthManager.runCatching {
                startLogin(mutableState.value.phoneNumber)

            }

            if (result.isFailure){
                mutableState.value = mutableState.value.copy(
                    errorMessage = ErrorMessage(result.exceptionOrNull()?.getRespondMessage().orEmpty()),
                    isLoading = false
                )
                return@launch
            }

            mutableState.value = mutableState.value.copy(
                code = result.getOrNull().orEmpty(),
                isLoading = false
            )

           while (true){
               val success = telegramAuthManager.login(mutableState.value.phoneNumber, mutableState.value.code)
               if (success){
                     authHistoryManager.updateLastPhoneNumber(mutableState.value.phoneNumber)
                   mutableState.value = mutableState.value.copy(isEnd = true)
                   break
               }
               delay(3000)
           }
        }
    }
}