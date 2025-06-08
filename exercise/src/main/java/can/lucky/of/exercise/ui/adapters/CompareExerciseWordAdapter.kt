package can.lucky.of.exercise.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import can.lucky.of.core.R
import can.lucky.of.core.databinding.BoxTextContentBinding
import can.lucky.of.exercise.domain.models.data.CompareWordBox
import can.lucky.of.exercise.ui.adapters.diff.CompareWordBoxDiffCallback

internal class CompareExerciseWordAdapter(
    isOriginal: Boolean,
    context: Context,
    isEnable: () -> Boolean,
    onClick: (id: String, index: Int) -> Unit
) : RecyclerView.Adapter<CompareExerciseWordAdapter.ExerciseViewHolder>() {
    private var compareBoxes: List<CompareWordBox> = mutableListOf()
    private val state: CompareExerciseWordState = CompareExerciseWordState(
        isOriginal = isOriginal,
        onClick = onClick,
        primaryColor = context.resources.getColor(R.color.primary_green, context.theme),
        primaryBack = context.resources.getColor(R.color.primary_back, context.theme),
        primaryRed = context.resources.getColor(R.color.primary_red, context.theme),
        isEnable = isEnable
    )

    class ExerciseViewHolder(
        private val binding: BoxTextContentBinding,
        private val state: CompareExerciseWordState
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(compareBox: CompareWordBox) {

            if (state.isOriginal) {
                binding.content.text = compareBox.word.original
            } else {
                binding.content.text = compareBox.word.translate
            }
            if (compareBox.isMistake) {
                binding.root.setBackgroundResource(R.drawable.box_red_back)
                binding.content.setTextColor(state.primaryRed)
            } else if(compareBox.position != null) {
                binding.root.setBackgroundResource(R.drawable.button_back)
                binding.content.setTextColor(state.primaryBack)
            }else{
                binding.root.setBackgroundResource(R.drawable.box_back)
                binding.content.setTextColor(state.primaryColor)
            }

            binding.root.setOnClickListener {
                if (state.isEnable().not()) {
                    return@setOnClickListener
                }
                state.onClick(compareBox.word.wordId, adapterPosition)
                binding.root.setBackgroundResource(R.drawable.button_back)
                binding.content.setTextColor(state.primaryBack)
            }
        }
    }

    data class CompareExerciseWordState(
        val isOriginal: Boolean,
        val onClick: (id: String, index: Int) -> Unit,
        val primaryColor: Int,
        val primaryBack: Int,
        val primaryRed: Int,
        val isEnable: () -> Boolean
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = BoxTextContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding, state)
    }

    override fun getItemCount(): Int = compareBoxes.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(compareBoxes[position])
    }

    fun notifyDataChanged(compareBoxes: List<CompareWordBox>) {
        val diffCallback = CompareWordBoxDiffCallback(this.compareBoxes, compareBoxes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.compareBoxes = compareBoxes
        diffResult.dispatchUpdatesTo(this)
    }
}