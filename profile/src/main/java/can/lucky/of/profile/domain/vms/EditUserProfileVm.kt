package can.lucky.of.profile.domain.vms

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.Currency
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.profile.domain.actions.EditUserProfileAction
import can.lucky.of.profile.domain.managers.EditUserManager
import can.lucky.of.profile.domain.models.data.EditUser
import can.lucky.of.profile.domain.models.states.EditUserProfileState
import can.lucky.of.validation.Validator
import can.lucky.of.validation.schemes.ValidationScheme
import can.lucky.of.validation.schemes.isPhoneNumber
import can.lucky.of.validation.schemes.length
import can.lucky.of.validation.schemes.notBlank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class EditUserProfileVm(
    private val editUserManager: EditUserManager,
    private val userCacheManager: UserCacheManager
): AbstractMviViewModel<EditUserProfileState,EditUserProfileAction>() {

    private val mutableState = MutableStateFlow(EditUserProfileState())
    override val state: StateFlow<EditUserProfileState> = mutableState
    private val validator = Validator(state)

    init {
        mutableState.value = EditUserProfileState(
            firstName = userCacheManager.user.firstName,
            lastName = userCacheManager.user.lastName,
            currency = Currency.valueOf(userCacheManager.user.currency),
            isInited = true
        )

        validator.add(EditUserProfileState::firstName,
            ValidationScheme.stringSchema("First name").length(min = 2, max = 60).notBlank())
        validator.add(EditUserProfileState::lastName,
            ValidationScheme.stringSchema("Last name").length(min = 2, max = 60).notBlank())
    }

    override fun sent(action: EditUserProfileAction) {
        when(action){
            is EditUserProfileAction.FirstNameChanged -> {
                mutableState.value = state.value.copy(firstName = action.firstName)
            }
            is EditUserProfileAction.LastNameChanged -> {
                mutableState.value = state.value.copy(lastName = action.lastName)
            }
            is EditUserProfileAction.CurrencyChanged -> {
                mutableState.value = state.value.copy(currency = action.currency)
            }
            is EditUserProfileAction.Submit -> handleSubmit()
        }
    }

    private fun handleSubmit(){
        val error = validator.validate(" - ")

        if (error.isNotBlank()){
            mutableState.value = state.value.copy(errorMessage = ErrorMessage(error))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val  result = editUserManager.edit(state.value.toModel())
            mutableState.value = state.value.copy(
                isEnd = result.success,
                errorMessage = result.message?.let { ErrorMessage(it) }
            )
        }
    }

    private fun EditUserProfileState.toModel():EditUser {
        return EditUser(
            firstName = firstName,
            lastName = lastName,
            currency = currency
        )
    }


}