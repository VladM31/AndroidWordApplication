package can.lucky.of.auth.net.clients


import can.lucky.of.auth.net.models.requests.LoginRequest
import can.lucky.of.auth.net.models.requests.SignUpRequest
import can.lucky.of.auth.net.models.responses.AuthResponse
import can.lucky.of.auth.net.models.responses.SignUpResponse
import can.lucky.of.core.domain.storages.BaseUrlStore
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

internal class OkHttpAuthClient(
    private val client: OkHttpClient,
    private val baseUrlStore: BaseUrlStore
) : AuthClient {
    private val jsonMapper = Gson()
    private val logInUrl by lazy { "${baseUrlStore.getBaseUrl()}/auth/login".toHttpUrl() }
    private val signUpUrl by lazy {"${baseUrlStore.getBaseUrl()}/auth/sign-up".toHttpUrl()}
    private val isRegisteredUrl by lazy {"${baseUrlStore.getBaseUrl()}/auth/is-registered".toHttpUrl()}

    override suspend fun logIn(req: LoginRequest): AuthResponse {
        val request = postReq(logInUrl, req)

        return try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful.not()){
                throw RuntimeException("Error: ${response.code}, response.body: ${response.body?.string()}")
            }

            response.body?.let {
                jsonMapper.fromJson(it.string(), AuthResponse::class.java)
            } ?: throw RuntimeException("Error: ${response.code}, response.body: ${response.body}")
        } catch (e: Exception) {
            AuthResponse(
                error = AuthResponse.Error(
                    message = "Error response.body: ${e.message}"
                )
            )
        }
    }

    override suspend fun signUp(req: SignUpRequest): SignUpResponse {
        val request = postReq(signUpUrl, req)

        return try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful.not()){
                return SignUpResponse.error(message = "Error: ${response.code}, response.body: ${response.body}")
            }

            val body = response.body?.string()

            body?.let {
                SignUpResponse(
                    it.toBoolean()
                )
            } ?: SignUpResponse.error(message = "Error: ${response.code}")
        } catch (e: Exception) {
            SignUpResponse.error(message = "Error ${e.message}")
        }
    }

    override suspend fun isRegistered(phoneNumber: String): Boolean {
        val url = isRegisteredUrl.newBuilder().addQueryParameter("phoneNumber", phoneNumber).build()
        val request = Request.Builder().url(url).get().build()

        return try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful.not()){
                throw RuntimeException("Error: ${response.code}, response.body: ${response.body?.string()}")
            }

            response.body?.string().toBoolean()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun parseToken(token: String): AuthResponse.User {
        TODO("Not yet implemented")
    }

    private fun postReq(url : HttpUrl, body: Any) : Request{
        return Request.Builder().url(url)
            .addHeader("Content-Type", "application/json")
            .post(jsonMapper.toJson(body).toRequestBody()).build()
    }
}