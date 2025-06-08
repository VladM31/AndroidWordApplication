package com.generagames.happy.town.farm.wordandroid.net.models.responses

data class WordRespond(
    val id: String,
    val original: String,
    val translate: String,
    val lang: String,
    val translateLang: String,
    val cefr: String,
    val description: String? = null,
    val category: String? = null,
    val soundLink: String? = null,
    val imageLink: String? = null
)
