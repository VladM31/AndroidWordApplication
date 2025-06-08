package com.generagames.happy.town.farm.wordandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.WordsAction
import com.generagames.happy.town.farm.wordandroid.databinding.BoxWordBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.states.WordsState
import can.lucky.of.core.R

class WordAdapter(
    private val viewModel: MviViewModel<WordsState, WordsAction>,
    private val onClickListener: (Word) -> Unit
) : PagingDataAdapter<Word, WordAdapter.WordAdapterHolder>(WordsDiffCallback) {

    class WordAdapterHolder(
        private val binding: BoxWordBinding,
        private val viewModel: MviViewModel<WordsState, WordsAction>,
        private val onClickListener: (Word) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val textTemplate = binding.root.context.getString(R.string.text_template)
        private val categoryTemplate = binding.root.context.getString(R.string.category_template)

        fun bind(word: Word, position: Int?) {
            binding.category.text = categoryTemplate.format(word.category.orEmpty())
            binding.originText.text = textTemplate.format(word.lang, word.original)
            binding.translateText.text = textTemplate.format(word.translateLang, word.translate)

            binding.hasImage.setVisibilityIfNotNull(word.imageLink)
            binding.hasSound.setVisibilityIfNotNull(word.soundLink)

            binding.checkMark.visibility =
                if (viewModel.state.value.selectedWords.contains(word.id)) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }

            binding.openWordButton.setOnClickListener {
                onClickListener(word)
            }

            binding.root.setOnLongClickListener {
                if (viewModel.state.value.selectedWords.contains(word.id)) {
                    viewModel.sent(WordsAction.RemoveWord(word.id))
                    binding.checkMark.visibility = android.view.View.GONE
                } else {
                    position?.let {
                        viewModel.sent(WordsAction.AddWord(word.id, it))
                    }
                    binding.checkMark.visibility = android.view.View.VISIBLE
                }
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordAdapterHolder {
        return WordAdapterHolder(
            BoxWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel,
            onClickListener
        )
    }


    override fun onBindViewHolder(holder: WordAdapterHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, position)
        }
    }

    companion object {
        private fun ImageView.setVisibilityIfNotNull(link: String?) {
            visibility = if (link != null) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    private object WordsDiffCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }

    }
}