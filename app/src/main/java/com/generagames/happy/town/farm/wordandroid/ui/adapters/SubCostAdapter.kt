package com.generagames.happy.town.farm.wordandroid.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.databinding.BoxSubscribeCostBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SubCost
import can.lucky.of.core.R as CoreR

class SubCostAdapter(
    private val subCosts: List<SubCost>,
    private val onChoose: (SubCost) -> Unit
) : RecyclerView.Adapter<SubCostAdapter.SubCostViewHolder>() {


    class SubCostViewHolder(
        private val binding: BoxSubscribeCostBinding,
        private val onChoose: (SubCost) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val costText = binding.root.context.getString(CoreR.string.cost_s)
        private val daysText = binding.root.context.getString(CoreR.string.days_s)

        fun bind(subCost: SubCost) {
            binding.costTextView.text = costText.format(subCost.cost,subCost.currency)
            binding.daysTextView.text = daysText.format(subCost.days)
            binding.payButton.setOnClickListener {
                onChoose(subCost)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCostViewHolder {
        return SubCostViewHolder(
            BoxSubscribeCostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onChoose
        )
    }

    override fun getItemCount(): Int = subCosts.size
    override fun onBindViewHolder(holder: SubCostViewHolder, position: Int) {
        holder.bind(subCosts[position])
    }
}