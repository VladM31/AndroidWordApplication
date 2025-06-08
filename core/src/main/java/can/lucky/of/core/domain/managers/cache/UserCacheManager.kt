package can.lucky.of.core.domain.managers.cache

import can.lucky.of.core.domain.models.data.Token
import can.lucky.of.core.domain.models.data.User


interface UserCacheManager {

    val user: User

    val token: Token

    val isExpired: Boolean

    fun saveUser(user: User)

    fun saveToken(token: Token)

    fun clear()
}