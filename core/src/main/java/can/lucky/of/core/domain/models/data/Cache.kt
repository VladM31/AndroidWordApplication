package can.lucky.of.core.domain.models.data

import java.time.LocalDateTime

data class Cache<T>(
    val value: T?,
    val cacheDate: LocalDateTime
) {

    val isEmpty : Boolean
        get() = value == null

    val isPresent : Boolean
        get() = value != null

    val durations: Int
        get() = LocalDateTime.now().minute - cacheDate.minute

    companion object{
        fun <T> ofNow(value: T): Cache<T?> {
            return Cache(
                value,
                LocalDateTime.now()
            )
        }

        fun <T> empty(): Cache<T?> {
            return Cache(
                null,
                LocalDateTime.now()
            )
        }
    }

}