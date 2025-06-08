package can.lucky.of.core.domain.results

data class ConfirmBox(
    val isConfirm: Boolean = false,
    val message: String = "",
    val id: Long = System.currentTimeMillis()
){
    val isStart = id == START_ID

    companion object{
        const val START_ID = 0L
    }
}
