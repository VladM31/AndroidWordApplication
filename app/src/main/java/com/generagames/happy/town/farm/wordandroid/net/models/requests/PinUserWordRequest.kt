package com.generagames.happy.town.farm.wordandroid.net.models.requests

import com.google.gson.annotations.Expose
import java.io.File

data class PinUserWordRequest(
    val wordId: String,
    @Expose
    override var image: ByteArray?,
    @Expose
    override var sound: File?
) : FileRequest {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PinUserWordRequest

        if (wordId != other.wordId) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (sound != other.sound) return false

        return true
    }

    override fun hashCode(): Int {
        var result = wordId.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + (sound?.hashCode() ?: 0)
        return result
    }
}
