package com.generagames.happy.town.farm.wordandroid.domain.managers.word

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.net.toUri
import can.lucky.of.core.domain.managers.word.WordManager
import can.lucky.of.core.domain.models.filters.PageFilter
import com.generagames.happy.town.farm.wordandroid.net.clients.word.WordClient
import can.lucky.of.core.domain.models.data.words.InsertWord
import can.lucky.of.core.domain.models.data.words.PinUserWord
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.filters.WordFilter
import com.generagames.happy.town.farm.wordandroid.net.models.requests.PinUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.WordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.WordRespond

import java.io.ByteArrayOutputStream

class WordManagerImpl(
    private val wordClient: WordClient,
    private val context: Context
) : WordManager {
    override suspend fun findBy(filter: WordFilter): List<Word> {
        return wordClient.findBy(filter).map(this::toWord)
    }

    override suspend fun insert(words: Collection<InsertWord>): IntArray {
        if (words.isEmpty()) return intArrayOf()

        return wordClient.save(words.map(this::toWordRequest))
    }

    override suspend fun findOne(filter: WordFilter): Word? {
        val newFilter = filter.copy(pagination = PageFilter(size = 1))
        return findBy(newFilter).firstOrNull()
    }

    override suspend fun pin(words: Collection<PinUserWord>): IntArray {
        return words.map {
            PinUserWordRequest(
                wordId = it.wordId,
                sound = it.sound,
                image = it.image?.toUri()?.fixImageOrientationFromUri(context)?.bitmapToByteArray()
            )
        }.let {
            wordClient.pin(it)
        }
    }

    private fun toWord(res: WordRespond): Word {
        return Word(
            id = res.id,
            original = res.original,
            translate = res.translate,
            lang = res.lang,
            translateLang = res.translateLang,
            cefr = res.cefr,
            description = res.description,
            category = res.category,
            soundLink = res.soundLink,
            imageLink = res.imageLink
        )
    }

    private fun toWordRequest(w: InsertWord): WordRequest {
        return WordRequest(
            word = w.word,
            translation = w.translation,
            language = w.language,
            translationLanguage = w.translationLanguage,
            category = w.category,
            sound = w.sound,
            image = w.image?.toUri()?.fixImageOrientationFromUri(context)?.bitmapToByteArray(),
            needSound = w.needSound,
            cefr = w.cefr,
            description = w.description
        )
    }

    private fun Uri.fixImageOrientationFromUri(context: Context): Bitmap? {
        context.contentResolver.openInputStream(this).use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)
//            inputStream?.reset()

            context.contentResolver.openInputStream(this).use { exifInputStream ->
                val ei = ExifInterface(exifInputStream!!)
                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

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
}