package can.lucky.of.exercise.domain.models.data

import can.lucky.of.core.domain.models.enums.Exercise


internal data class ExerciseSelection(
    val exercise: Exercise,
    val number: Int? = null,
): Comparable<ExerciseSelection> {
    override fun compareTo(other: ExerciseSelection): Int {
        if (number != null && other.number == null) {
            return -1
        }
        if (number == null && other.number != null) {
            return 1
        }
        return number?.compareTo(other.number ?: 0) ?: exercise.compareTo(other.exercise)
    }
}
