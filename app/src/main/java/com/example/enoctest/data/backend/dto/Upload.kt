package com.example.enoctest.data.backend.dto

import com.google.gson.annotations.SerializedName


data class Upload(
    @SerializedName("avatar_url")
    val avatarUrl: String
)