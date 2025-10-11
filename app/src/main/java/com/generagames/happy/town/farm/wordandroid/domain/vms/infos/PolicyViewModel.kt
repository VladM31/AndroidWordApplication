package com.generagames.happy.town.farm.wordandroid.domain.vms.infos

import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.infos.PolicyAction
import com.generagames.happy.town.farm.wordandroid.domain.managers.pdf.PolicyManager
import com.generagames.happy.town.farm.wordandroid.domain.models.states.infos.PolicyState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class PolicyViewModel(
    private val policyManager: PolicyManager
) : AbstractMviViewModel<PolicyState, PolicyAction>() {
    private val mutableState = MutableStateFlow(PolicyState())
    override val state = mutableState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = policyManager.getPolicyFile()
                mutableState.value = state.value.copy(
                    file = file
                )
            } catch (e: Exception) {
                mutableState.value = state.value.copy(
                    errorMessage = ErrorMessage(message = e.message ?: "Unknown Error")
                )
            }
        }
    }

    override fun sent(action: PolicyAction) {
        when (action) {
            is PolicyAction.End -> mutableState.value = state.value.copy(
                isEnd = true
            )
        }

    }
}