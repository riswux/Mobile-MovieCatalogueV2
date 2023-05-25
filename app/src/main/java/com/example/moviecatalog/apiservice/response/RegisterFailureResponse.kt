package com.example.moviecatalog.apiservice.response

import com.google.gson.annotations.SerializedName

data class RegisterFailureResponse (
    val message: String,
    val errors: Errors
)

data class Errors (
    @SerializedName("DuplicateUserName")
    val duplicateUserName: DuplicateUserName
)

data class DuplicateUserName (
    val rawValue: Any? = null,
    val attemptedValue: Any? = null,
    val errors: List<Error>,
    val validationState: Long,
    val isContainerNode: Boolean,
    val children: Any? = null
)

data class Error (
    val exception: Any? = null,
    val errorMessage: String
)
