package com.generagames.happy.town.farm.wordandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.generagames.happy.town.farm.wordandroid.databinding.BoxPlayListBinding
import can.lucky.of.core.domain.models.data.playlists.PlayListCount
import can.lucky.of.core.R as CoreR

class PlayListCountAdapter(
    private val openDetails: (String) -> Unit
) : PagingDataAdapter<PlayListCount, PlayListCountAdapter.PlayListViewHolder>(
    PlayListCountDiffCallback
) {

    class PlayListViewHolder(
        private val binding: BoxPlayListBinding,
        private val openDetails: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val playListDetailsTemplate = binding.root.context.getString(
            CoreR.string.play_list_details_template
        )

        fun bind(playList: PlayListCount){

            binding.name.text = playList.name
            binding.countAndDate.text = playListDetailsTemplate.format(
                playList.dateOfCreated,
                playList.count
            )
            binding.open.setOnClickListener {
                if (playList.count > 0L){
                    openDetails(playList.id)
                    return@setOnClickListener
                }
                Toast.makeText(binding.root.context,CoreR.string.play_list_is_empty, Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
       return PlayListViewHolder(
           BoxPlayListBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           ),
           openDetails
       )
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    private object PlayListCountDiffCallback : DiffUtil.ItemCallback<PlayListCount>() {
        override fun areItemsTheSame(oldItem: PlayListCount, newItem: PlayListCount): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlayListCount, newItem: PlayListCount): Boolean {
            return oldItem == newItem
        }

    }
}