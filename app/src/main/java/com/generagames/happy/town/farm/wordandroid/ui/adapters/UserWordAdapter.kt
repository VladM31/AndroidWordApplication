package com.generagames.happy.town.farm.wordandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.core.utils.toZoneDateTimeFormat
import com.generagames.happy.town.farm.wordandroid.databinding.BoxUserWordBinding
import can.lucky.of.core.R as CoreR

class UserWordAdapter(
    private val onDetails: (UserWord) -> Unit,
    private val isSelected: (String) -> Boolean,
    private val selectWord: (String, Int?) -> Unit
) : PagingDataAdapter<UserWord, UserWordAdapter.UserWordHolder>(UserWordsDiffCallback) {

    class UserWordHolder(
        private val binding: BoxUserWordBinding,
        private val onDetails: (UserWord) -> Unit,
        private val isSelected: (String) -> Boolean,
        private val selectWord: (String, Int?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val textTemplate = binding.root.context.getString(CoreR.string.text_template)
        private val categoryTemplate = binding.root.context.getString(CoreR.string.category_template)
        private val lastDateReadTemplate =
            binding.root.context.getString(CoreR.string.last_date_read_template)

        fun bind(userWord: UserWord, position: Int) {
            binding.apply {
                category.text = categoryTemplate.format(userWord.word.category.orEmpty())
                originText.text = textTemplate.format(userWord.word.lang, userWord.word.original)
                translateText.text =
                    textTemplate.format(userWord.word.translateLang, userWord.word.translate)
                lastDateRead.text =
                    lastDateReadTemplate.format(userWord.lastReadDate.toZoneDateTimeFormat())
                openWordButton.setOnClickListener {
                    onDetails(userWord)
                }
            }

            setVisibilityCheckMark(userWord.id)

            binding.root.setOnLongClickListener {
                selectWord(userWord.id, position)
                setVisibilityCheckMark(userWord.id)
                true
            }
        }

        private fun setVisibilityCheckMark(id: String) {
            binding.checkMark.visibility = if (isSelected(id)) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserWordHolder {
        return UserWordHolder(
            BoxUserWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onDetails,
            isSelected,
            selectWord
        )
    }


    override fun onBindViewHolder(holder: UserWordHolder, position: Int) {
        getItem(position)?.let { holder.bind(it,position) }
    }

    private object UserWordsDiffCallback : DiffUtil.ItemCallback<UserWord>() {
        override fun areItemsTheSame(oldItem: UserWord, newItem: UserWord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserWord, newItem: UserWord): Boolean {
            return oldItem == newItem
        }

    }
}