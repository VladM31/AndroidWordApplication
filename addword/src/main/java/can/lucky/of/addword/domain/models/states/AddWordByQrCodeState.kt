package can.lucky.of.addword.domain.models.states

import can.lucky.of.addword.domain.models.ShareUserWord
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.states.EndetableState
import can.lucky.of.core.domain.models.states.ErrorableState

data class AddWordByQrCodeState(
    val isInit: Boolean = false,
    val word: ShareUserWord? = null,
    val shareId: String? = null,
    override val isEnd: Boolean = false,
    override val errorMessage: ErrorMessage? = null
) : EndetableState, ErrorableState
