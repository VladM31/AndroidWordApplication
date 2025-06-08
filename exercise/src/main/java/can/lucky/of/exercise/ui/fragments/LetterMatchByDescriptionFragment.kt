package can.lucky.of.exercise.ui.fragments

import can.lucky.of.core.domain.models.enums.Exercise

class LetterMatchByDescriptionFragment : LetterMatchByFieldFragment(
    exerciseType = Exercise.LETTERS_MATCH_BY_DESCRIPTION,
    toText = { it.description ?: "Not found description for word ${it.original}" }
)