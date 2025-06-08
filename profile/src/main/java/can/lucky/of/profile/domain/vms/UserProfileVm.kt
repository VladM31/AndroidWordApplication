package can.lucky.of.profile.domain.vms

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import can.lucky.of.profile.domain.actions.UserProfileAction
import can.lucky.of.profile.domain.models.states.UserProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class UserProfileVm(
    private val userCacheManager: UserCacheManager
) : AbstractMviViewModel<UserProfileState, UserProfileAction>(){

    private val mutableState = MutableStateFlow(UserProfileState())
    override val state: StateFlow<UserProfileState> = mutableState

    init {
        setProfile()
    }

    override fun sent(action: UserProfileAction) {
        when (action) {
            UserProfileAction.Refresh -> setProfile()
        }
    }

    private fun setProfile() {
        val user = userCacheManager.user
        mutableState.value = UserProfileState(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phone = user.phoneNumber,
            currency = user.currency
        )
    }
}