package can.lucky.of.core.domain.models.data.words

import java.io.File

data class PinUserWord(
    val wordId: String,
    val sound: File? = null,
    val image: File? = null,
)