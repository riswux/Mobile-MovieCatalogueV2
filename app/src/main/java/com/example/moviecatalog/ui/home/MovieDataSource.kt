package com.example.moviecatalog.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviecatalog.apiservice.ApiConfig
import com.example.moviecatalog.apiservice.response.MoviesResponse

class MovieDataSource : PagingSource<Int, MoviesResponse.Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesResponse.Movie> {

        return try {

            val nextPage = params.key ?: 1

            val response = ApiConfig.getApiService()?.getAllMovies(nextPage)

            val first = response?.movies?.first()

            val list = ArrayList<MoviesResponse.Movie>()

            for (i in response?.movies!!) {
                if (i.id != first?.id) {
                    list.add(i)
                }
            }

            LoadResult.Page(
                data = list,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.movies.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MoviesResponse.Movie>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}