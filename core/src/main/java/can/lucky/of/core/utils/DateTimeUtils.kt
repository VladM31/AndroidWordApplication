package can.lucky.of.core.utils

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val DEFAULT_DATE_TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


fun OffsetDateTime.toZoneDateTimeFormat(format: DateTimeFormatter): String {
    return atZoneSameInstant(ZoneId.systemDefault()).format(format)
}

fun OffsetDateTime.toZoneDateTimeFormat(): String {
    return this.toZoneDateTimeFormat(DEFAULT_DATE_TIME_FORMAT)
}