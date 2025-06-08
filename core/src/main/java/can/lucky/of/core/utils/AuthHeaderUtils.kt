package can.lucky.of.core.utils

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import com.bumptech.glide.load.model.LazyHeaders


fun UserCacheManager.toPair(): Pair<String, String> {
    return "Authorization" to "Bearer ${token.value}"
}




