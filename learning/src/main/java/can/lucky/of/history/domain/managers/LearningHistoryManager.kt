package can.lucky.of.history.domain.managers

import can.lucky.of.core.domain.models.PagedModels
import can.lucky.of.history.domain.models.data.CountLearningHistory
import can.lucky.of.history.domain.models.data.LearningHistory
import can.lucky.of.history.domain.models.data.StatisticsLearningHistory
import can.lucky.of.history.domain.models.filters.LearningHistoryFilter
import can.lucky.of.history.domain.models.filters.StatisticsLearningHistoryFilter

internal interface LearningHistoryManager {

    suspend fun getLearningHistory(filter: LearningHistoryFilter): PagedModels<LearningHistory>

    suspend fun getLearningHistoryStatistic(filter: StatisticsLearningHistoryFilter): PagedModels<StatisticsLearningHistory>

    suspend fun getCount(): PagedModels<CountLearningHistory>
}