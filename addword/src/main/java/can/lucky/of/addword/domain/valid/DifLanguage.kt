package can.lucky.of.addword.domain.valid

import can.lucky.of.addword.domain.models.states.DefaultAddWordState
import can.lucky.of.validation.actions.ValidAction
import can.lucky.of.validation.models.ValidResult
import can.lucky.of.validation.schemes.ValidationScheme

private object DifLanguageAction : ValidAction<DefaultAddWordState> {
    override fun validate(value: DefaultAddWordState): ValidResult {
        return if (value.language != value.translationLanguage) {
            ValidResult.valid()
        } else {
            ValidResult.invalid("Languages must be different")
        }
    }
}

internal fun ValidationScheme<DefaultAddWordState>.difLanguage() : ValidationScheme<DefaultAddWordState> {
    return add(DifLanguageAction)
}