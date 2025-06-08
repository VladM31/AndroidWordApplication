package can.lucky.of.exercise.ui.adapters.diff

import androidx.recyclerview.widget.DiffUtil
import can.lucky.of.exercise.domain.models.data.ExerciseSelection


internal class ExerciseSelectionDiffCallback(
    private val oldList: List<ExerciseSelection>,
    private val newList: List<ExerciseSelection>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].exercise == newList[newItemPosition].exercise
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}