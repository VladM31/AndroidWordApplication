package com.generagames.happy.town.farm.wordandroid.domain.models.states

import android.net.Uri
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.states.EndetableState
import java.io.File


data class PinUserWordsState(
    val index: Int = 0,
    val words: List<Word> = emptyList(),

    val image: Uri? = null,
    val sound: Uri? = null,

    val isInited: Boolean = false,
    override val isEnd: Boolean = false,
): EndetableState{

    data class Word(
        val wordId: String,
        val original: String,
        val lang: Language,
        val soundLink: Uri? = null,
        val imageLink: String? = null,
        var customSound: File? = null,
        var customImage: File? = null,
    )
}
