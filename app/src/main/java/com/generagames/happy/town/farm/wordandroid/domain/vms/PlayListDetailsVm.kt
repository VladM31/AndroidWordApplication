package com.generagames.happy.town.farm.wordandroid.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.vms.MviViewModel
import can.lucky.of.exercise.domain.managers.ExerciseTransactionManager
import can.lucky.of.exercise.domain.managers.ExerciseWordManager
import can.lucky.of.exercise.domain.models.data.ExerciseTransaction
import can.lucky.of.exercise.domain.models.data.ExerciseWord
import com.generagames.happy.town.farm.wordandroid.actions.PlayListDetailsAction

import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.PinPlayListManager
import can.lucky.of.core.domain.managers.playlist.PlayListManager

import can.lucky.of.core.domain.models.data.playlists.PinPlayList
import can.lucky.of.core.domain.models.filters.DeletePlayListFilter
import can.lucky.of.core.domain.models.filters.PlayListFilter
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PlayListDetailsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Collections

private const val DEFAULT_NAME = "Loading..."
private const val DEFAULT_ID = "DEFAULT_ID"

class PlayListDetailsVm(
    private val playListManager: PlayListManager,
    private val pinPlayListManager: PinPlayListManager,
    private val exerciseWordManager: ExerciseWordManager,
    private val exerciseTransactionManager: ExerciseTransactionManager
) : ViewModel(), MviViewModel<PlayListDetailsState, PlayListDetailsAction> {

    private val mutableState = MutableStateFlow(
        PlayListDetailsState(
            id = DEFAULT_ID,
            name = DEFAULT_NAME,
            words = emptyList()
        )
    )
    override val state: StateFlow<PlayListDetailsState> = mutableState


    override fun sent(action: PlayListDetailsAction) {
        when (action) {
            is PlayListDetailsAction.Fetch -> fetchPlayList(action.id)
            is PlayListDetailsAction.SelectWord -> selectWord(action.id, action.position)
            is PlayListDetailsAction.UnSelect -> unSelect()
            is PlayListDetailsAction.UnPin -> unPin()
            is PlayListDetailsAction.StartTransaction ->  startTransaction()
            is PlayListDetailsAction.HandleEdit -> handleEdit(action.name)
            is PlayListDetailsAction.Delete -> handleDelete()
            is PlayListDetailsAction.ReFetch -> fetchPlayList(state.value.id)
        }
    }

    private fun handleDelete() {
        viewModelScope.launch {
            val isDeleted = playListManager.delete(DeletePlayListFilter(state.value.id))
            mutableState.value = mutableState.value.copy(
                isEnd = isDeleted != 0
            )
        }
    }

    private fun handleEdit(name: String) {
        mutableState.value = mutableState.value.copy(
            name = name
        )
    }

    private fun startTransaction() {
        viewModelScope.launch(Dispatchers.IO){
            val transaction = ExerciseTransaction()
            exerciseTransactionManager.save(transaction)

            val ids = if (state.value.selectedWords.isEmpty()){
                state.value.words.map { it.userWord.id }
            }else{
                state.value.selectedWords.keys
            }

            ids.map { ExerciseWord(userWordId = it, transactionId = transaction.id) }.let {
                exerciseWordManager.save(it)
            }
            mutableState.value = state.value.copy(transactionId = transaction.id)
            delay(300)
            mutableState.value = state.value.copy(transactionId = null)
        }
    }

    private fun unPin() {
        if (state.value.selectedWords.isEmpty()) return

        viewModelScope.launch {

            val upPinWords = state.value.selectedWords.keys.map {
                PinPlayList(
                    wordId = it,
                    playListId = state.value.id
                )
            }
            val isNotUnPinned = pinPlayListManager.unpin(upPinWords).toList().all { it == 0 }

            if (isNotUnPinned) return@launch

            val newWords = state.value.words.filter { state.value.selectedWords.containsKey(it.userWord.id).not() }

            mutableState.value = mutableState.value.copy(
                words = newWords,
                selectedWords = emptyMap()
            )
        }
    }

    private fun unSelect() {
        mutableState.value = mutableState.value.copy(
            selectedWords = emptyMap()
        )
    }

    private fun selectWord(id: String, position: Int) {
        val newSelectedWords = if (state.value.selectedWords.containsKey(id)) {
            state.value.selectedWords - id
        } else {
            state.value.selectedWords + (id to position)
        }

        mutableState.value = mutableState.value.copy(
            selectedWords = newSelectedWords
        )
    }

    private fun fetchPlayList(id: String) {
        if (id == DEFAULT_ID || id == mutableState.value.id) return

        fetchPlayListById(id)
    }

    private fun fetchPlayListById(id: String){
        viewModelScope.launch {
            val list = playListManager.findBy(PlayListFilter(ids = Collections.singletonList(id)))

            list.firstOrNull()?.let {
                mutableState.apply {
                    value = value.copy(
                        name = it.name,
                        words = it.words,
                        id = it.id
                    )
                }
            }
        }
    }
}