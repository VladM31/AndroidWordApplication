package can.lucky.of.addword.domain.models.filters

data class RecognizeResultFilter(
    val page: Int,
    val size: Int,
    val filterId: Long = System.currentTimeMillis()
)
