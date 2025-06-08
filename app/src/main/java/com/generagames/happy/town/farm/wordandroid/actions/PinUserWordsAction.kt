package com.generagames.happy.town.farm.wordandroid.actions

import android.net.Uri

sealed interface PinUserWordsAction{
    data class Load(val wordIds: Collection<String>) : PinUserWordsAction
    data object Pin : PinUserWordsAction
    data object NextWord : PinUserWordsAction
    data object PreviousWord : PinUserWordsAction
    data class SetImage(val image: Uri?) : PinUserWordsAction
    data class SetSound(val sound: Uri?) : PinUserWordsAction
    data object SaveFiles : PinUserWordsAction
}