package can.lucky.of.core.domain.models

data class PagedModels<T>(
    val content: List<T>,
    val page: Paged
){

    val isLast: Boolean
        get() = page.number + 1 >= page.totalPages
    data class Paged(
        val number: Long,
        val size: Long,
        val totalElements: Long,
        val totalPages: Long
    )
}