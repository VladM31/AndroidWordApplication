package can.lucky.of.validation.actions

import can.lucky.of.validation.models.ValidResult

internal object NotEmptyAction : ValidAction<String> {
    override fun validate(value: String): ValidResult {
        if (value.isEmpty()) {
            return ValidResult.invalid("Value should not be empty")
        }
        return ValidResult.valid()
    }
}