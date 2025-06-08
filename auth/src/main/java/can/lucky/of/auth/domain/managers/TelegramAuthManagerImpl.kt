package can.lucky.of.auth.domain.managers

import android.os.Build
import can.lucky.of.auth.domain.mappers.toUser
import can.lucky.of.auth.domain.utils.getDeviceName
import can.lucky.of.auth.net.clients.TelegramAuthClient
import can.lucky.of.auth.net.models.requests.TelegramAuthLoginReq
import can.lucky.of.auth.net.models.requests.TelegramAuthStartLoginReq
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.data.Token
import java.util.Locale

internal class TelegramAuthManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val telegramAuthClient: TelegramAuthClient
) : TelegramAuthManager {
    override suspend fun startLogin(phoneNumber: String): String {
        return telegramAuthClient.startLogin(
            TelegramAuthStartLoginReq(
                phoneNumber,
                getDeviceName()
            )
        )
    }

    override suspend fun login(phoneNumber: String, code: String): Boolean {
        val  respond = telegramAuthClient.login(
            TelegramAuthLoginReq(
                phoneNumber,
                code
            )
        )

        if(respond.success.not()){
            return false
        }

        userCacheManager.saveUser(respond.toUser())
        userCacheManager.saveToken(
            Token(
                respond.token?.value ?: "",
                respond.token?.expirationTime ?: 0
            )
        )

        return true
    }


}