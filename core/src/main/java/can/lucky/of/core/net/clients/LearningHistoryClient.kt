package can.lucky.of.core.net.clients

import can.lucky.of.core.net.responses.CountLearningHistoryResponse
import can.lucky.of.core.net.responses.LearningHistoryResponse
import can.lucky.of.core.net.responses.StatisticsLearningHistoryResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface LearningHistoryClient {

    @GET("storage/learning-history")
    suspend fun getLearningHistory(
        @Header("Authorization") token: String,
        @QueryMap query: Map<String,String>,
        @Query(value = "wordIds") wordIds: Collection<String>?
    ): List<LearningHistoryResponse>

    @GET("storage/learning-history/statistics")
    suspend fun getLearningHistoryStatistic(
        @Header("Authorization") token: String,
        @QueryMap query: Map<String,String>,
    ): List<StatisticsLearningHistoryResponse>

    @GET("storage/learning-history/count")
    suspend fun getCount(
        @Header("Authorization") token: String
    ): List<CountLearningHistoryResponse>
}