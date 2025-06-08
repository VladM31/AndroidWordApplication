package can.lucky.of.exercise.domain.models.data


internal data class CompareWordBox(
    val word: ExerciseWordDetails,
    val isMistake: Boolean = false,
    val position: Int? = null
) : Comparable<CompareWordBox> {
    override fun compareTo(other: CompareWordBox): Int {
        val thisPosition = position ?: -1
        val otherPosition = other.position ?: -1

        return otherPosition.compareTo(thisPosition)
    }
}
