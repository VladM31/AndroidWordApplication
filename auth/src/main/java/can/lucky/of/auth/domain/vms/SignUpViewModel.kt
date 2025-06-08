package can.lucky.of.auth.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.auth.domain.managers.AuthManager
import can.lucky.of.auth.domain.models.data.SignUpModel
import can.lucky.of.auth.domain.models.states.SignUpState
import can.lucky.of.auth.domain.actions.SighUpAction
import can.lucky.of.auth.ui.validation.signUpValidator
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.vms.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class SignUpViewModel(
    private val authManager: AuthManager
) : ViewModel(), MviViewModel<SignUpState, SighUpAction> {

    private val mutableState = MutableStateFlow(SignUpState())
    override val state: StateFlow<SignUpState> = mutableState
    private val validator = signUpValidator(state)

    override fun sent(action: SighUpAction) {
        when(action) {
            is SighUpAction.SetPhoneNumber -> {
                mutableState.value = state.value.copy(phoneNumber = action.value)
            }
            is SighUpAction.SetPassword -> {
                mutableState.value = state.value.copy(password = action.value)
            }
            is SighUpAction.SetFirstName -> {
                mutableState.value = state.value.copy(firstName = action.value)
            }
            is SighUpAction.SetLastName -> {
                mutableState.value = state.value.copy(lastName = action.value)
            }
            is SighUpAction.SetCurrency -> {
                mutableState.value = state.value.copy(currency = action.value)
            }
            is SighUpAction.SetEmail -> {
                mutableState.value = state.value.copy(email = action.value)
            }
            SighUpAction.Submit -> handleSubmit()
        }
    }

    private fun handleSubmit(){
        val errorMessage = validator.validate(" - ")

        if (errorMessage.isNotBlank()) {
            mutableState.value = state.value.copy(error = ErrorMessage(message = errorMessage))
            return
        }

        viewModelScope.launch(Dispatchers.IO){
            try {
                authManager.signUp(state.value.toModel()).let {
                    val erMes = it.message?.run { ErrorMessage(message = this) }
                    mutableState.value = state.value.copy(success = it.success, error = erMes)
                }
            }catch (e: Exception){
                mutableState.value = state.value.copy(error = ErrorMessage(message = e.message ?: "Unknown error"))
            }
        }
    }

    private fun SignUpState.toModel() = SignUpModel(
        phoneNumber = phoneNumber,
        password = password,
        firstName = firstName,
        lastName = lastName,
        currency = currency.name,
        email = email?.ifBlank { null }
    )
}