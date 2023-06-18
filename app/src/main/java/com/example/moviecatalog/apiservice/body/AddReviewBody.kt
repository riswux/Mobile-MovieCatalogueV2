package com.example.moviecatalog.apiservice.body

data class AddReviewBody(
    val reviewText: String,
    val rating: Int,
    val isAnonymous: Boolean
)
