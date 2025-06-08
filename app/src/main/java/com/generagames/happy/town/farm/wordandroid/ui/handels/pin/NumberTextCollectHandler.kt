package com.generagames.happy.town.farm.wordandroid.ui.handels.pin

import android.annotation.SuppressLint
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPinUserWordsBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PinUserWordsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

object NumberTextCollectHandler : PinUserWordCollectHandler {
    @SuppressLint("SetTextI18n")
    override suspend fun handleCollect(
        binding: FragmentPinUserWordsBinding?,
        state: Flow<PinUserWordsState>
    ) {
        state.distinctUntilChanged(NumberTextCollectHandler::isSameWord).collect {
            binding?.pageNumberText?.text = "${it.index + 1}/${it.words.size}"
        }
    }

    private fun isSameWord(old: PinUserWordsState, new: PinUserWordsState): Boolean {
        return old.index == new.index && old.isInited == new.isInited
    }
}