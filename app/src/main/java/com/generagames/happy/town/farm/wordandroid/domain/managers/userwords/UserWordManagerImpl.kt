package com.generagames.happy.town.farm.wordandroid.domain.managers.userwords

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.userwords.UserWordManager
import can.lucky.of.core.domain.models.PagedModels
import can.lucky.of.core.domain.models.data.words.DeleteUserWord
import can.lucky.of.core.domain.models.data.words.PinUserWord
import can.lucky.of.core.domain.models.data.words.SaveWord
import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.filters.UserWordFilter
import can.lucky.of.core.utils.toPair
import com.generagames.happy.town.farm.wordandroid.domain.utils.LanguageSerializer
import com.generagames.happy.town.farm.wordandroid.net.clients.files.FileApiClient
import com.generagames.happy.town.farm.wordandroid.net.clients.userword.UserWordClient
import com.generagames.happy.town.farm.wordandroid.net.models.requests.DeleteUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.files.AudioGenerationRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.files.SaveFileRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.words.PinUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.words.UserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.words.WordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.WordRespond
import com.google.gson.GsonBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

class UserWordManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val userWordClient: UserWordClient,
    private val fileClient: FileApiClient,
    private val context: Context
) : UserWordManager {
    private val gson =
        GsonBuilder().registerTypeAdapter(Language::class.java, LanguageSerializer()).create()

    private data class UploadResults(
        val imageFileName: String?,
        val soundFileName: String?
    )


    override suspend fun findBy(filter: UserWordFilter): PagedModels<UserWord> {
        val paged = userWordClient.findBy(
            userCacheManager.toPair().second,
            filter.toQueryMap(gson)
        )

        return PagedModels.of(paged) {
            UserWord(
                id = it.id,
                learningGrade = it.learningGrade,
                createdAt = it.createdAt,
                lastReadDate = it.lastReadDate,
                word = it.word.toWord()
            )
        }
    }

    override suspend fun save(words: Collection<SaveWord>) {
        withContext(kotlinx.coroutines.Dispatchers.IO) {
            val asyncList = words.map { async { toRequest(it) } }
            userWordClient.save(
                userCacheManager.toPair().second,
                asyncList.awaitAll()
            )
        }
    }

    override suspend fun pin(pins: Collection<PinUserWord>) {
        withContext(kotlinx.coroutines.Dispatchers.IO) {
            val asyncList = pins.map { async { toRequest(it) } }
            userWordClient.pin(
                userCacheManager.toPair().second,
                asyncList.awaitAll()
            )
        }
    }


    override suspend fun delete(requests: List<DeleteUserWord>) {
        userWordClient.delete(
            userCacheManager.toPair().second,
            requests.map {
                DeleteUserWordRequest(
                    id = it.id,
                    wordId = it.wordId
                )
            }
        )
    }

    private suspend fun toRequest(word: PinUserWord): PinUserWordRequest {
        val uploadResults = toUploadResults(
            image = word.image,
            sound = word.sound
        )
        return PinUserWordRequest(
            customSoundFileName = uploadResults.soundFileName,
            customImageFileName = uploadResults.imageFileName,
            wordId = word.wordId
        )
    }

    private suspend fun toRequest(word: SaveWord): UserWordRequest {
        var audioRequest: AudioGenerationRequest? = null

        if (word.needSound && word.sound == null) {
            audioRequest = AudioGenerationRequest(
                text = word.word,
                language = word.language
            )
        }

        val uploadResults = toUploadResults(
            image = word.image,
            sound = word.sound,
            audioRequest = audioRequest
        )

        return UserWordRequest(
            customSoundFileName = uploadResults.soundFileName,
            customImageFileName = uploadResults.imageFileName,
            word = word.toRequest()
        )
    }

    private suspend fun toUploadResults(
        image: File?,
        sound: File?,
        audioRequest: AudioGenerationRequest? = null,
    ): UploadResults {
        val imageFileName = image?.let {
            runCatching {
                image.toUri().fixImageOrientationFromUri(context)?.let {
                    val req =
                        SaveFileRequest(content = it.bitmapToByteArray(80), fileName = image.name)
                    fileClient.uploadFile(req).fileName
                }
            }.onFailure { Log.e("UserWordManager", "Image upload failed", it) }
        }?.getOrNull()

        var soundFileName = sound?.let {
            runCatching {
                sound.toUri().let {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val bytes = inputStream?.readBytes() ?: ByteArray(0)
                    val req = SaveFileRequest(content = bytes, fileName = sound.name)
                    fileClient.uploadFile(req).fileName
                }
            }.onFailure { Log.e("UserWordManager", "Sound upload failed", it) }
        }?.getOrNull()

        if (soundFileName == null && audioRequest != null) {
            soundFileName = runCatching {
                fileClient.textToAudioFile(audioRequest).fileName
            }.onFailure { Log.e("UserWordManager", "Sound upload failed", it) }
                .getOrNull()
        }

        return UploadResults(
            imageFileName = imageFileName,
            soundFileName = soundFileName
        )
    }

    private fun Uri.fixImageOrientationFromUri(context: Context): Bitmap? {
        context.contentResolver.openInputStream(this).use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)

            context.contentResolver.openInputStream(this).use { exifInputStream ->
                val ei = ExifInterface(exifInputStream!!)
                val orientation = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )

                return when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                    else -> bitmap
                }
            }
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun Bitmap.bitmapToByteArray(quality: Int = 100): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    companion object {
        private fun WordRespond.toWord() = Word(
            id = id,
            original = original,
            translate = translate,
            lang = lang,
            translateLang = translateLang,
            cefr = cefr,
            description = description,
            category = category,
            soundLink = soundLink,
            imageLink = imageLink
        )

        private fun SaveWord.toRequest() = WordRequest(
            original = word,
            lang = language,

            translate = translation,
            translateLang = translationLanguage,

            category = category,
            description = description,
            cefr = cefr
        )
    }
}