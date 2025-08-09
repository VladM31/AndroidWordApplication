package can.lucky.of.addword.ui.adapters

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import can.lucky.of.addword.databinding.BoxAnalyzeWordTaskResultBinding
import can.lucky.of.addword.domain.models.AiRecognizeResult
import can.lucky.of.addword.domain.models.enums.RecognizeStatus
import can.lucky.of.core.utils.dp
import java.time.format.DateTimeFormatter

class AiRecognizeResultAdapter(
    private val onOpenClick: (AiRecognizeResult) -> Unit = { _ -> }
) :
    PagingDataAdapter<AiRecognizeResult, AiRecognizeResultAdapter.AiRecognizeResultHolder>(
        AiRecognizeResultDiffCallback) {


    class AiRecognizeResultHolder(
        private val binding: BoxAnalyzeWordTaskResultBinding,
        private val onOpenClick: (AiRecognizeResult) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: AiRecognizeResult) {
            binding.createdAt.text = result.createdAt.format(DATE_TIME_FORMAT)
            binding.status.text = result.status.name

            setStatusColor(result)
            setButton(result)

            binding.originText.text =
                WORD_FORMAT.format(result.language.fixedShortName.uppercase(), result.word ?: "-")
            binding.translateText.text = WORD_FORMAT.format(
                result.translationLanguage.fixedShortName.uppercase(),
                result.translation ?: "-"
            )
        }

        private fun setButton(result: AiRecognizeResult){
            val visibility = if (result.status == RecognizeStatus.SUCCESS) {
                View.VISIBLE
            } else View.GONE
            binding.processResultBtn.visibility = visibility
            binding.processResultBtn.setOnClickListener {
                onOpenClick(result)
            }
        }

        private fun setStatusColor(result: AiRecognizeResult) {
            val color = COLOR_BY_STATUS[result.status] ?: can.lucky.of.core.R.color.primary_gray
            val original = binding.status.background as? GradientDrawable
            val drawableCopy =
                original?.mutate()?.constantState?.newDrawable()?.mutate() as? GradientDrawable
            drawableCopy?.setStroke(
                2.dp.toInt(),
                ContextCompat.getColor(binding.root.context, color)
            )
            drawableCopy?.setColor(ContextCompat.getColor(binding.root.context, color))
            binding.status.background = drawableCopy
        }
    }

    private object AiRecognizeResultDiffCallback : DiffUtil.ItemCallback<AiRecognizeResult>() {
        override fun areItemsTheSame(
            oldItem: AiRecognizeResult,
            newItem: AiRecognizeResult
        ): Boolean {
            return oldItem.recognizeId == newItem.recognizeId
        }

        override fun areContentsTheSame(
            oldItem: AiRecognizeResult,
            newItem: AiRecognizeResult
        ): Boolean {
            return oldItem == newItem
        }

    }

    companion object {
        private val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        private val COLOR_BY_STATUS = mapOf(
            RecognizeStatus.SUCCESS to can.lucky.of.core.R.color.primary_green_status,
            RecognizeStatus.FAILED to can.lucky.of.core.R.color.primary_red,
            RecognizeStatus.SEND to can.lucky.of.core.R.color.primary_yellow,
            RecognizeStatus.IN_PROGRESS to can.lucky.of.core.R.color.primary_violet
        )

        private val WORD_FORMAT = "(%s): %s"
    }

    override fun onBindViewHolder(holder: AiRecognizeResultHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiRecognizeResultHolder {
        return AiRecognizeResultHolder(
            BoxAnalyzeWordTaskResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onOpenClick
        )
    }
}