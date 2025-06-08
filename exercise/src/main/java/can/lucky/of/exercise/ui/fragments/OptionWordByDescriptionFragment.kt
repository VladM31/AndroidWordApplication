package can.lucky.of.exercise.ui.fragments

import can.lucky.of.core.domain.models.enums.Exercise

class OptionWordByDescriptionFragment : SelectingAnOptionFragment(
    exerciseType = Exercise.WORD_BY_DESCRIPTIONS,
    toText = { it.original },
    toOption = { it?.description ?: "Description not found, word -> ${it?.original}" },
    enableImage = false,
    soundBeforeAnswer = true,
)