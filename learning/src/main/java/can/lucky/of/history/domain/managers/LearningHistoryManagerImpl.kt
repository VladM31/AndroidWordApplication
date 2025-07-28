package can.lucky.of.history.domain.managers

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.LearningHistoryType
import can.lucky.of.history.domain.models.data.CountLearningHistory
import can.lucky.of.history.domain.models.data.LearningHistory
import can.lucky.of.history.domain.models.data.StatisticsLearningHistory
import can.lucky.of.history.domain.models.filters.LearningHistoryFilter
import can.lucky.of.history.domain.models.filters.StatisticsLearningHistoryFilter
import can.lucky.of.core.net.clients.LearningHistoryClient
import can.lucky.of.core.net.responses.CountLearningHistoryResponse
import can.lucky.of.core.net.responses.LearningHistoryResponse
import can.lucky.of.core.net.responses.StatisticsLearningHistoryResponse
import com.google.gson.Gson
import java.time.LocalDate

internal class LearningHistoryManagerImpl(
    private val client: LearningHistoryClient,
    private val userCacheManager: UserCacheManager
) : LearningHistoryManager {
    private val gson = Gson()
    private val token: String
        get() = userCacheManager.token.value

    override suspend fun getLearningHistory(filter: LearningHistoryFilter): List<LearningHistory> {
        val query = filter.toQueryMap(gson) - "wordIds"

        return client.getLearningHistory(token, query, filter.wordIds)
            .filter { it.cefr != "Unknown CEFR" }
            .map { it.toModel() }
    }

    override suspend fun getLearningHistoryStatistic(filter: StatisticsLearningHistoryFilter): List<StatisticsLearningHistory> {
        val query = filter.toQueryMap(gson)

        return client.getLearningHistoryStatistic(token, query).map { it.toModel() }
    }

    override suspend fun getCount(): List<CountLearningHistory> {
        return client.getCount(token).map { it.toModel() }
    }

    private fun LearningHistoryResponse.toModel(): LearningHistory {
        return LearningHistory(
            id = id,
            wordId = wordId,
            grade = grade,
            date = LocalDate.parse(date),
            type = LearningHistoryType.valueOf(type),
            original = original,
            nativeLang = Language.fromShortName(nativeLang),
            learningLang = Language.fromShortName(learningLang),
            cefr = CEFR.valueOf(cefr)
        )
    }

    private fun StatisticsLearningHistoryResponse.toModel(): StatisticsLearningHistory {
        return StatisticsLearningHistory(
            count = count,
            grades = grades,
            date = LocalDate.parse(date),
            type = LearningHistoryType.valueOf(type)
        )
    }

    private fun CountLearningHistoryResponse.toModel(): CountLearningHistory {
        return CountLearningHistory(
            count = count,
            type = type
        )
    }
}