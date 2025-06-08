package can.lucky.of.addword.domain.managers

import androidx.core.net.toFile
import can.lucky.of.addword.domain.models.WordImageOptions
import can.lucky.of.addword.domain.models.WordTextOptions
import can.lucky.of.addword.net.clients.RecognizeWordClient
import can.lucky.of.addword.net.models.requests.RecognizeWordByPictureRequest
import can.lucky.of.addword.net.models.requests.RecognizeWordByTextRequest
import can.lucky.of.addword.net.models.responses.RecognizeWordResponse
import can.lucky.of.core.domain.models.data.words.Word


internal class WordRecognizeManagerImpl(
    private val recognizeWordClient: RecognizeWordClient
) : WordRecognizeManager {
    override suspend fun recognize(options: WordImageOptions): Word {
        val request = options.toRequest()
        val response = recognizeWordClient.recognize(request)
        return response.toWord(options)
    }

    override suspend fun recognize(options: WordTextOptions): Word {
        val request = options.toRequest()
        val response = recognizeWordClient.recognize(request)
        return response.toWord(options)
    }

    private fun WordImageOptions.toRequest(): RecognizeWordByPictureRequest {
        return RecognizeWordByPictureRequest(
            image = this.imageUri.toFile(),
            language = this.wordLang.shortName,
            translationLanguage = this.translateLang.shortName
        )
    }

    private fun WordTextOptions.toRequest(): RecognizeWordByTextRequest {
        return RecognizeWordByTextRequest(
            original = this.original.ifBlank { null },
            translate = this.translate.ifBlank { null },
            language = this.wordLang.shortName,
            translationLanguage = this.translateLang.shortName
        )
    }

    private fun RecognizeWordResponse.toWord(options: WordImageOptions): Word {
        return Word(
            id = "",
            original = this.word,
            translate = this.translate,
            lang = options.wordLang.name,
            translateLang = options.translateLang.name,
            cefr = this.cefr,
            description = this.description,
            category = this.category,
            imageLink = options.imageUri.toString()
        )
    }


    private fun RecognizeWordResponse.toWord(options: WordTextOptions): Word {
        return Word(
            id = "",
            original = this.word,
            translate = this.translate,
            lang = options.wordLang.name,
            translateLang = options.translateLang.name,
            cefr = this.cefr,
            description = this.description,
            category = this.category
        )
    }
}