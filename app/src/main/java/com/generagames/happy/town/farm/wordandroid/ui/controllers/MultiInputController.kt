package com.generagames.happy.town.farm.wordandroid.ui.controllers

import androidx.recyclerview.widget.LinearLayoutManager
import com.generagames.happy.town.farm.wordandroid.ui.adapters.MultiInputAdapter
import com.generagames.happy.town.farm.wordandroid.databinding.BoxMultiInputBinding

class MultiInputController(
    private val binding: BoxMultiInputBinding,
    startValues: List<String> = listOf(),
    title: String,
    onChange: (elements: List<String>) -> Unit
) {

    init {
        val adapter = MultiInputAdapter(startValues,onChange)
        binding.multiInputTitle.text = title
        binding.addButton.setOnClickListener {
            val text = binding.multiInput.text.toString()
            if (text.isNotEmpty() and text.isNotBlank() and adapter.notContains(text)) {
                adapter.addElement(text)
                binding.multiInput.text.clear()
            }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
    }

    fun clear(){
        (binding.recyclerView.adapter as? MultiInputAdapter)?.clear()
    }
}