package com.example.moviecatalog.detailmovie

import com.example.moviecatalog.apiservice.response.MovieDetailResponse

interface EditReviewClick {
    fun onEditClick(review: MovieDetailResponse.Review)
}