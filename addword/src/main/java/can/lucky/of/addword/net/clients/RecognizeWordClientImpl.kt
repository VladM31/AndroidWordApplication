package can.lucky.of.addword.net.clients

import can.lucky.of.addword.net.models.requests.RecognizeWordByPictureRequest
import can.lucky.of.addword.net.models.requests.RecognizeWordByTextRequest
import can.lucky.of.addword.net.models.responses.RecognizeWordResponse
import can.lucky.of.core.domain.factories.HttpOkHeaderFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import com.google.gson.Gson
import okhttp3.Headers
import okhttp3.RequestBody.Companion.toRequestBody


internal class RecognizeWordClientImpl(
    private val okHttpClient: OkHttpClient,
    private val imagePath: String,
    private val textPath: String,
    private val headerFactory: HttpOkHeaderFactory,
) : RecognizeWordClient {
    private val gson = Gson()

    override suspend fun recognize(request: RecognizeWordByPictureRequest): RecognizeWordResponse {
        val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        multipartBuilder.addFormDataPart(
            "image",
            request.image.name,
            request.image.asRequestBody("image/*".toMediaType())
        )
        multipartBuilder.addFormDataPart("language", request.language)
        multipartBuilder.addFormDataPart("translationLanguage", request.translationLanguage)

        val req: Request = Request.Builder()
            .url(imagePath)
            .post(multipartBuilder.build())
            .headers(headerFactory.createHeaders())
            .build()

        return okHttpClient.newCall(req).execute().use { response ->
            val body = response.body?.string()
            gson.fromJson(body, RecognizeWordResponse::class.java)
        }
    }

    override suspend fun recognize(request: RecognizeWordByTextRequest): RecognizeWordResponse {
        val json = gson.toJson(request)
        val req: Request = Request.Builder()
            .url(textPath)
            .post(json.toRequestBody("application/json".toMediaType()))
            .headers(headerFactory.createHeaders())
            .build()

        return okHttpClient.newCall(req).execute().use { response ->
            if (response.code > 299) {
                throw Exception("Error code: ${response.code}, message: ${response.message}")
            }
            val body = response.body?.string() ?: throw Exception("Empty response")
            gson.fromJson(body, RecognizeWordResponse::class.java)
        }
    }
}