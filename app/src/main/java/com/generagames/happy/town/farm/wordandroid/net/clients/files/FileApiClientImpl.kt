package com.generagames.happy.town.farm.wordandroid.net.clients.files

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import com.generagames.happy.town.farm.wordandroid.net.models.requests.files.AudioGenerationRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.files.SaveFileRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.UploadRespond
import com.generagames.happy.town.farm.wordandroid.utils.baseUrl
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Locale

class FileApiClientImpl(
    private val client: OkHttpClient,
    private val userCacheManager: UserCacheManager,
    private val gson: Gson = Gson()
) : FileApiClient {
    private val saveFileUrl by lazy { "${baseUrl()}/files/upload" }
    private val textToAudioUrl by lazy { "${baseUrl()}/files/text-to-audio" }

    override suspend fun uploadFile(request: SaveFileRequest): UploadRespond {
        return upload(request.content, request.fileName)
    }

    override suspend fun textToAudioFile(request: AudioGenerationRequest): UploadRespond {
        val json = gson.toJson(request)
        val body: RequestBody = json.toRequestBody("application/json".toMediaType())
        val httpRequest: Request = Request.Builder()
            .url(textToAudioUrl)
            .addHeader("Authorization", "Bearer ${userCacheManager.token.value}")
            .post(body)
            .build()
        client.newCall(httpRequest).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException(("Error: " + response.code) + " " + response.message)
            }

            return gson.fromJson(response.body?.string().orEmpty(), UploadRespond::class.java)
        }
    }


    @Throws(java.lang.Exception::class)
    fun upload(fileContent: ByteArray, filename: String): UploadRespond {
        val mimeType = guessMimeType(filename)

        val fileBody: RequestBody = fileContent.toRequestBody(mimeType.toMediaType())

        val requestBody: MultipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", filename, fileBody)
            .build()

        val request: Request = Request.Builder()
            .url(saveFileUrl)
            .addHeader("Authorization", "Bearer ${userCacheManager.token.value}")
            .post(requestBody)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException(("Error: " + response.code) + " " + response.message)
            }

            return gson.fromJson(response.body?.string().orEmpty(), UploadRespond::class.java)
        }
    }

    // Метод для подбора MIME-типа по расширению
    private fun guessMimeType(filename: String): String {
        val lower = filename.lowercase(Locale.getDefault())
        if (lower.endsWith(".webp")) return "image/webp"
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg"
        if (lower.endsWith(".png")) return "image/png"
        if (lower.endsWith(".wav")) return "audio/wav"
        return if (lower.endsWith(".mp3")) "audio/mpeg" else "application/octet-stream"
        // дефолтный тип
    }
}