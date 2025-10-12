package can.lucky.of.core.domain.managers.infos

import java.io.File

interface InstructionManager {

    suspend fun getInstructionFile(): File
}