package can.lucky.of.validation.actions

import can.lucky.of.validation.models.DifferentLanguageable
import can.lucky.of.validation.models.ValidResult

class DifferentLanguageAction<T: DifferentLanguageable> : ValidAction<T> {
    override fun validate(value: T): ValidResult {
        if (value.getLanguage() == value.getSecondLanguage()) {
            return ValidResult.invalid("Languages should be different")
        }
        return ValidResult.valid()
    }
}