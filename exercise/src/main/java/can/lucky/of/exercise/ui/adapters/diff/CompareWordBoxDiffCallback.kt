package can.lucky.of.exercise.ui.adapters.diff

import androidx.recyclerview.widget.DiffUtil
import can.lucky.of.exercise.domain.models.data.CompareWordBox

internal class CompareWordBoxDiffCallback(
    private val oldList: List<CompareWordBox>,
    private val newList: List<CompareWordBox>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].word.wordId == newList[newItemPosition].word.wordId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}