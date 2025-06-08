package can.lucky.of.auth.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState

internal data class TelegramLoginState(
    val code: String = "",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    override val errorMessage: ErrorMessage? = null,
    override val isEnd: Boolean = false

): ErrorableState, EndetableState
