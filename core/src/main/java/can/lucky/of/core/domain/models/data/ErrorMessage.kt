package can.lucky.of.core.domain.models.data

data class ErrorMessage(
    val message: String = "",
    val id: Long = System.currentTimeMillis(),
)
