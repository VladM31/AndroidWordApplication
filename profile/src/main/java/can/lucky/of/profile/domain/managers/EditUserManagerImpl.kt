package can.lucky.of.profile.domain.managers

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.data.Token
import can.lucky.of.core.domain.models.data.User
import can.lucky.of.profile.domain.models.data.EditResult
import can.lucky.of.profile.domain.models.data.EditUser
import can.lucky.of.profile.net.clients.EditUserClient
import can.lucky.of.profile.net.requests.EditUserRequest
import can.lucky.of.profile.net.responds.EditUserRespond
import java.util.Date

internal class EditUserManagerImpl(
    private val editUserClient: EditUserClient,
    private val userCacheManager: UserCacheManager
) : EditUserManager {
    override suspend fun edit(user: EditUser): EditResult {
        return try {
            val respond = editUserClient.editUser(userCacheManager.token.value, user.toRequest())
            userCacheManager.saveUser(respond.user.toUser())
            userCacheManager.saveToken(
                Token(
                    respond.token.value,
                    respond.token.expirationTime
                )
            )

            EditResult(true)
        } catch (e: Exception) {
            EditResult(false, e.message)
        }
    }

    private fun EditUser.toRequest(): EditUserRequest {
        return EditUserRequest(
            firstName = firstName, lastName = lastName, currency = currency
        )
    }


    private fun EditUserRespond.User.toUser(): User {
        return User(
            id = id, firstName = firstName, lastName = lastName, phoneNumber = phoneNumber,
            email = email, currency = currency, role = role, dateOfLonIn = Date().time
        )
    }
}