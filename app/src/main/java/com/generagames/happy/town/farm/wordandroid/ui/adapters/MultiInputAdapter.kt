package com.generagames.happy.town.farm.wordandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.generagames.happy.town.farm.wordandroid.databinding.BoxMultiInputElementBinding

class MultiInputAdapter(
    startValues: List<String>,
    private val onChange: (elements: List<String>) -> Unit
) : RecyclerView.Adapter<MultiInputAdapter.MultiInputHolder>() {
    private val content = startValues.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiInputHolder {
        return MultiInputHolder(
            BoxMultiInputElementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MultiInputHolder, position: Int) {
        content[position].apply {
            holder.bind(this) {
                val index = content.indexOf(this)
                content.removeAt(index)
                notifyItemRemoved(index)
                onChange(content.toList())
            }
        }
    }

    override fun getItemCount(): Int = content.size

    fun addElement(element: String) {
        content.add(element)
        notifyItemInserted(content.size - 1)
        onChange(content)
    }

    fun notContains(element: String): Boolean {
        return !content.contains(element)
    }

    fun clear(){
        content.clear()
        notifyDataSetChanged()
        onChange(emptyList())
    }

    class MultiInputHolder(
        private val binding: BoxMultiInputElementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(element: String, onClickListener: OnClickListener) {
            binding.elementValue.text = element
            binding.removeButton.setOnClickListener(onClickListener)
        }
    }
}


