package com.example.moviecatalog.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalog.apiservice.ApiConfig
import com.example.moviecatalog.apiservice.body.LoginBody
import com.example.moviecatalog.apiservice.response.AuthResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val loading = MutableLiveData(false)
    val getLoading: LiveData<Boolean> get() = loading

    private val loginResponse = MutableLiveData<Any>()
    val loginData: LiveData<Any> get() = loginResponse

    fun login(body: LoginBody) {
        loading.value = true
        ApiConfig.getApiService()?.login(body)?.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                loading.value = false
                if (response.isSuccessful) {

                    val gson = Gson()
                    val login = gson.fromJson(gson.toJson(response.body()), AuthResponse::class.java)

                    loginResponse.value = login
                } else {
                    loginResponse.value = "Login Failed"
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                loading.value = false
                loginResponse.value = t.message
            }

        })
    }
}