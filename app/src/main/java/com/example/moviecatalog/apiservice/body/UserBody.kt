package com.example.moviecatalog.apiservice.body

data class UserBody(
    val id: String,
    val nickName: String,
    val email: String,
    val avatarLink: String,
    val name: String,
    val birthDate: String,
    val gender: Long
)
