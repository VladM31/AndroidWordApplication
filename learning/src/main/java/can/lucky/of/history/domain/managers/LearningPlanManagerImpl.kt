package can.lucky.of.history.domain.managers

import can.lucky.of.core.domain.factories.HttpOkHeaderFactory
import can.lucky.of.core.domain.managers.LearningPlanManager
import can.lucky.of.core.domain.models.data.LearningPlan
import can.lucky.of.core.net.clients.LearningPlanClient
import can.lucky.of.core.net.requests.LearningPlanRequest
import can.lucky.of.core.net.responses.LearningPlanResponse
import java.io.EOFException

internal class LearningPlanManagerImpl(
    private val httpOkHeaderFactory: HttpOkHeaderFactory,
    private val learningPlanClient: LearningPlanClient
) : LearningPlanManager {
    private val token: String
        get() = httpOkHeaderFactory.createHeaders()["Authorization"].orEmpty()

    override suspend fun fetchLearningPlan(): LearningPlan? {
        return try {
            learningPlanClient.getPlan(token)?.toModel()
        } catch (e: EOFException) {
            null
        }
    }

    override suspend fun createLearningPlan(learningPlan: LearningPlan): LearningPlan {
        return learningPlanClient.createPlan(token, learningPlan.toRequest()).toModel()
    }

    override suspend fun updateLearningPlan(learningPlan: LearningPlan) {
        learningPlanClient.updatePlan(token, learningPlan.toRequest())
    }

    private fun LearningPlanResponse.toModel(): LearningPlan {
        return LearningPlan(
            wordsPerDay = wordsPerDay,
            nativeLang = nativeLang,
            learningLang = learningLang,
            cefr = cefr,
            createdAt = createdAt
        )
    }

    private fun LearningPlan.toRequest(): LearningPlanRequest {
        return LearningPlanRequest(
            wordsPerDay = wordsPerDay,
            nativeLang = nativeLang,
            learningLang = learningLang,
            cefr = cefr
        )
    }
}