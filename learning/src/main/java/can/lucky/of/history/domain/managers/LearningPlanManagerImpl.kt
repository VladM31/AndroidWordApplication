package can.lucky.of.history.domain.managers

import can.lucky.of.core.domain.factories.HttpOkHeaderFactory
import can.lucky.of.core.domain.managers.LearningPlanManager
import can.lucky.of.core.domain.models.data.LearningPlan
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
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

    override suspend fun updateLearningPlan(learningPlan: LearningPlan): Int {
        return learningPlanClient.updatePlan(token, learningPlan.toRequest()).code
    }

    private fun LearningPlanResponse.toModel(): LearningPlan {
        return LearningPlan(
            wordsPerDay = wordsPerDay,
            nativeLang = Language.fromShortName(nativeLang),
            learningLang = Language.fromShortName(learningLang),
            cefr = CEFR.valueOf(cefr),
            dateOfCreation = dateOfCreation
        )
    }

    private fun LearningPlan.toRequest(): LearningPlanRequest {
        return LearningPlanRequest(
            wordsPerDay = wordsPerDay,
            nativeLang = nativeLang.shortName,
            learningLang = learningLang.shortName,
            cefr = cefr.name
        )
    }
}