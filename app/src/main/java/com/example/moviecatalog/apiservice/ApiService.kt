package com.example.moviecatalog.apiservice

import com.example.moviecatalog.apiservice.body.AddReviewBody
import com.example.moviecatalog.apiservice.body.LoginBody
import com.example.moviecatalog.apiservice.body.RegisterBody
import com.example.moviecatalog.apiservice.body.UserBody
import com.example.moviecatalog.apiservice.response.FavoritesResponse
import com.example.moviecatalog.apiservice.response.LogoutResponse
import com.example.moviecatalog.apiservice.response.MovieDetailResponse
import com.example.moviecatalog.apiservice.response.MoviesResponse
import com.example.moviecatalog.apiservice.response.UserResponse
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

    //favorite movies
    @GET("api/favorites")
    fun fetchFavorites(
        @Header("Authorization") token: String
    ): Call<Any>

    @POST("api/favorites/{id}/add")
    fun addFavorite(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<ResponseBody>

    @DELETE("api/favorites/{id}/delete")
    fun deleteFavorite(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<Any>

    //movies
    @GET("api/movies/{page}")
    suspend fun getAllMovies(
        @Path("page") page: Int
    ): MoviesResponse

    @GET("api/movies/details/{id}")
    suspend fun movieDetail(
        @Path("id") id: String
    ): MovieDetailResponse

    //review
    @POST("api/movie/{movieId}/review/add")
    fun addReview(
        @Header("Authorization") token: String,
        @Path("movieId") movieId: String,
        @Body body: AddReviewBody
    ): Call<ResponseBody>

    @PUT("api/movie/{movieId}/review/{id}/edit")
    fun editReview(
        @Header("Authorization") token: String,
        @Path("movieId") movieId: String,
        @Path("id") id: String,
        @Body body: AddReviewBody
    ): Call<ResponseBody>

    @DELETE("api/movie/{movieId}/review/{id}/delete")
    fun deleteReview(
        @Header("Authorization") token: String,
        @Path("movieId") movieId: String,
        @Path("id") id: String
    ): Call<ResponseBody>

    @GET("api/account/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): UserResponse

    @PUT("api/account/profile")
    fun editProfile(
        @Header("Authorization") token: String,
        @Body body: UserBody
    ): Call<ResponseBody>

}