package can.lucky.of.core.ui.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState

typealias TemplatePageLoader<M> = suspend (page: Int, size: Int) -> List<M>

abstract class AbstractPagingSource<M : Any>(
    private val pageLoader: TemplatePageLoader<M>,
    private val pageSize: Int
) : PagingSource<Long, M>() {

    override fun getRefreshKey(state: PagingState<Long, M>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey ?: state.closestPageToPosition(
                anchorPosition
            )?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, M> {
        val page = params.key ?: 0

        return try {
            val words = pageLoader.invoke(page.toInt(), params.loadSize)

            LoadResult.Page(
                data = words,
                prevKey = if (page == 0L) null else page - 1,
                nextKey = if (words.size == params.loadSize) page + (params.loadSize / pageSize) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}