package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.ViewModel
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.WordFilterAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.WordFilterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.generagames.happy.town.farm.wordandroid.utils.mappers.toWordFilterState

class WordFilterViewModel : ViewModel(), MviViewModel<WordFilterState, WordFilterAction> {

    private val mutableState = MutableStateFlow(WordFilterState())
    override val state: StateFlow<WordFilterState> = mutableState

    override fun sent(action: WordFilterAction) {
        mutableState.value = when (action) {
            is WordFilterAction.SetAsc -> state.value.copy(asc = action.value)
            is WordFilterAction.SetCategories -> state.value.copy(categories = action.value)
            is WordFilterAction.SetOriginal -> state.value.copy(original = action.value)
            is WordFilterAction.SetOriginalLang -> state.value.copy(originalLang = action.value)
            is WordFilterAction.SetSortBy -> state.value.copy(sortBy = action.value)
            is WordFilterAction.SetTranslate -> state.value.copy(translate = action.value)
            is WordFilterAction.SetTranslateLang -> state.value.copy(translateLang = action.value)
            is WordFilterAction.Init -> { handleInit(action) }
            is WordFilterAction.SetCefr -> state.value.copy(cefrs = action.value?.let { listOf(it) })
        }
    }

    private fun handleInit(action: WordFilterAction.Init) =
        if (state.value.isInit.not()) {
            action.value.toWordFilterState().copy(
                isInit = true
            )
        } else {
            state.value
        }
}