package can.lucky.of.auth.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.auth.domain.managers.AuthManager
import can.lucky.of.auth.domain.models.states.LoginState
import can.lucky.of.auth.domain.actions.LoginAction
import can.lucky.of.auth.domain.managers.AuthHistoryManager
import can.lucky.of.auth.domain.managers.BiometricAuthenticationManager
import can.lucky.of.auth.ui.validation.loginValidator
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.utils.getRespondMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


internal class LoginViewModel(
    private val authManager: AuthManager,
    userCacheManager : UserCacheManager,
    private val authHistoryManager: AuthHistoryManager,
    private val biometricAuthManager: BiometricAuthenticationManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(LoginState(
        isNotExpired = userCacheManager.isExpired.not(),
        phoneNumber = authHistoryManager.lastPhoneNumber.orEmpty(),
        isAvailableBiometric = biometricAuthManager.isBiometricAuthAvailable
    ))
    val state: StateFlow<LoginState> = mutableState
    private val validator = loginValidator(mutableState)


    fun sent(action: LoginAction){
        when(action){
            is LoginAction.Submit -> submit()
            is LoginAction.SetPhoneNumber -> setPhoneNumber(action)
            is LoginAction.SetPassword -> setPassword(action)
            is LoginAction.BiometricAuthFailed -> handleBiometricAuthFailed()
            is LoginAction.BiometricAuthSuccess -> handleBiometricAuthSuccess()
        }
    }

    private fun handleBiometricAuthFailed() {
        biometricAuthManager.onFailedBiometricAuth()
        mutableState.value = state.value.copy(isAvailableBiometric = biometricAuthManager.isBiometricAuthAvailable)
    }

    private fun handleBiometricAuthSuccess() {
        mutableState.value = state.value.copy(isAvailableBiometric = false)
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = biometricAuthManager.onSuccessfulBiometricAuth()
                authHistoryManager.updateLastPhoneNumber(state.value.phoneNumber)
                mutableState.value = state.value.copy(isEnd = result)
            }catch (e: Exception){
                mutableState.value = state.value.copy(errorMessage = ErrorMessage(message = e.getRespondMessage()))
            }
        }
    }

    private fun setPassword(action: LoginAction.SetPassword) {
        mutableState.value = mutableState.value.copy(password = action.value)
    }

    private fun setPhoneNumber(action: LoginAction.SetPhoneNumber) {
        mutableState.value = mutableState.value.copy(phoneNumber = action.value, isAvailableBiometric = false)

    }

    private fun submit(){
        val errors = validator.validate(" - ")

        if (errors.isNotEmpty()){
            mutableState.value = state.value.copy(errorMessage = ErrorMessage(message = errors))
            return
        }

        viewModelScope.launch(Dispatchers.IO){
            logIn()
        }
    }

    private suspend fun logIn() {
        try {
            val result = authManager.logIn(
                phoneNumber = state.value.phoneNumber,
                password = state.value.password
            )

            val error = result.message?.let {
                ErrorMessage(message = it)
            }

            if (result.success) {
                authHistoryManager.updateLastPhoneNumber(state.value.phoneNumber)
            }

            mutableState.value = state.value.copy(
                isEnd = result.success,
                errorMessage = error
            )
        } catch (e: Exception) {
            mutableState.value =
                state.value.copy(errorMessage = ErrorMessage(message = e.message ?: "Error"))
        }
    }
}