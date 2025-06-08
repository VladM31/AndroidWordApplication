package can.lucky.of.addword.domain.managers

import can.lucky.of.addword.domain.models.WordImageOptions
import can.lucky.of.addword.domain.models.WordTextOptions
import can.lucky.of.core.domain.models.data.words.Word

internal interface WordRecognizeManager {

    suspend fun recognize(options: WordImageOptions): Word

    suspend fun recognize(options: WordTextOptions): Word
}