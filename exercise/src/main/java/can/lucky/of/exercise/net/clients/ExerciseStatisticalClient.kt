package can.lucky.of.exercise.net.clients

import can.lucky.of.exercise.net.requests.EndExerciseTransactionRequest
import can.lucky.of.exercise.net.requests.StartExerciseTransactionRequest
import can.lucky.of.exercise.net.requests.WordCompletedRequest

internal interface ExerciseStatisticalClient {

    suspend fun startExercise(request: StartExerciseTransactionRequest, vararg additionalHeaders: Pair<String, String> = emptyArray());

    suspend fun completeWord(request: WordCompletedRequest, vararg additionalHeaders: Pair<String, String> = emptyArray());

    suspend fun endExercise(request: EndExerciseTransactionRequest, vararg additionalHeaders: Pair<String, String> = emptyArray());
}