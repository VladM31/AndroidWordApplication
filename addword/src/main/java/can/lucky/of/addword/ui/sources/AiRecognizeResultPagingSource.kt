package can.lucky.of.addword.ui.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import can.lucky.of.addword.domain.models.AiRecognizeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias AiRecognizeResultPageLoader = suspend (page: Int, size: Int) -> List<AiRecognizeResult>


class AiRecognizeResultPagingSource(
    private val pageLoader: AiRecognizeResultPageLoader
) : PagingSource<Int, AiRecognizeResult>() {
    override fun getRefreshKey(state: PagingState<Int, AiRecognizeResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AiRecognizeResult> {
        val page = params.key ?: 0

        return try {
            withContext(Dispatchers.IO){
                val words = pageLoader.invoke(page, params.loadSize)

                return@withContext LoadResult.Page(
                    data = words,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (words.isEmpty()) null else page + 1
                )
            }
        }catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}