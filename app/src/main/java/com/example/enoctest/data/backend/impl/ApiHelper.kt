package com.example.enoctest.data.backend.impl

class ApiHelper(private val api: ApiService) {

    suspend fun login(username: String, password: String) = api.login(username, password)
    suspend fun getUser(userId: String, token: String) = api.getUser(userId, token)
    suspend fun uploadImage(base64Image: String, userId: String, token: String) = api.upload(userId, base64Image, token)
}