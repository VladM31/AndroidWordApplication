package com.generagames.happy.town.farm.wordandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.generagames.happy.town.farm.wordandroid.databinding.BoxShorWordBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SharePlayList

class ShortWordAdapter(
    private val playList: SharePlayList
) : RecyclerView.Adapter<ShortWordAdapter.Holder>(){


    class Holder(
        val binding: BoxShorWordBinding
    ): RecyclerView.ViewHolder(binding.root){
        private val template = binding.originalText.text.toString();

        fun bind(word: SharePlayList.SharePlayListWord){
            binding.originalText.text = template.format(word.lang,word.original)
            binding.translateText.text = template.format(word.translateLang,word.translate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return BoxShorWordBinding.inflate(LayoutInflater.from(parent.context),parent,false).let {
            Holder(it)
        }
    }

    override fun getItemCount(): Int = playList.words.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(playList.words[position])
    }
}