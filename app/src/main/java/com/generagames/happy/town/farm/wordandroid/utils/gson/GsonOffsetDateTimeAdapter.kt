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
import java.time.OffsetDateTime

object GsonOffsetDateTimeAdapter : JsonSerializer<OffsetDateTime>,
    JsonDeserializer<OffsetDateTime> {
    override fun serialize(
        src: OffsetDateTime?,
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
    ): OffsetDateTime {
        return OffsetDateTime.parse(json?.asString)
    }

    fun GsonBuilder.addOffsetDateTimeAdapter(): GsonBuilder {
        return registerTypeAdapter(OffsetDateTime::class.java, GsonOffsetDateTimeAdapter)
    }
}
