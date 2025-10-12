package can.lucky.of.history.domain.models.data

import can.lucky.of.core.domain.models.enums.LearningHistoryType

internal data class CountLearningHistory(
    val count: Int,
    val type: LearningHistoryType
)
