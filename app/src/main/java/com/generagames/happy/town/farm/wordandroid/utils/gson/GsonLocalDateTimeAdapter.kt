package com.generagames.happy.town.farm.wordandroid.utils.gson;

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object GsonLocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        return JsonPrimitive(src.toString());
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        return LocalDateTime.parse(json?.asString, DateTimeFormatter.ISO_DATE_TIME)
    }

    fun GsonBuilder.addLocalDateTimeAdapter() : GsonBuilder{
        return registerTypeAdapter(LocalDateTime::class.java, GsonLocalDateTimeAdapter)
    }
}
