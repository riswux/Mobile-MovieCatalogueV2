package com.example.moviecatalog.apiservice

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private var createBaseApiService: ApiService? = null
        fun getApiService(): ApiService? {
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
                val client = OkHttpClient.Builder().addInterceptor(this).build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

                createBaseApiService = retrofit.create(ApiService::class.java)
            }
            return createBaseApiService
        }
    }
}