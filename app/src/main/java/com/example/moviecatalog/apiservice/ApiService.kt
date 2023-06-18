package com.example.moviecatalog.apiservice

import com.example.moviecatalog.apiservice.body.LoginBody
import com.example.moviecatalog.apiservice.body.RegisterBody
import com.example.moviecatalog.apiservice.response.LogoutResponse
import com.example.moviecatalog.apiservice.response.MovieDetailResponse
import com.example.moviecatalog.apiservice.response.MoviesResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //auth
    @POST("api/account/register")
    fun register(
        @Body body: RegisterBody
    ): Call<Any>

    @POST("api/account/login")
    fun login(
        @Body body: LoginBody
    ): Call<Any>

    @POST("api/account/logout")
    suspend fun logout(): LogoutResponse


    //movies
    @GET("api/movies/{page}")
    suspend fun getAllMovies(
        @Path("page") page: Int
    ): MoviesResponse

    @GET("api/movies/details/{id}")
    suspend fun movieDetail(
        @Path("id") id: String
    ): MovieDetailResponse


}