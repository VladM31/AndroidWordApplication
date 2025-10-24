package can.lucky.of.exercise.net.clients.impls

import android.util.Log
import can.lucky.of.exercise.net.clients.ExerciseStatisticalClient
import can.lucky.of.exercise.net.requests.EndExerciseTransactionRequest
import can.lucky.of.exercise.net.requests.StartExerciseTransactionRequest
import can.lucky.of.exercise.net.requests.WordCompletedRequest
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

internal class OkHttpExerciseStatisticalClient(
    private val okHttpClient : okhttp3.OkHttpClient
) : ExerciseStatisticalClient {
    private val gson = Gson()
    private val baseUrl = "http://vps2498837.fastwebserver.de:8030/api/v1/statistical"

    override suspend fun startExercise(request: StartExerciseTransactionRequest, vararg additionalHeaders: Pair<String, String>) {
        executeRequest(request, "/start", additionalHeaders)
    }

    override suspend fun completeWord(request: WordCompletedRequest, vararg additionalHeaders: Pair<String, String> ) {
        executeRequest(request, "/word-completed", additionalHeaders)
    }

    override suspend fun endExercise(request: EndExerciseTransactionRequest, vararg additionalHeaders: Pair<String, String> ) {
        executeRequest(request, "/end", additionalHeaders)
    }

    private fun executeRequest(
        body: Any,
        endpoint: String,
        additionalHeaders: Array<out Pair<String, String>>
    ) {
        val json = gson.toJson(body)

        val headerBuilder = okhttp3.Headers.Builder()
        additionalHeaders.forEach {
            headerBuilder.add(it.first, it.second)
        }

        val httpRequest = okhttp3.Request.Builder()
            .url("$baseUrl$endpoint")
            .post(json.toRequestBody("application/json".toMediaType()))
            .headers(headerBuilder.build())
            .build()

        okHttpClient.newCall(httpRequest).execute().use { response ->
            Log.d("OkHttpExerciseStatisticalClient", "Response: ${response.code} - ${response.message}")
        }
    }
}