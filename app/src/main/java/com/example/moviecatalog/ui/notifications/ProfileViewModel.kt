package com.example.moviecatalog.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviecatalog.apiservice.ApiConfig
import com.example.moviecatalog.apiservice.body.UserBody
import com.example.moviecatalog.apiservice.response.UserResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val userResponse = MutableLiveData<UserResponse>()
    val userData: LiveData<UserResponse> get() = userResponse

    private val updateUserResponse = MutableLiveData<Any>()
    val updateUserData: LiveData<Any> get() = updateUserResponse

    private val loading = MutableLiveData<Boolean>(false)
    val getLoading: LiveData<Boolean> get() = loading

    fun getProfile(token: String) {
        viewModelScope.launch {
            val response = ApiConfig.getApiService()?.getProfile(token)
            userResponse.value = response!!
        }
    }

    fun updateProfile(token: String, body: UserBody) {
        loading.value = true
        ApiConfig.getApiService()?.editProfile(token, body)?.enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                loading.value = false
                if (response.isSuccessful) {
                    updateUserResponse.value = "Successfully update profile"
                } else {
                    updateUserResponse.value = "Failed (Email have been used, birth date invalid)"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loading.value = false
                updateUserResponse.value = t.message
            }

        })
    }

    fun logout() {
        viewModelScope.launch{
            ApiConfig.getApiService()?.logout()
        }
    }
}