package can.lucky.of.exercise.ui.fragments

import can.lucky.of.core.domain.models.enums.Exercise

class OptionWordByOriginalFragment : SelectingAnOptionFragment(
    exerciseType = Exercise.WORD_BY_ORIGINALS,
    toText = { it.translate },
    toOption = { it?.original ?: "Original not found" },
    soundAfterAnswer = true
)