package can.lucky.of.exercise.domain.managers

import can.lucky.of.exercise.domain.models.data.EndExerciseTransaction
import can.lucky.of.exercise.domain.models.data.StartExerciseTransaction
import can.lucky.of.exercise.domain.models.data.WordCompleted


internal interface ExerciseStatisticalManager {

    suspend fun startExercise(data: StartExerciseTransaction);

    suspend fun completeWord(data: WordCompleted);

    suspend fun endExercise(data: EndExerciseTransaction);
}