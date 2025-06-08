package com.generagames.happy.town.farm.wordandroid.domain.models.data

data class SharePlayList(
    val name: String,
    val shareId: String,
    val words: List<SharePlayListWord>
) {

    data class SharePlayListWord(
        val original: String,
        val lang: String,
        val translate: String,
        val translateLang: String
    )
}
