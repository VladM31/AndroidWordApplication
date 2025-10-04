package can.lucky.of.core.net.responses

import can.lucky.of.core.domain.models.enums.LearningHistoryType

data class StatisticsLearningHistoryResponse(
    val count: Int,
    val grades: Long,
    val type: LearningHistoryType,
    val date: String
)
