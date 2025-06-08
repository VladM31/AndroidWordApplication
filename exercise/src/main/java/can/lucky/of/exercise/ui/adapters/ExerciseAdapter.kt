package can.lucky.of.exercise.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import can.lucky.of.exercise.domain.models.data.ExerciseSelection
import can.lucky.of.exercise.ui.controllers.ExerciseSelectionController
import can.lucky.of.exercise.databinding.BoxExerciseSelectionBinding
import can.lucky.of.exercise.ui.adapters.diff.ExerciseSelectionDiffCallback
import can.lucky.of.core.R as CoreR

internal class ExerciseAdapter(
    private val controller: ExerciseSelectionController,
    context: Context
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {
    @SuppressLint("UseCompatLoadingForDrawables")
    private val selectedColorState = ColorState(
        text = context.getColorStateList(CoreR.color.primary_back),
        background = context.getDrawable(CoreR.drawable.button_back)
    )

    @SuppressLint("UseCompatLoadingForDrawables")
    private val unselectedColorState = ColorState(
        text = context.getColorStateList(CoreR.color.primary_green),
        background = context.getDrawable(CoreR.drawable.box_back)
    )
    private var exercises: List<ExerciseSelection> = controller.exercises

    class ExerciseViewHolder(
        private val binding: BoxExerciseSelectionBinding,
        private val selectedColorState: ColorState,
        private val unselectedColorState: ColorState,
        private val controller: ExerciseSelectionController
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(exerciseSelection: ExerciseSelection) {

            val colorState = if (controller.isSelected(exerciseSelection.exercise)) {
                selectedColorState
            } else {
                unselectedColorState
            }

            with(binding) {
                binding.content.text =
                    getNumber(exerciseSelection.number) + exerciseSelection.exercise.text
                root.background = colorState.background
                content.setTextColor(colorState.text)
            }

            binding.root.setOnClickListener {
                if (controller.isSelected(exerciseSelection.exercise)) {
                    controller.unselect(exerciseSelection.exercise)
                } else {
                    controller.select(exerciseSelection.exercise)
                }
            }
        }

        private fun getNumber(number: Int?): String {
            val newText = number?.toString() ?: ""
            return if (newText.isEmpty()) newText else "$newText. "
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = BoxExerciseSelectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExerciseViewHolder(
            binding, selectedColorState,
            unselectedColorState, controller
        )
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }


    fun notifyDataChanged() {
        val diffCallback = ExerciseSelectionDiffCallback(exercises, controller.exercises)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        exercises = controller.exercises
        diffResult.dispatchUpdatesTo(this)
    }

    data class ColorState(
        val text: ColorStateList,
        val background: Drawable?
    )
}