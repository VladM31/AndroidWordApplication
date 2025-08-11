package com.generagames.happy.town.farm.wordandroid.domain.managers.payment

import com.generagames.happy.town.farm.wordandroid.domain.models.data.SubCost

interface PayPropositionManager {

    fun getProposition() : SubCost?

    fun setProposition(subCost: SubCost)
}