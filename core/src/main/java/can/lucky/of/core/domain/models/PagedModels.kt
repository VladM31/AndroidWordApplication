package can.lucky.of.core.domain.models

import can.lucky.of.core.net.responses.PagedRespond

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

    companion object {
        fun <T, R> of(respond: PagedRespond<T>, transform: (T) -> R) = PagedModels<R>(
            content = respond.content.map(transform),
            page = Paged(
                number = respond.page.number,
                size = respond.page.size,
                totalElements = respond.page.totalElements,
                totalPages = respond.page.totalPages
            )
        )

        fun <T> empty() = PagedModels<T>(
            content = emptyList(),
            page = Paged(
                number = 0,
                size = 0,
                totalElements = 0,
                totalPages = 0
            )
        )

    }
}