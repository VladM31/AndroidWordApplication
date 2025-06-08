package com.generagames.happy.town.farm.wordandroid.net.models.responses

data class PlayListRespond(
    val id: String,
    val userId: String,
    val name: String,
    val dateOfCreated: String,
    val words: List<PinnedWordRespond>
){

    data class PinnedWordRespond(
        val learningGrade: Long,
        val lastReadDate: String,
        val word: UserWordRespond
    )

    data class UserWordRespond(
        val id: String,
        val userId: String,
        val learningGrade: Long,
        val dateOfAdded: String,
        val lastReadDate: String,
        val word: WordRespond
    )

    data class WordRespond(
        val id: String,
        val original: String,
        val lang: String,
        val translate: String,
        val translateLang: String,
        val cefr: String,
        val description: String?,
        val category: String?,
        val soundLink: String?,
        val imageLink: String?,
        val custom: Boolean?,
    )
}
