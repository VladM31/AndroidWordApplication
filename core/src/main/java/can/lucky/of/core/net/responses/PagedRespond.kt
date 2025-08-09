package can.lucky.of.core.net.responses

data class PagedRespond<T>(
    val content: List<T>,
    val page: PagedRespond
){
    data class PagedRespond(
        val number: Long,
        val size: Long,
        val totalElements: Long,
        val totalPages: Long
    )
}
