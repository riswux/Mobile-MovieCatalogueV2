package com.example.moviecatalog.apiservice.response

data class UserResponse (
    val id: String,
    val nickName: String,
    val email: String,
    val avatarLink: String,
    val name: String,
    val birthDate: String,
    val gender: Long
)
