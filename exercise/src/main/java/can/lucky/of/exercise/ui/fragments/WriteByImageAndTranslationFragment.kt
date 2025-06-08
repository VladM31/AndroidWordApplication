package can.lucky.of.exercise.ui.fragments

import can.lucky.of.core.domain.models.enums.Exercise

class WriteByImageAndTranslationFragment : WriteByImageAndFieldFragment(
    exerciseType = Exercise.WORD_BY_WRITE_TRANSLATE,
    toText = { it.translate }
)