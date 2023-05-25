package com.example.moviecatalog.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalog.apiservice.ApiConfig
import com.example.moviecatalog.apiservice.body.LoginBody
import com.example.moviecatalog.apiservice.body.RegisterBody
import com.example.moviecatalog.apiservice.response.AuthResponse
import com.example.moviecatalog.apiservice.response.RegisterFailureResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val loading = MutableLiveData(false)
    val getLoading: LiveData<Boolean> get() = loading

    private val registerResponse = MutableLiveData<Any>()
    val registerData: LiveData<Any> get() = registerResponse

    fun register(body: RegisterBody) {
        loading.value = true
        ApiConfig.getApiService()?.register(body)?.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                loading.value = false
                if (response.isSuccessful) {

                    val gson = Gson()
                    val login = gson.fromJson(gson.toJson(response.body()), AuthResponse::class.java)

                    registerResponse.value = login
                } else {
                    val gson = Gson()
                    val errorBody = response.errorBody() ?: return
                    val type = object : TypeToken<RegisterFailureResponse>() {}.type
                    val errorResponse: RegisterFailureResponse? = gson.fromJson(errorBody.charStream(), type)
                    val errorMessage = errorResponse?.errors?.duplicateUserName?.errors?.get(0)?.errorMessage ?: ""
                    registerResponse.value = errorMessage
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                loading.value = false
                registerResponse.value = t.message
            }

        })
    }
}