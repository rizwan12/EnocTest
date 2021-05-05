package com.example.enoctest.data.repository

import com.example.enoctest.data.backend.impl.ApiHelper

class LoginRepository(private val apiHelper: ApiHelper) {
    suspend fun login(username: String, password: String) = apiHelper.login(username, password)
}