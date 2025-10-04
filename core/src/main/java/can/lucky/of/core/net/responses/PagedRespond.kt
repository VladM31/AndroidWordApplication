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

    companion object {
        fun <T> empty() = PagedRespond<T>(
            content = emptyList(),
            page = PagedRespond(
                number = 0,
                size = 0,
                totalElements = 0,
                totalPages = 0
            )
        )

    }
}
