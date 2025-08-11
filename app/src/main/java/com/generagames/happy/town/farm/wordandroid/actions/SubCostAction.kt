package com.generagames.happy.town.farm.wordandroid.actions

import com.generagames.happy.town.farm.wordandroid.domain.models.data.SubCost

sealed interface SubCostAction {

    data class Selected(val subCost: SubCost) : SubCostAction
}