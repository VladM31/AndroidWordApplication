package com.generagames.happy.town.farm.wordandroid.ui.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import can.lucky.of.core.domain.models.data.words.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias WordsPageLoader = suspend (page: Int, size: Int) -> List<Word>

class WordsPagingSource(
    private val pageLoader: WordsPageLoader
) : PagingSource<Int, Word>(){
    override fun getRefreshKey(state: PagingState<Int, Word>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Word> {
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