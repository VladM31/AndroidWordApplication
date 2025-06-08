package can.lucky.of.core.domain.factories

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.utils.toPair
import okhttp3.Headers

class HttpOkHeaderFactory(
    private val userCacheManager: UserCacheManager
) {

    fun createHeaders(): Headers {
        return userCacheManager.toOkHttpHeaders()
    }

    private fun UserCacheManager.toOkHttpHeaders() : Headers{
        return toPair().let {
            Headers.Builder()
                .add(it.first, it.second)
                .build()
        }
    }
}


