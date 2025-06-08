package com.generagames.happy.town.farm.wordandroid.domain.models.data

data class ShareResult(
    val qrCode: ByteArray,
    val lifeTimeInSeconds: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShareResult

        if (!qrCode.contentEquals(other.qrCode)) return false
        if (lifeTimeInSeconds != other.lifeTimeInSeconds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = qrCode.contentHashCode()
        result = 31 * result + lifeTimeInSeconds.hashCode()
        return result
    }
}