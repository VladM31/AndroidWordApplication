package can.lucky.of.validation.actions

import can.lucky.of.validation.models.ValidResult

internal data class EqualsAction<T>(
    val values: List<T>,
    val isEqual: Boolean = true
) : ValidAction<T> {
    override fun validate(value: T): ValidResult {
        if (values.contains(value) == isEqual) {
            return ValidResult.valid()
        }
        return ValidResult.invalid("Value should be one of $values")
    }
}