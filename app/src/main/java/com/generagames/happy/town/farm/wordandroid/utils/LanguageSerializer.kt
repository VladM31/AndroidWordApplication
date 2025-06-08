package com.generagames.happy.town.farm.wordandroid.utils

import can.lucky.of.core.domain.models.enums.Language
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class LanguageSerializer : JsonSerializer<Language> {
    override fun serialize(
        src: Language?,
        typeOfSrc: Type?,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(src?.shortName.toString())
    }
}