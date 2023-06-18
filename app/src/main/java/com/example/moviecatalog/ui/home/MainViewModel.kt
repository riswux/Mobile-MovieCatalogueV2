package com.example.moviecatalog.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.moviecatalog.apiservice.ApiConfig
import com.example.moviecatalog.apiservice.response.AuthResponse
import com.example.moviecatalog.apiservice.response.FavoritesResponse
import com.example.moviecatalog.apiservice.response.MoviesResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val favResponse = MutableLiveData<Any>()
    val favData: LiveData<Any> get() = favResponse

    private val moviesResponse = MutableLiveData<MoviesResponse>()
    val moviesData: LiveData<MoviesResponse> get() = moviesResponse

    private val deleteFavResponse = MutableLiveData<String>()
    val deleteFavData: LiveData<String> get() = deleteFavResponse

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

    fun getFavoriteMovie(token: String) {

        ApiConfig.getApiService()?.fetchFavorites(token)?.enqueue(object :  Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val fav = gson.fromJson(gson.toJson(response.body()), FavoritesResponse::class.java)
                    favResponse.value = fav
                } else {
                    if (response.code() == 401) {
                        favResponse.value = "401"
                    } else {
                        favResponse.value = "Failed Fetch Data"
                    }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                favResponse.value = t.message
            }
        })
    }

    fun deleteFavMovie(id: String, token: String) {
        ApiConfig.getApiService()?.deleteFavorite(token, id)?.enqueue(object :  Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    deleteFavResponse.value = "Successfully deleted favorite movie"
                } else {
                    deleteFavResponse.value = "Delete favorite movie failed"
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                deleteFavResponse.value = t.message
            }
        })
    }
}