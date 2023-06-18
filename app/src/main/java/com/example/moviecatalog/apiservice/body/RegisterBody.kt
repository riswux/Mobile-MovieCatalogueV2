package com.example.moviecatalog.apiservice.body

data class RegisterBody(
    val userName: String,
    val name: String,
    val password: String,
    val email: String,
    val birthDate: String,
    val gender: Int
)
