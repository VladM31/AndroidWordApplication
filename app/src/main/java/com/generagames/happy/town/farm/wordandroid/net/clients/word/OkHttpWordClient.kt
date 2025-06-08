package com.generagames.happy.town.farm.wordandroid.net.clients.word

import android.util.Log
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.filters.WordFilter
import com.generagames.happy.town.farm.wordandroid.net.models.requests.FileRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.PinUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.WordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.WordRespond
import com.generagames.happy.town.farm.wordandroid.utils.LanguageSerializer
import com.generagames.happy.town.farm.wordandroid.utils.baseUrl
import com.generagames.happy.town.farm.wordandroid.utils.fetch
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class OkHttpWordClient(
    private val client: OkHttpClient,
    private val userCacheManager: UserCacheManager
) : WordClient {
    private val jsonMapper = GsonBuilder().registerTypeAdapter(Language::class.java, LanguageSerializer()).create()
    private val saveUrl by lazy {"${baseUrl()}/storage-api/words/".toHttpUrl() }
    private val pinUrl by lazy {"${baseUrl()}/storage-api/words/pin".toHttpUrl() }
    private val findByUrl by lazy {"${baseUrl()}/storage/words/?"}

    override suspend fun save(words: Collection<WordRequest>): IntArray {
        return saveFiles(saveUrl, words)
    }

    override suspend fun pin(words: Collection<PinUserWordRequest>): IntArray {
        return saveFiles(pinUrl, words)
    }

    private fun saveFiles(url : HttpUrl, wordFiles: Collection<FileRequest>): IntArray {
        if (wordFiles.isEmpty()) return intArrayOf()
        val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

        wordFiles.forEachIndexed { index, word ->
            word.image?.let {
                multipartBuilder.addFormDataPart("images[$index]", "image.jpeg", it.toRequestBody("image/*".toMediaType()))
            }
            word.sound?.let {
                multipartBuilder.addFormDataPart("sounds[$index]", it.name, it.asRequestBody("audio/mpeg".toMediaType()))
            }

            word.image = null
            word.sound = null
            multipartBuilder.addFormDataPart("words[$index]", jsonMapper.toJson(word))
        }

        val multipartBody = multipartBuilder.build()

        val request = Request.Builder()
            .url(url)
            .post(multipartBody)
            .addHeader("Authorization", "Bearer ${userCacheManager.token.value}")
            .build()

        return try {
            return client.fetch(request, intArrayOf(), jsonMapper, "OkHttpWordClient")
        } catch (e: Exception) {
            Log.d("OkHttpWordClient", "Error: ${e.message}")
            intArrayOf()
        }
    }

    override suspend fun findBy(filter: WordFilter): List<WordRespond> {
        val url = findByUrl + filter.toQuery(jsonMapper)

        val request = Request.Builder().url(url).get().build()

        return try {
            return client.fetch(request, arrayOf<WordRespond>(), jsonMapper, "OkHttpWordClient").toList()
        } catch (e: Exception) {
            Log.d("OkHttpWordClient", "Error: ${e.message}")
            listOf()
        }
    }
}