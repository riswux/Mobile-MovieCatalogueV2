package com.example.moviecatalog.apiservice.response

data class FavoritesResponse (
    val movies: List<Movie>
) {
    data class Movie (
        val id: String,
        val name: String,
        val poster: String,
        val year: Long,
        val country: String,
        val genres: List<Genre>,
        val reviews: List<Review>
    )

    data class Genre (
        val id: String,
        val name: String
    )

    data class Review (
        val id: String,
        val rating: Long
    )
}

