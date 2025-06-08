package can.lucky.of.history.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import can.lucky.of.history.databinding.BoxLearningHistoryBinding
import can.lucky.of.history.domain.models.data.LearningHistory
import can.lucky.of.history.domain.models.enums.LearningHistoryType
import can.lucky.of.history.ui.mappers.LearningHistoryTypeMapper

internal class LearningHistoryAdapter : PagingDataAdapter<LearningHistory,LearningHistoryAdapter.LearningHistoryHolder>(LearningHistoryDiffCallback)  {

    class LearningHistoryHolder(
        private val binding: BoxLearningHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val date = binding.dateText.text.toString() + " ";
        private val original = binding.wordText.text.toString() + " ";
        private val type = binding.typeAndGradeText.text.toString() + " ";
        private val mapper = LearningHistoryTypeMapper();

        @SuppressLint("SetTextI18n")
        fun bind(model: LearningHistory) {
            binding.dateText.text = date + model.date
            binding.wordText.text = original + model.original
            binding.typeAndGradeText.text = type + mapper.toUiString(model.type)

            if (model.type != LearningHistoryType.CREATE) {
                binding.typeAndGradeText.apply {
                    text = text.toString() + ", grade = " + model.grade
                }
            }
        }
    }

    private object LearningHistoryDiffCallback : DiffUtil.ItemCallback<LearningHistory>() {
        override fun areItemsTheSame(oldItem: LearningHistory, newItem: LearningHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LearningHistory, newItem: LearningHistory): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearningHistoryHolder {
        val binding = BoxLearningHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LearningHistoryHolder(binding)
    }



    override fun onBindViewHolder(holder: LearningHistoryHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}