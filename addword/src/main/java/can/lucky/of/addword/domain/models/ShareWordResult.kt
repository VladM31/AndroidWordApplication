package can.lucky.of.addword.domain.models

data class ShareWordResult(
    val qrCode: ByteArray,
    val liveTimeInSeconds: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShareWordResult

        if (!qrCode.contentEquals(other.qrCode)) return false
        if (liveTimeInSeconds != other.liveTimeInSeconds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = qrCode.contentHashCode()
        result = 31 * result + liveTimeInSeconds
        return result
    }
}
