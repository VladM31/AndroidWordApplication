package can.lucky.of.addword.domain.managers.recognizes

import can.lucky.of.addword.domain.models.AiRecognizeResult
import can.lucky.of.addword.domain.models.AiRecognizedWord
import can.lucky.of.addword.domain.models.RecognizeConfirmation
import can.lucky.of.addword.domain.models.RecognizeTextOptions
import can.lucky.of.addword.domain.models.filters.RecognizeResultFilter
import can.lucky.of.core.domain.models.PagedModels

internal interface AiRecognizeWordManager {

    suspend fun getRecognizeResults(filter: RecognizeResultFilter):  PagedModels<AiRecognizeResult>

    suspend fun recognize(options: RecognizeTextOptions): AiRecognizeResult

    suspend fun getWord(recognizeId: String) : AiRecognizedWord

    suspend fun confirm(options: RecognizeConfirmation)
}