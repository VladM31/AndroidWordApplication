package can.lucky.of.addword.domain.valid

import can.lucky.of.addword.domain.models.states.DefaultAddWordState
import can.lucky.of.validation.Validator
import can.lucky.of.validation.schemes.ValidationScheme
import can.lucky.of.validation.schemes.length
import can.lucky.of.validation.schemes.notBlank
import kotlinx.coroutines.flow.StateFlow

internal val defaultAddWordValidator : (StateFlow<DefaultAddWordState>) -> Validator<DefaultAddWordState> = { state ->
    Validator(state).apply {


        add(DefaultAddWordState::word,
            ValidationScheme.stringSchema("Word")
                .notBlank()
                .length(min = 2, max = 255)
        )

        add(DefaultAddWordState::translation,
            ValidationScheme.stringSchema("Translation")

        )

        add(DefaultAddWordState::category,
            ValidationScheme.stringSchema("Category")
                .length(min = 0, max = 255)
                .notBlank(canBeEmpty = true)
        )

        add(DefaultAddWordState::description,
            ValidationScheme.stringSchema("Description")
                .length(min = 0, max = 1000)
                .notBlank(canBeEmpty = true)
        )

        add({it}, ValidationScheme<DefaultAddWordState>("Language")
            .difLanguage()
        )
    }
}