package can.lucky.of.validation.actions

import can.lucky.of.validation.models.ValidResult

interface ValidAction<T> {

    fun validate(value: T): ValidResult
}