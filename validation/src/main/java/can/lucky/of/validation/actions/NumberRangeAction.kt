package can.lucky.of.validation.actions

import can.lucky.of.validation.models.ValidResult

internal data class NumberRangeAction<N : Number>(
    val comparator: Comparator<N>,
    val minInclusive: Boolean = false,
    val maxInclusive: Boolean = false,
    val min: N? = null,
    val max: N? = null
) : ValidAction<N> {
    override fun validate(value: N): ValidResult {
        if (min != null && comparator.compare(value, min) < 0 || !minInclusive && comparator.compare(value, min) == 0) {
            return ValidResult.invalid("Value should be greater than or equal to $min")
        }
        if (max != null && comparator.compare(value, max) > 0 || !maxInclusive && comparator.compare(value, max) == 0) {
            return ValidResult.invalid("Value should be less than or equal to $max")
        }
        return ValidResult.valid()
    }
}
