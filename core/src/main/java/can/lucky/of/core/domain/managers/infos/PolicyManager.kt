package can.lucky.of.core.domain.managers.infos

import java.io.File

interface PolicyManager {

    suspend fun getPolicyFile(): File
}