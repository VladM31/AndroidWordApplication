package can.lucky.of.exercise.ui.fragments

import can.lucky.of.core.domain.models.enums.Exercise

class OptionWordByTranslateFragment : SelectingAnOptionFragment(
    exerciseType = Exercise.WORD_BY_TRANSLATES,
    toText = { it.original },
    toOption = { it?.translate ?: "Translate not found, word -> ${it?.original}" },
    soundBeforeAnswer = true
)
