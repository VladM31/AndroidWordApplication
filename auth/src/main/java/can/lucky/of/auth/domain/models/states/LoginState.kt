package can.lucky.of.auth.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState

internal data class LoginState(
    val isNotExpired: Boolean = false,
    val isAvailableBiometric: Boolean = false,
    val phoneNumber: String = "",
    val password: String = "",
    override val errorMessage: ErrorMessage? = null,
    override val isEnd: Boolean = false
) : ErrorableState, EndetableState
