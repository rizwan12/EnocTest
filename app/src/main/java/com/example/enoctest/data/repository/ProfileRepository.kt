package com.example.enoctest.data.repository

import com.example.enoctest.data.backend.impl.ApiHelper

class ProfileRepository(private val apiHelper: ApiHelper) {
    suspend fun getUser(userId: String, token: String) = apiHelper.getUser(userId, token)
    suspend fun uploadImage(base64Image: String, userId: String, token: String) = apiHelper.uploadImage(base64Image, userId, token)
}