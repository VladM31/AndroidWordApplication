package com.generagames.happy.town.farm.wordandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import can.lucky.of.core.domain.models.data.words.PinnedWord
import can.lucky.of.core.domain.models.data.words.UserWord
import com.generagames.happy.town.farm.wordandroid.databinding.BoxWordBinding
import can.lucky.of.core.R as CoreR

class PinnedWordAdapter(
    private val words: List<PinnedWord>,
    private val isSelected: (String) -> Boolean,
    private val selectWord: (String,Int) -> Unit,
    private val openWord: (UserWord) -> Unit
) : RecyclerView.Adapter<PinnedWordAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(
        private val binding: BoxWordBinding,
        private val isSelected: (String) -> Boolean,
        private val selectWord: (String,Int) -> Unit,
        private val openWord: (UserWord) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val textTemplate = binding.root.context.getString(CoreR.string.text_template)
        private val categoryTemplate = binding.root.context.getString(CoreR.string.category_template)

        fun bind(pinnedWord: PinnedWord) {
            val word = pinnedWord.userWord.word

            binding.category.text = categoryTemplate.format(word.category.orEmpty())
            binding.originText.text = textTemplate.format(word.lang.fixedShortName, word.original)
            binding.translateText.text =
                textTemplate.format(word.translateLang.fixedShortName, word.translate)

            binding.hasImage.visibility =  (word.imageLink != null).toVisibility()
            binding.hasSound.visibility = (word.soundLink != null).toVisibility()

            binding.checkMark.visibility = isSelected(pinnedWord.userWord.id).toVisibility()

            binding.openWordButton.setOnClickListener {
                openWord(pinnedWord.userWord)
            }

            binding.root.setOnLongClickListener {
                selectWord(pinnedWord.userWord.id,bindingAdapterPosition)
                binding.checkMark.visibility = isSelected(pinnedWord.userWord.id).toVisibility()

                true
            }
        }

        private fun Boolean.toVisibility(): Int {
            return if (this) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        return DetailsViewHolder(
            binding = BoxWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            isSelected = isSelected,
            selectWord = selectWord,
            openWord = openWord
        )
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(words[position])
    }
}