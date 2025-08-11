package com.generagames.happy.town.farm.wordandroid.domain.vms.pay

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.SubCostAction
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayManager
import com.generagames.happy.town.farm.wordandroid.domain.managers.payment.PayPropositionManager
import com.generagames.happy.town.farm.wordandroid.domain.models.states.pay.SubCostState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubCostViewModel(
    private val payManager: PayManager,
    private val payPropositionManager: PayPropositionManager
) : ViewModel(), MviViewModel<SubCostState, SubCostAction> {

    private val mutableState = MutableStateFlow(SubCostState())
    override val state: StateFlow<SubCostState> = mutableState

    init {
        viewModelScope.launch{
           try {
               payManager.getCosts().let {
                   mutableState.value = SubCostState(it)
               }
           }catch (e: Exception) {
               Log.e("SubCostViewModel", "Error: ${e.message}")
           }
        }
    }

    override fun sent(action: SubCostAction) {
        when (action) {
            is SubCostAction.Selected -> handleSelected(action)
        }
    }

    private fun handleSelected(action: SubCostAction.Selected){
        payPropositionManager.setProposition(action.subCost)
        mutableState.update { it.copy(isSelected = true) }

        viewModelScope.launch {
            delay(1000L)
            mutableState.update { it.copy(isSelected = false) }
        }
    }
}