package can.lucky.of.core.domain.models.filters

data class Range<T>(
    val to: T? = null,
    val from: T? = null
){

    companion object{
        fun <T> of(value: T) : Range<T> {
            return Range(value, value)
        }
    }
}
