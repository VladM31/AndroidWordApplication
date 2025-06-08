package can.lucky.of.profile.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.Currency
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState

data class EditUserProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val currency: Currency = Currency.USD,
    val isInited: Boolean = false,
    override val isEnd: Boolean = false,
    override val errorMessage: ErrorMessage? = null
) : EndetableState, ErrorableState
