package can.lucky.of.addword.domain.managers.recognizes

import can.lucky.of.addword.domain.models.AiRecognizeResult
import can.lucky.of.addword.domain.models.AiRecognizedWord
import can.lucky.of.addword.domain.models.RecognizeConfirmation
import can.lucky.of.addword.domain.models.RecognizeTextOptions
import can.lucky.of.addword.domain.models.filters.RecognizeResultFilter
import can.lucky.of.addword.net.clients.AiRecognizeWordClient
import can.lucky.of.addword.net.models.requests.AiRecognizeWordByTextRequest
import can.lucky.of.addword.net.models.requests.RecognizeConfirmationRequest
import can.lucky.of.addword.utils.MAPPER
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.PagedModels
import can.lucky.of.core.utils.toPair


internal class AiRecognizeWordManagerImpl(
    private val aiRecognizeWordClient: AiRecognizeWordClient,
    private val userCacheManager: UserCacheManager
) : AiRecognizeWordManager {


    override suspend fun getRecognizeResults(filter: RecognizeResultFilter): PagedModels<AiRecognizeResult> {
        val respond = aiRecognizeWordClient.getRecognizeResults(
            userCacheManager.toPair().second,
            filter.page,
            filter.size
        )
        return MAPPER.convertValue(
            respond,
            object : com.fasterxml.jackson.core.type.TypeReference<PagedModels<AiRecognizeResult>>() {}
        )
    }

    override suspend fun recognize(options: RecognizeTextOptions): AiRecognizeResult {
        val request = MAPPER.convertValue(options, AiRecognizeWordByTextRequest::class.java)
        val respond = aiRecognizeWordClient.recognizeWord(
            userCacheManager.toPair().second,
            request
        )
        return MAPPER.convertValue(respond, AiRecognizeResult::class.java)
    }

    override suspend fun getWord(recognizeId: String): AiRecognizedWord {
        val respond = aiRecognizeWordClient.getWord(
            userCacheManager.toPair().second,
            recognizeId
        )
        return MAPPER.convertValue(respond, AiRecognizedWord::class.java)
    }

    override suspend fun confirm(options: RecognizeConfirmation) {
        val body = MAPPER.convertValue(options, RecognizeConfirmationRequest::class.java)
        aiRecognizeWordClient.confirm(
            userCacheManager.toPair().second,
            body
        )
    }
}