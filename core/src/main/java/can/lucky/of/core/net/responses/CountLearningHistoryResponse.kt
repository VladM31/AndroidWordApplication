package can.lucky.of.core.net.responses

import can.lucky.of.core.domain.models.enums.LearningHistoryType

data class CountLearningHistoryResponse(
    val count: Int,
    val type: LearningHistoryType
)
