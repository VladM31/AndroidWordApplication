package can.lucky.of.validation.actions

import can.lucky.of.validation.models.ValidResult

internal data class StringLengthAction(
    val min: Int? = null,
    val max: Int? = null
) : ValidAction<String> {
    override fun validate(value: String): ValidResult {
        if (min != null && value.length < min) {
            return ValidResult.invalid("Length should be at least $min")
        }
        if (max != null && value.length > max) {
            return ValidResult.invalid("Length should be at most $max")
        }
        return ValidResult.valid()
    }
}

