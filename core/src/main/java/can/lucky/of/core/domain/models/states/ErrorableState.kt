package can.lucky.of.core.domain.models.states

import can.lucky.of.core.domain.models.data.ErrorMessage

interface ErrorableState {
    val errorMessage: ErrorMessage?
}