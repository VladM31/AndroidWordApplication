package can.lucky.of.addword.domain.managers.userwords

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.utils.toPair
import can.lucky.of.addword.domain.models.ShareUserWord
import can.lucky.of.addword.domain.models.ShareWordOptionals
import can.lucky.of.addword.domain.models.ShareWordResult
import can.lucky.of.addword.net.clients.ShareUserWordClient
import can.lucky.of.addword.net.models.responses.ShareUserWordResponse
import can.lucky.of.addword.net.models.responses.ShareWordResponse
import java.util.Base64

internal class ShareUserWordManagerImpl(
    private val shareUserWordClient: ShareUserWordClient,
    private val userCacheManager: UserCacheManager
) : ShareUserWordManager {
    private val minusSeconds = 20
    private val token: String
        get() = userCacheManager.toPair().second

    override suspend fun share(optionals: ShareWordOptionals): ShareWordResult {
        val res = shareUserWordClient.share(
            token = token,
            userWordId = optionals.userWordId,
            width = optionals.width,
            height = optionals.height
        )

        return ShareWordResult(
            liveTimeInSeconds = res.liveTimeInSeconds - minusSeconds,
            qrCode = res.toByteArray()
        )
    }

    override suspend fun getShareWord(qrCodeContent: String): ShareUserWord {
        return shareUserWordClient.getShareWord(token = token, shareId = qrCodeContent).toModel()
    }

    override suspend fun acceptShare(qrCodeContent: String) {
        shareUserWordClient.acceptShare(token = token, shareId = qrCodeContent)
    }

    private fun ShareWordResponse.toByteArray(): ByteArray {
        return Base64.getDecoder().decode(qrCode)
    }


    private fun ShareUserWordResponse.toModel(): ShareUserWord {
        return ShareUserWord(
            wordId = wordId,
            original = original,
            lang = lang,
            translate = translate,
            translateLang = translateLang,
            category = category,
            hasSound = hasSound,
            hasImage = hasImage,
            cefr = cefr,
            description = description,
            expiredDate = expiredDate,
        )
    }
}