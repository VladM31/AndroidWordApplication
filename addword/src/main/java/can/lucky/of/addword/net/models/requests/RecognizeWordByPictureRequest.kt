package can.lucky.of.addword.net.models.requests

import java.io.File

internal data class RecognizeWordByPictureRequest(
    val image: File,
    val language: String,
    val translationLanguage: String
)
