package can.lucky.of.core.domain.models.data

data class Token(
    val value : String,
    val expirationTime : Long
){

    fun isExpired() = (System.currentTimeMillis() + DAY) > expirationTime



    companion object{
        const val DAY = 24 * 60 * 60 * 1000
        var DEFAULT = Token("", 0L)
    }
}