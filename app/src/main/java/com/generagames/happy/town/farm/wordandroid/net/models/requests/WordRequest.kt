package com.generagames.happy.town.farm.wordandroid.net.models.requests

import com.google.gson.annotations.Expose
import java.io.File


data class WordRequest(
    val word: String,
    val language: String,
    val translationLanguage: String,
    @Expose
    val translation: String?,
    @Expose
    val category: String?,
    @Expose
    val description: String?,
    val cefr: String?,
    @Expose
    override var image: ByteArray?,
    @Expose
    override var sound: File?,
    val needSound: Boolean,
): FileRequest {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WordRequest

        if (word != other.word) return false
        if (language != other.language) return false
        if (translationLanguage != other.translationLanguage) return false
        if (translation != other.translation) return false
        if (category != other.category) return false
        if (description != other.description) return false
        if (cefr != other.cefr) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (sound != other.sound) return false
        if (needSound != other.needSound) return false

        return true
    }

    override fun hashCode(): Int {
        var result = word.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + translationLanguage.hashCode()
        result = 31 * result + (translation?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (cefr?.hashCode() ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + (sound?.hashCode() ?: 0)
        result = 31 * result + needSound.hashCode()
        return result
    }
}
