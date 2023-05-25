package com.example.moviecatalog.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.moviecatalog.apiservice.ApiConfig
import com.example.moviecatalog.apiservice.response.MoviesResponse
import com.example.moviecatalog.home.MovieDataSource
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val moviesResponse = MutableLiveData<MoviesResponse>()
    val moviesData: LiveData<MoviesResponse> get() = moviesResponse

    val movies = Pager(
        config = PagingConfig(pageSize = 25),
        pagingSourceFactory = {
            MovieDataSource()
        }).flow.cachedIn(viewModelScope)

    fun getMovies() {
        viewModelScope.launch {
            val response = ApiConfig.getApiService()?.getAllMovies(1)
            moviesResponse.value = response!!
        }
    }
}