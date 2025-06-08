package can.lucky.of.core.domain.managers.subscribe


import can.lucky.of.core.domain.models.data.Cache
import can.lucky.of.core.domain.models.data.Subscribe
import java.time.LocalDateTime


interface SubscribeCacheManager {
    suspend fun fetch(): Subscribe?

    suspend fun cache() : Cache<Subscribe?>

    suspend fun isActiveSubscribe(): Boolean

    suspend fun update(expirationDate: LocalDateTime)
}