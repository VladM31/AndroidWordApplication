package can.lucky.of.exercise.ui.fragments

import can.lucky.of.core.domain.models.enums.Exercise

class WriteByImageAndDescriptionFragment : WriteByImageAndFieldFragment(
    exerciseType = Exercise.WORD_BY_WRITE_BY_DESCRIPTION,
    toText = { it.description ?: "Description not found, word -> ${it.original}" }
)