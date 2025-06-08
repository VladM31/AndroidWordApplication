package can.lucky.of.core.net.responses

data class StatisticsLearningHistoryResponse(
    val count: Int,
    val grades: Long,
    val type: String,
    val date: String
)
