package can.lucky.of.validation.actions

import can.lucky.of.validation.models.ValidResult

internal data class IsMatchesAction(
    private val regex: Regex,
    private val message: String = "Value should match the pattern"
) : ValidAction<String> {
    override fun validate(value: String): ValidResult {
        if (!regex.matches(value)) {
            return ValidResult.invalid(message)
        }
        return ValidResult.valid()
    }
}
