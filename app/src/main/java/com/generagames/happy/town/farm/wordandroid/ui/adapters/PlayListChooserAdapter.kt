package com.generagames.happy.town.farm.wordandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import can.lucky.of.core.domain.models.data.playlists.PlayListCount
import can.lucky.of.core.utils.toZoneDateTimeFormat
import com.generagames.happy.town.farm.wordandroid.databinding.BoxPlayListChoosersBinding
import can.lucky.of.core.R as CoreR

class PlayListChooserAdapter(
    private val isChose: (String) -> Boolean,
    private val onChoose: (String?, Int) -> Unit
) : PagingDataAdapter<PlayListCount, PlayListChooserAdapter.ViewHolderChooser>(
    PlayListChooserDiffUtil
) {


    class ViewHolderChooser(
        private val binding: BoxPlayListChoosersBinding,
        private val onChoose: (String?, Int) -> Unit,
        private val isChose: (String) -> Boolean
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dateOfCreationTemplate =
            binding.root.context.getString(CoreR.string.date_of_creation_template)

        fun bind(playList: PlayListCount) {
            if (isChose(playList.id)) activate() else deactivate()

            binding.nameText.text = playList.name
            binding.dateOfCreationText.text =
                dateOfCreationTemplate.format(playList.createdAt.toZoneDateTimeFormat())
            binding.root.setOnClickListener {
                onChoose(playList.id, bindingAdapterPosition)
                activate()
            }
        }

        private fun activate() {
            binding.markRight.visibility = View.VISIBLE
        }

        private fun deactivate() {
            binding.markRight.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: ViewHolderChooser, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChooser {
        return ViewHolderChooser(
            BoxPlayListChoosersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onChoose,
            isChose
        )
    }


    object PlayListChooserDiffUtil : DiffUtil.ItemCallback<PlayListCount>() {
        override fun areItemsTheSame(oldItem: PlayListCount, newItem: PlayListCount): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlayListCount, newItem: PlayListCount): Boolean {
            return oldItem == newItem
        }
    }
}