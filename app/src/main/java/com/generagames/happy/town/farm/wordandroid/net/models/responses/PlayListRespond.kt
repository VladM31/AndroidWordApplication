package com.generagames.happy.town.farm.wordandroid.net.models.responses

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.WordType
import java.time.OffsetDateTime

data class PlayListRespond(
    val id: String,
    val userId: String,
    val name: String,
    val createdAt: OffsetDateTime,
    val words: List<PinnedWordRespond>
){

    data class PinnedWordRespond(
        val learningGrade: Long,
        val createdAt: OffsetDateTime,
        val lastReadDate: OffsetDateTime,
        val word: UserWordRespond
    )

    data class UserWordRespond(
        val id: String,
        val userId: String,
        val learningGrade: Long,
        val createdAt: OffsetDateTime,
        val lastReadDate: OffsetDateTime,
        val word: WordRespond
    )

    data class WordRespond(
        val id: String,
        val original: String,
        val lang: Language,
        val translate: String,
        val translateLang: Language,
        val cefr: CEFR,
        val description: String?,
        val category: String?,
        val soundLink: String?,
        val imageLink: String?,
        val type: WordType,
        val createdAt: OffsetDateTime
    )
}
