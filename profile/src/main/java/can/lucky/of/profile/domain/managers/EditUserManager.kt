package can.lucky.of.profile.domain.managers

import can.lucky.of.profile.domain.models.data.EditResult
import can.lucky.of.profile.domain.models.data.EditUser

internal interface EditUserManager {

    suspend fun edit(user: EditUser) : EditResult
}