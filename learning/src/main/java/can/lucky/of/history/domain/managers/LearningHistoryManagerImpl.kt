package can.lucky.of.history.domain.managers

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.PagedModels
import can.lucky.of.core.net.clients.LearningHistoryClient
import can.lucky.of.core.net.responses.CountLearningHistoryResponse
import can.lucky.of.core.net.responses.LearningHistoryResponse
import can.lucky.of.core.net.responses.StatisticsLearningHistoryResponse
import can.lucky.of.history.domain.models.data.CountLearningHistory
import can.lucky.of.history.domain.models.data.LearningHistory
import can.lucky.of.history.domain.models.data.StatisticsLearningHistory
import can.lucky.of.history.domain.models.filters.LearningHistoryFilter
import can.lucky.of.history.domain.models.filters.StatisticsLearningHistoryFilter
import com.google.gson.Gson
import java.time.LocalDate

internal class LearningHistoryManagerImpl(
    private val client: LearningHistoryClient,
    private val userCacheManager: UserCacheManager
) : LearningHistoryManager {
    private val gson = Gson()
    private val token: String
        get() = userCacheManager.token.value

    override suspend fun getLearningHistory(filter: LearningHistoryFilter): PagedModels<LearningHistory> {
        val query = filter.toQueryMap(gson) - "wordIds"
        val respond = client.getLearningHistory(token, query, filter.wordIds);
        return PagedModels.of(respond) { it.toModel() }
    }

    override suspend fun getLearningHistoryStatistic(filter: StatisticsLearningHistoryFilter): PagedModels<StatisticsLearningHistory> {
        val query = filter.toQueryMap(gson)
        val respond = client.getLearningHistoryStatistic(token, query);

        return PagedModels.of(respond) { it.toModel() }
    }

    override suspend fun getCount(): PagedModels<CountLearningHistory> {
        return PagedModels.of(client.getCount(token)) { it.toModel() }
    }

    private fun LearningHistoryResponse.toModel(): LearningHistory {
        return LearningHistory(
            id = id,
            wordId = wordId,
            grade = grade,
            date = LocalDate.parse(date),
            type = type,
            original = original,
            nativeLang = nativeLang,
            learningLang = learningLang,
            cefr = cefr
        )
    }

    private fun StatisticsLearningHistoryResponse.toModel(): StatisticsLearningHistory {
        return StatisticsLearningHistory(
            count = count,
            grades = grades,
            date = LocalDate.parse(date),
            type = type
        )
    }

    private fun CountLearningHistoryResponse.toModel(): CountLearningHistory {
        return CountLearningHistory(
            count = count,
            type = type
        )
    }
}