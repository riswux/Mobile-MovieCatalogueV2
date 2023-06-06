package com.example.moviecatalog.apiservice.response

data class MovieDetailResponse (
    val id: String,
    val name: String,
    val poster: String,
    val year: Long,
    val country: String,
    val genres: List<Genre>,
    val reviews: List<Review>,
    val time: Long,
    val tagline: String,
    val description: String,
    val director: String,
    val budget: Long,
    val fees: Long,
    val ageLimit: Long
) {
    data class Genre (
        val id: String,
        val name: String
    )

    data class Review (
        val id: String,
        val rating: Long,
        val reviewText: String,
        val isAnonymous: Boolean,
        val createDateTime: String,
        val author: Author
    ) {
        data class Author (
            val userId: String,
            val nickName: String,
            val avatar: String
        )
    }
}
