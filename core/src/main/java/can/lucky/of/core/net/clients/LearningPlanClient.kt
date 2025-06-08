package can.lucky.of.core.net.clients

import can.lucky.of.core.net.requests.LearningPlanRequest
import can.lucky.of.core.net.responses.LearningPlanResponse
import can.lucky.of.core.net.responses.UpdateLearningPlanResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface LearningPlanClient {

    @GET("storage/learning-plan")
    suspend fun getPlan(@Header("Authorization") token: String): LearningPlanResponse?

    @POST("storage/learning-plan")
    suspend fun createPlan(@Header("Authorization") token: String,@Body learningPlan: LearningPlanRequest): LearningPlanResponse

    @PUT("storage/learning-plan")
    suspend fun updatePlan(@Header("Authorization") token: String,@Body learningPlan: LearningPlanRequest): UpdateLearningPlanResponse
}