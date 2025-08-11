package com.generagames.happy.town.farm.wordandroid.domain.models.states.pay

import com.generagames.happy.town.farm.wordandroid.domain.models.data.SubCost

data class SubCostState(
    val content: List<SubCost> = listOf(),
    val isSelected: Boolean = false,
)
