package can.lucky.of.exercise.domain.managers.impls

import android.util.Log
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.utils.toPair
import can.lucky.of.exercise.domain.managers.ExerciseStatisticalManager
import can.lucky.of.exercise.domain.models.data.EndExerciseTransaction
import can.lucky.of.exercise.domain.models.data.StartExerciseTransaction
import can.lucky.of.exercise.domain.models.data.WordCompleted
import can.lucky.of.exercise.net.clients.ExerciseStatisticalClient
import can.lucky.of.exercise.net.requests.EndExerciseTransactionRequest
import can.lucky.of.exercise.net.requests.StartExerciseTransactionRequest
import can.lucky.of.exercise.net.requests.WordCompletedRequest

internal class ExerciseStatisticalManagerImpl(
    private val client: ExerciseStatisticalClient,
    private val userManager: UserCacheManager
) : ExerciseStatisticalManager {
    override suspend fun startExercise(data: StartExerciseTransaction) {
        data.toRequest().runCatching {
            client.startExercise(this, userManager.toPair())
        }.onFailure {
            Log.e("ExerciseStatisticalManager", "Failed to start exercise: ${it.message}")
        }
    }

    override suspend fun completeWord(data: WordCompleted) {
        data.toRequest().runCatching {
            client.completeWord(this, userManager.toPair())
        }.onFailure {
            Log.e("ExerciseStatisticalManager", "Failed to complete word: ${it.message}")
        }
    }

    override suspend fun endExercise(data: EndExerciseTransaction) {
        data.toRequest().runCatching {
            client.endExercise(this, userManager.toPair())
        }.onFailure {
            Log.e("ExerciseStatisticalManager", "Failed to end exercise: ${it.message}")
        }
    }

    private fun StartExerciseTransaction.toRequest(): StartExerciseTransactionRequest {
        return StartExerciseTransactionRequest(
            transactionId = this.transactionId,
            exercises = this.exercises,
            createdAt = this.createdAt,
            words = this.words.map { it.toRequest() },
        )
    }

    private fun StartExerciseTransaction.Word.toRequest(): StartExerciseTransactionRequest.WordRequest {
        return StartExerciseTransactionRequest.WordRequest(
            wordId = this.wordId,
            userWordId = this.userWordId,
            grade = this.grade,
            dateOfAdded = this.dateOfAdded,
            lastReadDate = this.lastReadDate,
            original = this.original,
            translate = this.translate,
            lang = this.lang,
            translateLang = this.translateLang,
            cefr = this.cefr,
            hasDescription = this.hasDescription,
            category = this.category,
            hasSound = this.hasSound,
            hasImage = this.hasImage
        )
    }

    private fun EndExerciseTransaction.toRequest() : EndExerciseTransactionRequest {
        return EndExerciseTransactionRequest(
            transactionId = this.transactionId,
            endedAt = this.endedAt
        )
    }

    private fun WordCompleted.toRequest() : WordCompletedRequest {
        return WordCompletedRequest(
            transactionId = this.transactionId,
            wordId = this.wordId,
            userWordId = this.userWordId,
            exerciseId = this.exerciseId,
            attempts = this.attempts,
            isCorrect = this.isCorrect,
            completedAt = this.completedAt
        )
    }
}