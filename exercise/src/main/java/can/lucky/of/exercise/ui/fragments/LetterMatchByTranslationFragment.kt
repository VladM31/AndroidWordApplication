package can.lucky.of.exercise.ui.fragments

import can.lucky.of.core.domain.models.enums.Exercise

class LetterMatchByTranslationFragment : LetterMatchByFieldFragment(
    exerciseType = Exercise.LETTERS_MATCH_BY_TRANSLATION,
    toText = { it.translate }
)