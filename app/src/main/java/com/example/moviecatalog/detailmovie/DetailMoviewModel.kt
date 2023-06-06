package com.example.moviecatalog.detailmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviecatalog.apiservice.ApiConfig
import com.example.moviecatalog.apiservice.response.MovieDetailResponse
import kotlinx.coroutines.launch

class DetailMoviewModel : ViewModel() {

    private val loading = MutableLiveData<Boolean>()
    val getLoading: LiveData<Boolean> get() = loading

    private val detailResponse = MutableLiveData<MovieDetailResponse>()
    val detailData: LiveData<MovieDetailResponse> get() = detailResponse

    fun getDetailMovie(id: String) {
        loading.value = true
        viewModelScope.launch {
            val response = ApiConfig.getApiService()?.movieDetail(id)
            detailResponse.value = response!!
            loading.value = false
        }
    }

}