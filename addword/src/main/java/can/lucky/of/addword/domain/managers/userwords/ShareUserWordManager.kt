package can.lucky.of.addword.domain.managers.userwords

import can.lucky.of.addword.domain.models.ShareUserWord
import can.lucky.of.addword.domain.models.ShareWordOptionals
import can.lucky.of.addword.domain.models.ShareWordResult

interface ShareUserWordManager {

    suspend fun share(optionals: ShareWordOptionals): ShareWordResult

    suspend fun getShareWord(qrCodeContent: String): ShareUserWord

    suspend fun acceptShare(qrCodeContent: String)
}