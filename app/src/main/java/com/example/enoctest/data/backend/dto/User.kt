package com.example.enoctest.data.backend.dto

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("avatar_url")
    val avatarUrl: String
)
