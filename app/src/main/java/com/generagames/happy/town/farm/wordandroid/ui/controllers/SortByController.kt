package com.generagames.happy.town.farm.wordandroid.ui.controllers

import android.view.View
import android.widget.AdapterView
import android.widget.BaseAdapter
import com.generagames.happy.town.farm.wordandroid.databinding.BoxSortByBinding

class SortByController(
    binding: BoxSortByBinding,
    adapter: BaseAdapter,
    initDirection: Boolean = false,
    private val onChangeSortBy: (String?) -> Unit,
    private val onChangeDirection: (Boolean) -> Unit
) {

    init {
        binding.sortSpinner.adapter = adapter

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.getItemAtPosition(position)?.toString()?.let(onChangeSortBy)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                onChangeSortBy(null)
            }
        }

        binding.directionButton.rotation = if (initDirection) 180f else 0f

        binding.directionButton.setOnClickListener {
            binding.directionButton.apply {
                rotation = if (rotation == 0f) {
                    onChangeDirection(true)
                    180f
                } else {
                    onChangeDirection(false)
                    0f
                }
            }

        }
    }

}