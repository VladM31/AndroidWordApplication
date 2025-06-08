package com.generagames.happy.town.farm.wordandroid.domain.vms

import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.managers.word.WordManager
import can.lucky.of.core.domain.models.data.words.PinUserWord
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.filters.WordFilter
import can.lucky.of.core.domain.vms.MviViewModel
import can.lucky.of.core.utils.localhostUrlToEmulator
import com.generagames.happy.town.farm.wordandroid.actions.PinUserWordsAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PinUserWordsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PinUserWordsViewModel(
    private val wordManager: WordManager,
    private val subscribeCacheManager: SubscribeCacheManager
) : ViewModel(), MviViewModel<PinUserWordsState, PinUserWordsAction> {

    private val mutableState: MutableStateFlow<PinUserWordsState> =
        MutableStateFlow(
            PinUserWordsState(
                index = 0
            )
        )

    override val state: StateFlow<PinUserWordsState> = mutableState


    private fun setNewValue(factory: (PinUserWordsState) -> PinUserWordsState) {
        mutableState.value = factory(state.value)
    }

    override fun sent(action: PinUserWordsAction) {
        when (action) {
            is PinUserWordsAction.Load -> { loadWords(action.wordIds) }
            is PinUserWordsAction.Pin -> { handlePin() }
            is PinUserWordsAction.NextWord -> { nextWord() }
            is PinUserWordsAction.PreviousWord -> { previousWord() }
            is PinUserWordsAction.SaveFiles -> { saveFiles() }
            is PinUserWordsAction.SetImage -> { handleSetImage(action) }
            is PinUserWordsAction.SetSound -> { handleSetSound(action) }
        }
    }

    private fun handlePin() {
        if (state.value.words.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            state.value.words.toPinWords().runCatching {
                wordManager.pin(this@runCatching)
            }.onFailure {
                Log.d("PinUserWordsViewModel", "handlePin: ${it.message}")
            }

            mutableState.value = state.value.copy(
                isEnd = true
            )
        }
    }

    private fun Collection<PinUserWordsState.Word>.toPinWords() : Collection<PinUserWord> {
        return map {
            PinUserWord(
                wordId = it.wordId,
                sound = it.customSound,
                image = it.customImage
            )
        }
    }

    private fun handleSetImage(action: PinUserWordsAction.SetImage) {
        setNewValue {
            it.copy(
                image = action.image,
                
            )
        }
    }

    private fun handleSetSound(action: PinUserWordsAction.SetSound) {
        setNewValue {
            it.copy(
                sound = action.sound,
                
            )
        }
    }

    private fun loadWords(wordIds: Collection<String>) {
        if (state.value.isInited) return

        viewModelScope.launch(Dispatchers.IO) {
            val words = wordManager.findBy(WordFilter(wordIds = wordIds)).map {
                PinUserWordsState.Word(
                    wordId = it.id,
                    original = it.original,
                    lang = Language.fromShortName(it.lang),
                    soundLink = it.soundLink?.localhostUrlToEmulator()?.toUri(),
                    imageLink = it.imageLink
                )
            }

            if (subscribeCacheManager.isActiveSubscribe()){
                mutableState.value = state.value.copy(words = words, isInited = true)
                return@launch
            }

            words.toPinWords().runCatching {
                wordManager.pin(this@runCatching)
            }.onFailure {
                Log.d("PinUserWordsViewModel", "handlePin: ${it.message}")
            }

            mutableState.value = state.value.copy(
                isEnd = true
            )

        }
    }

    private fun nextWord() {
        if (START_INDEX == state.value.index) return
        val newIndex = state.value.index + 1
        if (newIndex >= state.value.words.size) return
        setNewValue { state ->
            state.copy(
                index = newIndex,
                sound = null,
                image = null,
            )
        }
    }

    private fun previousWord() {
        val newIndex = state.value.index - 1
        if (newIndex < 0) return

        setNewValue { state ->
            state.copy(
                index = newIndex,
                sound = null,
                image = null,
            )
        }
    }

    private fun saveFiles() {
        val word = state.value.run {
            words[index]
        }

        word.customImage = state.value.image?.toFile()
        word.customSound = state.value.sound?.toFile()
    }



    companion object {
        private const val START_INDEX = -1
    }
}