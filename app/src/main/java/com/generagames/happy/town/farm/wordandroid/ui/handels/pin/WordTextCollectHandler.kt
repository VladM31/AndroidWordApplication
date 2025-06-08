package com.generagames.happy.town.farm.wordandroid.ui.handels.pin

import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPinUserWordsBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PinUserWordsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class WordTextCollectHandler(
    private  val textTemplate: String
) : PinUserWordCollectHandler {
    override suspend fun handleCollect(
        binding: FragmentPinUserWordsBinding?,
        state: Flow<PinUserWordsState>
    ) {
        state.distinctUntilChanged(this::isSameWord).collect {
            if (it.index < it.words.size){
                val word = it.words[it.index]
                binding?.wordText?.text = textTemplate.format(word.lang.titleCase, word.original)
            }
        }
    }

    private fun isSameWord(old: PinUserWordsState, new: PinUserWordsState): Boolean {
        return old.index ==new.index && old.isInited == new.isInited
    }
}