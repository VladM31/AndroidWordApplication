package com.generagames.happy.town.farm.wordandroid.ui.handels.pin

import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPinUserWordsBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PinUserWordsState
import kotlinx.coroutines.flow.Flow

interface PinUserWordCollectHandler {

    suspend fun handleCollect(binding: FragmentPinUserWordsBinding?, state: Flow<PinUserWordsState>)
}