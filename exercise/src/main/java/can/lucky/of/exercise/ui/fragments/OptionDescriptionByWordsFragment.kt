package can.lucky.of.exercise.ui.fragments

import can.lucky.of.core.domain.models.enums.Exercise

class OptionDescriptionByWordsFragment :
    SelectingAnOptionFragment(
        exerciseType = Exercise.DESCRIPTION_BY_WORDS,
        toText = { it.description ?: "Description not found, word -> ${it.original}"},
        toOption = { it?.original.orEmpty() },
        enableImage = false,
        soundAfterAnswer = true
    )
