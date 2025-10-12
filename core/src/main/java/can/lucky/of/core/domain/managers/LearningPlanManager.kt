package can.lucky.of.core.domain.managers

import can.lucky.of.core.domain.models.data.LearningPlan

interface LearningPlanManager {

    suspend fun fetchLearningPlan(): LearningPlan?

    suspend fun createLearningPlan(learningPlan: LearningPlan): LearningPlan

    suspend fun updateLearningPlan(learningPlan: LearningPlan)
}