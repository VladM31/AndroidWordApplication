package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.domain.models.states.EndetableState

data class SharePlayListState(
    val time: Int = 1,
    val qrCode: ByteArray = byteArrayOf(),
    val isInit: Boolean = false,
    val playListId: String = "",
    override val isEnd: Boolean = false
): EndetableState {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SharePlayListState

        if (time != other.time) return false
        if (!qrCode.contentEquals(other.qrCode)) return false
        if (isInit != other.isInit) return false
        if (playListId != other.playListId) return false
        if (isEnd != other.isEnd) return false

        return true
    }

    override fun hashCode(): Int {
        var result = time
        result = 31 * result + qrCode.contentHashCode()
        result = 31 * result + isInit.hashCode()
        result = 31 * result + playListId.hashCode()
        result = 31 * result + isEnd.hashCode()
        return result
    }

}
