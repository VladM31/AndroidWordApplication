package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.userwords.UserWordManager
import can.lucky.of.core.domain.managers.word.WordManager
import can.lucky.of.core.domain.models.data.words.DeleteUserWord
import can.lucky.of.core.domain.vms.MviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.WordAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.WordState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WordViewModel(
    private val wordManager: UserWordManager
) : ViewModel(), MviViewModel<WordState, WordAction> {

    private val mutableState = MutableStateFlow(WordState())
    override val state: StateFlow<WordState> = mutableState

    override fun sent(action: WordAction) {
        when (action) {
            is WordAction.Init -> handleInit(action)
            is WordAction.Delete -> handleDelete()
        }
    }

    private fun handleInit(action: WordAction.Init) {
        mutableState.value = state.value.copy(
            word = action.word,
            userWordDetails = action.details
        )
    }

    private fun handleDelete() {
        viewModelScope.launch(Dispatchers.IO){
            val deleteWord = DeleteUserWord(
                wordId = state.value.word?.id.orEmpty(),
                id = state.value.userWordDetails?.userWordId.orEmpty()
            )

            wordManager.delete(listOf(deleteWord))

            mutableState.value = state.value.copy(isDeleted = true)
        }
    }

}