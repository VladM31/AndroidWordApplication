package com.generagames.happy.town.farm.wordandroid.ui.handels.pin

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.core.net.toUri
import com.bumptech.glide.load.model.LazyHeaders
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPinUserWordsBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PinUserWordsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import can.lucky.of.core.R as CoreR

class SetSoundCollectHandler(
    private val headers: LazyHeaders,
    private val context: Context,
    private val mediaPlayer: MediaPlayer
) : PinUserWordCollectHandler {
    override suspend fun handleCollect(
        binding: FragmentPinUserWordsBinding?,
        stateFlow: Flow<PinUserWordsState>
    ) {
        stateFlow.distinctUntilChanged(::isSame).collect{state ->
            if (state.words.isEmpty()){
                return@collect
            }

            if (state.sound != null) {
                handleSet(state.sound)
                activeBtn(binding)
                return@collect
            }

            if(state.getWord().customSound != null){
                handleSet(state.getWord().customSound?.toUri())
                activeBtn(binding)
                return@collect
            }

            if (state.getWord().soundLink != null) {
                handleSet(state.getWord().soundLink, true)
                activeBtn(binding)
                return@collect
            }

            disableBtn(binding)
        }
    }

    private fun isSame(old: PinUserWordsState, new: PinUserWordsState): Boolean {
        return old.index == new.index
                && old.sound == new.sound
                && old.isInited == new.isInited

    }

    private fun activeBtn(binding: FragmentPinUserWordsBinding?){
        binding?.playSoundBtn?.run{
            setImageResource(CoreR.drawable.sound)
            isClickable = true
        }
    }

    private fun disableBtn(binding: FragmentPinUserWordsBinding?){
        binding?.playSoundBtn?.run{
            setImageResource(CoreR.drawable.sound_disable)
            isClickable = false
        }
    }

    private fun PinUserWordsState.getWord() : PinUserWordsState.Word{
        return this.words[this.index]
    }

    private fun handleSet(uri: Uri?, useHeader: Boolean = false) {
        mediaPlayer.reset()
        uri?.let {
            if (useHeader){
                mediaPlayer.setDataSource(context, it, headers.headers)
            }else{
                mediaPlayer.setDataSource(context, it)
            }
            mediaPlayer.prepareAsync()
        }
    }
}