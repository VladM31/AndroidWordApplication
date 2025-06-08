package com.generagames.happy.town.farm.wordandroid.ui.handels.pin

import android.content.Context
import android.widget.ImageView
import androidx.core.net.toUri
import can.lucky.of.core.utils.listeners.RequestFailedImageHideListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.LazyHeaders
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPinUserWordsBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PinUserWordsState
import com.generagames.happy.town.farm.wordandroid.utils.localhostUrlToEmulatorGlide
import can.lucky.of.core.utils.setImageByUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import can.lucky.of.core.R as CoreR

class SetImageCollectHandler(
    private val headers: LazyHeaders,
    private val context: Context,
) : PinUserWordCollectHandler {
    override suspend fun handleCollect(
        binding: FragmentPinUserWordsBinding?,
        state: Flow<PinUserWordsState>
    ) {
        state.distinctUntilChanged(this::isSameWord).collect {
            if (it.words.isEmpty()){
                return@collect
            }

            if(it.image != null){
                binding?.wordImage?.setImageByUri(it.image)
                return@collect
            }

            if(it.getWord()?.customImage != null){
                it.getWord()?.customImage?.let { file ->
                    binding?.wordImage?.setImageByUri(file.toUri())
                }
                return@collect
            }

            handleNewWord(it.getWord()?.imageLink, binding?.wordImage)
        }
    }

    private fun isSameWord(old: PinUserWordsState, new: PinUserWordsState): Boolean {
        return old.index == new.index
                && old.image == new.image
                && old.isInited == new.isInited

    }

    private fun PinUserWordsState.getWord() : PinUserWordsState.Word?{
        return this.words.getOrNull(this.index)
    }

    private fun handleNewWord(imageLink: String?, image: ImageView?){
        if (imageLink == null){
            image?.setImageResource(CoreR.drawable.image_icon)
            return
        }

        if (image == null){
            return
        }

        Glide.with(context)
            .load(imageLink.localhostUrlToEmulatorGlide(headers))
            .addListener(RequestFailedImageHideListener(image))
            .placeholder(CoreR.drawable.image_icon)
            .fallback(CoreR.drawable.image_icon)
            .error(CoreR.drawable.image_icon)
            .into(image)
    }


}