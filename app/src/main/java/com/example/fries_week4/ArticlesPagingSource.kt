package com.example.fries_week4

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.lang.Integer.min

class ArticlesPagingSource : PagingSource<Int, Article>() {

//    val pagingConfig = PagingConfig(
//        pageSize = 10, // Number of items loaded at once
//        initialLoadSize = 20, // Initial number of items loaded
//        enablePlaceholders = false
//    )
    private val articles : List<Article> = mutableListOf()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        try {
            val nextPageNumber = params.key ?: 1 // Assuming 1-based indexing
            val articles = loadData(nextPageNumber)

            return LoadResult.Page(
                data = articles,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (articles.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        TODO("Not yet implemented")
    }
    suspend fun loadData(pageNumber: Int): List<Article> {
        val pageSize = 10

        // Calculate the start and end indices based on the page number
        val startIndex = (pageNumber - 1) * pageSize
        val endIndex = min(startIndex + pageSize, articles.size)

        // Return the sublist for the specified page
        return articles.subList(startIndex, endIndex)
    }
}