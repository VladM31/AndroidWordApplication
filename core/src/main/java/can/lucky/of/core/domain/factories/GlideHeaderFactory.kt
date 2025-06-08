package can.lucky.of.core.domain.factories

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.utils.toPair
import com.bumptech.glide.load.model.LazyHeaders

class GlideHeaderFactory(
    private val userCacheManager: UserCacheManager
) {

    fun createHeaders(): LazyHeaders {
        return userCacheManager.toLazyHeaders()
    }

    private fun UserCacheManager.toLazyHeaders() : LazyHeaders {
        val pair = toPair()

        return LazyHeaders.Builder()
            .addHeader(pair.first, pair.second)
            .build()
    }
}
