package com.generagames.happy.town.farm.wordandroid.net.models.requests.files

import can.lucky.of.core.domain.models.enums.Language

data class AudioGenerationRequest(
    val text: String,
    val language: Language
)
