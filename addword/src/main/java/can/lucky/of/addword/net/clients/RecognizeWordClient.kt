package can.lucky.of.addword.net.clients

import can.lucky.of.addword.net.models.requests.RecognizeWordByPictureRequest
import can.lucky.of.addword.net.models.requests.RecognizeWordByTextRequest
import can.lucky.of.addword.net.models.responses.RecognizeWordResponse

internal interface RecognizeWordClient {

    suspend fun recognize(request: RecognizeWordByPictureRequest): RecognizeWordResponse

    suspend fun recognize(request: RecognizeWordByTextRequest): RecognizeWordResponse
}