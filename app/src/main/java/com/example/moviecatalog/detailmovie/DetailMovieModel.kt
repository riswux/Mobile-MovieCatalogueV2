package com.example.moviecatalog.detailmovie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviecatalog.apiservice.ApiConfig
import com.example.moviecatalog.apiservice.body.AddReviewBody
import com.example.moviecatalog.apiservice.response.FavoritesResponse
import com.example.moviecatalog.apiservice.response.MovieDetailResponse
import com.example.moviecatalog.apiservice.response.UserResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel : ViewModel() {
    private val loading = MutableLiveData<Boolean>()
    val getLoading: LiveData<Boolean> get() = loading

    private val detailResponse = MutableLiveData<MovieDetailResponse>()
    val detailData: LiveData<MovieDetailResponse> get() = detailResponse

    private val deleteReviewResponse = MutableLiveData<Any>()
    val deleteReviewData: LiveData<Any> get() = deleteReviewResponse

    private val editReviewResponse = MutableLiveData<Any>()
    val editReviewData: LiveData<Any> get() = editReviewResponse

    private val addReviewResponse = MutableLiveData<Any>()
    val addReviewData: LiveData<Any> get() = addReviewResponse

    private val userResponse = MutableLiveData<UserResponse>()
    val userData: LiveData<UserResponse> get() = userResponse

    private val favResponse = MutableLiveData<Any>()
    val favData: LiveData<Any> get() = favResponse

    private val addFavResponse = MutableLiveData<Any>()
    val addFavData: LiveData<Any> get() = addFavResponse


    fun getDetailMovie(id: String) {
        loading.value = true
        viewModelScope.launch {
            val response = ApiConfig.getApiService()?.movieDetail(id)
            detailResponse.value = response!!
            loading.value = false
        }
    }

    fun deleteReview(token: String, id: String, movieId: String) {
        ApiConfig.getApiService()?.deleteReview(token, movieId, id)?.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    deleteReviewResponse.value = "Successfully delete review"
                } else {
                    deleteReviewResponse.value = "Failed delete review"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                deleteReviewResponse.value = t.message
            }

        })
    }

    fun editReview(token: String, id: String, movieId: String, body: AddReviewBody) {
        ApiConfig.getApiService()?.editReview(token, movieId, id, body)?.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    editReviewResponse.value = "Successfully update review"
                } else {
                    editReviewResponse.value = "Failed update review"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                editReviewResponse.value = t.message
            }

        })
    }

    fun addReview(token: String,movieId: String, body: AddReviewBody) {
        ApiConfig.getApiService()?.addReview(token, movieId, body)?.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    addReviewResponse.value = "Successfully add review"
                } else {
                    addReviewResponse.value = "Failed add review"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                addReviewResponse.value = t.message
            }

        })
    }

    fun getProfile(token: String) {
        viewModelScope.launch {
            val response = ApiConfig.getApiService()?.getProfile(token)
            userResponse.value = response!!
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

    fun addFavorite(token: String, id: String) {
        ApiConfig.getApiService()?.addFavorite(token, id)?.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    addFavResponse.value = "Added to favorite"
                } else {
                    addFavResponse.value = "Failed Added to favorite"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                addFavResponse.value = t.message
            }

        })
    }

}