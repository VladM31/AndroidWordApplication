package can.lucky.of.addword.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val MAPPER: ObjectMapper = jacksonObjectMapper().registerModule(
    KotlinModule.Builder()
        .configure(KotlinFeature.NullToEmptyCollection, true)
        .configure(KotlinFeature.NullToEmptyMap, true)
        .configure(KotlinFeature.NullIsSameAsDefault, true)
        .configure(KotlinFeature.SingletonSupport, true)
        .configure(KotlinFeature.StrictNullChecks, true)
        .build()
) .registerModule(JavaTimeModule())
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

fun <T> readValueOrNull(value: String, valueType: Class<T>): T? {
    return try {
        MAPPER.readValue(value, valueType)
    } catch (e: Exception) {
        null
    }
}

fun <T> writeValueAsStringOrNull(value: T): String? {
    return try {
        MAPPER.writeValueAsString(value)
    } catch (e: Exception) {
        null
    }
}